package br.com.mos.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import br.com.mos.net.exception.ResponseException;

public class HttpClient {

	private final String USER_AGENT = "Mozilla/5.0";
	private final String CHARSET = "UTF-8";

	private String host;

	public HttpClient(String host) {
		this.host = host;
	}

	public String get(MimeType mimeType) throws ResponseException, IOException {

		URL targetURL = new URL(this.host);

		HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
		con.setRequestMethod("GET");

		// add headers
		addDefaultHeaders(con, mimeType);

		// send get request
		int responseCode = con.getResponseCode();

		// System.out.println("[GET]: " + this.host);
		// System.out.println("[Response Code]: " + responseCode);

		if (responseCode >= 200 && responseCode < 300)
			return getResponseBody(con.getInputStream());
		else
			throw new ResponseException(String.valueOf(responseCode));
	}

	public String post(Map<String, String> params, MimeType mimeType) throws Exception {

		URL targetURL = new URL(this.host);

		HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// add headers
		addDefaultHeaders(con, mimeType);

		// send post request
		con.setDoOutput(true);

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(parseParameters(params));
		out.flush();
		out.close();

		// get response
		int responseCode = con.getResponseCode();

		if (responseCode >= 200 && responseCode < 300)
			return getResponseBody(con.getInputStream());
		else
			throw new ResponseException(String.valueOf(responseCode));
	}

	public String post(String params, MimeType contentType) throws Exception {

		URL targetURL = new URL(this.host);

		HttpURLConnection con = (HttpURLConnection) targetURL.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", contentType.getValue());

		// add headers
		addDefaultHeaders(con, contentType);

		// send post request
		con.setDoOutput(true);

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(params);
		out.flush();
		out.close();

		// get response
		int responseCode = con.getResponseCode();

		if (responseCode >= 200 && responseCode < 300)
			return getResponseBody(con.getInputStream());
		else
			throw new ResponseException(String.valueOf(responseCode));
	}

	private void addDefaultHeaders(HttpURLConnection con, MimeType mimeType) throws IOException {

		// header: user agent
		con.setRequestProperty("User-Agent", USER_AGENT);

		// header: accept media type
		con.setRequestProperty("Accept", mimeType.getValue());

		// header: accept-charset
		con.setRequestProperty("Accept-Charset", "utf-8");
	}

	public String parseParameters(Map<String, String> parameters) throws UnsupportedEncodingException {

		String urlParams = "";
		Set<String> keys = parameters.keySet();

		for (String key : keys) {

			String value = parameters.get(key);
			urlParams += key.concat("=").concat(URLEncoder.encode(value, CHARSET)).concat("&");

		}

		if (urlParams.endsWith("&")) {
			urlParams = urlParams.substring(0, urlParams.length() - 1);
		}

		return urlParams;

	}

	private String getResponseBody(InputStream in) throws IOException {

		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		StringBuffer body = new StringBuffer();

		String line;
		while ((line = buffer.readLine()) != null) {
			body.append(line);
		}

		buffer.close();

		return body.toString();
	}

}
