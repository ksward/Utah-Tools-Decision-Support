package glucose.core.tests;

import static org.junit.Assert.assertTrue;
import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.drools.utilities.TrackingAgendaEventListener;
import core.drools.utilities.TrackingProcessEventListener;
import core.patient.object.Patient;

public class TestInsulinOnAndOff {
	private GregorianCalendar decisionDate;
	private GlucoseDecision decision;
	private Patient patient;
	private TrackingAgendaEventListener eventListener = GlucoseObjectsEngineNeededSuite.getEngine().getTrackingAgendaEventListener();
	private TrackingProcessEventListener nodeListener = GlucoseObjectsEngineNeededSuite.getEngine().getTrackingProcessEventListener();
	
	@Before
	public final void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), 12.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 435, 0, 1);
		patient.addDecision(decision);
		eventListener.reset();
		nodeListener.reset();
	}

	@After
	public final void tearDown() throws Exception {
		patient = null;
		decisionDate = null;
		decision = null;
	}
	
	@Test
	public final void testDetectionInsulinOffGlucoseInRange(){
		decision.setCurrentInsulinDripRate(0.0);
		decision.setSerumGlucoseConcentration(95);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to detect insulin off with glucose within range",
				eventListener.isRuleFired("Serum glucose within range and insulin is off"));
		assertTrue("Failure to detect insulin off with glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}
	
	@Test
	public final void testDetectionInsulinOnWithHypoglycemia(){
		decision.setCurrentInsulinDripRate(2.0);
		decision.setSerumGlucoseConcentration(79);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to detect insulin on with glucose hypoglycemic",
				eventListener.isRuleFired("Serum glucose below range and on insulin"));
		assertTrue("Failure to detect insulin on with glucose hypoglycemic",
				nodeListener.isNodeFired("Below Range"));
		assertTrue("Failure to detect insulin on with glucose hypoglycemic",
				nodeListener.isNodeFired("Hypoglycemia"));
	}
	
		
	@Test
	public final void testDetectionInsulinNegative(){
		decision.setCurrentInsulinDripRate(-0.001);
		decision.setSerumGlucoseConcentration(95);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to detect insulin off with glucose within range",
				eventListener.isRuleFired("Serum glucose within range and insulin is off"));
	}
	

	@Test 
	public final void testRecommendNegligibleInsulinTurnOff(){
		decision.setCurrentInsulinDripRate(0.01);
		decision.setSerumGlucoseConcentration(95);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
	}
}
