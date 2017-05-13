package br.com.mos.wrapper;

import org.json.JSONObject;

import br.com.mos.model.TipoEvento;

public class MosReqEventoWrapper {

	private Long id;
	private TipoEvento evento;
	private String xml;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoEvento getEvento() {
		return evento;
	}

	public void setEvento(TipoEvento evento) {
		this.evento = evento;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	@Override
	public String toString() {
		return new JSONObject(this).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MosReqEventoWrapper other = (MosReqEventoWrapper) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
