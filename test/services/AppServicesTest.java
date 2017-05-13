package services;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateParsingException;

import org.junit.Test;

import br.com.mos.model.CertificateDetails;
import br.com.mos.services.AppServices;
import br.com.mos.wrapper.MosConfiguracaoWrapper;
import br.com.nc.model.NFAmbiente;

public class AppServicesTest {

	public void getCnpjCertificadoPorAliaseTest() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, Exception {
		// String cnpj = AppServices.getCnpjCertificado(AppServices.carregarCertificado("AUTO PECAS SANTANA LTDA
		// ME:22065841000110").getCertificate().getSubjectDN().getName());
		// System.out.printf("CNPJ: %s\n", cnpj);
	}

	public void getCnpjCertificadoPorX509Test() throws CertificateParsingException, NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, Exception {
		// String cnpj = AppServices.getCnpjCertificado(AppServices.carregarCertificado("AUTO PECAS SANTANA LTDA ME:22065841000110").getCertificate());
		// System.out.printf("CNPJ: %s\n", cnpj);
	}

	public void validarCnpjCertificadoTest() {

		CertificateDetails certificateDetails;

		try {
			MosConfiguracaoWrapper configuracaoWrapper = new MosConfiguracaoWrapper(null, "22065841000110", "31", "AUTO PECAS SANTANA LTDA ME:22065841000110", true, NFAmbiente.HOMOLOGACAO);
			certificateDetails = AppServices.carregarCertificado(configuracaoWrapper.getCertificado());
			System.out.println(AppServices.validarCnpjCertificado(configuracaoWrapper, certificateDetails));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void certificadoVencidoTest() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, Exception {

		MosConfiguracaoWrapper configuracaoWrapper = new MosConfiguracaoWrapper(null, "22065841000110", "31", "AUTO PECAS SANTANA LTDA ME:22065841000110", true, NFAmbiente.HOMOLOGACAO);
		CertificateDetails certificateDetails = AppServices.carregarCertificado(configuracaoWrapper.getCertificado());

		System.out.println(AppServices.certificadoVencido(certificateDetails));
	}
}
