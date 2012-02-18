package glucose.core.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.drools.utilities.TrackingAgendaEventListener;
import core.patient.object.Patient;

public class TestDetectWeightClassification {
	private GregorianCalendar decisionDate;
	private GlucoseDecision decision;
	private Patient patient;
	private TrackingAgendaEventListener eventListener = GlucoseObjectsEngineNeededSuite.getEngine().getTrackingAgendaEventListener();
	
	@Before
	public final void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12),  12.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 435, 0, 1);
		patient.addDecision(decision);
		eventListener.reset();
	}

	@After
	public final void tearDown() throws Exception {
		patient = null;
		decisionDate = null;
		decision = null;
	}	

	@Test
	public final void testPediatricWeightDetection49Ext(){
		decision.setPatientWeight(49.0);
		decision.setSerumGlucoseConcentration(39);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and below adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia"));
	}

	@Test
	public final void testPediatricWeightDetection50Ext(){
		decision.setPatientWeight(50.0);
		decision.setSerumGlucoseConcentration(39);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and below adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia"));
	}
	
	
	@Test
	public final void testPediatricWeightDetection99Ext(){
		decision.setPatientWeight(99.0);
		decision.setSerumGlucoseConcentration(39);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and below adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and below adult weight definition"));
	}
	
	@Test
	public final void testPediatricWeightDetection49(){
		decision.setPatientWeight(49.0);
		decision.setSerumGlucoseConcentration(59);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and below adult weight definition"));
	}

	@Test
	public final void testPediatricWeightDetection50(){
		decision.setPatientWeight(50.0);
		decision.setSerumGlucoseConcentration(59);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and below adult weight definition"));
	}
	
	
	@Test
	public final void testPediatricWeightDetection99(){
		decision.setPatientWeight(99.0);
		decision.setSerumGlucoseConcentration(59);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and below adult weight definition"));
		assertTrue("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for moderate hypoglycemia and above adult weight definition"));
		assertFalse("Failure to fire pediatric weight range rule",
				eventListener.isRuleFired("Glucose bolus for extreme hypoglycemia and below adult weight definition"));
	}
	
}
