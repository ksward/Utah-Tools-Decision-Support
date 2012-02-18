package glucose.tests;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import vasoactive.decision.object.VasoactiveDecision;
import vasoactive.decision.object.VasoactiveDecisionState;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import core.patient.object.Gender;
import core.patient.object.Patient;

public class TestHypotensionRules {
	static Double factor = 0.00017143;
	private VasoactiveDecisionState decisionState;
	private GregorianCalendar decisionDate;
	private VasoactiveDecision decision;
	private Patient patient;

	@Before
	public void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), Gender.MALE, 12.34, 25.34);
		decisionState = new VasoactiveDecisionState();
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new VasoactiveDecision(decisionDate, "ACCEPT",120 ,70, 0.0);
		patient.addDecision(decision);
	}

	@After
	public void tearDown() throws Exception {
		patient = null;
		decisionState = null;
		decisionDate = null;
		decision = null;
	}
	
	@Test
	public void testAssignBloodPressureParamForNeonate(){
		decision.setPatientAgeDays(10);
		
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to assign correct warning blood pressure", 45, decisionState.getWarningSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct minimum blood pressure", 60, decisionState.getMinimumSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct maximum blood pressure", 75, decisionState.getMaximumSystolicBloodPressure().intValue());
	}
	@Test
	public void testAssignBloodPressureParamForInfant(){
		decision.setPatientAgeDays(30);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to assign correct warning blood pressure", 53, decisionState.getWarningSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct minimum blood pressure", 70, decisionState.getMinimumSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct maximum blood pressure", 88, decisionState.getMaximumSystolicBloodPressure().intValue());
	}
	@Test
	public void testAssignBloodPressureParamForChild(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to assign correct warning blood pressure", 59, decisionState.getWarningSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct minimum blood pressure", 78, decisionState.getMinimumSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct maximum blood pressure", 98, decisionState.getMaximumSystolicBloodPressure().intValue());
	}
	@Test
	public void testAssignBloodPressureParamForPreTeen(){
		decision.setPatientAgeDays(4020);	// it's an 11 yo
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to assign correct warning blood pressure", 68, decisionState.getWarningSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct minimum blood pressure", 90, decisionState.getMinimumSystolicBloodPressure().intValue());
		assertEquals("Failed to assign correct maximum blood pressure", 113, decisionState.getMaximumSystolicBloodPressure().intValue());
	}
	@Test
	public void DetectSevereHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(55);
		decision.setDiastolicBloodPressure(40);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to detect SEVERE hypotension", VasoactiveDecisionState.SEVERE_HYPOTENSION, 
					decisionState.getHypotensionLevel());
	}
	@Test
	public void DetectMildHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(60);
		decision.setDiastolicBloodPressure(40);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to detect MILD hypotension", VasoactiveDecisionState.MILD_HYPOTENSION, 
					decisionState.getHypotensionLevel());
	}
	@Test
	public void DetectAdequateBloodPressure(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(90);
		decision.setDiastolicBloodPressure(70);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to detect ADEQUATE blood pressure", VasoactiveDecisionState.ADEQUATE_BP, 
					decisionState.getHypotensionLevel());
	}
	@Test
	public void DetectHypertension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(120);
		decision.setDiastolicBloodPressure(90);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to detect ADEQUATE blood pressure", VasoactiveDecisionState.HYPERTENSION, 
					decisionState.getHypotensionLevel());
	}
	@Test
	public void DetectNeedForNewVasopressorForMildHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(60);
		decision.setDiastolicBloodPressure(40);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 5.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void DetectNeedForNewVasopressorForSevereHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(50);
		decision.setDiastolicBloodPressure(40);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 10.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
}