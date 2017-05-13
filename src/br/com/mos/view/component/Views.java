package br.com.mos.view.component;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class Views {

	public static void definirLookAndFeelNimbus() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		try {

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}

		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}

	}

}
