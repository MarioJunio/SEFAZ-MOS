package br.com.mos.services;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

import br.com.mos.app.Session;
import br.com.mos.exception.CertificadoExpiradoException;
import br.com.mos.model.CertificateDetails;
import br.com.mos.model.TipoEvento;
import br.com.mos.net.web.Web;
import br.com.mos.services.Certificados.Tipo;
import br.com.mos.utils.Logs;
import br.com.mos.utils.Utils;
import br.com.mos.wrapper.MosConfiguracaoWrapper;
import br.com.mos.wrapper.MosReqEventoWrapper;
import br.com.mos.wrapper.TmpRetEventoWrapper;
import br.com.nc.config.NFeCacerts;
import br.com.nc.model.UF;
import br.com.nc.security.SSL;
import br.com.nc.services.sefaz.nfe.DistDFe;
import br.com.nc.services.sefaz.nfe.RecepcaoEvento;

public abstract class AppServices {

	public static final ASN1ObjectIdentifier RESPONSAVEL = new ASN1ObjectIdentifier("2.16.76.1.3.2");
	public static final ASN1ObjectIdentifier CNPJ = new ASN1ObjectIdentifier("2.16.76.1.3.3");

	public static CertificateDetails carregarCertificado(String certificado) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, Exception {
		return Certificados.getCertificateByAlias(Tipo.KEYSTORE, certificado);
	}

	public static boolean validarCnpjCertificado(MosConfiguracaoWrapper configuracaoWrapper, CertificateDetails certificateDetails) throws CertificateParsingException {

		// obtem o cnpj pelo certificado X509
		String cnpj = getCnpjCertificado(certificateDetails.getCertificate());

		// System.out.println("[Recuperou Cnpj BC] : " + cnpj);

		// System.out.printf("Nome certificado: " + certificateDetails.getCertificate().getSubjectDN().getName());

		// verifica se o cnpj foi encontrado, se não tente obter o cnpj pelo nome do certificado
		if (Utils.isEmpty(cnpj)) {
			cnpj = getCnpjCertificado(certificateDetails.getCertificate().getSubjectDN().getName());
		}

		// System.out.println("Recuperou cnpj certificado por nome: " + cnpj);

		// se o cnpj não for encontrado em nenhum dos casos, então o cnpj do certificado é inválido
		if (Utils.isEmpty(cnpj)) {
			System.out.println("CNPJ não pode ser extraído do certificado");
			return false;
		}

		return configuracaoWrapper.getCnpj().equals(cnpj);
	}

	public static boolean certificadoVencido(CertificateDetails certificateDetails) throws CertificadoExpiradoException {

		Date dataAtual = Calendar.getInstance().getTime();
		Date dataValidade = certificateDetails.getCertificate().getNotAfter();

		if (dataAtual.after(dataValidade))
			throw new CertificadoExpiradoException("Este certificado venceu em " + new SimpleDateFormat("dd/MM/yyyy").format(dataValidade));
		else
			return false;

	}

	/**
	 * Processa os eventos encontrados
	 * 
	 * @param reqEventos
	 *            eventos a serem executados
	 */
	public static void processarEventos(List<MosReqEventoWrapper> reqEventos) {

		if (!reqEventos.isEmpty()) {

			// gera a cadeia de certificados da SEFAZ e carrega o certificado utilizado para comunicação com a SEFAZ
			setSSL();

			// lista contendo o retorno da SEFAZ aos eventos requisitados
			List<TmpRetEventoWrapper> retEventosWrapper = new ArrayList<>();

			// itera sobre os eventos a serem executados
			reqEventos.forEach(mosReqEventoWrapper -> {

				try {

					// se o evento for "consultar DFe"
					if (mosReqEventoWrapper.getEvento() == TipoEvento.NFE_DIST_DFE)
						retEventosWrapper.add(performNFeDistDFe(mosReqEventoWrapper));
					else if (mosReqEventoWrapper.getEvento() == TipoEvento.NFE_MANIFESTO_DESTINATARIO)
						retEventosWrapper.add(performNFeManifestoDestinatario(mosReqEventoWrapper));

				} catch (Exception e) {
					e.printStackTrace();
				}

			});

			// realiza requisicao ao WS, enviando os eventos retornados da SEFAZ
			Web.enviarEventosRetorno(Session.CA, Session.config.getCnpj(), retEventosWrapper);
		}

	}

