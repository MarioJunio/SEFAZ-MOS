package services;

import java.io.IOException;

import org.junit.Test;

import br.com.mos.net.exception.ResponseException;
import br.com.mos.net.web.Web;
import br.com.mos.wrapper.MosConfiguracaoWrapper;

public class WebServicesTest {
	
	final String CA = "67E0CB1455849C06BE765764416F35B4";
	
	@Test
	public void buscarConfiguracoesTest() {
		
		try {
			MosConfiguracaoWrapper configuracaoWrapper = Web.buscarConfiguracoes(CA);
			System.out.println(configuracaoWrapper.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ResponseException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
