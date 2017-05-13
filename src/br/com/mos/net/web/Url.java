package br.com.mos.net.web;

import br.com.mos.utils.Utils;

public interface Url {

	public static final String HOST = Utils.isMac() ? "http://localhost:8080" : "http://192.168.150.23:8080";

	/**
	 * Retorna as configurações para o MOS(Módulo de segurança), a partir do CA vinculado a ela.
	 */
	public static final String WS_MOS_BUSCAR_CONFIGURACOES = HOST.concat("/mos/configuracoes/{ca}");

	/**
	 * Salva as configurações realizadas pelo usuário no MOS.
	 */
	public static final String WS_MOS_SALVAR_CONFIGURACOES = HOST.concat("/mos/salvar/");

	/**
	 * Gera uma chave de acesso(CA) associado ao CNPJ, que está logado, cria o xml de requisição, ao final retorna como resposta a CA para cliente, para ser
	 * usada no MOS:
	 */
	public static final String WS_MOS_BUSCAR_EVENTOS = HOST.concat("/mos/eventos/{ca}/{cnpj}");
	
	/**
	 * Recebe todos os eventos processados pelo MOS
	 */
	public static final String WS_MOS_ENVIA_RET_EVENTOS = HOST.concat("/mos/eventos/");
}
