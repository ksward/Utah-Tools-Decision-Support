package glucose.core.tests;

import static org.junit.Assert.assertEquals;
import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import core.patient.object.Patient;

public class TestInsulinDosing {
	static Double factor = 0.00018;
	private GregorianCalendar decisionDate;
	private GlucoseDecision decision;
	private Patient patient;

	@Before
	public void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), 35.34, 55.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 435, 0, 1);
		patient.addDecision(decision);
	}

	@After
	public void tearDown() throws Exception {
		patient = null;
		decisionDate = null;
		decision = null;
	}
	
	@Ignore
	@Test
	public void testStartingInsulinGlucose200(){
		decision.setSerumGlucoseConcentration(200);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				factor*decision.getPatientWeight()*decision.getSerumGlucoseConcentration(), decision.getRecommendedInsulinDripRate(),0);
	}
	
	@Ignore
	@Test
	public void testStartingInsulinGlucose300(){
		decision.setSerumGlucoseConcentration(300);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				factor*decision.getPatientWeight()*decision.getSerumGlucoseConcentration(), decision.getRecommendedInsulinDripRate(),0);

	}
	
	@Ignore
	@Test
	public void testStartingInsulinGlucose400(){
		decision.setSerumGlucoseConcentration(400);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				factor*decision.getPatientWeight()*decision.getSerumGlucoseConcentration(), decision.getRecommendedInsulinDripRate(),0);

	}
	
	@Ignore
	@Test
	public void testStartingInsulinGlucose111(){
		decision.setSerumGlucoseConcentration(111);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				factor*decision.getPatientWeight()*decision.getSerumGlucoseConcentration(), decision.getRecommendedInsulinDripRate(),0);

	}
	
	@Test
	public void testStartingInsulinGlucose80(){
		decision.setSerumGlucoseConcentration(80);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				0, decision.getRecommendedInsulinDripRate(),0);

	}
	
	@Test
	public void testStartingInsulinGlucose90(){
		decision.setSerumGlucoseConcentration(90);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				0, decision.getRecommendedInsulinDripRate(),0);

	}
	
	@Test
	public void testStartingInsulinGlucose60(){
		decision.setSerumGlucoseConcentration(60);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Failed starting dose of insulin", 
				0, decision.getRecommendedInsulinDripRate(),0);

	}
}
