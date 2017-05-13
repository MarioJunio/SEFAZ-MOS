package br.com.mos.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import br.com.mos.model.CertificateDetails;
import br.com.mos.utils.Utils;

public class Certificados {

	public enum OS {
		MacOsX, Windows
	}

	public enum Tipo {
		CERTIFICATE, KEYSTORE, BOTH
	}

	public enum Validate {
		VALID, INVALID, EXPIRED
	}

	public static Set<String> getAllCertificates(Tipo tipo) throws Exception {

		OS currentOS = getMyOS();

		if (currentOS == null)
			throw new Exception("Sistema operacional não suportado");

		if (currentOS == OS.MacOsX)
			return getAllCertificatesMacOsX(tipo);
		else if (currentOS == OS.Windows)
			return getAllCertificatesWindows(tipo);

		throw new Exception(getTipoDoesntExistsExceptionMessage());
	}

	private static Set<String> getAllCertificatesWindows(Tipo tipo) throws NoSuchAlgorithmException, KeyStoreException,
			CertificateException, IOException, NoSuchProviderException {

		// O "SunMSCAPI", pode ser retirado
		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		ks.load(null, null);

		return getAliasCertificates(ks, tipo);
	}

	private static Set<String> getAllCertificatesMacOsX(Tipo tipo)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore ks = KeyStore.getInstance("KeychainStore");
		ks.load(null);

		return getAliasCertificates(ks, tipo);
	}

	private static Set<String> getAliasCertificates(KeyStore keyStore, Tipo tipo) throws KeyStoreException {

		Set<String> listAliasCertificados = new HashSet<>();

		Enumeration<String> aliases = keyStore.aliases();

		while (aliases.hasMoreElements()) {

			String aliasKey = (String) aliases.nextElement();

			if (tipo == Tipo.CERTIFICATE && keyStore.isCertificateEntry(aliasKey)) {
				listAliasCertificados.add(aliasKey);
			} else if (tipo == Tipo.KEYSTORE && keyStore.isKeyEntry(aliasKey)) {
				listAliasCertificados.add(aliasKey);
			} else if (tipo == Tipo.BOTH) {
				listAliasCertificados.add(aliasKey);
			}

		}

		return listAliasCertificados;

	}

	public static CertificateDetails getCertificateByAlias(Tipo tipo, String alias)
			throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, Exception {

		KeyStore keyStore = getKeyStoreCertificates(tipo, alias);

		KeyStore.PrivateKeyEntry pvKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias,
				new KeyStore.PasswordProtection("s".toCharArray()));

		X509Certificate x509Cert = (X509Certificate) pvKeyEntry.getCertificate();
		PrivateKey pvKey = pvKeyEntry.getPrivateKey();

		return new CertificateDetails(x509Cert, pvKey);
	}

	/*
	 * TODO: Remover public static X509Certificate getCertificateA1ByAlias(OS
	 * os, Tipo tipo, String alias, String fileCert, String password) throws
	 * KeyStoreException, NoSuchAlgorithmException, CertificateException,
	 * FileNotFoundException, IOException {
	 * 
	 * KeyStore keyStore = KeyStore.getInstance("PKCS12"); keyStore.load(new
	 * FileInputStream(fileCert), password.toCharArray());
	 * 
	 * return (X509Certificate) keyStore.getCertificate(alias); }
	 */

	public static CertificateDetails getCertificateA1(String pathCert, String password) throws Exception {

		// cria KeyStore, ou seja, o certificado no formato .pfx
		KeyStore ks = KeyStore.getInstance("pkcs12");
		CertificateDetails certificateDetails = null;

		try {
			// carrega certificado digital modelo A1, pode ser um arquivo com
			// extensão .pfx ou .cer
			ks.load(new FileInputStream(pathCert), password.toCharArray());

		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
		}

		Enumeration<String> aliasesEnum = ks.aliases();

		while (aliasesEnum.hasMoreElements()) {

			String alias = (String) aliasesEnum.nextElement();

			if (ks.isKeyEntry(alias)) {
				KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
						new KeyStore.PasswordProtection(password.toCharArray()));
				certificateDetails = new CertificateDetails((X509Certificate) pkEntry.getCertificate(),
						pkEntry.getPrivateKey());
				break;
			}
		}

		// certificado digital
		return certificateDetails;

	}

	private static KeyStore getKeyStoreCertificates(Tipo tipo, String alias) throws Exception {
		
		OS currentOS = getMyOS();
		KeyStore keyStore;

		if (currentOS == OS.MacOsX) {
			keyStore = KeyStore.getInstance("KeychainStore");
			keyStore.load(null);
		} else if (currentOS == OS.Windows) {
			keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			keyStore.load(null, null);
		} else {
			throw new Exception(getTipoDoesntExistsExceptionMessage());
		}

		Enumeration<String> aliasEnum = keyStore.aliases();

		while (aliasEnum.hasMoreElements()) {

			String aliasKey = (String) aliasEnum.nextElement();

			if (keyStore.isKeyEntry(aliasKey)) {

				if (keyStore.getCertificateAlias(keyStore.getCertificate(aliasKey)) == alias) {
					break;
				}

			}
		}

		return keyStore;

	}

	public Validate checkCertificateValidate(X509Certificate certificate) {

		try {
			certificate.checkValidity();
		} catch (CertificateExpiredException E) {
			return Validate.EXPIRED;
		} catch (CertificateNotYetValidException E) {
			return Validate.INVALID;
		}

		return Validate.VALID;
	}

	private static String getTipoDoesntExistsExceptionMessage() throws Exception {
		return "Tipo doesnt exists!";
	}

	/**
	 * returns OS: Sistema operacional que a aplicação está executando, ou null,
	 * caso não seja suportado
	 */
	private static OS getMyOS() {
		return Utils.isWindows() ? OS.Windows : (Utils.isMac() ? OS.MacOsX : null);
	}
}
