package sm130075.vl130298.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.cert.Certificate;
// PATH PASSWORD AND KEYSTORE --/> 
// SIZE OD KEY VERZIJA PERIOD VAZENJA(DATE TIP) SERIJSKI BROJ , INFO O KORISNIKU , KORISCENJE KLJUCA
// 

public class KeyStoreManager {

    String path;
    String password;
    KeyStore keyStore;

    public Certificate getCertificate(String alias, String password){
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            FileInputStream fStream = new FileInputStream(path);
            keyStore.load(fStream, this.password.toCharArray());

            if (!keyStore.containsAlias(alias)) {
                return null;
            }

            Certificate cert = keyStore.getCertificateChain(alias)[0];
            fStream.close();

            return cert;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public KeyStoreManager(String path, String password) {
        try {
            this.path = path;
            this.password = password;
            keyStore = KeyStore.getInstance("pkcs12");
            Path p = Paths.get(path);

            if (!Files.exists(p)) {
                keyStore.load(null, null);
                keyStore.store(new FileOutputStream(path), password.toCharArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeKey(Key key, String alias, String password) {
        try {

            keyStore.load(null, null);
            keyStore.setKeyEntry(alias, key, password.toCharArray(), null);
            OutputStream writeStream = new FileOutputStream(path);
            keyStore.store(writeStream, this.password.toCharArray());
            writeStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Key getKey(String alias, String password) {
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            FileInputStream fStream = new FileInputStream(path);
            keyStore.load(fStream, this.password.toCharArray());

            if (!keyStore.containsAlias(alias)) {
                return null;
            }

            Key key = keyStore.getKey(alias, password.toCharArray());
            fStream.close();

            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Key getKey(String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            FileInputStream fStream = new FileInputStream(path);
            keyStore.load(fStream, this.password.toCharArray());
            Key key = keyStore.getKey(alias, null);
            fStream.close();

            return key;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public KeyPair getKeyPair(String alias, String password){
         try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            FileInputStream fStream = new FileInputStream(path);
            keyStore.load(fStream, this.password.toCharArray());

            if (!keyStore.containsAlias(alias)) {
                return null;
            }
            PasswordProtection pwProt = new KeyStore.PasswordProtection(password.toCharArray());
            KeyStore.PrivateKeyEntry key = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, pwProt);
            KeyPair keypair = new KeyPair(key.getCertificate().getPublicKey(), key.getPrivateKey());
            fStream.close();

            return keypair;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    //public key u null treba da uglavim!

    public void storeKey(Key key, String alias, Certificate cert, String password) {
        try {
            keyStore.load(null, null);
            /*niz*/

            Certificate[] array = new Certificate[1];
            array[0] = cert;

            keyStore.setKeyEntry(alias, key, password.toCharArray(), array);
            OutputStream writeStream = new FileOutputStream(path);
            keyStore.store(writeStream, this.password.toCharArray());
            writeStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
