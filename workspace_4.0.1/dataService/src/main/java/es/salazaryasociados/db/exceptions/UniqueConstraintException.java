package es.salazaryasociados.db.exceptions;

public class UniqueConstraintException extends DataException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2164495052591693136L;
	
	private String[] columnNames;
	public UniqueConstraintException(GestorErrors err, String[] names) {
		super(err);
		columnNames = names;
	}

	public String getMessage(){
		String message = super.getMessage();

		String names = "";
        for (String columnName : columnNames) {
            names += columnName;
            names += ",";
        }
        names = names.substring(0, names.length() - 1);
        
        message += "\n" + names;
		
		return message;
	}
}
