package sm130075.vl130298.crypto;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PKCS12Attribute;

import javax.security.*;
import javax.xml.bind.DatatypeConverter;

import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;


public class KeyPairEntry implements KeyStore.Entry{
	KeyStore.Entry.Attribute[]attributes = new KeyStore.Entry.Attribute[2];
	public KeyPair keyPair;
	public KeyPairEntry(KeyPair keyPair){
		this.keyPair = keyPair;
		attributes[0] = new PKCS12Attribute("1.2.3.4", DatatypeConverter.printBase64Binary(keyPair.getPublic().getEncoded()));
		attributes[1] = new PKCS12Attribute("1.2.3.5", DatatypeConverter.printBase64Binary(keyPair.getPublic().getEncoded()));
	}
}
