package es.salazaryasociados.gestorui;

import org.junit.Assert;
import org.junit.Test;

public class PruebaCoverageTest {

	@Test
	public void testPrueba() {
		
		PruebaCoverage p = new PruebaCoverage();
		Assert.assertTrue(p.prueba(0).equals("positivo"));
	}

}
