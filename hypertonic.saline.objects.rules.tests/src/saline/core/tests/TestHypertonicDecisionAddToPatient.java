package saline.core.tests;

import static org.junit.Assert.assertEquals;
import hypertonic.decision.object.HypertonicSalineDecision;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import core.decision.object.ClinicalDecision;
import core.patient.object.Patient;

public class TestHypertonicDecisionAddToPatient {

	private HypertonicSalineDecision decision;
	protected Patient patient;
	protected ClinicalDecision otherDecision;
	protected GregorianCalendar birthdate, decisionDate;
	
	@Test
	public void testHypertonicDecisionCount(){
		assertEquals("Decisions not inserted correctly", 6,patient.getDecisions().size());
	}
	
	@Test
	public void testDuplicateDecisionRejection(){
		assertEquals("Decisions not inserted correctly", 6,patient.getDecisions().size());
		decisionDate = new GregorianCalendar(2007,0,5,14,0,0); //Jan 5 14:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		assertEquals("Duplicate decision inserted", 6,patient.getDecisions().size());
	}
	
	@Test
	public void testMultipleDuplicateDecisionRejection(){
		assertEquals("Decisions not inserted correctly", 6,patient.getDecisions().size());
		decisionDate = new GregorianCalendar(2007,0,5,14,0,0); //Jan 5 14:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,15,0,0); //Jan 5 15:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,10,0,0); //Jan 5 10:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,11,0,0); //Jan 5 11:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,12,0,0); //Jan 5 12:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,13,0,0); //Jan 5 13:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		assertEquals("Duplicate decision inserted", 6,patient.getDecisions().size());
	}
	
	@Test
	public void testNewDecisionAddition(){
		assertEquals("Decisions not inserted correctly", 6,patient.getDecisions().size());
		decisionDate = new GregorianCalendar(2007,0,5,14,0,1); //Jan 5 14:00:01 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		assertEquals("New decision insertion failed", 7,patient.getDecisions().size());
	}
		
	@Before
	public void setUp() throws Exception {
		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);

		decisionDate = new GregorianCalendar(2007,0,5,14,0,0); //Jan 5 14:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,15,0,0); //Jan 5 15:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,10,0,0); //Jan 5 10:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,11,0,0); //Jan 5 11:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,12,0,0); //Jan 5 12:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,13,0,0); //Jan 5 13:00:00 am
		decision = new HypertonicSalineDecision(decisionDate);
		patient.addDecision(decision);
	}
	
}
