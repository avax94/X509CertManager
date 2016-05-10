package sm130075.vl130298.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

public class AES {
	private Key key;
	private final String ivString = "AAAAAAAAAAAAAAAA";
	private Cipher cipher;

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
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] original = cipher.doFinal(toDecrypt);

			return original;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

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
}
