
package es.salazaryasociados.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.FrameworkWiring;

import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.ClientDTO;
import es.salazaryasociados.services.data.dto.ClientSummaryDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;

@Command(scope = "gestor", name = "randomize", description = "Pruebas")
@Service
public class randomizeUsers implements Action {

    @Reference
    IDataService dataService;
    
    @Override
    public Object execute() throws Exception {
    	
    	int maxId = 0;
    	int minId = 0;
    	// Se obtiene el mayor id y el meno id
    	List<ClientSummaryDTO> clients = dataService.getAllClients(1, 0, null, "id", true);
    	if (clients != null && clients.size() > 0) {
    		maxId = clients.get(0).getId();
    	}
    	clients = dataService.getAllClients(1, 0, null, "id", false);
    	if (clients != null && clients.size() > 0) {
    		minId = clients.get(0).getId();
    	}    	
    	
    	while (minId <= maxId) {
    		ClientDTO minClient = dataService.getClientById(minId);
    		ClientDTO maxClient = dataService.getClientById(maxId);
    		
    		try {
	    		if (minClient != null && maxClient != null) {
	    			randomizeName(minClient, maxClient);
	    			dataService.saveClient(minClient);
	    			dataService.saveClient(maxClient);
	    		}
    		} catch(DataServiceException e) {
    			System.out.println(e.getMessage());
	    		System.out.println(String.format("Excepción en usuarios: %s y %s", minClient.getId().toString(), maxClient.getId().toString()));
	    	}
    		
    		minId++;
    		maxId--;
    	}
    	return null;
    }

	private void randomizeName(ClientDTO minClient, ClientDTO maxClient) {
		
		String apellidosMin = minClient.getApellidos();
		String nombreMin = minClient.getNombre();
		String apellidosMax = maxClient.getApellidos();
		String nombreMax = maxClient.getNombre();
		
		String[] splitMax = apellidosMax.split(" ");
		String[] splitMin = apellidosMin.split(" ");
		
		minClient.setNombre(nombreMax);
		minClient.setApellidos((splitMax.length > 1 ? splitMax[1] : splitMax[0]) +  (splitMin.length > 1 ? " " + splitMin[1] : " " + splitMin[0]));
		if (minClient.getApellidos().length() < 4) {
			minClient.setApellidos(minClient.getApellidos() + " Corto");
		}
		if (minClient.getNombre() == null) {
			minClient.setNombre("Corto");
		}
		else if (minClient.getNombre().length() < 4) {
			minClient.setNombre(minClient.getNombre() + " Corto");
		}		
		minClient.setDni(minClient.getId().toString());
		
		maxClient.setNombre(nombreMin);
		maxClient.setApellidos((splitMin.length > 1 ? splitMin[1] : splitMin[0]) +  (splitMax.length > 1 ? " " + splitMax[1] : " " +splitMax[0]));
		maxClient.setDni(maxClient.getId().toString());
		if (maxClient.getApellidos().length() < 4) {
			maxClient.setApellidos(maxClient.getApellidos() + " Corto");
		}
		
		if (maxClient.getNombre() == null) {
			maxClient.setNombre("Corto");
		}
		else if (maxClient.getNombre().length() < 4) {
			maxClient.setNombre(maxClient.getNombre() + " Corto");
		}				
	}
}
