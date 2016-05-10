/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm130075.vl130298.types;

import sun.security.util.DerValue;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

import sm130075.vl130298.crypto.Algorithm;

import sun.security.util.DerOutputStream;
import sun.security.x509.AlgorithmId;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.IssuerAlternativeNameExtension;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.SerialNumber;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
/**
 *
 * @author Lazar
 */
public class UnsignedCert extends X509Certificate {
	X509CertInfo x509certInfo = null;
	private X509CertImpl x509cert = null;
	private byte[] encoded = null;

	CertificateVersion version;
	CertificateSerialNumber serialNum;
	SerialNumber s;
	CertificateAlgorithmId algId;
	CertificateValidity interval;
	X500Name subject;
	CertificateX509Key pubKey;
	BasicConstraintsExtension bse;
	IssuerAlternativeNameExtension issAltNames;
	KeyUsageExtension keyUsageEx;
	CertificateExtensions certEx = null;

	public UnsignedCert(BigInteger bigNum, Date date, Date date1, X500Name _subject, PublicKey pk,
			BasicConstraintsExtension bse, IssuerAlternativeNameExtension issAltN, KeyUsageExtension keyUsage) {
		try {
			version = new CertificateVersion(CertificateVersion.V3);
			serialNum  = new CertificateSerialNumber(bigNum);
			s = new SerialNumber(bigNum);
			algId = new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid));
			interval = new CertificateValidity(date, date1);
			subject = _subject;
			pubKey = new CertificateX509Key(pk);
			this.bse = bse;
			issAltNames = issAltN;
			keyUsageEx = keyUsage;
			
			x509certInfo = new X509CertInfo();
			x509certInfo.set(X509CertInfo.VERSION, version);
			x509certInfo.set(X509CertInfo.SERIAL_NUMBER, serialNum);
			x509certInfo.set(X509CertInfo.ALGORITHM_ID, algId);
			x509certInfo.set(X509CertInfo.SUBJECT, _subject);
			x509certInfo.set(X509CertInfo.VALIDITY, interval);
			x509certInfo.set(X509CertInfo.KEY, pubKey);
			x509certInfo.set(X509CertInfo.ISSUER, new X500Name("cn= , o= "));
			
			certEx = new CertificateExtensions();
			
			if(bse!=null)
				certEx.set(BasicConstraintsExtension.IDENT, bse);
			if(issAltN!=null)
				certEx.set(IssuerAlternativeNameExtension.IDENT, issAltN);
			if(keyUsage!=null)
				certEx.set(KeyUsageExtension.IDENT, keyUsage);
			
			
			if(bse!=null || issAltN!=null || keyUsage!=null)
				x509certInfo.set(X509CertInfo.EXTENSIONS, certEx);
			else
				certEx = null;

