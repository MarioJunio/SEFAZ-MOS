package br.com.mos.model;

public class ConfigDetails {

	private String cert;
	private boolean setup;

	public ConfigDetails() {
		super();
	}

	public ConfigDetails(String cert, boolean setup) {
		super();
		this.cert = cert;
		this.setup = setup;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public boolean isSetup() {
		return setup;
	}

	public void setSetup(boolean setup) {
		this.setup = setup;
	}

	@Override
	public String toString() {
		return "ConfigDetails [cert=" + cert + ", setup=" + setup + "]";
	}

}
