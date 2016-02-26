package es.salazaryasociados.gestor.commands;

import java.util.List;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import es.salazaryasociados.db.model.Expediente;
import es.salazaryasociados.db.service.IDataService;

@Command(name="listFiles", scope="gestor", description="Muestra los expedientes")
@Service
public class ListFiles implements Action{

	@Reference
	private IDataService dataService;
	
	public IDataService getDataService() {
		return dataService;
	}

	public void setDataService(IDataService dataService) {
		this.dataService = dataService;
	}

	public Object execute() throws Exception {
		if (dataService == null){
			System.out.println("No se ha definido el servicio de datos");
		}
		else
		{
			List<Expediente> list = dataService.getAllFiles(-1, 0, null, null, false);
			
			System.out.println("ID\tAsunto");
			System.out.println("_________________________________________________________");
			for (Expediente exp : list){
				System.out.println(exp.getId() + "\t" + exp.getAsunto());
			}
		}
		return null;
	}

}
