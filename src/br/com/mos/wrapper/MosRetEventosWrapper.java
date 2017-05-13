package br.com.mos.wrapper;

import java.util.Collection;

public class MosRetEventosWrapper {

	private String ca;
	private String cnpj;
	private Collection<TmpRetEventoWrapper> eventos;

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Collection<TmpRetEventoWrapper> getEventos() {
		return eventos;
	}

	public void setEventos(Collection<TmpRetEventoWrapper> eventos) {
		this.eventos = eventos;
	}

}
