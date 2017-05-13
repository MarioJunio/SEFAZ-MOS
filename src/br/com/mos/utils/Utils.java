package br.com.mos.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

	// Nome do sistema operacional que está sendo executado a aplicação
	private static String name;

	static {
		name = System.getProperty("os.name").toLowerCase();
	}

	public static String readLines(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

	public static void write(String filename, String text) throws FileNotFoundException {

		try (PrintWriter writer = new PrintWriter(new File(filename))) {
			writer.println(text);
		}
	}

	public static boolean isWindows() {
		return name.indexOf("win") >= 0;
	}

	public static boolean isUnix() {
		return name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") >= 0;
	}

	public static boolean isMac() {
		return name.indexOf("mac") >= 0;
	}

	public static boolean isSolaris() {
		return name.indexOf("sunos") >= 0;
	}

	public static String decodeXML(String xml) {
		return xml.replace("\\\"", "\"").replace("\\/", "/").replace("\\n", "\n").trim();
	}

	public static boolean isEmpty(Object obj) {

		boolean isNull = obj == null;

		if (obj instanceof String && !isNull) {
			return obj.toString().isEmpty();
		}

		return isNull;
	}
}
