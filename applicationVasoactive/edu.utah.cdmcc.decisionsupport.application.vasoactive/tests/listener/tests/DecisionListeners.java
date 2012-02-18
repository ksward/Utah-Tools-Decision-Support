package listener.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import core.decision.object.IDecisionListener;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.jface.viewers.DecisionTableView;

public class DecisionListeners {

	private ListenerList listenerList;
	Shell shell;

	@Before
	public void setUp() throws Exception {
		listenerList = new ListenerList();

	}

	@Test
	public void testDecisionListeners() throws Exception {

		IDecisionListener listener2 = new DecisionTableView();

		ApplicationControllers.getDecisionController().setDecisionFiredListeners(listenerList);
		assertEquals("Baseline Decision listeners should be zero", 0, ApplicationControllers.getDecisionController()
				.getDecisionFiredListeners().size());
		ApplicationControllers.getDecisionController().addDecisionFiredListener(listener2);
		assertEquals("After addition Decision listeners should be one", 1, ApplicationControllers.getDecisionController()
				.getDecisionFiredListeners().size());
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(listener2);
		assertNull("Decision listeners should be null after removal of last element", ApplicationControllers
				.getDecisionController().getDecisionFiredListeners());
	}
}