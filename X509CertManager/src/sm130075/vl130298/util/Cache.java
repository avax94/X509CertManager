package sm130075.vl130298.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import sm1300075.vl130298.types.CSR;
import sm1300075.vl130298.types.KeyStorage;
import sm130075.vl130298.crypto.KeyStoreManager;
import sm130075.vl130298.exception.NoSuchAliasException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.x509.X509CertImpl;

public class Cache {
	// PREFIX for Certificate signing request files
	private static final String CSRPREFIX = "-----BEGIN NEW CERTIFICATE REQUEST-----\n";

	// Sufix for Certificate signing request files
	private static final String CSRSUFIX = "\n-----END NEW CERTIFICATE REQUEST-----";

	// Prefix for certificate files
	private static final String CERPREFIX = "-----BEGIN CERTIFICATE-----\n";

	// Sufix for certificate files
	private static final String CERSUFIX = "\n-----END CERTIFICATE-----";

	// Storage for imported and created CSRs
	public static HashMap<String, CSR> csr = new HashMap<>();

	// Storage for imported and created KeyPairs
	public static HashMap<String, KeyStorage> keys = new HashMap<>();

	// Storage for imported and created Certificates
	public static HashMap<String, Certificate> certs = new HashMap<>();

	public static void importKeyPair(String path, String alias, String password, String filePassword)
			throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException,
			UnrecoverableEntryException, NoSuchAliasException {
		if (keys.containsKey(alias)) {
			return;
		}

		KeyStoreManager keyStore = new KeyStoreManager(path, filePassword);
		KeyStorage keyPair = keyStore.getKeyStorage(alias, password);
		keyPair.setFilePath(path);
		keys.put(alias, keyPair);
	}

	public static void importCSR(String path, String alias) throws IOException {
		Path _path = Paths.get(path);
		byte[] bytes = Files.readAllBytes(_path);
		BASE64Decoder bs64 = new BASE64Decoder();

		String encoded64 = new String(bytes);

		// Remove prefix and sufix
		encoded64 = encoded64.replace(CSRPREFIX, "");
		encoded64 = encoded64.replace(CSRSUFIX, "");

		importCSR(alias, bs64.decodeBuffer(encoded64));
	}

	public static void importCSR(String alias, byte[] encoded) {
		if (!csr.containsKey(alias)) {
			csr.put(alias, new CSR(encoded));
		}
	}

	public static void importCertificate(String path, String alias) throws IOException, CertificateException {
		if (!certs.containsKey(alias)) {
			Path _path = Paths.get(path);
			byte[] bytes = Files.readAllBytes(_path);
			String encoded64 = new String(bytes);
			BASE64Decoder bs64 = new BASE64Decoder();

			// remove prefix and sufix
			encoded64 = encoded64.replace(CERPREFIX, "");
			encoded64 = encoded64.replace(CERSUFIX, "");

			// decode and put
			certs.put(alias, new X509CertImpl(bs64.decodeBuffer(encoded64)));
		}
	}

	public static void exportKeyPair(String path, String alias, String password, String filePassword)
			throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException,
			UnrecoverableEntryException, NoSuchAliasException {
		if (!keys.containsKey(alias))
			throw new NoSuchAliasException("Failed to export key! There is no such alias!");

		KeyStorage toExport = keys.get(alias);
		KeyStoreManager keyStore = new KeyStoreManager(path, filePassword);
		keyStore.storeKey(alias, toExport, password);
	}

	public static void exportCSR(String alias, String filePath) throws NoSuchAliasException {
		try {
			if (!csr.containsKey(alias))
				throw new NoSuchAliasException("Failed to export CSR! There is no such alias!");

			CSR c = csr.get(alias);
			BASE64Encoder bs64 = new BASE64Encoder();
			byte[] encoded = c.getEncoded();
			String encoded64 = bs64.encode(encoded);
			String csrFile = CSRPREFIX + encoded64 + CSRSUFIX;

			Path path = Paths.get(filePath);
			Files.write(path, csrFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exportCertificate(String alias, String filePath)
			throws NoSuchAliasException, CertificateEncodingException {
		try {
			if (!certs.containsKey(alias))
				throw new NoSuchAliasException("Failed to export certificate! There is no such alias!");

			Certificate cert = certs.get(alias);
			BASE64Encoder bs64 = new BASE64Encoder();
			byte[] encoded = cert.getEncoded();
			String encoded64 = bs64.encode(encoded);
			String certFile = CERPREFIX + encoded64 + CERSUFIX;

			Path path = Paths.get(filePath);
			Files.write(path, certFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveCSR(String alias, CSR _csr) {
		csr.put(alias, _csr);
	}

	public static void saveCertificate(String alias, Certificate cert) {
		certs.put(alias, cert);
	}

	public static void saveKeyPair(String alias, KeyStorage keyStorage) {
		keys.put(alias, keyStorage);
	}

	// CSR cache and files are always bound so we have to delete file as well
	public static void deleteCSR(String alias) throws IOException  {
		// Path to CSR
		Path path = Paths.get(".\\CSRs\\" + alias + ".p10");
		Files.delete(path);
		csr.remove(alias);
	}

	public static void deleteCertificate(String alias) {
		certs.remove(alias);
	}

	public static void deleteKeyPair(String alias) {
		keys.remove(alias);
	}

	public static KeyStorage getKeyStorage(String alias) throws NoSuchAliasException {
		if (!keys.containsKey(alias))
			throw new NoSuchAliasException("Invalid key pair alias");

		return keys.get(alias);
	}

	public static Certificate getCertificate(String alias) throws NoSuchAliasException {
		if (!certs.containsKey(alias))
			throw new NoSuchAliasException("Invalid certificate alias");

		return certs.get(alias);
	}

	public static CSR getCSR(String alias) throws NoSuchAliasException {
		if (!csr.containsKey(alias))
			throw new NoSuchAliasException("Invalid CSR alias");

		return csr.get(alias);
	}
	
	public static void loadAllCSR() throws IOException{
		CSRLoader csrLoader = new CSRLoader();
		Path path = Paths.get(".\\CSRs\\");
		Files.walkFileTree(path, csrLoader);
	}
}
