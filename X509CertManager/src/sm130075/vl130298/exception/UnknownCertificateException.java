package sm130075.vl130298.exception;

public class UnknownCertificateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String poruka = "Invalid certificate type!";
	
	public UnknownCertificateException(){
		super(poruka);
	}
}
