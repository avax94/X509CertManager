package sm130075.vl130298.crypto;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateException;

import sun.security.x509.X509CertInfo;


public class Signer {
	public static void main(String[] argv){
		PublicKey key = KeyGen.generatePair(Algorithm.RSA, 1024).getPublic();
		key.getEncoded();
		X509CertInfo x509certInfo = new X509CertInfo();
		
		try {
			x509certInfo.set(X509CertInfo.SERIAL_NUMBER, arg1);
			x509certInfo.set(X509CertInfo.KEY, key);
			x509certInfo.get(X509CertInfo.KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
