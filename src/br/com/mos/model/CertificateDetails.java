package br.com.mos.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CertificateDetails {

	private X509Certificate certificate;
	private PrivateKey privateKey;

	public CertificateDetails() {
		super();
	}

	public CertificateDetails(X509Certificate certificate, PrivateKey privateKey) {
		super();
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public String toString() {
		return "CertificateDetails [certificate=" + certificate + ", privateKey=" + privateKey + "]";
	}

}
