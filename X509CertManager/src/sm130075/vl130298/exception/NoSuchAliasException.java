package sm130075.vl130298.exception;

//This exception must be caught seperately!
public class NoSuchAliasException extends Exception {
	String poruka;
	
	public NoSuchAliasException(String _poruka){
		super(_poruka);
		poruka = _poruka;
	}
}
