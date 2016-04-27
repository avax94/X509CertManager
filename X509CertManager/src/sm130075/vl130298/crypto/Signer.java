package sm130075.vl130298.crypto;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

import sun.security.x509.X509CertInfo;


public class Signer extends Certificate {
	public PublicKey publicKey;
	public Signer(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv){
		KeyPair key = KeyGen.generatePair(Algorithm.RSA, 1024);
		Signer cert = new Signer(Algorithm.RSAwithSHA1.toString());
		cert.publicKey = key.getPublic();
		KeyStoreManager keystore = new KeyStoreManager("avaxxxx.pk12", "hehe");
		keystore.storeKey(key.getPrivate(), "avax", cert, "pw");
		KeyPair keypair = keystore.getKeyPair("avax", "pw");
	}

	@Override
	public byte[] getEncoded() throws CertificateEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return publicKey;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		// TODO Auto-generated method stub
		
	}
}
