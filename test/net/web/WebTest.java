package net.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.mos.model.TipoEvento;
import br.com.mos.net.web.Web;
import br.com.mos.wrapper.MosConfiguracaoWrapper;
import br.com.mos.wrapper.TmpRetEventoWrapper;

public class WebTest {

	@Test
	public void buscarConfiguracoesTest() {

		String ca = "0B3D01F7AD637F186C8ED9C0434AE07C";

		try {
			MosConfiguracaoWrapper mosConfig = Web.buscarConfiguracoes(ca);
			System.out.println(mosConfig.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarConfiguracoesTest() {

		String ca = "0B3D01F7AD637F186C8ED9C0434AE07C";
		String cnpj = "15107846000100";
		String certificado = "Joao rodrigues neto:15107846000100";

		try {
			Web.salvarConfiguracoesMOS(null, ca, cnpj, certificado);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buscarEventosTest() {

		String ca = "BF5CD869F28021FE1AF11163FB9038B3";
		String cnpj = "15107846000100";

		try {

			Web.buscarEventos(ca, cnpj).forEach(eventoWrapper -> {
				System.out.println(eventoWrapper.toString());
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enviaRetEventosTest() {

		String ca = "BF5CD869F28021FE1AF11163FB9038B3";
		String cnpj = "15107846000100";
		List<TmpRetEventoWrapper> listRetEventoWrap = new ArrayList<>();
		listRetEventoWrap.add(new TmpRetEventoWrapper(1l, TipoEvento.NFE_DIST_DFE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <retorno>Teste de retorno</retorno>"));

		Web.enviarEventosRetorno(ca, cnpj, listRetEventoWrap);
	}
}
