package sm130075.vl130298.types;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

import sun.security.x509.*;

import sm130075.vl130298.sun.*;

import javax.xml.bind.DatatypeConverter;

import sm130075.vl130298.crypto.Algorithm;
import sm130075.vl130298.exception.CertificateAlreadySignedException;
import sm130075.vl130298.sun.PKCS9Attribute;

//Class for Certificate signing request
public class CSR {
	public static final String CERTIFICATE_VERSION_OID = "1.2.840.113549.1.9.9.1";
	public static final String VALIDITY_OID = "1.2.840.113549.1.9.9.2";
	private PKCS10 csr;
	byte[] encoded;
	
	public CSR(KeyStorage keyStorage) {
		try {
			Certificate cert = keyStorage.getCert();

			if (!(cert instanceof UnsignedCert))
				throw new CertificateAlreadySignedException();

			UnsignedCert unsignedCert = (UnsignedCert) cert;
			
			PrivateKey privateKey = keyStorage.getPrivateKey();
			PublicKey publicKey = unsignedCert.getPublicKey();
			X500Name x500Name = new X500Name(unsignedCert.getSubjectX500Principal().getName());

			PKCS10Attribute[] att;
			if (unsignedCert.certEx == null)
				att = new PKCS10Attribute[4];
			else
				att = new PKCS10Attribute[5];
			
			// Add Cert Version attribute
			att[0] = (new PKCS10Attribute(PKCS9Attribute.CERT_VERSION_OID,new Integer(unsignedCert.version.get(CertificateVersion.VERSION))));
			
			// Add serialNum attribute
			att[1] = (new PKCS10Attribute(PKCS9Attribute.SERIAL_OID, unsignedCert.serialNum));
	
			// Add AlgorithmId attribute
			att[2] = (new PKCS10Attribute(PKCS9Attribute.ALGORITHM_ID_OID, unsignedCert.algId));
			
			// Add Interval Validity attribute
			att[3] = (new PKCS10Attribute(PKCS9Attribute.VALIDITY_OID, unsignedCert.interval));
			
			// Add extensions attribute
			if (unsignedCert.certEx != null)
				att[4] = (new PKCS10Attribute(PKCS9Attribute.EXTENSION_REQUEST_OID, unsignedCert.certEx));
			
			PKCS10Attributes pkcs10atts = new PKCS10Attributes(att);
			csr = new PKCS10(publicKey, pkcs10atts);
			
			Signature sig = Signature.getInstance(Algorithm.RSAWITHSHA1.toString());
			sig.initSign(privateKey);
			csr.encodeAndSign(x500Name, sig);
			encoded = csr.getEncoded();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CSR(FileInputStream fStream) {
		try {
			int leftOvers = fStream.available();
			byte[] cert = new byte[leftOvers];
			csr = new PKCS10(cert);
			encoded = csr.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CSR(byte[] cert) {
		try {
			csr = new PKCS10(cert);
			encoded = csr.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] getEncoded() {
		return encoded.clone();
	}

	public String print() {
		return DatatypeConverter.printBase64Binary(encoded);
	}

	public X509CertInfo getCertInfo(X500Name issuer) {
		try {
			X509CertInfo result = new X509CertInfo();
			PKCS10Attribute att;

			// Get version
			att = (PKCS10Attribute) csr.getAttributes().getAttribute(PKCS9Attribute.CERT_VERSION_OID.toString());
			result.set(X509CertInfo.VERSION, new CertificateVersion((Integer)att.getAttributeValue()));

			// Get serial
			att = (PKCS10Attribute) csr.getAttributes().getAttribute(PKCS9Attribute.SERIAL_OID.toString());
			result.set(X509CertInfo.SERIAL_NUMBER, att.getAttributeValue());

			// Get alg id
			att = (PKCS10Attribute) csr.getAttributes().getAttribute(PKCS9Attribute.ALGORITHM_ID_OID.toString());
			result.set(X509CertInfo.ALGORITHM_ID, att.getAttributeValue());

			// Get validity
			att = (PKCS10Attribute) csr.getAttributes().getAttribute(PKCS9Attribute.VALIDITY_OID.toString());
			result.set(X509CertInfo.VALIDITY, att.getAttributeValue());

			// Set subject
			result.set(X509CertInfo.SUBJECT, csr.getSubjectName());

			// Set public key
			result.set(X509CertInfo.KEY, new CertificateX509Key(csr.getSubjectPublicKeyInfo()));
			
			//set issuer
			result.set(X509CertInfo.ISSUER, issuer);

			// Set extensions
			att = (PKCS10Attribute) csr.getAttributes().getAttribute(PKCS9Attribute.EXTENSION_REQUEST_OID.toString());

			if (att != null)
				result.set(X509CertInfo.EXTENSIONS, att.getAttributeValue());

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
