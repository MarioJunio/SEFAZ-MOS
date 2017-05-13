package br.com.mos.net;

public enum MimeType {

	TEXT_HTML("text/html"), PLAIN_TEXT("text/plain"), JSON("application/json"), XML("application/xml");

	private String value;

	private MimeType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
