package saline.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hypertonic.saline.preferences.HypertonicSalinePreferencePage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSalinePreferencePageConstruction {
	HypertonicSalinePreferencePage page;
	boolean testingOnly = true;
	
	@Before
	public final void setUp(){
		page = new HypertonicSalinePreferencePage(testingOnly);
	}

	@After
	public final void tearDown(){
		page = null;
	}
	
	@Test
	public final void testHypertonicSalinePreferencePageExists(){
		assertNotNull(page);
	}
	
	@Test
	public final void testRetrieveDescriptionHypertonicSalinePreferencePage(){
		assertEquals("Hypertonic preference description string failed",page.getDescription(),"Preference settings for hypertonic saline decision support tool:");
	}
}
