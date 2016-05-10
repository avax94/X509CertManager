package sm130075.vl130298.crypto;

import sm130075.vl130298.exception.NoSuchAliasException;
import sun.security.x509.X509CertImpl;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;

import java.security.cert.X509Certificate;

public class Generator {
	public static CSR generateSigningRequest(String alias) throws NoSuchAliasException{
		KeyStorage keyStorage = Cache.getKeyStorage(alias);
		CSR csr = new CSR(keyStorage);
		Cache.saveCSR(alias, csr);
		
		return csr;
	}
	
	public static KeyStorage generateKeyPair(String alias, int keySize, int version, Date notBefore, Date notAfter,  BigInteger serial ){
		//TODO
		return null;
	}
	
	public static Certificate issueCertificate(String csrAlias, String keyStorageAlias) throws NoSuchAliasException, InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException{
		//Get key for signing
		KeyStorage keyStorage = Cache.getKeyStorage(keyStorageAlias);
		PrivateKey signingKey = keyStorage.getPrivateKey();
		
		//Get signing request
		CSR signingRequest = Cache.getCSR(csrAlias);
		
		//Create certificate for signing and sign it
		X509CertImpl x509Cert = new X509CertImpl(signingRequest.getCertInfo());
		x509Cert.sign(signingKey, signingKey.getAlgorithm());
		
		//now we have to remove it from CSR
		return x509Cert;
	}
}
