package drools.engine;

import glucose.decision.object.GlucoseDecision;
import glucose.preferences.GlucosePreferenceConstants;
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
		knowledgeEngineFromDRLandRuleFlow("GlucoseDroolRules00.drl","glucoseDroolsRuleflow.rf");
		}

	/**
	 * 	This method is used to obtain the user preferences from the application and then
	 *  inject these global variables into the session.  This routine cannot be called from
	 *  unit core.tests because user preferences are not available in that environment.
	 */
	public void fireRules(final GlucoseDecision glucoseDecision) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		traceRulesLogger = new WorkingMemoryInMemoryLogger(session);		
		session.setGlobal("insulinOnlyPerHour", store.getString(GlucosePreferenceConstants.INSULIN_UNITS));
		session.setGlobal("severeHypoglycemiaLimit", store.getInt(GlucosePreferenceConstants.SEVERE_HYPOGLYCEMIA_LIMIT));
		session.setGlobal("moderateHypoglycemiaLimit", store.getInt(GlucosePreferenceConstants.MODERATE_HYPOGLYCEMIA_LIMIT));
		session.setGlobal("mildHypoglycemiaLimit", store.getInt(GlucosePreferenceConstants.MILD_HYPOGLYCEMIA_LIMIT));
		session.setGlobal("lowerRangeLimit", store.getInt(GlucosePreferenceConstants.LOWER_GLUCOSE_TARGET));
		session.setGlobal("upperRangeLimit", store.getInt(GlucosePreferenceConstants.UPPER_GLUCOSE_TARGET));
		session.setGlobal("adultWeightLimit", store.getInt(GlucosePreferenceConstants.ADULT_WEIGHT_LIMIT));
		session.insert(glucoseDecision);
		session.startProcess("glucose.flow.id");
		session.fireAllRules();
		session.dispose();
	}

	/**
	 * This method is for unit testing and should not be used in production code.
	 * In developer unit core.tests, call this method and set the global variables to be
	 * appropriate for the unit test setup.  In production code method, the global
	 * variables are obtained from user preferences in the application.
	 */
	public void testFireRules(final GlucoseDecision glucoseDecision) {
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
		session.addEventListener(trackingProcessEventListener);
		session.addEventListener(trackingAgendaEventListener);
		// The following values are defaults and are expected by the JUnit core.tests
		session.setGlobal("insulinOnlyPerHour", "adult");
		session.setGlobal("severeHypoglycemiaLimit", 40);
		session.setGlobal("moderateHypoglycemiaLimit", 60);
		session.setGlobal("mildHypoglycemiaLimit", 80);
		session.setGlobal("lowerRangeLimit", 80);
		session.setGlobal("upperRangeLimit", 110);
		session.setGlobal("adultWeightLimit", 50);
		session.insert(glucoseDecision);
		session.startProcess("glucose.flow.id");
		session.fireAllRules();
		session.dispose();
	}
}
