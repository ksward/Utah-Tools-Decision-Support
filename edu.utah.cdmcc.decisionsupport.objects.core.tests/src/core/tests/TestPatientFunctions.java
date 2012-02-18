package core.tests;

import static org.junit.Assert.assertEquals;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import core.decision.object.ClinicalDecision;
import core.patient.object.Patient;
import static org.mockito.Mockito.*;

public class TestPatientFunctions {
	private GregorianCalendar birthdate;
	private Patient patient;
	private GregorianCalendar testdate;
	private ClinicalDecision decision, anotherDecision, thirdDecision;

	@Before
	public void setUp() throws Exception {
		birthdate = new GregorianCalendar(1999, 12, 12);
		testdate = new GregorianCalendar(2000,12,12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		decision = new ClinicalDecision(testdate,ClinicalDecision.ACCEPT);
		anotherDecision = new ClinicalDecision(testdate,ClinicalDecision.ACCEPT);
		thirdDecision = new ClinicalDecision(testdate,ClinicalDecision.DECLINE);
	}

	@Test
	public void testListObjectWithMockito(){
	@SuppressWarnings("unchecked")
	List<String> mockedList = mock(List.class);
	mockedList.add("one");
	mockedList.clear();
	verify(mockedList).add("one");
	verify(mockedList).clear();	
	}
	
	
	@Test
	public void testGetDeltaAgeYears() {
		assertEquals("Age delta in years failed", 1,patient.getDeltaAgeYears(testdate));
	}

	@Test
	public void testGetDeltaAgeDays() {
		assertEquals("Age delta in days failed", 365, patient.getDeltaAgeDays(testdate),2);
	}
	
	@Test
	public void testAddDecision(){
		assertEquals("Patient object initially has no decisions",0,patient.getDecisions().size());
		patient.addDecision(decision);
		assertEquals("Patient decision addition failed",1,patient.getDecisions().size());
	}
	
	@Test
	public void testAddSameDecisionPrevented(){
		assertEquals("Patient object initially has no decisions",0,patient.getDecisions().size());
		patient.addDecision(decision);
		patient.addDecision(decision);
		assertEquals("Patient decision addition failed",1,patient.getDecisions().size());
	}
	
	@Test
	public void testAddEqualDecisionPrevented(){
		assertEquals("Patient object initially has no decisions",0,patient.getDecisions().size());
		patient.addDecision(decision);
		patient.addDecision(anotherDecision);
		assertEquals("Patient decision addition failed",1,patient.getDecisions().size());
	}
	
	@Test
	public void testAddNewDecisionSameTime(){
		assertEquals("Patient object initially has no decisions",0,patient.getDecisions().size());
		patient.addDecision(decision);
		patient.addDecision(thirdDecision);
		assertEquals("Patient different decision at same obs time not added",2,patient.getDecisions().size());
	}
	
}
