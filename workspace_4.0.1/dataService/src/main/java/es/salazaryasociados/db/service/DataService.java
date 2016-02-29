package es.salazaryasociados.db.service;

import static java.util.Collections.singletonList;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

import es.salazaryasociados.db.control.ClientDao;
import es.salazaryasociados.db.control.ConfiguracionDao;
import es.salazaryasociados.db.control.DocumentoDao;
import es.salazaryasociados.db.control.ExpedienteDao;
import es.salazaryasociados.db.control.ListadoExpRespDao;
import es.salazaryasociados.db.control.PagoDao;
import es.salazaryasociados.db.control.ResponsableDao;
import es.salazaryasociados.db.control.UserDao;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.exceptions.GestorErrors;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Configuracion;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.ListadoExpResp;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.model.Role;
import es.salazaryasociados.db.model.User;

@Transactional
public class DataService implements IDataService, ValidationProviderResolver   {

	private ClientDao clientDao;
	private ExpedienteDao fileDao;
	private ResponsableDao responsableDao;
	private UserDao userDao;
	private PagoDao pagoDao;
	private DocumentoDao documentoDao;
	private ConfiguracionDao configuracionDao;
	private ListadoExpRespDao listRespExpDao;
	
	private ValidatorFactory validatorF;
	
	private final static Map<String, String> themes;
	static {
		themes = new TreeMap<String, String>();
		themes.put("Aristo", "aristo");
		themes.put("Black-Tie", "black-tie");
		themes.put("Blitzer", "blitzer");
		themes.put("Bluesky", "bluesky");
		themes.put("Casablanca", "casablanca");
		themes.put("Cupertino", "cupertino");
		themes.put("Dark-Hive", "dark-hive");
		themes.put("Dot-Luv", "dot-luv");
		themes.put("Eggplant", "eggplant");
		themes.put("Excite-Bike", "excite-bike");
		themes.put("Flick", "flick");
		themes.put("Glass-X", "glass-x");
		themes.put("Hot-Sneaks", "hot-sneaks");
		themes.put("Humanity", "humanity");
		themes.put("Le-Frog", "le-frog");
		themes.put("Midnight", "midnight");
		themes.put("Mint-Choc", "mint-choc");
		themes.put("Overcast", "overcast");
		themes.put("Pepper-Grinder", "pepper-grinder");
		themes.put("Redmond", "redmond");
		themes.put("Rocket", "rocket");
		themes.put("Sam", "sam");
		themes.put("Smoothness", "smoothness");
		themes.put("South-Street", "south-street");
		themes.put("Start", "start");
		themes.put("Sunny", "sunny");
		themes.put("Swanky-Purse", "swanky-purse");
		themes.put("Trontastic", "trontastic");
		themes.put("UI-Darkness", "ui-darkness");
		themes.put("UI-Lightness", "ui-lightness");
		themes.put("Vader", "vader");		
	}
	
	private ValidatorFactory getValidatorF() {
		if (validatorF == null)
		{

			/*
			Thread.currentThread().setContextClassLoader(DataService.class.getClassLoader());
		    ApacheValidationProvider p = new ApacheValidationProvider();									
			validatorF = new ConfigurationImpl(null, p).buildValidatorFactory();*/
	
			/*validatorF = Validation.byProvider( HibernateValidator.class)
					.configure()
					.buildValidatorFactory();*/
			
			Configuration<?> config = Validation.byDefaultProvider()
					.providerResolver(this).configure();
			validatorF = config.buildValidatorFactory();
		}
		return validatorF;
	}
	
	@Override
	public List getValidationProviders() {
		return singletonList(new HibernateValidator());
	}	
	
