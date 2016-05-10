package sm130075.vl130298.types;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public class KeyStorage {
	private PrivateKey privateKey;
	private Certificate cert;
	
	//This value should be empty by default
	private String filePath = "";
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	public Certificate getCert() {
		return cert;
	}
	
	public void setCert(Certificate cert) {
		this.cert = cert;
	}
	
	public KeyStorage(PrivateKey privateKey, Certificate cert) {
		this.privateKey = privateKey;
		this.cert = cert;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
