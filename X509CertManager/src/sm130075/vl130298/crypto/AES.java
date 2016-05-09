package sm130075.vl130298.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES {
	private Key key;
	private final String ivString = "AAAAAAAAAAAAAAAA";
	private Cipher cipher;

	// TODO this is hardcoded should be passed by construcotr
	private final String _path = "keystore.p12";
	private final String alias = "_AES_2016_X509_";
	private final String password = "paSSword for A.Es 2016  ";
	private final String storePw = "storePw";

	public AES(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.key = key;
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	}

	public AES() {
		// See if we already have generated key
		Path file = Paths.get("storage.config");
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			if (Files.exists(file)) {
				byte[] keyBytes = Files.readAllBytes(file);
				key = new SecretKeySpec(keyBytes, 0, keyBytes.length, Algorithm.AES.toString());
			} else {
				key = KeyGen.generateKey(Algorithm.AES, 128);
				Files.write(file, key.getEncoded());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String encrypt(String text) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encrypted = cipher.doFinal(text.getBytes());

			return DatatypeConverter.printBase64Binary(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public byte[] encrypt(byte[] toEncrypt) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encrypted = cipher.doFinal(toEncrypt);

			return encrypted;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public byte[] decrypt(byte[] toDecrypt) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			//cipher.update(toDecrypt);
			byte[] original = cipher.doFinal(toDecrypt);

			return original;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	// TODO decrpytSTring

	public void encryptFile(String path) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			Path p = Paths.get(path);
			byte[] result = Files.readAllBytes(p);
			Files.write(p, encrypt(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ByteArrayInputStream decryptFile(String path, boolean overwrite) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			Path p = Paths.get(path);
			byte[] file = Files.readAllBytes(p);
			byte[] decr = decrypt(file);
			
			if(overwrite){
				Files.write(p, decr);
			}
			
			return new ByteArrayInputStream(decr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ByteArrayInputStream decryptFile(String path) {
		return decryptFile(path, false);
	}

/*	public static void main(String[] argv) {
		Path file = Paths.get("temp.config");
		byte[] toWRite = {12, 12, 13, 13, 14, 14, 15, 15};
		try {
			Files.write(file, toWRite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AES aes = new AES();
		aes.encryptFile("temp.config");
		int b;
		int i = 0;
		ByteArrayInputStream os = aes.decryptFile("temp.config");
		byte[] res = new byte[os.available()];
		
		while ((b = os.read()) != -1) {
			res[i++] = (byte) b;
			System.out.print(b);
		}
		System.out.println();
		System.out.print(DatatypeConverter.printBase64Binary(res));
		
	}*/
}
