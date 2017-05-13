package br.com.mos.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateParsingException;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import br.com.mos.app.Session;
import br.com.mos.exception.CertificadoExpiradoException;
import br.com.mos.model.CertificateDetails;
import br.com.mos.net.web.Web;
import br.com.mos.services.AppServices;
import br.com.mos.services.Certificados;
import br.com.mos.services.Certificados.Tipo;
import br.com.mos.view.component.CellRenderer;

public class SelecionarCertificados extends JPanel {

	private static final long serialVersionUID = 1L;

	private String colunas[] = { "Certificados encontrados" };
	private JTable table;
	private DefaultTableModel tableModel;

	public SelecionarCertificados() {

		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton btnNewButton = new JButton("Usar este certificado");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					String cert = (String) table.getValueAt(table.getSelectedRow(), 0);
					CertificateDetails certDetails = AppServices.carregarCertificado(cert);

					// valida se o cnpj dos certificados são os mesmos
					if (!AppServices.validarCnpjCertificado(Session.config, certDetails)) {
						JOptionPane.showMessageDialog(getParent(), "O cnpj do certificado selecionado não é o mesmo que você está utilizando.", "Atenção",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					// verifica se o certificado venceu, se sim irá lançar uma exceção
					AppServices.certificadoVencido(certDetails);

					Session.config.setCertificado(cert);

					// salva as configuracoes na nuvem
					Web.salvarConfiguracoesMOS(Session.config.getId(), Session.CA, Session.config.getCnpj(), Session.config.getCertificado());

					JOptionPane.showMessageDialog(getParent(), "Configuração realizada com sucesso", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);

				} catch (CertificadoExpiradoException cee) {
					JOptionPane.showMessageDialog(getParent(), cee.getMessage(), "Atenção", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (CertificateParsingException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (UnrecoverableEntryException e1) {
					e1.printStackTrace();
				} catch (KeyStoreException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnNewButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(btnNewButton, BorderLayout.SOUTH);

		tableModel = new DefaultTableModel(colunas, 0);
		table = new JTable(tableModel);
		table.setRowHeight(30);

		table.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());

		((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setOpaque(false);

		add(scrollPane, BorderLayout.CENTER);

		this.iniciar();
	}

	private void iniciar() {

		try {
			carregarCertificados();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void carregarCertificados() throws Exception {
		new CarregarCertificados().start();
	}

	private class CarregarCertificados extends Thread {

		@Override
		public void run() {

			try {

				Set<String> certificados = Certificados.getAllCertificates(Tipo.KEYSTORE);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						for (String cert : certificados) {
							tableModel.addRow(new String[] { cert });
						}
					}

				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
