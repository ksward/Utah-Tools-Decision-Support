package glucose.core.tests;

import static org.junit.Assert.*;
import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.drools.utilities.TrackingAgendaEventListener;
import core.drools.utilities.TrackingProcessEventListener;
import core.patient.object.Patient;

public class TestGlucoseRangeDetection {
	private GregorianCalendar decisionDate;
	private GlucoseDecision decision;
	private Patient patient;
	private TrackingAgendaEventListener eventListener = GlucoseObjectsEngineNeededSuite.getEngine().getTrackingAgendaEventListener();
	private TrackingProcessEventListener nodeListener = GlucoseObjectsEngineNeededSuite.getEngine().getTrackingProcessEventListener();

	@Before
	public void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56", "ST03CHOM0002", new GregorianCalendar(1999, 12, 12),
				 12.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 435, 0, 1);
		patient.addDecision(decision);
		eventListener.reset();
		nodeListener.reset();
	}

	@After
	public void tearDown() throws Exception {
		patient = null;
		decisionDate = null;
		decision = null;
	}

	@Test
	public void testClassifyGlucoseInRange() {
		decision.setSerumGlucoseConcentration(90);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testClassifyGlucoseBelowRange() {
		decision.setSerumGlucoseConcentration(78);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose below range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose below range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose below range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose below range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose below range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testClassifyGlucoseAboveRange() {
		decision.setSerumGlucoseConcentration(111);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose above range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertTrue("Failure to classify glucose above range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertFalse("Failure to classify glucose above range",
				nodeListener.isNodeFired("Below Range"));
		assertTrue("Failure to classify glucose above range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose above range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testDetectionExtremeHypoglycemia() {
		decision.setSerumGlucoseConcentration(39);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testDetectionExtremeHypoglycemia2() {
		decision.setSerumGlucoseConcentration(0);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testDetectionNegativeGlucoseValue() {
		decision.setSerumGlucoseConcentration(-60);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testDetectionModerateHypoglycemia() {
		decision.setSerumGlucoseConcentration(40);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Within Range"));
	}

	@Test
	public void testDetectionModerateHypoglycemia2() {
		decision.setSerumGlucoseConcentration(59);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Hypoglycemia"));
	}

	@Test
	public void testDetectionMildHypoglycemia() {
		decision.setSerumGlucoseConcentration(60);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Hypoglycemia"));
	}

	@Test
	public void testDetectionMildHypoglycemia2() {
		decision.setSerumGlucoseConcentration(79);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertTrue("Failure to classify glucose within range",
				nodeListener.isNodeFired("Hypoglycemia"));
	}

	@Test
	public void testDetectionMildHypoglycemia3() {
		decision.setSerumGlucoseConcentration(80);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertTrue("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose within range"));
		assertFalse("Failure to classify glucose within range",
				eventListener.isRuleFired("Explain glucose above range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Below Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Above Range"));
		assertFalse("Failure to classify glucose within range",
				nodeListener.isNodeFired("Hypoglycemia ?"));
	}
}
