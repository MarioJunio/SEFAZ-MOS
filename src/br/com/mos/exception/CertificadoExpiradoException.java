package br.com.mos.exception;

public class CertificadoExpiradoException extends Exception {
	
	private static final long serialVersionUID = -6890025029955428762L;

	public CertificadoExpiradoException(String message) {
		super(message);
	}
}
