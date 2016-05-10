package sm130075.vl130298.exception;

public class CertificateAlreadySignedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5015074182981118380L;
	private final static String message = "This certificate is already signed!";
	
	public CertificateAlreadySignedException(){
		super(message);
	}
}
