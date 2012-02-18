package ventilator.core.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ventilator.pediatric.preferences.VentilatorPediatricPreferencePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestVentilatorPreferencePageConstruction {
	VentilatorPediatricPreferencePage page;
	boolean testingOnly = true;
	
	@Before
	public final void setUp(){
		page = new VentilatorPediatricPreferencePage(testingOnly);
	}
	
	@After
	public final void tearDown(){
		page = null;
	}
	
	@Test
	public final void testVentilatorPreferencePageExists(){
		assertNotNull(page);
	}
	
	@Test
	public final void testRetrieveDescriptionVentilatorPreferencePage(){
		assertEquals("Ventilator preference description string failed",
				page.getDescription(),"Preference settings for pediatric ventilator decision support tool:");

		
		
	}
}
