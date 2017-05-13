package br.com.mos.net.web;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.mos.model.TipoEvento;
import br.com.mos.net.HttpClient;
import br.com.mos.net.MimeType;
import br.com.mos.net.exception.ResponseException;
import br.com.mos.wrapper.MosConfiguracaoWrapper;
import br.com.mos.wrapper.MosReqEventoWrapper;
import br.com.mos.wrapper.MosRetEventosWrapper;
import br.com.mos.wrapper.TmpRetEventoWrapper;
import br.com.nc.model.NFAmbiente;

public class Web {

	public static MosConfiguracaoWrapper buscarConfiguracoes(String ca) throws ResponseException, IOException, ConnectException {

		HttpClient httpClient = new HttpClient(Url.WS_MOS_BUSCAR_CONFIGURACOES.replace("{ca}", ca));

		// obtem resposta contendo as configurações do vinculadas ao CA
		String response = httpClient.get(MimeType.JSON);
		JSONObject json = new JSONObject(response);

		return new MosConfiguracaoWrapper(json.getLong("id"), json.getString("cnpj"), json.getString("uf"), json.getString("certificado"), json.getBoolean("setup"), json.getEnum(NFAmbiente.class, "ambiente"));
	}

	public static void salvarConfiguracoesMOS(Long id, String ca, String cnpj, String certificado) throws IOException {

		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		params.put("ca", ca);
		params.put("cnpj", cnpj);
		params.put("certificado", certificado);

		HttpClient httpClient = new HttpClient(Url.WS_MOS_SALVAR_CONFIGURACOES);

		try {
			httpClient.post(params, MimeType.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<MosReqEventoWrapper> buscarEventos(String ca, String cnpj) throws Exception {

		HttpClient httpClient = new HttpClient(Url.WS_MOS_BUSCAR_EVENTOS.replace("{ca}", ca).replace("{cnpj}", cnpj));
		String response = httpClient.get(MimeType.JSON);

		List<MosReqEventoWrapper> listReqEventoWrapper = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(response);

		System.out.println("Eventos de Requisição: ");
		// itera através dos eventos encontrados
		for (int i = 0; i < jsonArray.length(); i++) {

			// pega o evento na posicao atual em formato JSON
			JSONObject json = jsonArray.getJSONObject(i);

			// transforma JSON para objeto
			listReqEventoWrapper.add(parseReqEventoWrapper(json));

			System.out.println(json);
		}

		return listReqEventoWrapper;
	}

	public static void enviarEventosRetorno(String ca, String cnpj, List<TmpRetEventoWrapper> listTmpRetEventoWrap) {

		MosRetEventosWrapper mosRetEventosWrapper = new MosRetEventosWrapper();
		mosRetEventosWrapper.setCa(ca);
		mosRetEventosWrapper.setCnpj(cnpj);
		mosRetEventosWrapper.setEventos(listTmpRetEventoWrap);

		JSONObject jsonRequestBody = new JSONObject(mosRetEventosWrapper);

		try {
			HttpClient httpClient = new HttpClient(Url.WS_MOS_ENVIA_RET_EVENTOS);
			httpClient.post(jsonRequestBody.toString(), MimeType.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MosReqEventoWrapper parseReqEventoWrapper(JSONObject json) {

		MosReqEventoWrapper mosReqEventoWrapper = new MosReqEventoWrapper();
		mosReqEventoWrapper.setId(json.getLong("id"));
		mosReqEventoWrapper.setEvento(json.getEnum(TipoEvento.class, "evento"));
		mosReqEventoWrapper.setXml(json.getString("xml"));

		return mosReqEventoWrapper;
	}
}
