package br.com.mos.app;

import br.com.mos.wrapper.MosConfiguracaoWrapper;
import br.com.nc.security.Assinatura;

public abstract class Session {
	
	public static String CA;
	public static Assinatura assinatura;
	public static MosConfiguracaoWrapper config;
}