			x509cert = new X509CertImpl(x509certInfo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// get unsignedCert from its encoded form
	public UnsignedCert(byte[] encoded) throws IOException, CertificateException {
		// Get DerValue from encoded data
		DerValue val = new DerValue(encoded);

		// Check if we got sequence
		if (val.tag != DerValue.tag_Sequence)
			throw new CertificateParsingException("invalid DER-encoded certificate data");

		// Parse only certInfo, rest is dummy data
		DerValue x509certInfoDer = val.data.getDerValue();
		x509certInfo = new X509CertInfo(x509certInfoDer);

		// Create cert object
		x509cert = new X509CertImpl(x509certInfo);

		version = (CertificateVersion) x509certInfo.get(X509CertInfo.VERSION);
		serialNum = (CertificateSerialNumber) x509certInfo.get(X509CertInfo.SERIAL_NUMBER);
		algId = (CertificateAlgorithmId) x509certInfo.get(X509CertInfo.ALGORITHM_ID);
		interval = (CertificateValidity) x509certInfo.get(X509CertInfo.VALIDITY);
		subject = (X500Name) x509certInfo.get(X509CertInfo.SUBJECT);
		pubKey = new CertificateX509Key(x509cert.getPublicKey());
		//TODO check if works
		bse = (BasicConstraintsExtension) x509cert.getExtension(PKIXExtensions.BasicConstraints_Id);
		issAltNames = (IssuerAlternativeNameExtension) x509cert.getExtension(PKIXExtensions.IssuerAlternativeName_Id);
		keyUsageEx = (KeyUsageExtension) x509cert.getExtension(PKIXExtensions.KeyUsage_Id);
	}

	public X509CertInfo getX509certInfo() {
		return x509certInfo;
	}

	public X509CertImpl getX509cert() {
		return x509cert;
	}

	public CertificateSerialNumber getSerialNum() {
		return serialNum;
	}

	public CertificateAlgorithmId getAlgId() {
		return algId;
	}

	public CertificateValidity getInterval() {
		return interval;
	}

	public X500Name getSubject() {
		return subject;
	}

	public CertificateX509Key getPubKey() {
		return pubKey;
	}

	public BasicConstraintsExtension getBse() {
		return bse;
	}

	public IssuerAlternativeNameExtension getIssAltNames() {
		return issAltNames;
	}

	public KeyUsageExtension getKeyUsageEx() {
		return keyUsageEx;
	}

	public static boolean compare(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}

		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i])
				return false;
		}

		return true;
	}

	@Override
	public Set<String> getCriticalExtensionOIDs() {
		return x509cert.getCriticalExtensionOIDs();
	}

	@Override
	public byte[] getExtensionValue(String arg0) {
		return x509cert.getExtensionValue(arg0);
	}

	@Override
	public Set<String> getNonCriticalExtensionOIDs() {
		return x509cert.getNonCriticalExtensionOIDs();
	}

	@Override
	public boolean hasUnsupportedCriticalExtension() {
		return x509cert.hasUnsupportedCriticalExtension();
	}

	@Override
	public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
		// Nothing to do here since we havent signed anything
	}

	@Override
	public void checkValidity(Date arg0) throws CertificateExpiredException, CertificateNotYetValidException {
		// Nothing to do here since we havent signed anything
	}

	@Override
	public int getBasicConstraints() {
		return x509cert.getBasicConstraints();
	}

	@Override
	public Principal getIssuerDN() {
		return x509cert.getIssuerDN();
	}

	@Override
	public boolean[] getIssuerUniqueID() {
		return x509cert.getIssuerUniqueID();
	}

	@Override
	public boolean[] getKeyUsage() {
		return x509cert.getKeyUsage();
	}

	@Override
	public Date getNotAfter() {
		return x509cert.getNotAfter();
	}

	@Override
	public Date getNotBefore() {
		return x509cert.getNotBefore();
	}

	@Override
	public BigInteger getSerialNumber() {
		return x509cert.getSerialNumber();
	}

	@Override
	public String getSigAlgName() {
		return x509cert.getSigAlgName();
	}

	@Override
	public String getSigAlgOID() {
		return x509cert.getSigAlgOID();
	}

	@Override
	public byte[] getSigAlgParams() {
		return x509cert.getSigAlgParams();
	}

	@Override
	public byte[] getSignature() {
		return x509cert.getSignature();
	}

	@Override
	public Principal getSubjectDN() {
		return x509cert.getSubjectDN();
	}

	@Override
	public boolean[] getSubjectUniqueID() {
		return x509cert.getSubjectUniqueID();
	}

	@Override
	public byte[] getTBSCertificate() throws CertificateEncodingException {
		return x509cert.getTBSCertificate();
	}

	@Override
	public int getVersion() {
		return x509cert.getVersion();
	}

	@Override
	public byte[] getEncoded() throws CertificateEncodingException {
		if (encoded == null) {
			try {
				DerOutputStream out = new DerOutputStream();
				DerOutputStream derOStream = new DerOutputStream();
				x509certInfo.encode(derOStream);
				AlgorithmId algId = AlgorithmId.get(Algorithm.RSAWITHSHA1.toString());
				byte[] rawCert = derOStream.toByteArray();
				algId.encode(derOStream);
				derOStream.putBitString(rawCert);
				out.write(DerValue.tag_Sequence, derOStream);
				encoded = out.toByteArray();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return encoded;
	}

	@Override
	public PublicKey getPublicKey() {
		return x509cert.getPublicKey();
	}

	@Override
	public String toString() {
		return x509cert.toString();
	}

	@Override
	public void verify(PublicKey arg0) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException {
		// nothing to verify since we havent signed anything
	}

	@Override
	public void verify(PublicKey arg0, String arg1) throws CertificateException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		// nothing to verify since we havent signed anything
	}
}
