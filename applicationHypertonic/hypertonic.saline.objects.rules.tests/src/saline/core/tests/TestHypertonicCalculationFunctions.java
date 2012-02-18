package saline.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import hypertonic.decision.object.HypertonicSalineDecision;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;

public class TestHypertonicCalculationFunctions {
	private HypertonicSalineDecision decision;

	
	@Test
	public void testSodiumDerivative(){

		decision.setCurrentSodiumDateTime(new GregorianCalendar(2010,11,11,13,50));
		decision.setPreviousSodiumDateTime(new GregorianCalendar(2010,11,11,12,50)); // delta hour = 1
		decision.setPreviousSodiumValue(145);
		decision.setCurrentSodiumValue(165);  // delta sodium = 20
		

		assertEquals("Sodium derivative failed",(Double) 20.0, decision.getSodiumDerivative());
		decision.setCurrentSodiumValue(HypertonicSalineDecision.NOPREVIOUSVALUE);
		assertNull("No current sodium - should return null", decision.getSodiumDerivative());
		decision.setCurrentSodiumValue(165); 
		decision.setPreviousSodiumValue(HypertonicSalineDecision.NOPREVIOUSVALUE);
		assertNull("No previous sodium - should return null", decision.getSodiumDerivative());

	}
	
	@Test
	public void testOsmolalityDerivative(){
		
		decision.setCurrentOsmolalityDateTime(new GregorianCalendar(2010,11,11,13,50));
		decision.setPreviousOsmolalityDateTime(new GregorianCalendar(2010,11,11,12,50)); //delta hour = 1
		decision.setPreviousOsmolalityValue(265);
		decision.setCurrentOsmolalityValue(275); //delta 10
		
		assertEquals("Osmolality derivative failed", (Double) 10.0, decision.getOsmolalityDerivative());

		decision.setCurrentOsmolalityValue(HypertonicSalineDecision.NOPREVIOUSVALUE);
		assertNull("No current osm - should return null", decision.getOsmolalityDerivative());
		decision.setCurrentOsmolalityValue(275); 
		decision.setPreviousOsmolalityValue(HypertonicSalineDecision.NOPREVIOUSVALUE);
		assertNull("No previous osm - should return null", decision.getOsmolalityDerivative());
		
	}
	
	@Test
	public void testOsmolalityDerivativeIdenticalDateParameters(){
		
		decision.setCurrentOsmolalityDateTime(new GregorianCalendar(2010,11,11,13,50));
		decision.setPreviousOsmolalityDateTime(new GregorianCalendar(2010,11,11,13,50)); //delta hour = 0
		decision.setPreviousOsmolalityValue(265);
		decision.setCurrentOsmolalityValue(275); //delta 10
		
		assertNull("Osmolality derivative should be null because division by zero", decision.getOsmolalityDerivative());
	}
	
	@Test
	public void testSodiumDerivativeIdenticalDateParameters(){
		
		decision.setCurrentSodiumDateTime(new GregorianCalendar(2010,11,11,13,50));
		decision.setPreviousSodiumDateTime(new GregorianCalendar(2010,11,11,13,50)); //delta hour = 0
		decision.setPreviousSodiumValue(155);
		decision.setCurrentSodiumValue(175); //delta 20
		
		assertNull("Sodium derivative should be null because division by zero", decision.getSodiumDerivative());
	}
	
	@Before
	public void setUp(){
		decision = new HypertonicSalineDecision();

	}
}