	/**
	 * Executa evento distribuição DFe
	 * 
	 * @param mosReqEventoWrapper
	 * @return evento, e o xml de retorno
	 * @throws Exception
	 */
	private static TmpRetEventoWrapper performNFeDistDFe(MosReqEventoWrapper mosReqEventoWrapper) throws Exception {

		DistDFe nfeDistDfe = DistDFe.getInstance(Session.config.getAmbiente());
		String retXml = (String) nfeDistDfe.enviar(mosReqEventoWrapper.getXml());

		Logs.log("Retorno SEFAZ \"Distribuição DFe\": " + retXml, "DEBUG");

		return new TmpRetEventoWrapper(mosReqEventoWrapper.getId(), mosReqEventoWrapper.getEvento(), retXml);
	}

	/**
	 * Executa evento Recepção Evento (Manifesto do Destinatário)
	 * 
	 * @param mosReqEventoWrapper
	 * @return evento, e o xml de retorno
	 * @throws Exception
	 */
	private static TmpRetEventoWrapper performNFeManifestoDestinatario(MosReqEventoWrapper mosReqEventoWrapper) throws Exception {

		// assina o xml de requisição com o certificado do usuário
		String xmlAssinado = Session.assinatura.assinarInfEvento(mosReqEventoWrapper.getXml());
		Logs.log(xmlAssinado, "DEBUG");
		
		// transmite o xml assinado para a SEFAZ
		RecepcaoEvento nFeRecepcaoEvento = RecepcaoEvento.getInstance(Session.config.getAmbiente(), UF.AN.getCodigo());
		String retXml = nFeRecepcaoEvento.enviar(xmlAssinado);
		Logs.log("Retorno SEFAZ \"Manifestação Destinatario\": " + retXml, "DEBUG");

		return new TmpRetEventoWrapper(mosReqEventoWrapper.getId(), mosReqEventoWrapper.getEvento(), retXml);
	}

	/**
	 * Extrai o cnpj através do nome do sujeito gravado no certificado
	 * 
	 * @param certificadoNome
	 *            nome do certificado a ser usado
	 * @return cnpj
	 * @throws CertificateParsingException
	 */
	private static String getCnpjCertificado(X509Certificate certificado) throws CertificateParsingException {

		String cnpj = null;
		Collection<?> alternativeNames = X509ExtensionUtil.getSubjectAlternativeNames(certificado);

		// itera através das informações alternativas do certificado
		for (Object alternativeName : alternativeNames) {

			List<?> names = (ArrayList<?>) alternativeName;
			Object value = names.get(1);

			if (value instanceof DLSequence) {

				DLSequence dlSequence = (DLSequence) value;
				ASN1ObjectIdentifier identifier = (ASN1ObjectIdentifier) dlSequence.getObjectAt(0);

				// se a info for CNPJ
				if (identifier.equals(CNPJ)) {

					DERTaggedObject derTaggedObject = (DERTaggedObject) dlSequence.getObjectAt(1);
					ASN1Primitive object = derTaggedObject.getObject();

					if (object instanceof DEROctetString) {
						DEROctetString octet = (DEROctetString) object;
						cnpj = new String(octet.getOctets());
					} else if (object instanceof DERPrintableString) {
						DERPrintableString octet = (DERPrintableString) object;
						cnpj = new String(octet.getOctets());
					} else if (object instanceof DERUTF8String) {
						DERUTF8String str = (DERUTF8String) object;
						cnpj = str.getString();
					}

					break;
				}
			}
		}

		return cnpj;
	}

	/**
	 * Extrai o CNPJ do nome do certificado
	 * 
	 * @param certificadoNome
	 *            nome do certificado
	 * @return cnpj
	 */
	private static String getCnpjCertificado(String certificadoNome) {

		String cnpj = null;
		String tokens[] = certificadoNome.split(":");

		if (tokens.length > 1) {
			cnpj = tokens[1].substring(0, 14);
		}

		return cnpj;
	}

	private static void setSSL() {

		try {
			SSL.apply(Session.config.getCertificado());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		} catch (Exception e) {
			NFeCacerts.gerar(Session.config.getAmbiente());
			e.printStackTrace();
		}

	}
}
