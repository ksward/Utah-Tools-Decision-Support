package application.core.tests;
import static org.junit.Assert.*;

import org.eclipse.core.runtime.ListenerList;
import org.junit.*;

import core.decision.object.ClinicalDecision;
import core.decision.object.IDecisionListener;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;

public class TestDecisionListeners {
	private ListenerList listenerList;
	private TestListener listener2;

	@Before
	public void setUp() {
		listenerList = new ListenerList();
		listener2 = new TestListener();
	}

	@After
	public void tearDown() {
		listenerList = null;
		listener2 = null;
	}
	
	@Test
	public void testBaselineListenerListEmpty(){
		ApplicationControllers.getDecisionController().setDecisionFiredListeners(listenerList);
		assertEquals("Baseline Decision listeners should start at zero", 0, ApplicationControllers.getDecisionController()
				.getDecisionFiredListeners().size());
	}
	
	@Test
	public void testAddListenerToList(){
		ApplicationControllers.getDecisionController().addDecisionFiredListener(listener2);
		assertEquals("After addition Decision listeners should be one", 1, ApplicationControllers.getDecisionController()
				.getDecisionFiredListeners().size());
	}
	
	@Test
	public void testRemoveListenerFromList(){
		ApplicationControllers.getDecisionController().setDecisionFiredListeners(listenerList);
		ApplicationControllers.getDecisionController().addDecisionFiredListener(listener2);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(listener2);
		assertNull("Decision listeners should be null after removal of last element", ApplicationControllers
				.getDecisionController().getDecisionFiredListeners());	
	}
	
	private class TestListener implements IDecisionListener {
		public void decisionChanged(ClinicalDecision decision) {}
		public void decisionStored(ClinicalDecision decision) {}	
	}
}
