package glucose.tests;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vasoactive.decision.object.VasoactiveDecision;
import vasoactive.decision.object.VasoactiveDecisionState;
import core.patient.object.Gender;
import core.patient.object.Patient;

public class TestDopamineAdjustments {
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
	public void AdjustDopamineForMildHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(60);
		decision.setDiastolicBloodPressure(40);
		decision.setDopamineInfusionRate(5.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 6.3, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void AdjustDopamineForSevereHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(50);
		decision.setDiastolicBloodPressure(40);
		decision.setDopamineInfusionRate(5.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 7.5, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void MaxOutDopamineAdjustmentForSevereHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(50);
		decision.setDiastolicBloodPressure(40);
		decision.setDopamineInfusionRate(17.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 20.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void MaxOutDopamineForMildHypotension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(50);
		decision.setDiastolicBloodPressure(40);
		decision.setDopamineInfusionRate(18.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 20.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void TestFastWeanForHypertension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(140);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(18.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 14.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void TestFastWeanForHypertensionNearLimit(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(140);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(6.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 5.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void TestFastWeanForHypertensionNearLimit2(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(140);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(5.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 3.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void WeanToOffForHypertension(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(140);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(3.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 0.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void WeanForAdequateBP(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(100);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(10.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 8.0, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void WeanForAdequateBP2(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(100);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(6.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 5, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	@Test
	public void WeanForAdequateBP3(){
		decision.setPatientAgeDays(1461);	// it's a 4 yo
		decision.setSystolicBloodPressure(100);
		decision.setDiastolicBloodPressure(80);
		decision.setDopamineInfusionRate(5.0);
		AllGlucoseDroolsRulesTests.getEngine().fireRules(decision, decisionState);
		assertEquals("Failed to set correct dopamine infusion rate", 3, 
					decision.getRecommendedDopamineInfusionRate().doubleValue(),0.0001);
	}
	
	
	
	
}