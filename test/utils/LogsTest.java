package utils;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import br.com.mos.utils.Logs;

public class LogsTest {

	@Test
	public void log() throws IOException, URISyntaxException {
		Logs.log("Testando o log", "DEBUG");
	}
}
