package gui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.GregorianCalendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import core.decision.object.ClinicalDecision;
import core.decision.object.DecisionFieldsAddedComposite;

public class DecisionAddedCompositeTest {
	private int style = 0;
	private DecisionFieldsAddedComposite composite;
	private Shell testShell;
	
	@Before
	public void setUp() throws Exception {
		testShell = new Shell();
		composite = new DecisionFieldsAddedComposite(testShell, style);
		composite.getDecisionText().setText("This is my decision");
		composite.getExplanationText().setText("This is my explanation");
		composite.setDecisionFiredFlag(true);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompositeExists() {
		assertNotNull(composite);
	}
	
	@Test
	public void testDecisionAddedComposite() {
		try {
			composite = new DecisionFieldsAddedComposite(null, 0);
			fail("No exception thrown");
		}
		catch (IllegalArgumentException e) {
		}

		int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL};
		for (int i = 0; i < cases.length; i++)
			composite = new DecisionFieldsAddedComposite(testShell, cases[i]);
	}

	@Test
	public void testCompositeChildren(){
	assertEquals(composite.getChildren().length,2);
	Composite buttons = (Composite) composite.getChildren()[0];
	assertEquals(buttons.getParent(),composite);
	assertEquals(buttons.getChildren().length,4);
	Composite decisions = (Composite) composite.getChildren()[1];
	assertEquals(decisions.getParent(),composite);
	assertEquals(decisions.getChildren().length,2);
	}
	
	@Test
	public void testCompositeEnablementSetting(){
		assertTrue(composite.getChildren()[0].isEnabled());		
		assertTrue(composite.getChildren()[1].isEnabled());

	}
	
	@Test
	public void testClearOutputEditorFields() {
		assertEquals(composite.getDecisionText().getText(),"This is my decision");
		assertEquals(composite.getExplanationText().getText(),"This is my explanation");
		composite.clearOutputEditorFields();
		assertEquals(composite.getDecisionText().getText(),"");
		assertEquals(composite.getExplanationText().getText(),"");
	}

	@Test
	public void testClearAdvice() {
		assertEquals(composite.getDecisionText().getText(),"This is my decision");
		assertEquals(composite.getExplanationText().getText(),"This is my explanation");
		assertTrue(composite.getDecisionFiredFlag());
		composite.clearAdvice();
		assertEquals(composite.getDecisionText().getText(),"");
		assertEquals(composite.getExplanationText().getText(),"");
		assertFalse(composite.getDecisionFiredFlag());
	}

	@Test
	public void testDecisionChanged() {
		ClinicalDecision decision = new ClinicalDecision(new GregorianCalendar(199,12,12),"BACKCHART");
		decision.setAdviceText("This is my new advice");
		decision.setRationaleText("This is my new explanation");
		composite.clearAdvice();
		assertEquals(composite.getDecisionText().getText(),"");
		assertEquals(composite.getExplanationText().getText(),"");
		composite.decisionChanged(decision);
		assertEquals(composite.getDecisionText().getText(),"This is my new advice");
		assertEquals(composite.getExplanationText().getText(),"This is my new explanation");
	}
}
