package es.salazaryasociados.gestorui;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Configuracion;
import es.salazaryasociados.db.service.IDataService;

@ManagedBean(eager = true)
@ApplicationScoped
public class ThemeSwitcher {

	private IDataService dataService;
	private String theme = "overcast";

	public Map<String, String> getThemes() {
		return dataService.getAllThemes();
	}

	public String getTheme() {
		return theme.trim();
	}

	public void setTheme(String th) {
		if (th == null || th.equals("")) {
			theme = "overcast";
		}
		else {
			this.theme = th;
		}
	}

	@PostConstruct
	public void init() {
		dataService = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
		try {
			theme  = dataService.getConfiguracion().getTheme();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveTheme(AjaxBehaviorEvent ajax) {
		Configuracion conf;
		try {
			conf = dataService.getConfiguracion();
			conf.setTheme(theme);
			dataService.saveConfiguracion(conf);
			setTheme((String) ((ThemeSwitcher) ajax.getSource()).getTheme());			
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
