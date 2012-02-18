package gui.tests;

import static org.junit.Assert.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vasoactive.decision.object.GlucoseFieldsAddedComposite3;


public class TestGlucoseFieldsAddedComposite {
	private int style = 0;
	private GlucoseFieldsAddedComposite3 composite;
	private Shell testShell;
	
	@Before
	public void setUp() throws Exception {
		testShell = new Shell();
		composite = new GlucoseFieldsAddedComposite3(testShell,style);
		composite.getCurrentGlucoseText().setText("12");
		composite.getCurrentInsulinText().setText("0.02");
		composite.getObsDateText().setText("12/12/2003");
		composite.getObsTimeText().setText("12:45");
		composite.setDecisionFiredFlag(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompositeExists() {
		assertNotNull(composite);
	}
	
	@Test
	public void testGlucoseFieldsAddedComposite() {
		try {
			composite = new GlucoseFieldsAddedComposite3(null, 0);
			fail("No exception thrown");
		}
		catch (IllegalArgumentException e) {
		}

		int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL};
		for (int i = 0; i < cases.length; i++)
			composite = new GlucoseFieldsAddedComposite3(testShell, cases[i]);
	}

	@Test
	public void testCompositeChildren(){
	assertEquals(composite.getChildren().length,3);
	Composite buttons = (Composite) composite.getChildren()[0];
	assertEquals(buttons.getParent(),composite);
	assertEquals(buttons.getChildren().length,4);
	Composite decisions = (Composite) composite.getChildren()[1];
	assertEquals(decisions.getParent(),composite);
	assertEquals(decisions.getChildren().length,2);
	Composite glucoseStuff = (Composite) composite.getChildren()[2];
	assertEquals(glucoseStuff.getParent(),composite);
	}
	
	@Test
	public void testCompositeEnablementSetting(){
		assertTrue(composite.getChildren()[0].isEnabled());		
		assertTrue(composite.getChildren()[1].isEnabled());
		assertTrue(composite.getChildren()[2].isEnabled());
	}


	@Test
	public void testGetCurrentGlucoseText() {
		assertEquals(composite.getCurrentGlucoseText().getText(),"12");
	}

	@Test
	public void testGetCurrentInsulinText() {
		assertEquals(composite.getCurrentInsulinText().getText(),"0.02");
	}

	@Test
	public void testGetObsDateText() {
		assertEquals(composite.getObsDateText().getText(),"12/12/2003");
	}

	@Test
	public void testGetObsTimeText() {
		assertEquals(composite.getObsTimeText().getText(),"12:45");
	}

	@Test
	public void testEnableButtonCombinations() {
		composite.setAcceptDeclineOn();
		assertTrue(composite.getAcceptButton().isEnabled());
		assertFalse(composite.getDecisionButton().isEnabled());
		assertFalse(composite.getChartButton().isEnabled());
		assertTrue(composite.getDeclineButton().isEnabled());	
		composite.setAcceptDeclineOff();
		assertFalse(composite.getAcceptButton().isEnabled());
		assertTrue(composite.getDecisionButton().isEnabled());
		assertTrue(composite.getChartButton().isEnabled());
		assertFalse(composite.getDeclineButton().isEnabled());		
	}

	@Test
	public void testEnableCorrectButtonCombinations(){
		assertTrue(composite.allRequiredFieldsFilledIn());
		composite.enableCorrectButtonCombination();
		assertFalse(composite.getAcceptButton().isEnabled());
		assertTrue(composite.getDecisionButton().isEnabled());
		assertTrue(composite.getChartButton().isEnabled());
		assertFalse(composite.getDeclineButton().isEnabled());
		composite.setDecisionFiredFlag(true);
		composite.enableCorrectButtonCombination();
		assertTrue(composite.getAcceptButton().isEnabled());
		assertFalse(composite.getDecisionButton().isEnabled());
		assertFalse(composite.getChartButton().isEnabled());
		assertTrue(composite.getDeclineButton().isEnabled());
	}
	
	@Test
	public void testCheckTimeFormat(){

		composite.getObsTimeText().setText("02:30 AM");
		assertTrue(composite.checkTimeFormat());
		composite.getObsTimeText().setText("22:30 PM");
		assertTrue(composite.checkTimeFormat());
		composite.getObsTimeText().setText("14:8 pM");
		assertTrue(composite.checkTimeFormat());
		composite.getObsTimeText().setText("14:30 ");
		assertTrue(composite.checkTimeFormat());
	}
	
	@Test
	public void testCheckDateFormat(){
		composite.setDecisionFiredFlag(true);
		assertTrue(composite.allRequiredFieldsFilledIn());
		composite.getObsDateText().setText("12/12/2003");
		assertTrue(composite.allRequiredFieldsFilledIn());
	}
	
	@Test
	public void testAllRequiredFieldsFilledInGlucoseMissing(){
		assertTrue(composite.allRequiredFieldsFilledIn());
		composite.getCurrentGlucoseText().setText("");
		assertFalse("Failed to detect empty glucose text",composite.allRequiredFieldsFilledIn());
	}
	
	@Test
	public void testAllRequiredFieldsFilledInInsulinMissing(){
		assertTrue(composite.allRequiredFieldsFilledIn());
		composite.getCurrentInsulinText().setText("");
		assertFalse("Failed to detect empty insulin text",composite.allRequiredFieldsFilledIn());
	}
	
	@Test
	public void testAllRequiredFieldsFilledInDateMissing(){
		assertTrue(composite.allRequiredFieldsFilledIn());
		composite.getObsDateText().setText("");
		assertFalse("Failed to detect empty date text",composite.allRequiredFieldsFilledIn());
	}

}
