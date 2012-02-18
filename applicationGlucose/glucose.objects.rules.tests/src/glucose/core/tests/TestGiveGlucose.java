package glucose.core.tests;

import static org.junit.Assert.assertEquals;
import glucose.decision.object.GlucoseDecision;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import core.patient.object.Patient;

public class TestGiveGlucose {
	private GregorianCalendar decisionDate;
	private GlucoseDecision decision;
	private Patient patient;

	@Before
	public final void setUp() throws Exception {
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", new GregorianCalendar(1999, 12, 12), 12.34, 25.34);
		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 435, 0, 1);
		patient.addDecision(decision);
	}

	@After
	public final void tearDown() throws Exception {
		patient = null;
		decisionDate = null;
		decision = null;
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaSmallWeight(){ 
		decision.setSerumGlucoseConcentration(40);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and glucose equal 39",
				12.34*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaNegativeGlucoseSmallWeight(){ 
		decision.setSerumGlucoseConcentration(-1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and negative glucose",
				12.34*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaZeroGlucoseSmallWeight(){ 
		decision.setSerumGlucoseConcentration(0);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and zero glucose",
				12.34*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseUpperModerateHypoglycemiaSmallWeight(){ 
		decision.setSerumGlucoseConcentration(59);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and glucose equal 59",
				12.34*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerModerateHypoglycemiaSmallWeight(){ 
		decision.setSerumGlucoseConcentration(41);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and glucose equal 40",
				12.34*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerInRangeSmallWeight(){ 
		decision.setSerumGlucoseConcentration(80);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for child weight and bottom of in range glucose",
				0.0, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaBigWeight(){ 
		decision.setSerumGlucoseConcentration(39);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and glucose equal 39",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaNegativeGlucoseBigWeight(){ 
		decision.setSerumGlucoseConcentration(-1);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and negative glucose",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaZeroGlucoseBigWeight(){ 
		decision.setSerumGlucoseConcentration(0);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and zero glucose",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseUpperModerateHypoglycemiaBigWeight(){ 
		decision.setSerumGlucoseConcentration(59);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and glucose equal 59",
				50.0*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerModerateHypoglycemiaBigWeight(){ 
		decision.setSerumGlucoseConcentration(41);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and glucose equal 40",
				50.0*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerInRangeBigWeight(){ 
		decision.setSerumGlucoseConcentration(80);
		decision.setPatientWeight(52.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for adult weight and bottom of in range glucose",
				0.0, decision.getRecommendedGlucoseBolus(),0);
	}

	// REAL FAT PEOPLE
	@Test
	public final void testGlucoseExtremeHypoglycemiaFatWeight(){ 
		decision.setSerumGlucoseConcentration(39);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and glucose equal 39",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaNegativeGlucoseFatWeight(){ 
		decision.setSerumGlucoseConcentration(-1);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and negative glucose",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseExtremeHypoglycemiaZeroGlucoseFatWeight(){ 
		decision.setSerumGlucoseConcentration(0);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and zero glucose",
				50.0*0.50, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseUpperModerateHypoglycemiaFatWeight(){ 
		decision.setSerumGlucoseConcentration(59);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and glucose equal 59",
				50.0*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerModerateHypoglycemiaFatWeight(){ 
		decision.setSerumGlucoseConcentration(41);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and glucose equal 40",
				50.0*0.25, decision.getRecommendedGlucoseBolus(),0);
	}
	
	@Test
	public final void testGlucoseLowerInRangeFatWeight(){ 
		decision.setSerumGlucoseConcentration(80);
		decision.setPatientWeight(150.1);
		GlucoseObjectsEngineNeededSuite.getEngine().testFireRules(decision);
		assertEquals("Incorrect glucose calculation for obese adult weight and bottom of in range glucose",
				0.0, decision.getRecommendedGlucoseBolus(),0);
	}
}
