package br.com.mos.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import br.com.mos.view.panel.SelecionarCertificados;

public class Introducao {

	public JFrame frmConfigurao;

	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */

	/**
	 * Create the application.
	 */
	public Introducao() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmConfigurao = new JFrame();
		frmConfigurao.setFont(new Font("Arial", Font.PLAIN, 12));
		frmConfigurao.setTitle("Módulo de Segurança - Configuração");
		frmConfigurao.setResizable(false);
		frmConfigurao.getContentPane().setBackground(Color.WHITE);
		frmConfigurao.setBounds(100, 100, 800, 600);
		frmConfigurao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConfigurao.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Continuar");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// exibe a tela para selecionar os certificados
				SelecionarCertificados scView = new SelecionarCertificados();
				frmConfigurao.setTitle("Módulo de Segurança - Certificados");
				frmConfigurao.setContentPane(scView);
				frmConfigurao.getContentPane().revalidate();
			}
		});

		btnNewButton.setBounds(223, 504, 366, 38);
		frmConfigurao.getContentPane().add(btnNewButton);

		JLabel lblTextoDeIntroduo = new JLabel("Seja bem vindo, ao módulo de segurança onde você poderá selecionar o certificado digital da sua empresa para ser");
		lblTextoDeIntroduo.setVerticalAlignment(SwingConstants.TOP);
		lblTextoDeIntroduo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTextoDeIntroduo.setBounds(26, 33, 768, 23);
		frmConfigurao.getContentPane().add(lblTextoDeIntroduo);

		JLabel lblParaSerUtilizado = new JLabel("utilizado na comunicação com os serviços da receita federal.");
		lblParaSerUtilizado.setVerticalAlignment(SwingConstants.TOP);
		lblParaSerUtilizado.setHorizontalAlignment(SwingConstants.LEFT);
		lblParaSerUtilizado.setBounds(26, 54, 728, 23);
		frmConfigurao.getContentPane().add(lblParaSerUtilizado);

		JLabel imgLogo = new JLabel("");
		Image imageIcon = new ImageIcon(this.getClass().getResource("/images/img_logo_placeholder.png")).getImage();
		imgLogo.setIcon(new ImageIcon(imageIcon));
		imgLogo.setBounds(292, 139, 224, 235);
		frmConfigurao.getContentPane().add(imgLogo);
	}

}
