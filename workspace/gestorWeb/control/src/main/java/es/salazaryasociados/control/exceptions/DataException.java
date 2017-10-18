package es.salazaryasociados.control.exceptions;

public class DataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5602453214265572822L;
	
	private GestorErrors error = GestorErrors.NO_ERROR;
	private String aditionalMessage = "";
	
	public DataException(GestorErrors err){
		super(err.getMessage());
		error = err;
	}
	
	public DataException(GestorErrors err, Throwable e){
		super(e);
		error = err;
	}
	
	public DataException(GestorErrors err, String m) {
		aditionalMessage = (m == null ? "" : m);
		error = err;
	}

	public String getMessage(){
		String message = error.getMessage() + "." + aditionalMessage;
		return message;
	}
	
	public GestorErrors getErrorCode(){
		return error;
	}
}
