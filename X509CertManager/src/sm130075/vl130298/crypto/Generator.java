package sm130075.vl130298.crypto;

import sm130075.vl130298.exception.NoSuchAliasException;

import java.math.BigInteger;
import java.util.Date;

public class Generator {
	public static void generateSigningRequest(String alias) throws NoSuchAliasException{
		KeyStorage keyStorage = Cache.getKeyStorage(alias);
	}
	
	public static KeyStorage generateKeyPair(String alias, int keySize, int version, Date notBefore, Date notAfter,  BigInteger serial ){
		//TODO finish this
		return null;
	}
}
