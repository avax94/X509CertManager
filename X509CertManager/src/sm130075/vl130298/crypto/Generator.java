package sm130075.vl130298.crypto;

import sm130075.vl130298.exception.NoSuchAliasException;
import sm130075.vl130298.exception.UnknownCertificateException;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.IssuerAlternativeNameExtension;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;

import java.security.cert.X509Certificate;

public class Generator {
	// This method also saves CSR to cache
	public static CSR generateSigningRequest(String alias) throws NoSuchAliasException {
		KeyStorage keyStorage = Cache.getKeyStorage(alias);
		CSR csr = new CSR(keyStorage);
		Cache.saveCSR(alias, csr);

		return csr;
	}

	// This method also saves CSR to cache
	public static KeyStorage generateKeyPair(String alias, int keySize, BigInteger bigNum, Date date, Date date1,
			X500Name _subject, BasicConstraintsExtension bse, IssuerAlternativeNameExtension issAltN,
			KeyUsageExtension keyUsage) throws NoSuchAlgorithmException {
		KeyPair keypair = KeyGen.generatePair(Algorithm.RSAWITHSHA1, keySize);
		UnsignedCert uc = new UnsignedCert(bigNum, date, date1, _subject, keypair.getPublic(), bse, issAltN, keyUsage);

		return null;
	}

	public static Certificate issueCertificate(String csrAlias, String keyStorageAlias)
			throws NoSuchAliasException, InvalidKeyException, CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException, SignatureException, IOException, UnknownCertificateException {
		// Get key for signing
		KeyStorage keyStorage = Cache.getKeyStorage(keyStorageAlias);
		PrivateKey signingKey = keyStorage.getPrivateKey();

		if (!(keyStorage.getCert() instanceof X509Certificate))
			throw new UnknownCertificateException();

		X509Certificate issuerCert = (X509Certificate) keyStorage.getCert();
		
		//Issuer name is name of subject related to public key
		X500Name issuer = new X500Name(issuerCert.getSubjectX500Principal().getName());
		// Get signing request
		CSR signingRequest = Cache.getCSR(csrAlias);

		// Create certificate for signing and sign it
		X509CertImpl x509Cert = new X509CertImpl(signingRequest.getCertInfo(issuer));
		x509Cert.sign(signingKey, signingKey.getAlgorithm());

		// now we have to remove it from CSR
		Cache.deleteCSR(csrAlias);

		return x509Cert;
	}
}
