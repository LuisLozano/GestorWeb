package es.salazaryasociados.db.service;

import java.util.List;
import java.util.Map;

import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Configuracion;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.model.Role;
import es.salazaryasociados.db.model.User;

public interface IDataService {

	public List<Cliente> getAllClients(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException;
	public Cliente getClientById(int id, String ... fetch) throws DataException;
	public List<Expediente> getAllFiles(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException;
	public Expediente getFileById(int id, String ... fetch) throws DataException;
	public long getFilesCount (final Map<String, Object> params) throws DataException;
	public long getClientsCount (final Map<String, Object> params) throws DataException;
	public Pago getPagoById(int id, String ... fetch) throws DataException;
	public List<Responsable> getAllResponsables(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException;
	public long getResponsablesCount (final Map<String, Object> params) throws DataException;
	public Responsable getResponsableById(int id, String ... fetch) throws DataException;
	public List<User> getAllUsers(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException;
	public User getUser(String userName) throws DataException;
	public List<Role> getAllRoles(int pageSize, int first, final Map<String, Object> params, String order, boolean desc) throws DataException;
	public Expediente saveExpediente(Expediente exp) throws DataException;
	public Cliente saveCliente(Cliente client) throws DataException;	
	public Responsable saveResponsable(Responsable responsable) throws DataException;
	public User saveUser (User user) throws DataException;
	public Configuracion getConfiguracion() throws DataException;
	public Configuracion saveConfiguracion(Configuracion configuracion) throws DataException;
	public Map<String, String> getAllThemes();
	
	public Expediente addClienteToExpediente(int expediente, int cliente) throws DataException;
	public Expediente removeClienteFromExpediente(int intValue, int intValue2) throws DataException;
	
	public Expediente addPagoToExpediente(int expediente, Pago pago) throws DataException;
	public Expediente removePagoFromExpediente(int expID, int pagoID) throws DataException;
	
	public void deleteExpediente(int expID) throws DataException;
	public void deleteCliente(int clientID)  throws DataException;
}
