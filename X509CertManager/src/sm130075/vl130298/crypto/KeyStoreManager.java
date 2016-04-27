package sm130075.vl130298.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.KeyStore.PasswordProtection;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateFactorySpi;

public class KeyStoreManager {
	String path;
	String password;
	KeyStore keyStore;

	public KeyStoreManager(String path, String password) {
		try {
			this.path = path;
			this.password = password;
			keyStore = KeyStore.getInstance("pkcs12");
			Path p = Paths.get(path);
			
			if(!Files.exists(p)){
				keyStore.load(null, null);
		        keyStore.store(new FileOutputStream(path), password.toCharArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void storeKey(Key key, String alias, String password) {
		try {
			
			keyStore.load(null, null);
			keyStore.setKeyEntry(alias, key, password.toCharArray(), null);
			OutputStream writeStream = new FileOutputStream(path);
			keyStore.store(writeStream, this.password.toCharArray());
			writeStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Key getKey(String alias, String password) {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			FileInputStream fStream = new FileInputStream(path);
			keyStore.load(fStream, this.password.toCharArray());
			
			if (!keyStore.containsAlias(alias)) { 
				 return null;
			} 
			
			Key key = keyStore.getKey(alias, password.toCharArray());
			fStream.close();
			
			
			return key;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public Key getKey(String alias) {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			FileInputStream fStream = new FileInputStream(path);
			keyStore.load(fStream, this.password.toCharArray());
			Key key = keyStore.getKey(alias, null);
			fStream.close();
			
			return key;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public KeyPair getKeyPair(String alias, String password) {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			FileInputStream fStream = new FileInputStream(path);
			keyStore.load(fStream, this.password.toCharArray());
			PasswordProtection pwProt = new PasswordProtection(password.toCharArray());
			KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, pwProt);
			fStream.close();
			KeyPair result = new KeyPair(key.getCertificate().getPublicKey(), key.getPrivateKey());
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public void storeKey(Key key, String alias, java.security.cert.Certificate c, String password) {
		try {
			keyStore.load(null, null);
			java.security.cert.Certificate[] cr = new java.security.cert.Certificate[1];
			cr[0] = c;
			keyStore.setKeyEntry(alias, key, password.toCharArray(), cr);
			OutputStream writeStream = new FileOutputStream(path);
			keyStore.store(writeStream, this.password.toCharArray());
			writeStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(){
		try {
			CertificateFactory cf = CertificateFactory.getInstance("RSA");	
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
