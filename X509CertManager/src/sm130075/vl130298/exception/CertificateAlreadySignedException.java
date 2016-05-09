package sm130075.vl130298.exception;

public class CertificateAlreadySignedException extends Exception {
	private final static String message = "This certificate is already signed!";
	
	public CertificateAlreadySignedException(){
		super(message);
	}
}
