package sm130075.vl130298.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
// PATH PASSWORD AND KEYSTORE --/> 
// SIZE OD KEY VERZIJA PERIOD VAZENJA(DATE TIP) SERIJSKI BROJ , INFO O KORISNIKU , KORISCENJE KLJUCA
// 

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.X500Name;

import java.util.Date;

public class KeyStoreManager {

	private String path;
	private String password;
	private KeyStore keyStore;
	private AES aes;

	public KeyStoreManager(String path, String password)
			throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		this.path = path;
		this.password = password;

		// Create file if it doesnt exist
		keyStore = KeyStore.getInstance("pkcs12");
		Path p = Paths.get(path);
		aes = new AES();
		// check and then create
		if (!Files.exists(p)) {
			// this is creation
			keyStore.load(null, null);
			keyStore.store(new FileOutputStream(path), password.toCharArray());
		}

	}

	public KeyStorage getKeyStorage(String alias, String password) throws NoSuchAlgorithmException,
			CertificateException, IOException, KeyStoreException, UnrecoverableEntryException {
		// Instantiate storage
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		keyStore.load(aes.decryptFile(path, true), this.password.toCharArray());

		// Set protection parameters
		PasswordProtection pwProt = new KeyStore.PasswordProtection(password.toCharArray());

		KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, pwProt);
	
		return new KeyStorage(key.getPrivateKey(), new UnsignedCert(key.getCertificate().getEncoded()));
	}

	public void storeKey(String alias, KeyStorage keyStorage, String password)
			throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		PrivateKey key = keyStorage.getPrivateKey();
		Certificate cert = keyStorage.getCert();

		keyStore.load(null, null);

		// Create certificate chain - only one UNSIGNED certificate
		Certificate[] array = new Certificate[1];
		array[0] = cert;

		keyStore.setKeyEntry(alias, key, password.toCharArray(), array);
		FileOutputStream writeStream = new FileOutputStream(path);
		aes.decryptFile(path, true);
		keyStore.store(writeStream, this.password.toCharArray());
		writeStream.close();
		aes.encryptFile(path);
	}
}
