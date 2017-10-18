package es.salazaryasociados.services.data.exceptions;

import es.salazaryasociados.services.data.exceptions.GestorErrors;

public class DataServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2128824170295466494L;
	private GestorErrors error = GestorErrors.NO_ERROR;
	private String aditionalMessage = "";
	
	public DataServiceException(GestorErrors err){
		super(err.getMessage());
		error = err;
	}
	
	public DataServiceException(GestorErrors err, Throwable e){
		super(e);
		error = err;
	}
	
	public DataServiceException(GestorErrors err, String m) {
		aditionalMessage = (m == null ? "" : m);
		error = err;
	}

	public String getMessage(){
		String message = error.getMessage() + ". " + aditionalMessage;
		return message;
	}
	
	public GestorErrors getErrorCode(){
		return error;
	}

}
