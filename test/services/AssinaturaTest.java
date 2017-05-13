package services;

import org.junit.Test;

public class AssinaturaTest {

	@Test
	public void assinarEvento() {

		System.out.println(System.getProperty("user.home"));
		return;
		
		/*String apelidoCertificado = "/Users/MarioJ/Desktop/hp-logistica.pfx";
		String senhaCertificado = "20151972";
		String xmlEvento = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envEvento versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\" >";
		xmlEvento += "<idLote>1</idLote>";
		xmlEvento += "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\">";
		xmlEvento += "<infEvento Id=\"ID21020043243432fdsfds1\">";
		xmlEvento += "<cOrgao>91</cOrgao>";
		xmlEvento += "<tpAmb>2</tpAmb>";
		xmlEvento += "<CNPJ>15107846000100</CNPJ>";
		xmlEvento += "<chNFe>43243432fdsfds</chNFe>";
		xmlEvento += "<dhEvento>2017-05-09T14:13:19-03</dhEvento>";
		xmlEvento += "<tpEvento>210200</tpEvento>";
		xmlEvento += "<nSeqEvento>1</nSeqEvento>";
		xmlEvento += "<verEvento>1.00</verEvento>";
		xmlEvento += "<detEvento versao=\"1.00\">";
		xmlEvento += "<descEvento>Confirmacao da Operacao</descEvento>";
		xmlEvento += "</detEvento>";
		xmlEvento += "</infEvento>";
		xmlEvento += "</evento>";
		xmlEvento += "</envEvento>";

		try {
			
			Assinatura assinatura = Assinatura.getInstance(apelidoCertificado, senhaCertificado);
			String xmlAssinado = assinatura.assinarInfEvento(xmlEvento);
			System.out.println(xmlAssinado);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

}
