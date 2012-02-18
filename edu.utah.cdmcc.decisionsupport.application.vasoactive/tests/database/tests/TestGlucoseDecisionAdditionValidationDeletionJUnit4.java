//package database.tests;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//
//import java.util.GregorianCalendar;
//
//import org.hibernate.Query;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;
//import org.junit.Before;
//import org.junit.Test;
//
//import vasoactive.decision.object.VasoactiveDecision;
//
//import core.dao.DAOFactory;
//import core.dao.IPatientDAO;
//import core.decision.object.ClinicalDecision;
//import core.patient.object.Gender;
//import core.patient.object.Patient;
//
//public class TestGlucoseDecisionAdditionValidationDeletionJUnit4 {
//
//	protected Patient patient;
//	protected VasoactiveDecision decision;
//	protected ClinicalDecision otherDecision;
//	protected ClassValidator<Patient> personValidator;
//	protected ClassValidator<VasoactiveDecision> glucoseValidator;
//	protected InvalidValue[] validationMessages;
//	protected int oldRecordCount, newRecordCount;
//	protected Query query;
//	protected GregorianCalendar birthdate, decisionDate;
//	private int oldGlucoseDecisionCount;
//	private int oldAllDecisionCount;
//	private int newGlucoseDecisionCount;
//	private int newAllDecisionCount;
//
//	@Test
//	public void testDecisionCreation() {
//		assertEquals("User action BACKCHART: ", "BACKCHART", otherDecision
//				.getUserAction());
//		assertEquals("User action ACCEPT: ", "ACCEPT", decision
//				.getUserAction());
//		assertSame("Patient assoc with decision is same: ", patient, decision
//				.getPatient());
//		assertSame("Patient assoc with nonglucose decision is same: ", patient,
//				otherDecision.getPatient());
//		assertSame("Patient assoc with both decisions is same: ", decision
//				.getPatient(), otherDecision.getPatient());
//		assertEquals("Comment field is empty string: ",ClinicalDecision.EMPTY_STRING, decision.getDeclineComment());
//		assertEquals("Comment field is empty string: ",ClinicalDecision.EMPTY_STRING, decision.getAcceptComment());
//		assertEquals("Comment field is empty string: ",ClinicalDecision.EMPTY_STRING, decision.getOtherComment());
//		assertEquals("Glucose value not correct: ", 435, (long) decision.getSerumGlucoseConcentration());
//		assertEquals("Insulin value not correct: ",0, (double) decision.getCurrentInsulinDripRate(),0);
//		assertEquals("Insulin bolus not correct: ", 0, (double) decision.getRecommendedInsulinBolus(),0);
//		assertEquals("Previous glucose not correct: ", VasoactiveDecision.NOPREVIOUSGLUCOSE, (long) decision.getPreviousGlucoseConcentration());
//		assertEquals("Previous observation not correct: ", VasoactiveDecision.NOPREVIOUSOBSERVATIONDATE, decision.getPreviousObservationTime());
//		
//
//	}
//	
//	@Test
//	public void testDifferenceBetweenDecisionTypes(){
//		Long temp;
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		temp =   (Long) patientDAO.getSession().getNamedQuery(
//				VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		oldGlucoseDecisionCount = temp.intValue();
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		oldAllDecisionCount = temp.intValue();
//		assertEquals("Glucose decision count initially 1: ", 1,
//				oldGlucoseDecisionCount);
//		assertEquals("Total decision count initially 2: ", 2,
//				oldAllDecisionCount);
//		patientDAO.getSession().getTransaction().commit();
//	}
//
//	@Test
//	public void testGlucoseTooHighValidation() {
//		decision.setSerumGlucoseConcentration(2001);
//		validationMessages = glucoseValidator.getInvalidValues(decision);
//		assertEquals("Glucose invalid high concentration: ", 1,
//				validationMessages.length);
//	}
//	
//	@Test
//	public void testGlucoseTooHighExceptionFires() {
//		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
//		try {
//			decision = new VasoactiveDecision(decisionDate, "ACCEPT", 100, 1.0, 120,70,5);
//		} catch (RuntimeException e) {
//			validationMessages = glucoseValidator.getInvalidValues(decision);
//			assertEquals("Glucose invalid high concentration exception error: ", 1,
//					validationMessages.length);
//		}
//	}
//
//	@Test
//	public void testGlucoseTooLowExceptionFires() {
//		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
//		try {
//			decision = new VasoactiveDecision(decisionDate, "ACCEPT", 100, 1.0, 120,70,5);
//		} catch (RuntimeException e) {
//			validationMessages = glucoseValidator.getInvalidValues(decision);
//			assertEquals("Glucose invalid low concentration exception error: ", 1,
//					validationMessages.length);
//		}
//	}
//	
//	@Test
//	public void testGlucoseTooLowValidation() {
//		decision.setSerumGlucoseConcentration(-1);
//		validationMessages = glucoseValidator.getInvalidValues(decision);
//		assertEquals("Glucose invalid low concentration: ", 1,
//				validationMessages.length);
//	}
//	
//	@Test
//	public void testGlucoseCorrectValidation() {
//		decision.setSerumGlucoseConcentration(200);
//		validationMessages = glucoseValidator.getInvalidValues(decision);
//		assertEquals("Glucose valid concentration but message fired anyway: ", 0,
//				validationMessages.length);
//	}
//	
//	@Test
//	public void testDecisionAddition() {
//		Long temp;
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		temp =   (Long) patientDAO.getSession().getNamedQuery(
//				VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		oldGlucoseDecisionCount = temp.intValue();
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		oldAllDecisionCount = temp.intValue();
//		assertEquals("Glucose decision count initially 1: ", 1,
//				oldGlucoseDecisionCount);
//		assertEquals("Total decision count initially 2: ", 2,
//				oldAllDecisionCount);
//	
//		patient = patientDAO.getLastPatient();
//		decisionDate = new GregorianCalendar(2005, 9, 25, 13, 56, 56);
//		otherDecision = new ClinicalDecision(decisionDate, "BACKCHART");
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 100, 1.0, 120,70,5);
//		patient.addDecision(decision);
//		patient.addDecision(decision); // No effect because same object
//		patient.addDecision(decision);
//	
//		patientDAO.updatePatientValues(patient);
//		temp =  (Long) patientDAO.getSession().getNamedQuery(
//				VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		newGlucoseDecisionCount = temp.intValue();
//		assertEquals("Record addition ", newGlucoseDecisionCount,
//				oldGlucoseDecisionCount + 1);
//		patient.addDecision(otherDecision);
//		patientDAO.updatePatientValues(patient);
//		temp =  (Long) patientDAO.getSession().getNamedQuery(
//				VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		newGlucoseDecisionCount = temp.intValue();
//		
//		assertEquals("Second record addition: ", newGlucoseDecisionCount,
//				oldGlucoseDecisionCount + 1);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		newAllDecisionCount = temp.intValue();
//		assertEquals("Total decision count: ", 4, newAllDecisionCount);
//		otherDecision.setValid(false);
//		patientDAO.updatePatientValues(patient);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		newAllDecisionCount = temp.intValue();
//		
//		assertEquals("Total decision count: ", 3, newAllDecisionCount);	
//		newAllDecisionCount = (Integer) patient.getDecisions().size();
//		assertEquals("Total decision count from patient object: ", 4, newAllDecisionCount);
//		patient.deleteDecision(otherDecision);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		newAllDecisionCount = temp.intValue();
//		
//		assertEquals("Total decision count: ", 3, newAllDecisionCount);	
//		newAllDecisionCount = (Integer) patient.getDecisions().size();
//		assertEquals("Total decision count from patient object: ", 3, newAllDecisionCount);
//		patientDAO.getSession().getTransaction().commit();
//	}
//
//	@Test
//	public void testDecisionCascadeDeletion() {
//		Long temp;
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		temp = (Long) patientDAO.getSession()
//				.getNamedQuery(VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		oldRecordCount = temp.intValue();
//		assertEquals("Old glucose decision count ", 1, oldRecordCount);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		oldRecordCount = temp.intValue();
//		assertEquals("Old clinical decision count ", 2, oldRecordCount);
//		oldRecordCount = patientDAO.patientCount().intValue();
//		assertEquals("Old patient count", 1, oldRecordCount);
//		patient = patientDAO.getLastPatient();
//		patientDAO.deletePatientWithoutWarning(patient);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		newRecordCount = temp.intValue();
//		assertEquals("New glucose decision count ", 0, newRecordCount);
//		temp = (Long) patientDAO.getSession().getNamedQuery(
//				ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
//		oldRecordCount = temp.intValue();
//		assertEquals("New clinical decision count", 0, oldRecordCount);
//		newRecordCount = patientDAO.patientCount().intValue();
//		assertEquals("New patient count", 0, newRecordCount);
//		patientDAO.getSession().getTransaction().commit();
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		ResetTestingDatabaseSchema reset = new ResetTestingDatabaseSchema();
//		reset.resetDatabase();
//		
//		birthdate = new GregorianCalendar(1999, 12, 12);
//		patient = new Patient("TestLast", "TestFirst", "12-34-56",
//				"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
//		personValidator = new ClassValidator<Patient>(Patient.class);
//		glucoseValidator = new ClassValidator<VasoactiveDecision>(
//				VasoactiveDecision.class);
//		decisionDate = new GregorianCalendar(2005, 9, 25, 14, 55, 55);
//		otherDecision = new ClinicalDecision(decisionDate, "BACKCHART");
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 100, 1.0, 120,70,5);
//		
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		patientDAO.createPatient(patient);
//		patient.addDecision(decision);
//		patient.addDecision(otherDecision);
//		patientDAO.updatePatientValues(patient);
//		patientDAO.getSession().getTransaction().commit();
//		
//	}
//
//}
