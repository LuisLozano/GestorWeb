
package es.salazaryasociados.commands;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import es.salazaryasociados.services.data.api.IDataService;

@Command(scope = "gestor", name = "allfiles", description = "Pruebas")
@Service
public class allFiles implements Action {

    @Option(name = "-o", aliases = { "--option" }, description = "An option to the command", required = false, multiValued = false)
    private String option;

    @Argument(name = "argument", description = "Argument to the command", required = false, multiValued = false)
    private String argument;

    @Reference
    IDataService dataService;
    
    @Override
    public Object execute() throws Exception {
    	
    	dataService.getAllFiles(10, 0, null, null, false);
    	
    	return null;
    }
}
