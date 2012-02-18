package drools.engine;

import hypertonic.decision.object.GenericDecision;
import hypertonic.saline.preferences.GenericPreferenceConstants;

import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.runtime.StatefulKnowledgeSession;
import org.eclipse.jface.preference.IPreferenceStore;
import core.drools.utilities.KnowledgeEngineController;
import activator.Activator;

public class KnowledgeEngine extends KnowledgeEngineController {

	/**
	 * The application developer should use this constructor to pass in the names of the
	 * rule files.  There are built in choices for combinations of rules files,
	 * rule flow files, and tables.  These are described here:
	 * 
	 * knowledgeEngineFromDRL(String rulesFile)
	 * knowledgeEngineFromDRLandRuleFlow(String rulesFile, String flowFile)
	 * knowledgeEngineFromDRLandSpreadsheet(String rulesFile, String tableFile)
	 * 
	 * If additional combinations are desired, these should be added to the superclass
	 * definition rather than here, to keep it decoupled and enable its use by other
	 * application developers.
	 * 
	 * This application also handles the injection of domain specific global variables.
	 * 
	 */
	public KnowledgeEngine() {
		knowledgeEngineFromDRL("GenericPassiveDroolRules00.drl");
		}

	/**
	 * 	This method is used to obtain the user preferences from the application and then
	 *  inject these global variables into the session.  This routine cannot be called from
	 *  unit core.tests because user preferences are not available in that environment.
	 */
	public void fireRules(final GenericDecision salineDecision) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		traceRulesLogger = new WorkingMemoryInMemoryLogger(session);
		
		// Here is where you get the user preferences to insert as global variables
		session.setGlobal("adultWeightLimit", store.getInt(GenericPreferenceConstants.ADULT_WEIGHT_LIMIT));
		
		// Insert the decision object of the proper type
		session.insert(salineDecision);
		
		// IF you have a flow process, start it here
		//session.startProcess("glucose.flow.id");
		
		session.fireAllRules();
		session.dispose();
	}

	/**
	 * This method is for unit testing and should not be used in production code.
	 * In developer unit core.tests, call this method and set the global variables to be
	 * appropriate for the unit test setup.  In production code method, the global
	 * variables are obtained from user preferences in the application.
	 */
	public void testFireRules(final GenericDecision salineDecision) {
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		session.addEventListener(trackingProcessEventListener);
		session.addEventListener(trackingAgendaEventListener);
		
		// The following values should be the numbers expected by your unit core.tests
		// and probably correspond to your default values
		session.setGlobal("adultWeightLimit", 50);
		
		session.insert(salineDecision);
		// session.startProcess("glucose.flow.id"); IF YOU HAVE A FLOW THEN START IT HERE
		
		session.fireAllRules();
		session.dispose();
	}
}


