package sm130075.vl130298.crypto;

import javax.crypto.Cipher;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES {
	private Key key;
	private final String ivString = "AAAAAAAAAAAAAAAA";

	//TODO this is hardcoded should be passed by construcotr
	private final String _path = "keystore.p12";
	private final String alias = "_AES_2016_X509_";
	private final String password = "paSSword for A.Es 2016  ";
	private final String storePw = "storePw";
			
	public AES(Key key) {
		this.key = key;
	}

	public AES() {
		// See if we already have generated key
		Path file = Paths.get("storage.config");
		try {
			if (Files.exists(file)) {
				byte[] keyBytes = Files.readAllBytes(file);
				key = new SecretKeySpec(keyBytes, 0, keyBytes.length, Algorithm.AES.toString());
			} else {
				key = KeyGen.generateKey(Algorithm.AES, 128);
				Files.write(file, key.getEncoded());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String encrypt(String text) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			byte[] encrypted = cipher.doFinal(text.getBytes());

			return DatatypeConverter.printBase64Binary(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public String decrypt(String text) {
		try {
			IvParameterSpec iv = new IvParameterSpec(ivString.getBytes("UTF-8"));

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(text));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

/*	public static void main(String[] argv) {
		Path file = Paths.get("temp.config");
		try {
			String s = new String(Files.readAllBytes(file));
			AES aes = new AES();
			s = aes.decrypt(s);
			System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Avax = aes.decrypt(Avax);
		//System.out.println(Avax);
	}*/
}
