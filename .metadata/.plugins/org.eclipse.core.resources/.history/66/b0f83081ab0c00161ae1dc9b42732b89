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
<<<<<<< HEAD
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
=======
		PublicKey key = KeyGen.generatePair(Algorithm.RSA, 1024).getPublic();
		key.getEncoded();
		X509CertInfo x509certInfo = new X509CertInfo();
		
		try {
			
			x509certInfo.set(X509CertInfo.KEY, key);
			x509certInfo.get(X509CertInfo.KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
>>>>>>> da42c6d49e7e8252fd69afafd93caffaffe52884
		
	}

	@Override
	public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		// TODO Auto-generated method stub
		
	}
}
