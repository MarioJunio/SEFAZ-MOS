package br.com.bootstrap;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.mos.app.Session;
import br.com.mos.net.exception.ResponseException;
import br.com.mos.net.web.Web;
import br.com.mos.services.AppServices;
import br.com.mos.utils.Logs;
import br.com.mos.view.Introducao;
import br.com.mos.view.component.Views;
import br.com.nc.config.NFeCacerts;
import br.com.nc.model.NFAmbiente;
import br.com.nc.security.Assinatura;
import br.com.nc.utils.Utils;

public class MosInitializer {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

//		CertificateDetails certDetails = null;
		
		try {
			// grava data e horario que iniciou a applicacao
			Logs.log("-----------------------------------------------------------------------------", "APP");
			Logs.log("Inicialização da aplicação...", "APP");
			Logs.log("-----------------------------------------------------------------------------", "APP");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		Views.definirLookAndFeelNimbus();

		// acesso do cliente a ser buscado na nuvem
		Session.CA = getCAByArgs(args);

		// JOptionPane.showMessageDialog(null, "Parametros: " + Session.CA, "Atenção", JOptionPane.INFORMATION_MESSAGE);

		// não encontrou CA
		if (Session.CA == null) {
			return;
		}

		// se a CA for 'update_cacerts', então atualizar a cadeia de certificados da SEFAZ
		if (!Utils.isEmpty(Session.CA) && Session.CA.equals("update_cacerts")) {
			NFeCacerts.gerar(NFAmbiente.PRODUCAO);
			return;
		} else if (!Utils.isEmpty(Session.CA) && Session.CA.equals("update_cacerts_homo")) {
			NFeCacerts.gerar(NFAmbiente.HOMOLOGACAO);
			return;
		}

		try {
			// busca as configurações associadas a esse CA
			Session.config = Web.buscarConfiguracoes(Session.CA);
			System.out.printf("[MOS Configurações]: %s\n\n", Session.config);
		} catch (ConnectException ce) {
			ce.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível estabelecer conexão com a nuvem.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			return;
		} catch (ResponseException re) {

			System.out.println("[Buscar Configuracoes] Response Exception: " + re.getMessage());

			if (Integer.parseInt(re.getMessage()) == 404) {
				showIntroducao();
			} else {
				JOptionPane.showMessageDialog(null, "Ocorreu um probleminha ao processar os dados. Tente novamente daqui alguns minutos", "Atenção",
						JOptionPane.INFORMATION_MESSAGE);
			}

			return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// verifica se encontrou as configurações do MOS da empresa, se não existe uma incosistencia, pois o WS não retornou status correto
		if (Session.config != null) {

			// verifica se á primeira vez que a empresa está configurando o MOS
			if (Utils.isEmpty(Session.config.getCertificado())) {
				showIntroducao();
				return;
			}

		} else {
			System.out.println("MOS Configuração não encontrada, existe uma possível inconsistência no cadastro da empresa.");
		}

		try {

			// carrega o certificado já previamente configurado
			// certDetails = Certificados.getCertificateByAlias(Tipo.KEYSTORE, Session.config.getCertificado());

			// log para debug do nome do certificado
			System.out.printf("Certificado: %s\n\n", Session.config.getCertificado());

			// obtem um instância da classe que irá assinar o XML
			Session.assinatura = Assinatura.getInstance(Session.config.getCertificado());

			AppServices.processarEventos(Web.buscarEventos(Session.CA, Session.config.getCnpj()));

		} catch (NullPointerException rp) {
			JOptionPane.showMessageDialog(null, "O certificado configurado anteriormente não foi encontrado!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			showIntroducao();
		} catch (ConnectException ce) {
			JOptionPane.showMessageDialog(null, "Não foi possível estabelecer conexão com a nuvem.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (ResponseException re) {
			System.out.println("[Buscar Eventos] Response Exception: " + re.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showIntroducao() {

		// exibe janela para configuração
		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {
					Introducao window = new Introducao();
					window.frmConfigurao.setVisible(true);
					window.frmConfigurao.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	private static String getCAByArgs(String args[]) {

		String acesso = null;

		if (args.length > 0) {

			String arg = args[0];

			String tokens[] = arg.split(":");

			if (tokens.length == 2) {
				acesso = tokens[1].split("&")[0].split("=")[1];
			}
		}

		return acesso != null ? acesso.trim() : null;
	}

}
