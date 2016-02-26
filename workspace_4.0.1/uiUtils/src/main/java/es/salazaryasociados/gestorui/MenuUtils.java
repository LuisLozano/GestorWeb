package es.salazaryasociados.gestorui;

import java.util.Arrays;
import java.util.List;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

public class MenuUtils {

	private static MenuModel menu = new DefaultMenuModel();

	/**
	 * Añade un submenu
	 * @param menuPath Ruta del submenu: Menu 1 > Submenu 1.1 > Submenu 1.1.1 > accion
	 * @param url
	 */
	public static void AddMenu(String menuPath, String url)
	{
		String[] options = GetOptions(menuPath);
		
		if (options != null && options.length > 0)
		{
			CreateOptions(options, url);
		}
	}

	private static DefaultMenuItem CreateOptions(String[] options, String url) {
		
		List<MenuElement> existsOptions = menu.getElements();
		MenuElement parent = null;
		
		int from = 0;
		String id = "";
		if (existsOptions != null)
		{
			for(String option : options)
			{
				id = id + option;
				for (MenuElement element : existsOptions){
					
					if (element.getId().equals(id) && element instanceof DefaultSubMenu)
					{
						existsOptions = ((DefaultSubMenu)element).getElements();
						parent = element;
						from++;
						break;
					}
				}
				
				if (parent == null)
				{
					break;
				}
				id += ".";
			}			
		}
			
		// Si no se encontró el padre, se crean todos los menu subsiguientes:
		int to = options.length - 1;
		String[] optionsAux = null;
		DefaultSubMenu parentSub = null;
		if (from < to)
		{
			optionsAux = Arrays.copyOfRange(options, from, to);
			parentSub = CreateOptions(optionsAux, parent);
		}	
		else if (parent != null)
		{
			parentSub = (DefaultSubMenu)parent;
		}

		String menuName = options[options.length - 1];
		DefaultMenuItem item = new DefaultMenuItem(menuName);
        item.setUrl(url);
		// Se crea la opción de acción:
		if (parentSub != null)
        {
			item.setId(parentSub.getId() + "." + menuName);
	        parentSub.addElement(item);					
        }
		else
		{
			item.setId(id);
			menu.addElement(item);
		}
		
		return item;
	}

	/**
	 * Crea las opciones indicadas en options como submenus
	 * @param options
	 * @param parent
	 * @return
	 */
	private static DefaultSubMenu CreateOptions(String[] options, MenuElement parent) {
		
		DefaultSubMenu parentSub = null;
		if (parent != null && parent instanceof DefaultSubMenu)
		{
			parentSub = (DefaultSubMenu)parent;
		}
		else if (parent != null)
		{
			//TODO: log. Esto no puede darse, una opción no puede colocarse debajo de una acción
			return null;
		}
		
		DefaultSubMenu subMenu = null;
		String id = "";
		for (String option : options)
		{
			id = id + option;
			subMenu = new DefaultSubMenu(option);
			subMenu.setId(id);
			if (parentSub == null)
			{
				menu.addElement(subMenu);
				parentSub = subMenu;
			}
			else
			{
				parentSub.addElement(subMenu);
			}
			id += ".";
		}
		
		return subMenu;
	}

	private static String[] GetOptions(String menuPath) {
		String[] result = null;
		if (menuPath != null && menuPath.length() > 0)
		{
			result = menuPath.split(">");
		}
		return result;
	}

	public static MenuModel GetMenu() {
		return menu;
	}

	public static void Clear() {
		menu.getElements().clear();
	}
}
