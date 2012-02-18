package tests.appStartup.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.utah.cdmcc.decisionsupport.application.ApplicationWorkbenchAdvisor;

public class ApplicationWorkbenchAdvisorTest {

	@Test
	public void testGetInitialWindowPerspectiveId() {
		ApplicationWorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
		assertEquals("edu.utah.cdmcc.decisionsupport.glucose.perspective.normalUser",
				advisor.getInitialWindowPerspectiveId());		
	}

}