	public void setValidatorF(ValidatorFactory validatorF) {
		this.validatorF = validatorF;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public ExpedienteDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(ExpedienteDao fileDao) {
		this.fileDao = fileDao;
	}

	public ResponsableDao getResponsableDao() {
		return responsableDao;
	}

	public void setResponsableDao(ResponsableDao responsableDao) {
		this.responsableDao = responsableDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public PagoDao getPagoDao() {
		return pagoDao;
	}

	public void setPagoDao(PagoDao pDao) {
		this.pagoDao = pDao;
	}

	public DocumentoDao getDocumentoDao() {
		return documentoDao;
	}

	public void setDocumentoDao(DocumentoDao documentoDao) {
		this.documentoDao = documentoDao;
	}

	public ConfiguracionDao getConfiguracionDao() {
		return configuracionDao;
	}

	public void setConfiguracionDao(ConfiguracionDao configuracionDao) {
		this.configuracionDao = configuracionDao;
	}

	public ListadoExpRespDao getListRespExpDao() {
		return listRespExpDao;
	}

	public void setListRespExpDao(ListadoExpRespDao listRespExpDao) {
		this.listRespExpDao = listRespExpDao;
	}

	public List<Cliente> getAllClients(int pageSize, int first,
			Map<String, Object> params, String order, boolean desc) throws DataException {
		if (clientDao != null)
		{
			return clientDao.getAll(pageSize, first, params, order, desc);
		}
		throw new DataException(GestorErrors.CLIENT_DAO_NOT_DEFINED);
	}

	public Cliente getClientById(int id, String ... fetch) throws DataException {
		if (clientDao != null)
		{
			// Esta es la solución más sencilla. Lo suyo es usar JPA 2.1 y NamedEntityGraph
			Cliente result = clientDao.getById(id, fetch);
			return result;
		}
		throw new DataException(GestorErrors.CLIENT_DAO_NOT_DEFINED);
	}

	public List<Expediente> getAllFiles(int pageSize, int first,
			Map<String, Object> params, String order, boolean desc) throws DataException {
		if (fileDao != null)
		{
			return fileDao.getAll(pageSize, first, params, order, desc);
		}
		throw new DataException(GestorErrors.FILE_DAO_NOT_DEFINED);
	}

	public Expediente getFileById(int id, String ... fetch) throws DataException {
		if (fileDao != null)
		{
			// Esta es la solución más sencilla. Lo suyo es usar JPA 2.1 y NamedEntityGraph
			Expediente result = fileDao.getById(id, fetch);			
			return result;
		}
		throw new DataException(GestorErrors.FILE_DAO_NOT_DEFINED);
	}

	public long getFilesCount (final Map<String, Object> params) throws DataException {
		if (fileDao != null)
		{
			return fileDao.getCount(params);
		}
		throw new DataException(GestorErrors.FILE_DAO_NOT_DEFINED);
	}
	
	public long getClientsCount (final Map<String, Object> params) throws DataException {
		if (clientDao != null)
		{
			return clientDao.getCount(params);
		}
		throw new DataException(GestorErrors.CLIENT_DAO_NOT_DEFINED);		
	}

	public List<Responsable> getAllResponsables(int pageSize, int first,
			Map<String, Object> params, String order, boolean desc) throws DataException {
		if (responsableDao != null)
		{
			return responsableDao.getAll(pageSize, first, params, order, desc);
		}
		throw new DataException(GestorErrors.RESPONSABLE_DAO_NOT_DEFINED);
	}

	public long getResponsablesCount (final Map<String, Object> params) throws DataException {
		if (responsableDao != null)
		{
			return responsableDao.getCount(params);
		}
		throw new DataException(GestorErrors.RESPONSABLE_DAO_NOT_DEFINED);		
	}
	
	public Responsable getResponsableById(int id, String ... fetch) throws DataException {
		if (responsableDao != null)
		{
			// Esta es la solución más sencilla. Lo suyo es usar JPA 2.1 y NamedEntityGraph
			Responsable result = responsableDao.getById(id, fetch);
			return result;
		}
		throw new DataException(GestorErrors.RESPONSABLE_DAO_NOT_DEFINED);
	}
	
	public List<User> getAllUsers(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException{
		if (userDao != null)
		{
			return userDao.getAll(pageSize, first, params, order, desc);
		}
		throw new DataException(GestorErrors.FILE_DAO_NOT_DEFINED);		
	}
	
	public User getUser(String userName) throws DataException {
		User result = null;
		
		if (userDao != null)
		{
			result = userDao.getUser(userName);
			if (result != null)
			{
				// Se obtiene el usuario para que obtenga todos los roles:
				try{
					result = userDao.getById(result.getId());
				}
				catch(PersistenceException p)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, p);
				}				
			}
			return result;
		}
		
		throw new DataException(GestorErrors.USER_DAO_NOT_DEFINED);
	}
	
	public Expediente saveExpediente(Expediente exp) throws DataException
	{
		Expediente result = null;
		//Primero comprobar la integridad del expediente:
		if (fileDao != null)
		{
			//Primero comprobar la integridad del expediente:
			GestorErrors err = checkExpIntegrity(exp);
			if (err == GestorErrors.NO_ERROR)
			{
				if (exp.getId() == null)
				{					
					int lastIndex = getLaxExpID();
					exp.setId(lastIndex + 1);
					result = fileDao.persist(exp);
				}
				else
				{
					result = fileDao.update(exp);
				}
				return result;
			}
			throw new DataException(err);
		}
		throw new DataException(GestorErrors.FILE_DAO_NOT_DEFINED);
	}

	private GestorErrors checkExpIntegrity(Expediente exp) {
		GestorErrors err = GestorErrors.NO_ERROR;
		
		if (exp == null)
			err = GestorErrors.EXP_NULL;
		
		else if (getValidatorF() != null)
		{
			Validator validator = validatorF.getValidator();			
			Set<ConstraintViolation<Expediente>> constraints = validator.validate(exp);
			if (constraints.size() > 0)
			{
				ConstraintViolation<Expediente> constraint = constraints.iterator().next();
				err = GestorErrors.DATA_EXCEPTION.setMessage(constraint.getMessage());
			}	
		}				
		
		if (err == GestorErrors.NO_ERROR)
		{
			if (exp.getClientes() == null || exp.getClientes().size() <= 0)
				err = GestorErrors.EXP_NO_CLIENTS;
			else if (exp.getResponsable1() == null)
				err = GestorErrors.EXP_NO_RESPONSABLE;
		}
		
		return err;
	}

	private int getLaxExpID() throws DataException {
		List<Expediente> exps = getAllFiles(1, 0, null, "id", true);
		int result = 0;
		if (exps != null && exps.size() > 0)
		{
			result = exps.get(0).getId();
		}
		return result;
	}
	
	public Cliente saveCliente(Cliente client) throws DataException
	{
		Cliente result = null;
		if (clientDao != null)
		{
			GestorErrors err = checkCliente(client);
			if (err == GestorErrors.NO_ERROR)
			{
				try{
					if (client.getId() == null || client.getId().intValue() < 0)
					{
						result = clientDao.persist(client);
					}
					else
					{
						result = clientDao.update(client);
					}
				}catch(PersistenceException persistException)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
				}
				return result;
			}
			throw new DataException(err);
		}
		throw new DataException(GestorErrors.CLIENT_DAO_NOT_DEFINED);
	}

