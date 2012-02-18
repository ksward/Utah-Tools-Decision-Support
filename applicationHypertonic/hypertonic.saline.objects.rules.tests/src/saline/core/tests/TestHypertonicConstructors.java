package saline.core.tests;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import hypertonic.decision.object.HypertonicSalineDecision;
import org.junit.*;
import core.decision.object.ClinicalDecision;
import core.patient.object.Patient;

public class TestHypertonicConstructors {
	
	private HypertonicSalineDecision decision;
	protected Patient patient;
	protected ClinicalDecision otherDecision;
	protected GregorianCalendar birthdate, decisionDate;
	
	@Test
	public void testEmptyConstructor(){
		GregorianCalendar rightNow = new GregorianCalendar();
		rightNow.get(GregorianCalendar.YEAR);
		decision = new HypertonicSalineDecision();
		assertNotNull("Empty constructor failed",decision);
		assertTrue("ToString should report the year ",decision.toString().contains(String.valueOf(rightNow.get(GregorianCalendar.YEAR))));
	}
	
	@Test 
	public void testOneArgConstructor(){
		GregorianCalendar observationDate = new GregorianCalendar(1999,11,12,0,0);
		decision = new HypertonicSalineDecision(observationDate);
		assertNotNull("Single argument constructor failed", decision);
		assertEquals("Date not set correctly",1999, observationDate.get(GregorianCalendar.YEAR));
		assertEquals("Date not set correctly",11, observationDate.get(GregorianCalendar.MONTH));
		assertTrue("ToString should report the year ",decision.toString().contains("1999"));
	}
	
	@Test
	public void testFullConstructor(){
		decision = new HypertonicSalineDecision(new GregorianCalendar(), 4.5, 12, 56, 12);
		assertNotNull("Full constructor failure", decision);
		assertEquals("ICP not set correctly",(Integer) 12,decision.getIntracranialPressure());
		assertEquals("MAP not set correctly", (Integer) 56, decision.getMeanArterialPressure());
		assertEquals("CVP not set correctly", (Integer) 12, decision.getCentralVenousPressure());
		assertEquals("Drip rate not set correctly", (Double) 4.5,decision.getCurrentHypertonicSalineDripRate());
	}
	
}
