package sm130075.vl130298.crypto;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import sm130075.vl130298.exception.NoSuchAliasException;
import sm130075.vl130298.types.KeyStorage;
import sm130075.vl130298.types.UnsignedCert;

import java.util.Enumeration;

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
			aes.encryptFile(path);
		}

	}

	public KeyStorage getKeyStorage(String alias, String password) throws NoSuchAlgorithmException,
			CertificateException, IOException, KeyStoreException, UnrecoverableEntryException, NoSuchAliasException {
		// Instantiate storage
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		ByteArrayInputStream store = aes.decryptFile(path);
	
		keyStore.load(store, this.password.toCharArray());

		// Set protection parameters
		PasswordProtection pwProt = new KeyStore.PasswordProtection(password.toCharArray());
		
		
		Enumeration<String> s = keyStore.aliases();
		while(s.hasMoreElements()){
			System.out.println(s.nextElement());
		}
		KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, pwProt);

		if (key == null)
			throw new NoSuchAliasException("Alias not found in key storage!");

		return new KeyStorage(key.getPrivateKey(), new UnsignedCert(key.getCertificate().getEncoded()));
	}

	public void storeKey(String alias, KeyStorage keyStorage, String password)
			throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
		PrivateKey key = keyStorage.getPrivateKey();
		Certificate cert = keyStorage.getCert();
		ByteArrayInputStream store = aes.decryptFile(path);
		keyStore.load(store, this.password.toCharArray());

		// Create certificate chain - only one UNSIGNED certificate
		Certificate[] array = new Certificate[1];
		array[0] = cert;
		
		//TODO try to encrypt it with CipherOutputStream
		keyStore.setKeyEntry(alias, key, password.toCharArray(), array);
		FileOutputStream writeStream = new FileOutputStream(path);
		keyStore.store(writeStream, this.password.toCharArray());
		writeStream.close();
		aes.encryptFile(path);
	}
}
