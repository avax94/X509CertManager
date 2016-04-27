/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm130075.vl130298.crypto;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Date;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/**
 *
 * @author Lazar
 */
public class UnsignedCert {

    protected CertificateVersion version = new CertificateVersion();
    protected CertificateSerialNumber serialNum = null;
    protected CertificateAlgorithmId algId = null;
    protected CertificateIssuerName issuer = null;
    protected CertificateValidity interval = null;
    protected CertificateSubjectName subject = null;
    protected CertificateX509Key pubKey = null;
    
    private X509CertInfo x509certInfo = null;
    private X509CertImpl x509;
    

    public UnsignedCert(BigInteger bigNum, X500Name issuer, Date date, Date date1, X500Name subject, PublicKey pk) {
        try {
            this.version = new CertificateVersion(CertificateVersion.V3);
            this.serialNum = new CertificateSerialNumber(bigNum);
            this.algId = new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid));
            this.issuer = new CertificateIssuerName(issuer);
            this.interval = new CertificateValidity(date, date1);
            this.subject = new CertificateSubjectName(subject);
            this.pubKey = new CertificateX509Key(pk);
            
            this.x509certInfo = new X509CertInfo();
            x509certInfo.set(X509CertInfo.VERSION, version);
            x509certInfo.set(X509CertInfo.SERIAL_NUMBER, serialNum);
            x509certInfo.set(X509CertInfo.ALGORITHM_ID, algId );
            x509certInfo.set(X509CertInfo.ISSUER , issuer);
            x509certInfo.set(X509CertInfo.SUBJECT, subject);
            x509certInfo.set(X509CertInfo.VALIDITY,interval );
            x509certInfo.set(X509CertInfo.KEY, pubKey);
            
            x509 = new X509CertImpl(x509certInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setVersion(CertificateVersion version) {
        this.version = version;
    }

    public CertificateSerialNumber getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(CertificateSerialNumber serialNum) {
        this.serialNum = serialNum;
    }

    public CertificateAlgorithmId getAlgId() {
        return algId;
    }

    public void setAlgId(CertificateAlgorithmId algId) {
        this.algId = algId;
    }

    public CertificateIssuerName getIssuer() {
        return issuer;
    }

    public void setIssuer(CertificateIssuerName issuer) {
        this.issuer = issuer;
    }

    public CertificateValidity getInterval() {
        return interval;
    }

    public void setInterval(CertificateValidity interval) {
        this.interval = interval;
    }

    public CertificateSubjectName getSubject() {
        return subject;
    }

    public void setSubject(CertificateSubjectName subject) {
        this.subject = subject;
    }

    public CertificateX509Key getPubKey() {
        return pubKey;
    }

    public void setPubKey(CertificateX509Key pubKey) {
        this.pubKey = pubKey;
    }

    public X509CertImpl getCert(){
        return x509;
    }
    public static void main(String[] argv){
        
        try{
            KeyPair keyPair = KeyGen.generatePair(Algorithm.RSA , 1024);
        UnsignedCert uc = new UnsignedCert(BigInteger.ONE,
                          new X500Name("cn=test,o=gina"),
                          new Date(System.currentTimeMillis()),
                          new Date(2*System.currentTimeMillis()),
                          new X500Name("cn=test,o=gina"),
                          keyPair.getPublic());
        
        uc.getCert().sign(keyPair.getPrivate(), Algorithm.RSAWITHSHA1.toString());
        KeyStoreManager keyStore = new KeyStoreManager("laza.pk12", "zmajce");
        keyStore.storeKey(keyPair.getPrivate(), "avax4", uc.getCert(), "zmaj");
        KeyPair keyPair2 = keyStore.getKeyPair("avax4", "zmaj");
        X509CertImpl x509certImpl = new X509CertImpl(keyStore.getCertificate("avax4", "").getEncoded());
        System.out.println("whaaaat");
        
        } catch(Exception e){
            e.printStackTrace();
        }
       
        System.out.println("Nesto za kraj!");
    }
}
