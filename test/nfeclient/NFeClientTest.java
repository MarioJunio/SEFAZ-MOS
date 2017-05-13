package nfeclient;

import org.junit.Test;

import br.com.nc.model.NFAmbiente;
import br.com.nc.security.SSL;
import br.com.nc.services.sefaz.nfe.DistDFe;

public class NFeClientTest {

	@Test
	public void consultarDFe() {

		// String certificadoFisico = "/Users/MarioJ/Desktop/hp-logistica.pfx";
		String apelidoCertificado = "HP LOGISTICA LTDA ME:15107846000100";
		// String senhaCertificado = "20151972";
		NFAmbiente ambiente = NFAmbiente.HOMOLOGACAO;

		/*
		 * Carrega certificado A1 AppConfig config = new AppConfig(cacerts, certificadoParaSSL, senhaCertificado, ambiente); config.carregarCertificado();
		 */

		try {
			// Pode user usado para carregar certificados A1 ou A3
			SSL.apply(apelidoCertificado);

			DistDFe nfeDistDfe = DistDFe.getInstance(ambiente);
			String retXml = (String) nfeDistDfe.enviar(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><distDFeInt versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><tpAmb>1</tpAmb><cUFAutor>31</cUFAutor><CNPJ>15107846000100</CNPJ><distNSU><ultNSU>000000000000000</ultNSU></distNSU></distDFeInt>");

			System.out.println(retXml);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
