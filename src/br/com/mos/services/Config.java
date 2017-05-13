package br.com.mos.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import br.com.mos.model.ConfigDetails;
import br.com.mos.utils.Utils;

public class Config {

	private static String location;
	private static final String CONFIG_FILE;

	static {
		location = System.getProperty("user.home").concat("/.mos/");
		CONFIG_FILE = "config.ini";
	}

	public static ConfigDetails getConfigDetails() throws IOException {

		ConfigDetails configDetails = new ConfigDetails();

		String configIniPath = location.concat(CONFIG_FILE);
		
		if (Files.notExists(Paths.get(configIniPath))) {
			return configDetails;
		}

		String fileContent = Utils.readLines(configIniPath);

		String[] lines = fileContent.split("\n");

		if (lines.length == 2) {

			String[] certLines = lines[0].split("=");
			String[] setupLines = lines[1].split("=");

			if (certLines.length != 2) {
				configDetails.setCert("");
			} else {
				configDetails.setCert(certLines[1].trim());
			}

			if (setupLines.length != 2) {
				configDetails.setSetup(false);
			} else {
				configDetails.setSetup(Boolean.parseBoolean(setupLines[1].trim()));
			}

		}

		return configDetails;
	}

	public static void done(String certificado) throws IOException {
		Files.write(Paths.get(location.concat(CONFIG_FILE)), certificado.getBytes());
	}

	public static boolean isConfigured() throws IOException {

		if (Files.notExists(Paths.get(location), LinkOption.NOFOLLOW_LINKS)) {
			Files.createDirectory(Paths.get(location));
		}

		ConfigDetails configDetails = getConfigDetails();
		return configDetails.isSetup();
	}

}
