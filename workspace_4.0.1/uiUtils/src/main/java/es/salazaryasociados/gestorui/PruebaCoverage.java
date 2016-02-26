package es.salazaryasociados.gestorui;

public class PruebaCoverage {

	public String prueba(int i) {
		
		String result = "";
		if (i >= 0) {
			result = "positivo";
		}
		else
		{
			result = "negativo";
		}
		
		return result;
	}
}
