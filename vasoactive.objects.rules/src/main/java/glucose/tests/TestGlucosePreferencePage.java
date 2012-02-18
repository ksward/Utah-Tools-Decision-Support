package glucose.tests;

import static org.junit.Assert.*;
import glucose.preferences.GlucosePreferencePage;
import org.junit.*;


public class TestGlucosePreferencePage {
	GlucosePreferencePage page;
	boolean testingOnly = true;
	
	@Before
	public final void setUp(){
		page = new GlucosePreferencePage(testingOnly);
		// The latter constructor is artificial so I can test the page
	}
	
	@After
	public final void tearDown(){
		page = null;
	}
	
	@Test
	public final void testGlucosePreferencePageExists(){
		assertNotNull(page);
	}
	
	@Test
	public final void testRetrieveDescriptionGlucosePreferencePage(){
		assertEquals("Glucose preference description string failed",page.getDescription(),"Preference settings for glucose decision support tool:");
	}
}
