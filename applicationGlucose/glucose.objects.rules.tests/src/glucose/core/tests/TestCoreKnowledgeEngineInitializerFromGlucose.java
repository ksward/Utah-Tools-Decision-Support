package glucose.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.InputStream;
import java.util.GregorianCalendar;
import org.drools.audit.WorkingMemoryInMemoryLogger;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;
import core.patient.object.Patient;
import edu.utah.cdmcc.drools.engine.KnowledgeEngineInitializer;
import glucose.decision.object.GlucoseDecision;

public class TestCoreKnowledgeEngineInitializerFromGlucose {
	
	private KnowledgeBuilder builder;
	private KnowledgeEngineInitializer initializer;
	private static GlucoseDecision decision;
	private static Patient patient;
	private static GregorianCalendar decisionDate;
	private final static int HighGlucose = 450;
	private final static int WithinRangeGlucose = 100;

	@Before
	public void setUp() {
		initializer = new KnowledgeEngineInitializer();
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), 22.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", HighGlucose, 5, 1);
		patient.addDecision(decision);
	}


	@Test
	public void testAddDrlResourceToKnowledgeBase() {
		InputStream inputStream = getClass().getResourceAsStream("/GlucoseDroolRules00.drl");
		builder = initializer.getKnowledgeBuilder();
		builder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRL);
		assertEquals(
				"DRL should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddRuleflowResourceToKnowledgeBase() {
		InputStream inputStream = getClass()
				.getResourceAsStream("/glucoseDroolsRuleflow.rf");
		builder = initializer.getKnowledgeBuilder();
		builder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.DRF);
		assertEquals("Rule flow should have been added without errors.\n"
				+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddDrlResourceViaInitializer() throws Exception {
		initializer.addDrlResource("/GlucoseDroolRules00.drl");
		builder = initializer.getKnowledgeBuilder();
		assertEquals("Drl should have been added", 1, builder
				.getKnowledgePackages().size());
		assertEquals(
				"DRL should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddFlowResourceViaInitializer() throws Exception {
		initializer.addFlowResource("/glucoseDroolsRuleflow.rf");
		builder = initializer.getKnowledgeBuilder();
		assertEquals("Flow should have been added", 1, builder
				.getKnowledgePackages().size());
		assertEquals(
				"Flow should have been added without errors.\n"
						+ builder.getErrors(), 0, builder.getErrors().size());
	}

	@Test
	public void testAddTrackingAgendaListenerToSession() throws Exception{
		initializer.addDrlResource("/GlucoseDroolRules00.drl");
		initializer.addPackagesToKnowledgeBase();
		assertEquals("Process listener should not yet have been added to session",
				0,initializer.getStatefulKnowledgeSession().getProcessEventListeners().size());
		initializer.addTrackingAgendaEventListenerToSession();
		assertEquals("Process listener should  have been added to session",
				0,initializer.getStatefulKnowledgeSession().getProcessEventListeners().size());				
	}
	
	@Test 
	public void testInsertGlucoseGlobalsIntoSession() throws Exception {
		initializer.addDrlResource("/GlucoseDroolRules00.drl");
		initializer.addPackagesToKnowledgeBase();
		StatefulKnowledgeSession session = initializer.getStatefulKnowledgeSession();
		insertGlucoseGlobals(session);
		assertEquals("Should return value for global insulinOnlyPerHour","adult",session.getGlobal("insulinOnlyPerHour"));
		assertEquals("Should return value for global severeHypoglycemiaLimit",40,session.getGlobal("severeHypoglycemiaLimit"));
		assertEquals("Should return value for global moderateHypoglycemiaLimit",60,session.getGlobal("moderateHypoglycemiaLimit"));
		assertEquals("Should return value for global mildHypoglycemiaLimit",80,session.getGlobal("mildHypoglycemiaLimit"));
		assertEquals("Should return value for global lowerRangeLimit",80,session.getGlobal("lowerRangeLimit"));
		assertEquals("Should return value for global upperRangeLimit",110,session.getGlobal("upperRangeLimit"));
		assertEquals("Should return value for global adultWeightLimit",50,session.getGlobal("adultWeightLimit"));
	}


	
	@Test
	public void testInsertGlucoseDecision() throws Exception {
		StatefulKnowledgeSession session = getInitializedGlucoseSession();
		assertEquals("Initially should contain no facts",0,session.getFactHandles().size());
		session.insert(decision);
		assertEquals("Initially should contain one fact",1,session.getFactHandles().size());
	}



	
	@Test
	public void testFireGlucoseRulesWithoutFlowResourceShouldFail() throws Exception {
		initializer.addDrlResource("/GlucoseDroolRules00.drl");
		//initializer.addFlowResource("/glucoseDroolsRuleflow.rf");
		initializer.addPackagesToKnowledgeBase();
		StatefulKnowledgeSession session = initializer.getStatefulKnowledgeSession();
		session.addEventListener(initializer.getTrackingAgendaEventListener());
		insertGlucoseGlobals(session);
		session.insert(decision);
		//session.startProcess("glucose.flow.id");
		session.fireAllRules();
		assertFalse("Glucose above range rule should not have fired in absence of flow",initializer.getTrackingAgendaEventListener().isRuleFired("Explain glucose above range"));
	}
	
	@Test 
	public void testFireGlucoseRules() throws Exception {
		StatefulKnowledgeSession session = getInitializedGlucoseSession();
		session.addEventListener(initializer.getTrackingAgendaEventListener());
		insertDecisionAndFireRules(session);
		assertTrue("Glucose above range rule should have fired",initializer.getTrackingAgendaEventListener().isRuleFired("Explain glucose above range"));
		assertFalse("Glucose within range should not have fired",initializer.getTrackingAgendaEventListener().isRuleFired("Explain glucose within range"));
	}



	@Test
	public void testWorkingMemoryInMemoryLoggerToGlucoseSession() throws Exception {
		StatefulKnowledgeSession session = getInitializedGlucoseSession();
		WorkingMemoryInMemoryLogger traceRulesLogger = new WorkingMemoryInMemoryLogger(session);
		insertDecisionAndFireRules(session);
		assertTrue("Logger should contain trace of high glucose",traceRulesLogger.getEvents().contains("Explain glucose above range"));
		assertFalse("Logger should not contain trace of within range glucose",traceRulesLogger.getEvents().contains("Explain glucose within range"));
	}
	
	@Test
	public void testMemoryLoggerCorrectWithDifferentGlucose() throws Exception {
		StatefulKnowledgeSession session = getInitializedGlucoseSession();
		WorkingMemoryInMemoryLogger traceRulesLogger = new WorkingMemoryInMemoryLogger(session);
		insertGlucoseGlobals(session);
		decision.setSerumGlucoseConcentration(WithinRangeGlucose);
		session.insert(decision);
		session.startProcess("glucose.flow.id");
		session.fireAllRules();
		assertFalse("Logger should not contain trace of high glucose",traceRulesLogger.getEvents().contains("Explain glucose above range"));
		assertTrue("Logger should  contain trace of within range glucose",traceRulesLogger.getEvents().contains("Explain glucose within range"));
	}
	
// HELPER FUNCTIONS BELOW
	
	private void insertGlucoseGlobals(StatefulKnowledgeSession session) {
		session.setGlobal("insulinOnlyPerHour", "adult");
		session.setGlobal("severeHypoglycemiaLimit", 40);
		session.setGlobal("moderateHypoglycemiaLimit", 60);
		session.setGlobal("mildHypoglycemiaLimit", 80);
		session.setGlobal("lowerRangeLimit", 80);
		session.setGlobal("upperRangeLimit", 110);
		session.setGlobal("adultWeightLimit", 50);
	}
	

	private void insertDecisionAndFireRules(StatefulKnowledgeSession session) {
		insertGlucoseGlobals(session);
		session.insert(decision);
		session.startProcess("glucose.flow.id");
		session.fireAllRules();
	}
	
	private StatefulKnowledgeSession getInitializedGlucoseSession() throws Exception {
		initializer.addDrlResource("/GlucoseDroolRules00.drl");
		initializer.addFlowResource("/glucoseDroolsRuleflow.rf");
		initializer.addPackagesToKnowledgeBase();
		StatefulKnowledgeSession session = initializer.getStatefulKnowledgeSession();
		return session;
	}
}