	private GestorErrors checkCliente(Cliente client){

		GestorErrors err = GestorErrors.NO_ERROR;
		if (client == null)
			err = GestorErrors.CLIENT_NULL;
		else if (getValidatorF() != null)
		{
			Validator validator = validatorF.getValidator();			
			Set<ConstraintViolation<Cliente>> constraints = validator.validate(client);
			if (constraints.size() > 0)
			{
				ConstraintViolation<Cliente> constraint = constraints.iterator().next();
				err = GestorErrors.DATA_EXCEPTION.setMessage(constraint.getMessage());
			}	
		}		
		return err;
	}
	
	protected Pago savePago(Pago pago)  throws DataException
	{
		Pago result = null;
		if (pagoDao != null)
		{
			GestorErrors err = checkPago(pago);
			if (err == GestorErrors.NO_ERROR)
			{
				if (pago.getFecha() == null)
				{
					Date today = new Date();
					pago.setFecha(new Timestamp(today.getTime()));
				}
				try{
					if (pago.getId() == null || pago.getId().intValue() < 0)
					{
						result = pagoDao.persist(pago);
					}
					else
					{
						result = pagoDao.update(pago);
					}
				}catch(PersistenceException persistException)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
				}
				return result;
			}
			throw new DataException(err);
		}
		throw new DataException(GestorErrors.PAGO_DAO_NOT_DEFINED);
	}

	private GestorErrors checkPago(Pago pago){
		
		GestorErrors err = GestorErrors.NO_ERROR;
		if (pago == null)
			err = GestorErrors.PAGO_NULL;
		else if (getValidatorF() != null)
		{
			Validator validator = validatorF.getValidator();			
			Set<ConstraintViolation<Pago>> constraints = validator.validate(pago);
			if (constraints.size() > 0)
			{
				ConstraintViolation<Pago> constraint = constraints.iterator().next();
				err = GestorErrors.DATA_EXCEPTION.setMessage(constraint.getMessage());
			}	
		}		
		if (err == GestorErrors.NO_ERROR)
		{		
			if (pago.getExpediente() == null)
				err = GestorErrors.PAGO_EXP_NULL;
			else if (pago.getPagador() == null)
				err = GestorErrors.PAGO_CLIENT_NULL;
		}
		
		return err;
	}
	
	public Responsable saveResponsable(Responsable responsable) throws DataException
	{
		Responsable result = null;
		
		if (responsableDao != null)
		{
			GestorErrors err = checkResponsable(responsable);
			if (err == GestorErrors.NO_ERROR){
				try{
					if (responsable.getId() == null || responsable.getId().intValue() < 0)
					{
						result = responsableDao.persist(responsable);
					}
					else
					{
						result = responsableDao.update(responsable);
					}
				}catch(PersistenceException persistException)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
				}
				return result;
			}
			throw new DataException(err);
		}
		throw new DataException(GestorErrors.RESPONSABLE_DAO_NOT_DEFINED);
	}

	private GestorErrors checkResponsable(Responsable responsable) throws DataException {
		GestorErrors result = GestorErrors.NO_ERROR;
		
		if (responsable == null)
			result = GestorErrors.RESPONSABLE_NULL;
		else if (getValidatorF() != null)
		{
			Validator validator = validatorF.getValidator();			
			Set<ConstraintViolation<Responsable>> constraints = validator.validate(responsable);
			if (constraints.size() > 0)
			{
				ConstraintViolation<Responsable> constraint = constraints.iterator().next();
				result = GestorErrors.DATA_EXCEPTION.setMessage(constraint.getMessage());
			}	
		}						
		return result;
	}

	public User saveUser(User user) throws DataException {		
		User result = null;
		if (userDao != null)
		{
			GestorErrors err = checkUser(user);
			if (err == GestorErrors.NO_ERROR)
			{
				try{
					if (user.getId() == null || user.getId().intValue() < 0)
					{
						result = userDao.persist(user);
					}
					else
					{
						result = userDao.update(user);
					}
				}catch(PersistenceException persistException)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
				}
				return result;
			}
			throw new DataException(err);
		}
		throw new DataException(GestorErrors.USER_DAO_NOT_DEFINED);
	}

	private GestorErrors checkUser(User user) throws DataException {
		
		GestorErrors result = GestorErrors.NO_ERROR;
				
		if (user == null)
			result = GestorErrors.USER_NULL;
		else if (getValidatorF() != null)
		{
			Validator validator = validatorF.getValidator();			
			Set<ConstraintViolation<User>> constraints = validator.validate(user);
			if (constraints.size() > 0)
			{
				ConstraintViolation<User> constraint = constraints.iterator().next();
				result = GestorErrors.DATA_EXCEPTION.setMessage(constraint.getMessage());
			}	
		}
		
		if (result == GestorErrors.NO_ERROR)
		{
			if (user.getRoles() == null || user.getRoles().size() <= 0)
				result = GestorErrors.USER_NO_ROLES;			
			/*else if (userDao.getUser(user.getUsername()) != null)
				result = GestorErrors.USER_EXISTS;*/
		}
		return result;
	}
	
	public List<Role> getAllRoles(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException
	{
		if (userDao != null)
		{
			try{
				return userDao.getAllRoles(pageSize, first, params, order, desc);
			}catch(PersistenceException persistException)
			{
				throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
			}			
		}
		throw new DataException(GestorErrors.USER_DAO_NOT_DEFINED);
	}

	public Expediente addClienteToExpediente(int expediente, int cliente)
			throws DataException {
		
		Expediente result = getFileById(expediente);
		if (result != null)
		{
			Cliente theCliente = getClientById(cliente);
			if (theCliente != null){
				result.getClientes().add(theCliente);
			}
			else
			{
				throw new DataException(GestorErrors.ADD_CLIENT_TO_EXP_CLIENT_NOT_FOUND);
			}
			
			saveExpediente(result);
		}
		else
		{
			throw new DataException(GestorErrors.ADD_CLIENT_TO_EXP_NOT_FOUND);
		}		
		return result;
	}

	public Expediente removeClienteFromExpediente(int expID, int clienteID) throws DataException{
		
		Expediente result = getFileById(expID);
		if (result != null)
		{
			if (result.getClientes().size() <= 1){
				throw new DataException(GestorErrors.EXP_NO_CLIENTS);
			}
			
			Cliente theCliente = getClientById(clienteID);
			if (theCliente != null){
				result.getClientes().remove(theCliente);
			}
			saveExpediente(result);						
		}
		else
		{
			throw new DataException(GestorErrors.REMOVE_CLIENT_FROM_EXP_NOT_FOUND);
		}
		
		return result;
	}

	public Expediente addPagoToExpediente(int expediente, Pago pago)
			throws DataException {
		
		if (pago == null){
			throw new DataException(GestorErrors.PAGO_NULL);
		}
		if (pago.getPagador() == null)
		{
			throw new DataException(GestorErrors.PAGO_CLIENT_NULL);
		}
			
		Expediente result = getFileById(expediente);
		if (result != null)
		{
			if (result.getClientes().contains(pago.getPagador()))
			{
				pago.setExpediente(result);			
				savePago(pago);
				result.getPagos().add(pago);
			}
			else
			{
				throw new DataException(GestorErrors.ADD_PAGO_TO_EXP_NO_VALID_CLIENT);
			}
		}
		else
		{
			throw new DataException(GestorErrors.PAGO_EXP_NULL);
		}
		
		return result;
	}
	
	public Expediente removePagoFromExpediente(int expID, int pagoID) throws DataException{
		
		Expediente result = getFileById(expID);
		if (result != null)
		{			
			Pago thePago = getPagoById(pagoID);
			if (thePago != null){
				result.getPagos().remove(thePago);
				pagoDao.delete(thePago);
			}
		}
		else
		{
			throw new DataException(GestorErrors.REMOVE_PAGO_FROM_EXP_NOT_FOUND);
		}
		
		return result;
	}

	public Pago getPagoById(int id, String ... fetch) throws DataException {
		if (pagoDao != null)
		{
			return pagoDao.getById(id, fetch);
		}
		throw new DataException(GestorErrors.PAGO_DAO_NOT_DEFINED);
	}
	
	public void deleteExpediente(int expID) throws DataException {
		Expediente exp = getFileById(expID);
		if (exp != null)
		{			
			fileDao.delete(exp);
		}
		else {
			throw new DataException(GestorErrors.EXP_NOT_FOUND);
		}
	}

	@Override
	public void deleteCliente(int clientID) throws DataException {
		Cliente client = getClientById(clientID);
		if (client != null){
			
			//TODO: esto se puede hacer con una anotación
			for (Pago pago : client.getPagos()) {
				pagoDao.delete(pago);
			}
			clientDao.delete(client);
		}
		else {
			throw new DataException(GestorErrors.CLIENT_NOT_FOUND);
		}		
	}

	@Override
	public Configuracion getConfiguracion() throws DataException {
		if (configuracionDao != null)
		{
			return configuracionDao.getById(new Integer(1));
		}
		throw new DataException(GestorErrors.CONFIGURACION_DAO_NOT_DEFINED);		
	}

	@Override
	public Configuracion saveConfiguracion(Configuracion configuracion)
			throws DataException {
		Configuracion result = null;
		if (configuracionDao != null)
		{
			if (configuracion.getId() == null || configuracion.getId().intValue() < 0){
				throw new DataException(GestorErrors.CONFIGURACION_NOT_EXISTS);
			}
			if (configuracion.getTheme() != null && themes.containsValue(configuracion.getTheme()))
			{
				try{
					result = configuracionDao.update(configuracion);
				}catch(PersistenceException persistException)
				{
					throw new DataException(GestorErrors.PERSISTENCE_EXCEPTION, persistException);
				}					
			}
			else {
				throw new DataException(GestorErrors.CONFIGURACION_THEME_NOT_EXISTS);
			}
		}
		return result;
	}

	@Override
	public Map<String, String> getAllThemes() {
		return themes;
	}

	@Override
	public List<ListadoExpResp> getAllExpResp(int pageSize, int first,
			Map<String, Object> params, String order, boolean desc)
			throws DataException {
		if (listRespExpDao != null)
		{
			return listRespExpDao.getAll(pageSize, first, params, order, desc);
		}
		throw new DataException(GestorErrors.LISTADO_EXP_DAO_NOT_DEFINED);
	}

	@Override
	public long getAllExpRespCount(Map<String, Object> params)
			throws DataException {
		if (listRespExpDao != null)
		{
			return listRespExpDao.getCount(params);
		}
		throw new DataException(GestorErrors.LISTADO_EXP_DAO_NOT_DEFINED);
	}
}
