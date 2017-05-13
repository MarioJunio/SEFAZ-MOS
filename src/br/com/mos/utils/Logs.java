package br.com.mos.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {

	private static final String FS = System.getProperty("file.separator");
	private static final String LS = System.getProperty("line.separator");

	private static String path;
	private static File fLog;
	private static SimpleDateFormat dFormat;

	static {
		dFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		setLog();
	}

	private static void setLog() {

		path = System.getProperty("user.home") + FS + "mos.log";
		fLog = new File(path);

		if (!fLog.exists()) {

			try {
				fLog.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void changePath(String newPath) {
		path = newPath;
		setLog();
	}

	public static void log(String message, String tag) throws IOException, URISyntaxException {
		Files.write(Paths.get("", path), String.format("[%s\t%s]: %s" + LS, tag, dFormat.format(new Date()), message).getBytes(), StandardOpenOption.APPEND);
	}

}
