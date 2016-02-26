package es.salazaryasociados.gestor.test.integration;


import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionBaseConfigurationOption;
import org.ops4j.pax.exam.karaf.options.KarafDistributionConfigurationFilePutOption;
import org.ops4j.pax.exam.karaf.options.KarafDistributionKitConfigurationOption;
import org.ops4j.pax.exam.karaf.options.KarafDistributionKitConfigurationOption.Platform;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;

import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.exceptions.GestorErrors;
import es.salazaryasociados.db.model.Cliente;
import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.model.Pago;
import es.salazaryasociados.db.model.Responsable;
import es.salazaryasociados.db.model.Role;
import es.salazaryasociados.db.model.User;
import es.salazaryasociados.db.service.IDataService;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class DataServiceTest {

	// LOS SERVICIOS ESTÁN DEFINIDOS EN src/test/resources
	@Inject @Filter("(osgi.service.blueprint.compname=dataService)")
	private IDataService dataService;
	
	/**
	 * Método de configuración
	 * @return
	 */
	@Configuration
	public Option[] config() {	
		
		// Se crea la bb.dd. de pruebas de integración:
		Path source = Paths.get("F:/Desarrollo/projects/SalazarYAsociados/llaroqui/BBDD/hsqldb/salazar.script");
		Path target = Paths.get("F:/Desarrollo/projects/SalazarYAsociados/llaroqui/BBDD/hsqldb/integrationTest/salazar.script");
		
		try {
			Files.delete(target);
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		KarafDistributionConfigurationFilePutOption fileOpt = new KarafDistributionConfigurationFilePutOption(
				  "etc/GestorConfig.cfg", // config file to modify based on karaf.base
				  "db.url", // key to add or change
				  "jdbc:hsqldb:file:F:/Desarrollo/projects/SalazarYAsociados/llaroqui/BBDD/hsqldb/integrationTest/salazar"); // value to add or change
		
		KarafDistributionBaseConfigurationOption opt = 
				karafDistributionConfiguration()
				.frameworkUrl(
						maven().groupId("es.salazaryasociados")
								.artifactId("gestorWeb").type("zip")
								.version("0.0.1")).karafVersion("4.0.1")
				.name("Apache Karaf").unpackDirectory(new File("target/pax"))
				.useDeployFolder(false);//SI SE PONE A TRUE, NO ENCUENTRA LA CLASE es.cgs.sacomar.pruebas.NamesService
				
		KarafDistributionKitConfigurationOption kOption = new KarafDistributionKitConfigurationOption(opt, Platform.WINDOWS);
				
		Option[] result = CoreOptions.options(kOption,
				debugConfiguration(),
				fileOpt,
				//PONER ESTO PARA MANTENER EL KARAF DE LA PRUEBA EN TARGET
				//KarafDistributionOption.keepRuntimeFolder(),
				configureConsole().ignoreLocalConsole(),
				logLevel(LogLevel.ERROR)				
				);
		
		return result;		
	}	

	@Test public void testDataService() throws Exception{
		testFilesCount();
		testNewClient();
		testNewExp();
		testNewPago();
		testNewResponsable();
		testNewUser();
	}
	
	
    protected void testFilesCount() throws Exception {		
		System.out.println("testFilesCount");
		
		Assert.assertNotNull(dataService);
		long count = dataService.getFilesCount(null);
		List<Expediente> exp = dataService.getAllFiles(-1, 0, null, null, false);
		if (count > 0)
		{
			Assert.assertNotNull(exp);
			Assert.assertTrue(count > 0);
		}
		else
		{
			Assert.assertTrue(exp == null || exp.size() == 0);
		}
		
	}
	
	//TODO: probar la paginación.
    public void testNewClient() {
		
		System.out.println("testNewClient");
		
		// El Cliente debe tener nombre y apellidos:
		Cliente client = new Cliente();
		Cliente created = null;
		
		try{
			created = dataService.saveCliente(null);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.CLIENT_NULL);
		}
		Assert.assertNull(created);		
		
		try{
			created = dataService.saveCliente(client);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("CLIENT_NULL_NAME") || 
							  e.getErrorCode().getMessage().equals("CLIENT_NULL_SURNAME"));
		}
		Assert.assertNull(created);
		
		
		client.setNombre("");
		try{
			created = dataService.saveCliente(client);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("CLIENT_SHORT_NAME") || 
							  e.getErrorCode().getMessage().equals("CLIENT_NULL_SURNAME"));
		}
		Assert.assertNull(created);
		
		client.setNombre("Pruebas");
		try{
			created = dataService.saveCliente(client);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("CLIENT_NULL_SURNAME"));
		}
		Assert.assertNull(created);
		
		client.setApellidos("");
		try{
			created = dataService.saveCliente(client);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("CLIENT_SHORT_SURNAME"));
		}
		Assert.assertNull(created);
		
		client.setApellidos("Pruebas");
		try{
			created = dataService.saveCliente(client);
		}catch(Exception e)
		{
			// No debe entrar
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertNotNull(created);
		
		Assert.assertEquals(client.getId(), created.getId());
		
		// No se permiten dos clientes con el mismo nombre y apellidos:
		Cliente newCliente2 = new Cliente();
		newCliente2.setNombre("Pruebas");
		newCliente2.setApellidos("Pruebas");
		created = null;
		try{
			created = dataService.saveCliente(newCliente2);
		}
		catch(DataException e)
		{			
			Assert.assertTrue(e.getErrorCode() == GestorErrors.UNIQUE_CONSTRAINT);
		}	
		Assert.assertNull(created);
	}
	
    public void testNewExp() throws Exception {
		
		System.out.println("testNewExp");
		
		int lastIndx = 0;
		try{
			List<Expediente> exps = dataService.getAllFiles(1, 0, null, "id", true);			
			if (exps != null && exps.size() > 0)
			{
				lastIndx = exps.get(0).getId();
			}
		}catch(DataException e)
		{
			// No debe entrar
			Assert.assertTrue(false);
		}
		
		Expediente newExp = new Expediente();
		Expediente created = null;
		
		try{
			created = dataService.saveExpediente(null);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.EXP_NULL);
		}
		Assert.assertNull(created);		
		
		//no se permiten expedientes vacios:
		try{
			created = dataService.saveExpediente(newExp);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("FILE_SHORT_ISSUE"));
		}
		Assert.assertNull(created);		
		
		// El expediente debe tener asunto, un cliente y responsable.
		newExp.setAsunto("Prueba");
		try{
			created = dataService.saveExpediente(newExp);
		}catch(DataException e)
		{
			Assert.assertTrue((e.getErrorCode() == GestorErrors.EXP_NO_CLIENTS) ||
							  (e.getErrorCode() == GestorErrors.EXP_NO_RESPONSABLE)
					);
		}
		Assert.assertNull(created);		
		
		// Se añade un cliente
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("apellidos", "Pruebas");
		Set<Cliente> clientes = new HashSet<Cliente>();
		Cliente aux = dataService.getAllClients(1, 0, params, null, false).get(0);
		clientes.add(aux);
		Assert.assertNotNull(clientes);
		Assert.assertTrue(clientes.size() > 0);
		newExp.setClientes(clientes);
		try{
			created = dataService.saveExpediente(newExp);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.EXP_NO_RESPONSABLE);
		}
		Assert.assertNull(created);		
		
		// Se añade un responsable:
		List<Responsable> resp = dataService.getAllResponsables(1, 0, null, null, false);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.size() > 0);
		newExp.setResponsable1(resp.get(0));
		
		Set<Cliente> duplicateClientes = new HashSet<Cliente>();
		Cliente c = clientes.iterator().next();
		Cliente c2 = dataService.getAllClients(1, 0, params, null, false).get(0);		
		duplicateClientes.add(c);
		duplicateClientes.add(c2);
		newExp.setClientes(duplicateClientes);

		//No se admiten duplicados
		Assert.assertTrue(newExp.getClientes().size() == 1);
				
		Cliente c3 = dataService.getAllClients(1, 0, null, "apellidos", true).get(0);
		clientes.add(c3);		
		
		newExp.setClientes(clientes);
		created = dataService.saveExpediente(newExp);
		Assert.assertNotNull(created);
		
		newExp = dataService.getFileById(created.getId());
		
		// El expediente debe tener un número de identificación después de
		// la prueba que debe ser un número más del último expediente que existiera
		
		Assert.assertEquals(newExp.getId(), created.getId());		
		Assert.assertTrue((lastIndx + 1) == newExp.getId().intValue());
		
		// Añadir un cliente:
		Cliente c4 = dataService.getAllClients(1, 0, null, "apellidos", false).get(0);
		dataService.addClienteToExpediente(newExp.getId().intValue(), c4.getId().intValue());
		
		Expediente exp = dataService.getFileById(newExp.getId(), "clientes");
		Assert.assertNotNull(exp);
		Assert.assertTrue(exp.getClientes().size() == 3);
		
		for (Cliente cl : exp.getClientes()){
			Assert.assertTrue(cl.getId().intValue() == c.getId() ||
							  cl.getId().intValue() == c3.getId() ||
							  cl.getId().intValue() == c4.getId());
			
			// Se obtiene de nuevo para que haga el fetch:
			Cliente cl2 = dataService.getClientById(cl.getId(), "expedientes");
			Assert.assertTrue(cl2.getExpedientes().contains(exp));
		}
		
		//TODO: no se admite añadir un cliente que ya esté
		exp = dataService.addClienteToExpediente(newExp.getId().intValue(), c4.getId().intValue());
		Assert.assertTrue(exp.getClientes().size() == 3);
		exp = dataService.getFileById(exp.getId(), "clientes");
		Assert.assertTrue(exp.getClientes().size() == 3);
		
		//eliminar clientes:
		Assert.assertTrue(exp.getClientes().contains(c4));
		exp = dataService.removeClienteFromExpediente(newExp.getId().intValue(), c4.getId().intValue());
		Assert.assertFalse(exp.getClientes().contains(c4));
		Assert.assertTrue(exp.getClientes().size() == 2);
		exp = dataService.getFileById(exp.getId(), "clientes");
		Assert.assertFalse(exp.getClientes().contains(c4));
		
		//Borrar uno que no está
		exp = dataService.removeClienteFromExpediente(newExp.getId().intValue(), c4.getId().intValue());
		
		//No se pueden eliminar todos los clientes
		dataService.removeClienteFromExpediente(newExp.getId().intValue(), c.getId().intValue());

		try {
			dataService.removeClienteFromExpediente(newExp.getId().intValue(), c2.getId().intValue());
		} catch (DataException e) {
			Assert.assertTrue(e.getErrorCode() == GestorErrors.EXP_NO_CLIENTS);
		}
	}
	
	//Probar nuevo pago:
	protected void testNewPago() throws Exception {
		
		System.out.println("testNewPago");
				
		Pago newPago = new Pago();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("asunto", "Prueba");
		Expediente exp = dataService.getAllFiles(1, 0, params, null, false).get(0);
		// Esto se hace para que haga el fetch
		exp = dataService.getFileById(exp.getId(), "clientes", "pagos");
		
		// No se permiten pagos nulos, ni sin cantidad, ni fecha, expediente o pagador
		try{
			dataService.addPagoToExpediente(exp.getId(), null);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.PAGO_NULL);
		}
		
		newPago.setPagador(exp.getClientes().iterator().next());
		try{
			dataService.addPagoToExpediente(-1, newPago);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.PAGO_EXP_NULL);
		}	

		newPago.setPagador(null);
		
		try{
			exp = dataService.addPagoToExpediente(exp.getId(), newPago);
		}catch(DataException e)
		{			
			Assert.assertTrue(e.getErrorCode().getMessage().equals("NOT_MIN_PAYMENT") 	|| 
							  e.getErrorCode().getMessage().equals("NULL_PAYMENT_DATE")	||
							  e.getErrorCode() == GestorErrors.PAGO_CLIENT_NULL);			
		}
		Assert.assertFalse(exp.getPagos().contains(newPago));
		
		newPago.setFecha(new Timestamp(new Date().getTime()));
		newPago.setCantidad(new BigDecimal(0.0));
		try{
			exp = dataService.addPagoToExpediente(exp.getId(), newPago);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode().getMessage().equals("NOT_MIN_PAYMENT")	||
					  		  e.getErrorCode() == GestorErrors.PAGO_CLIENT_NULL);			
		}
		Assert.assertFalse(exp.getPagos().contains(newPago));
		
		newPago.setCantidad(new BigDecimal(10.0));
		try{
			exp = dataService.addPagoToExpediente(exp.getId(), newPago);
		}catch(DataException e)
		{
			Assert.assertTrue((e.getErrorCode() == GestorErrors.PAGO_CLIENT_NULL));
		}
		Assert.assertFalse(exp.getPagos().contains(newPago));

		// No se admiten pagos de clientes no contenidos en el expediente:
		int id = 1;
		Cliente pagador = dataService.getClientById(id);		
		while(pagador == null || exp.getClientes().contains(pagador)){
			pagador = dataService.getClientById(id);
		}
		newPago.setPagador(pagador);
		
		try{
			exp = dataService.addPagoToExpediente(exp.getId(), newPago);
		}catch(DataException e)
		{
			Assert.assertTrue((e.getErrorCode() == GestorErrors.ADD_PAGO_TO_EXP_NO_VALID_CLIENT));
		}
		Assert.assertFalse(exp.getPagos().contains(newPago));
		
		Iterator<Cliente> it = exp.getClientes().iterator();
		newPago.setPagador(it.next());
		
		try{
			exp = dataService.addPagoToExpediente(exp.getId(), newPago);
		}catch(DataException e)
		{
			//No debe entrar
			Assert.assertTrue(false);
		}
		Assert.assertTrue(exp.getPagos().contains(newPago));
		
		//Al buscarlo en la base de datos, contiene el pago
		exp = dataService.getFileById(exp.getId(), "pagos");
		
		Pago pago = exp.getPagos().iterator().next();
		Assert.assertTrue(pago.getFecha().equals(newPago.getFecha()) &&
						  pago.getCantidad().doubleValue() == newPago.getCantidad().doubleValue() &&
						  pago.getExpediente().getId().intValue() == newPago.getExpediente().getId().intValue() &&
						  pago.getPagador().getId().intValue() == newPago.getPagador().getId().intValue()
				);
		
		//Ahora se borra el pago:
		exp = dataService.removePagoFromExpediente(exp.getId(), pago.getId());
		Assert.assertFalse(exp.getPagos().contains(pago));
		
		exp = dataService.getFileById(exp.getId(), "pagos");
		Assert.assertFalse(exp.getPagos().contains(pago));
	}

	// Probar nuevo responsable:
	public void testNewResponsable() throws Exception {
		
		System.out.println("testNewResponsable");
		
		// No se permiten responsables sin nombre:
		Responsable newResp = new Responsable();
		Responsable created = null;
		
		try{
			created = dataService.saveResponsable(null);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.RESPONSABLE_NULL);
		}
		catch(Exception e2)
		{
			System.out.println("1");
			e2.printStackTrace();
		}
		Assert.assertNull(created);		
		
		try{
			created = dataService.saveResponsable(newResp);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("RESPONSABLE_SHORT_NAME") || e.getErrorCode().getMessage().equals("RESPONSABLE_NULL_NAME"));			
		}
		catch(Exception e2)
		{
			System.out.println("2");
			e2.printStackTrace();
		}
		Assert.assertNull(created);	
		
		newResp.setNombre("Pruebas");
		try{
			created = dataService.saveResponsable(newResp);
		}
		catch(Exception e2)
		{
			System.out.println("3");
			e2.printStackTrace();
		}		
		Assert.assertNotNull(created);
		Assert.assertTrue(newResp.getId() == created.getId());
		
		// No se permiten 2 responsables con el mismo nombre:
		Responsable newResp2 = new Responsable();
		newResp2.setNombre("Pruebas");
		created = null;
		try{
			created = dataService.saveResponsable(newResp2);
		}
		catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.UNIQUE_CONSTRAINT);
		}		
		catch(Exception e2)
		{
			System.out.println("4");
			e2.printStackTrace();
		}		
		Assert.assertNull(created);		
	}
	
	// Probar nuevo usuario:
	public void testNewUser() throws Exception {
		
		System.out.println("testNewUser");
		
		// No se permite usuario sin nombre, password o al menos un rol
		User newUser = new User();
		User created = null;
		
		try{
			created = dataService.saveUser(null);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.USER_NULL);
		}
		Assert.assertNull(created);			
		
		//Debe tener nombre y password
		try{
			created = dataService.saveUser(newUser);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			System.out.println(e.getMessage());
			Assert.assertTrue(e.getErrorCode().getMessage().equals("USER_SHORT_NAME") || 
							  e.getErrorCode().getMessage().equals("USER_SHORT_PASSWORD"));			
		}
		Assert.assertNull(created);			
		
		// El nombre no debe ser demasiado corto
		newUser.setUsername("Pru");
		newUser.setPassword("Pruebas");
		try{
			created = dataService.saveUser(newUser);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("USER_SHORT_NAME"));			
		}
		Assert.assertNull(created);
		
		// La clave no debe ser demasiado corta
		newUser.setUsername("Pruebas");
		newUser.setPassword("P");
		try{
			created = dataService.saveUser(newUser);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(e.getErrorCode().getMessage().equals("USER_SHORT_PASSWORD"));			
		}
		Assert.assertNull(created);		
		
		// Debe tener roles
		newUser.setPassword("Pruebas");
		try{
			created = dataService.saveUser(newUser);
		}catch(DataException e)
		{
			Assert.assertTrue(
					(e.getErrorCode() == GestorErrors.USER_NO_ROLES)
			);
		}
		Assert.assertNull(created);
		
		List<Role> rolesAux = dataService.getAllRoles(-1, 0, null, null, false);
		HashSet<Role> roles = new HashSet<Role>(rolesAux);
		if (roles != null && roles.size() > 0)
		{
			newUser.setRoles(roles);
			created = dataService.saveUser(newUser);					
		}
		Assert.assertNotNull(created);
		
		// No se admiten dos usuarios con el mismo nombre:
		User newUser2 = new User();
		newUser2.setUsername("Pruebas");
		newUser2.setPassword("Pruebas");
		newUser2.setRoles(roles);
		created = null;
		try{
			created = dataService.saveUser(newUser2);
		}catch(DataException e)
		{
			Assert.assertTrue(e.getErrorCode() == GestorErrors.UNIQUE_CONSTRAINT);
		}		
		Assert.assertNull(created);

		testUpdateUser();
	}	

	private void testUpdateUser() throws Exception {
		
		System.out.println("testUpdateUser");
				
		// Se obtiene el usuario con nombre Pruebas:		
		User user = dataService.getUser("Pruebas");
		Assert.assertNotNull(user);
		Assert.assertTrue(user.getPassword().equals("Pruebas"));
		
		// Tiene todos los roles
		List<Role> roles = dataService.getAllRoles(-1, 0, null, null, false);
		Assert.assertTrue(user.getRoles().size() == roles.size());
		for (Role r : roles)
		{
			Assert.assertTrue(user.getRoles().contains(r));
		}
		
		// No se puede cambiar la password a una menor que 4
		user.setPassword("Pr");
		User updated = null;
		try{
			updated = dataService.saveUser(user);
		}catch(DataException de){
			Assert.assertTrue(de.getErrorCode() == GestorErrors.DATA_EXCEPTION);
			Assert.assertTrue(de.getErrorCode().getMessage().equals("USER_SHORT_PASSWORD"));						
		}
		Assert.assertNull(updated);
		
		// Se puede cambiar la password
		user.setPassword("Pruebas2");
		try{
			updated = dataService.saveUser(user);
		}catch(DataException de){
			Assert.assertTrue(false);
		}
		Assert.assertNotNull(updated);
		Assert.assertTrue(user.getId().intValue() == updated.getId().intValue());
		Assert.assertTrue(updated.getPassword().equals("Pruebas2"));
		
		User user2 = dataService.getUser("Pruebas");
		Assert.assertTrue(user2.getPassword().equals("Pruebas2"));
		
		//Cambios de roles:
		
		//No se admite que no tenga roles:
		updated = null;
		user.setRoles(new HashSet<Role>());
		try{
			updated = dataService.saveUser(user);
		}catch(DataException de){
			Assert.assertTrue(de.getErrorCode() == GestorErrors.USER_NO_ROLES);
		}
		Assert.assertNull(updated);
		
		user.setRoles(null);
		try{
			updated = dataService.saveUser(user);
		}catch(DataException de){
			Assert.assertTrue(de.getErrorCode() == GestorErrors.USER_NO_ROLES);
		}
		Assert.assertNull(updated);		
		
		// Cambiar a un solo rol:
		HashSet<Role> newRoles = new HashSet<Role>();
		newRoles.add(roles.get(0));
		user.setRoles(newRoles);
		try{
			updated = dataService.saveUser(user);
		}catch(DataException de){
			Assert.assertTrue(false);
		}
		Assert.assertNotNull(updated);
		Assert.assertTrue(updated.getRoles().size() == 1);
		Assert.assertTrue(updated.getRoles().iterator().next().getId().intValue() == roles.get(0).getId().intValue());
		
		user2 = dataService.getUser("Pruebas");
		Assert.assertNotNull(user2);
		Assert.assertTrue(user2.getRoles().size() == 1);
		Assert.assertTrue(user2.getRoles().iterator().next().getId().intValue() == roles.get(0).getId().intValue());		
	}	
}
