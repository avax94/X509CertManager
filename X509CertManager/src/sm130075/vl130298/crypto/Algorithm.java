package sm130075.vl130298.crypto;

public enum Algorithm{
	AES("AES"),
	RSA("RSA");
	
	String text;
	
	private Algorithm(String txt){
		text = txt;
	}
	
	public String toString(){
		return text;
	}
}
