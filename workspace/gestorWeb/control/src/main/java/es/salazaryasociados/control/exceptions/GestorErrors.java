package es.salazaryasociados.control.exceptions;

public enum GestorErrors {
	NO_ERROR ("No hay errores"),
	
	UNKNOWN_EXCEPTION ("Error no tratado"),
	
	FILE_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a expedientes"),
	CLIENT_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a clientes"),
	RESPONSABLE_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a responsables"),
	PAGO_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a pagos"),
	DOCUMENTO_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a documentos"),
	USER_DAO_NOT_DEFINED("No se ha configurado el servicio de acceso a usuarios"),
	
	PERSISTENCE_EXCEPTION("Ocurrió un error al guardar el objeto"),
	
	EXP_NULL("No se puede guardar un expediente nulo"),
	EXP_NO_CLIENTS("No se permiten expedientes sin clientes"),
	EXP_NO_RESPONSABLE("No se permiten expedientes sin responsable"),
	EXP_NO_ISSUE("No se permiten expedientes sin asunto"),
	EXP_NOT_FOUND("Expediente no encontrado"),
	
	CLIENT_NULL("No se puede guardar un cliente nulo"),
	CLIENT_NO_NAME("No se permiten clientes sin nombre"),
	CLIENT_NO_SURNAME("No se permiten clientes sin apellidos"),
	CLIENT_NOT_FOUND("Cliente no encontrado"),
	
	PAGO_NULL("No se puede guardar un pago nulo"),
	PAGO_AMOUNT_ZERO("No se pueden efectuar pagos de cantidades menores o igual a 0"),
	PAGO_EXP_NULL("No se puede hacer un pago sin expediente"),
	PAGO_CLIENT_NULL("Se debe especificar un pagador"),
	
	RESPONSABLE_NULL("No se puede guardar un responsable nulo"),
	RESPONSABLE_NO_NAME("No se puede guardar un responsable sin nombre"),
	
	USER_NULL("No se puede guardar un usuario nulo"),
	USER_NO_USERNAME("No se puede guardar un usuario sin nombre"),
	USER_NO_PASSWORD("No se puede guardar un usuario sin password"),
	USER_NO_ROLES("No se puede guardar un usuario sin roles"),
	USER_EXISTS("No se puede guardar el usuario, ya existe un usuario con el mismo nombre"), 
	UNIQUE_CONSTRAINT("Restricción única violada"),
	
	DATA_EXCEPTION("Excepción no controlada"),
	
	ADD_CLIENT_TO_EXP_NOT_FOUND("Añadir cliente: No se encuentra el expediente"),
	ADD_CLIENT_TO_EXP_CLIENT_NOT_FOUND("Añadir cliente: No se encuentra el cliente"),
	REMOVE_CLIENT_FROM_EXP_NOT_FOUND("Eliminar cliente: No se encuentra el expediente"),
	
	ADD_PAGO_TO_EXP_NO_VALID_CLIENT("El pagador no es cliente del expediente"),
	REMOVE_PAGO_FROM_EXP_NOT_FOUND("Eliminar pago: No se encuentra el expediente"), 
	
	CONFIGURACION_DAO_NOT_DEFINED("No se ha definido el servicio de acceso a la configuración"), 
	CONFIGURACION_NOT_EXISTS ("No se pueden crear objetos nuevos de configuración"), 
	CONFIGURACION_THEME_NOT_EXISTS ("El tema seleccionado no existe"), 
	LISTADO_EXP_DAO_NOT_DEFINED ("No se ha definido el DAO para listado de expedientes por responsable")
	;
	
	private String message = "";
	public final String getMessage() {
		return message;
	}
	
	GestorErrors(String mssg){
		message = mssg;
	}

	public GestorErrors setMessage(String mssg) {
		message = mssg;
		return this;
	}
}
