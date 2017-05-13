package br.com.mos.wrapper;

import org.json.JSONObject;

import br.com.nc.model.NFAmbiente;

public class MosConfiguracaoWrapper {

	private Long id;
	private String cnpj;
	private String uf;
	private String certificado;
	private boolean setup;
	private NFAmbiente ambiente;

	public MosConfiguracaoWrapper(Long id, String cnpj, String uf, String certificado, boolean setup, NFAmbiente ambiente) {
		super();
		this.id = id;
		this.cnpj = cnpj;
		this.uf = uf;
		this.certificado = certificado;
		this.setup = setup;
		this.ambiente = ambiente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public boolean isSetup() {
		return setup;
	}

	public void setSetup(boolean setup) {
		this.setup = setup;
	}

	public NFAmbiente getAmbiente() {
		return ambiente;
	}

	@Override
	public String toString() {
		return new JSONObject(this).toString();
	}

}
