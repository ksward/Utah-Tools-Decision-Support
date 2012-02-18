package glucose.core.tests;

import static org.junit.Assert.assertEquals;
import glucose.decision.object.GlucoseDecision;

import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestGlucoseFunctions{
	private GregorianCalendar observationDate;
	private GregorianCalendar previousDate;
	private GlucoseDecision glucoseDecision;

	@Before
	public final  void setUp() throws Exception {
		observationDate = new GregorianCalendar(2005,9,25,14,55,55);
		previousDate = new GregorianCalendar(2005,9,25,13,56,55);
		glucoseDecision = new GlucoseDecision(observationDate, "BACKCHART", 400,0,1);
		glucoseDecision.setPreviousGlucoseConcentration(600);
		glucoseDecision.setPreviousObservationTime(previousDate);		
	}

	@After
	public final void tearDown() throws Exception {
		observationDate = null;
		previousDate = null;
		glucoseDecision = null;
	}
	
	@Test
	public final void testGetDeltaMinutes(){
		assertEquals("Get delta minutes test failed", 59., 
				glucoseDecision.getDeltaMinutes(observationDate, previousDate),0);
	}
	@Test
	public final void testGetDeltaMinutesNull(){
		glucoseDecision.setPreviousGlucoseConcentration(GlucoseDecision.NOPREVIOUSGLUCOSE);
		glucoseDecision.setPreviousObservationTime(null);
		assertEquals("Get null delta minutes test failed", null, 
				glucoseDecision.getDeltaMinutes(observationDate, glucoseDecision.getPreviousObservationTime()));
	}
	@Test
	public final void testGlucoseChangePerHour(){
		glucoseDecision.setSerumGlucoseConcentration(400);
		glucoseDecision.setPreviousGlucoseConcentration(600);
		assertEquals("Glucose change per hour test failed", -200.*60/59, glucoseDecision.getGlucoseChangePerHour(),0.1);
	}
	@Test
	public final void testGlucoseChangePerHourNull(){
		glucoseDecision.setSerumGlucoseConcentration(400);
		glucoseDecision.setPreviousGlucoseConcentration(GlucoseDecision.NOPREVIOUSGLUCOSE);
		assertEquals("Glucose change per hour when null failed", null, glucoseDecision.getGlucoseChangePerHour());
	}
}
