package edu.utah.cdmcc.decisionsupport.objects.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.decision.object.DecisionCompositeButtonsOnly;


public class DemographicCompositeTests {

	private int style = 0;
	private DecisionCompositeButtonsOnly composite;
	private Shell testShell;

	@Before
	public void setUp() throws Exception {
		testShell = new Shell();
		composite = new DecisionCompositeButtonsOnly(testShell, style );
	}

	@After
	public void tearDown() throws Exception {
		//composite.dispose();
	}

	@Test
	public void testCompositeExists() {
		assertNotNull(composite);
	}
	
	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
		try {
			composite = new DecisionCompositeButtonsOnly(null, 0);
			fail("No exception thrown");
		}
		catch (IllegalArgumentException e) {
		}

		int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL};
		for (int i = 0; i < cases.length; i++)
			composite = new DecisionCompositeButtonsOnly(testShell, cases[i]);
	}

	@Test
	public void testCompositeChildren(){
	assertEquals(composite.getChildren().length,1);
	Composite buttons = (Composite) composite.getChildren()[0];
	assertEquals(buttons.getParent(),composite);
	assertEquals(buttons.getChildren().length,4);
	}
	
	@Test
	public void testCompositeEnablementSetting(){
		assertTrue(composite.getChildren()[0].isEnabled());		
	}
	
	
	@Test
	public void testButtonsDisabled(){
		assertFalse(composite.getAcceptButton().isEnabled());
		assertFalse(composite.getDeclineButton().isEnabled());
		assertFalse(composite.getDecisionButton().isEnabled());
		assertFalse(composite.getChartButton().isEnabled());
	}
	
	@Test
	public void testEnableButtonsAndTurnOff(){
		composite.getAcceptButton().setEnabled(true);
		composite.getDecisionButton().setEnabled(true);
		composite.getDeclineButton().setEnabled(true);
		composite.getChartButton().setEnabled(true);
		assertTrue(composite.getAcceptButton().isEnabled());
		assertTrue(composite.getDeclineButton().isEnabled());
		assertTrue(composite.getDecisionButton().isEnabled());
		assertTrue(composite.getChartButton().isEnabled());
		composite.turnOffAllButtons();
		assertFalse(composite.getAcceptButton().isEnabled());
		assertFalse(composite.getDeclineButton().isEnabled());
		assertFalse(composite.getDecisionButton().isEnabled());
		assertFalse(composite.getChartButton().isEnabled());
	}
}
