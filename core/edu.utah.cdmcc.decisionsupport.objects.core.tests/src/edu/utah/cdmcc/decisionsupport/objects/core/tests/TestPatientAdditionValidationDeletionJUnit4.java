package edu.utah.cdmcc.decisionsupport.objects.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.Patient;

public class TestPatientAdditionValidationDeletionJUnit4 {
	protected Patient patient, patient2;
	protected GregorianCalendar birthdate;
	protected ClassValidator<Patient> personValidator;
	protected InvalidValue[] validationMessages;
	protected int oldRecordCount, newRecordCount;
	protected Query query;

	@Test
	public void testPatientCreation() {
		assertEquals("First name assertion: ", "TestFirst", patient
				.getFirstName());
		assertEquals("Last name assertion: ", "TestLast", patient.getLastName());
		assertEquals("Med rec num assertion: ", "12-34-56", patient
				.getMedRecNum());
		assertEquals("Study ID assertion: ", "ST03CHOM0002", patient
				.getStudyID());
		assertEquals("Birthdate assertion: ", birthdate, patient.getBirthdate());
		assertEquals("Weight expected within range: ", 12.0, (double) patient
				.getWeight(), 0.5);
		assertEquals("height expected within range: ", 25.0, (double) patient
				.getHeight(), 0.5);
		assertNotNull("New patient should have non-null Decision list: ",
				patient.getDecisions());
		assertEquals("Decision list should be zero length: ", 0, patient
				.getDecisions().size());
	}

	@Test
	public void testEmptyPatientDatabase() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("The database should start out empty", 0, oldRecordCount);
	}

	@Test
	public void testDuplicatePatientDetection() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, 12.34, 25.34);
			patient2 = patientDAO.getLastPatient();
			if (!patient.equals(patient2)) {
				patientDAO.createPatient(patient2);
			}
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();	
		assertEquals("Record addition ", 1, newRecordCount);
	}

	//TODO This skipped test creates a dialog so cannot run in the thread
//	@Test
//	public void testDuplicateStudyIDDetection(){
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//			patientDAO.getSession().beginTransaction();
//			patientDAO.createPatient(patient);
//			patient = new Patient("TestLast", "TestFirst", "12-34-56",
//					"ST03CHOM0002", birthdate,  12.34, 25.34);
//			patientDAO.createPatient(patient);
//			patient = new Patient("TestLast", "TestFirst", "12-34-56",
//					"ST03CHOM0002", birthdate,  12.34, 25.34);
//			patientDAO.createPatient(patient);
//			assertEquals("Study ID duplicates not detected", 1, patientDAO.patientCount().intValue());
//			patientDAO.getSession().getTransaction().commit();
//	}
	
	@Test
	public void testUpdateAllowsDuplicateStudyID(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, 12.34, 25.34);
			patientDAO.updatePatientValues(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, 12.34, 25.34);
			patientDAO.updatePatientValues(patient);
			assertEquals("Study ID duplicates detected by update", 3, patientDAO.patientCount().intValue());
			patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void testPatientValidationMessages() {
		patient = new Patient("TestLast",
				"TestFirstName is really really too long for this field ",
				"12-34-56", "NUMBC7OM0002", birthdate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		if (validationMessages.length > 0) {
			assertEquals("First name validation failed: ",
					validationMessages[0].getPropertyName(), "firstName");
			assertEquals("Study ID code validation failed: ",
					validationMessages[1].getPropertyName(), "studyID");
		} else
			fail("Should have had messages for two invalid patient values.");
	}

	@Test
	public void testPatientFirstNameTooLongValidation() {
		patient = new Patient("TestLast",
				"TestFirstName is really really too long for this field ",
				"12-34-56", "ST02CHOM0003", birthdate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name long validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientLastNameTooLongValidation() {
		patient = new Patient("TestLast is really too lng for this field",
				"TestFirstName", "12-34-56", "ST02CHOM0003", birthdate, 
				12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Last name long validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientLastNameTooShortValidation() {
		patient = new Patient("L", "TestFirstName", "12-34-56", "ST02CHOM0003",
				birthdate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Last name short validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientFirstNameTooShortValidation() {
		patient = new Patient("TestLast", "F", "12-34-56", "ST02CHOM0003",
				birthdate,  12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name short validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientAddition() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.createPatient(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}

	@Test
	public void testPatientMakePersistent() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.makePersistent(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}
	
	@Test
	public void testPatientUpdateDirectly() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.updatePatientValues(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}
	
	@Test
	public void testPatientDeletion() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			oldRecordCount = patientDAO.patientCount().intValue();
			patient = patientDAO.getLastPatient();
			patientDAO.deletePatient(patient);	
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("Record deletion error:  ", 0, newRecordCount);
	}

	@Test
	public void testPatientValidateBeforeAddition() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast2", "TestFirst2", "12-34-67",
					"ST03CHOM0003", birthdate,  12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			assertEquals("Valid records inserted ", 2, oldRecordCount);

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast3",
					"TestFirst3 Name is too long to get entered", "12-34-78",
					"ST03CHOM0004", birthdate,  12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			assertEquals("Firstname too long ", 2, oldRecordCount);

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast4", "TestFirst4", "12-34-90",
					"NUMBCHO70005", birthdate,  12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("Study ID pattern no good ", 2, oldRecordCount);
	}

	@Test
	public void testPatientUpdates() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		List<Patient> patients;
			patientDAO.getSession().beginTransaction();
			assertTrue(patientDAO.createPatient(patient));
			patients = patientDAO.getAllPatients();
			assertEquals("There should only be one patient in the list", 1,
					patients.size());
			assertEquals(12.34, (double) patients
					.get(0).getWeight(),0);

			for (Patient p : patients) {
				p.setWeight(25.95);
				assertTrue(patientDAO.updatePatientValues(p));
			}

			patients = patientDAO.getAllPatients();
			patientDAO.getSession().getTransaction().commit();
		assertEquals("There should only be one patient in the list", 1,
				patients.size());
		assertEquals("The new weight should be 25.95: ", 25.95, (double) patients.get(0)
				.getWeight(),0);
			
	}
	
	@Test
	public void testPatientUpdates2(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			List<Patient> patients = patientDAO.getAllPatients();
			
			for (Patient p : patients) {
				p.setWeight(25.95);
				patientDAO.updatePatientValues(p);
			}
			
			patients = patientDAO.getAllPatients();
			patient.setFirstName("Sammy");
			IPatientDAO patientDAO1 = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			IPatientDAO patientDAO2 = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO1.updatePatientValues(patient);
			patient.setLastName("Revised");
			patientDAO2.updatePatientValues(patient);
			patientDAO2.getSession().getTransaction().commit();
	}
	

	@Test
	public void testPatientUpdateGenderWeight(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
				patientDAO.createPatient(patient);
				List<Patient> patients = patientDAO.getAllPatients();
				for (Patient p : patients) {

					p.setWeight(25.95);
					assertTrue(patientDAO.updatePatientValues(p));
				}
				patients = patientDAO.getAllPatients();	
				assertEquals("Error in patient updates 4 test: ",1, patients.size());
				assertEquals("Error in updating weight: ",25.95, (double) patients.get(0).getWeight(),0);
				patientDAO.getSession().getTransaction().commit();
		}
	
	@Test
	public void testMultipleUpdatesFromSingleDAO(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			assertTrue(patientDAO.createPatient(patient));
			patient.setFirstName("Sammy");
			assertTrue(patientDAO.updatePatientValues(patient));
			patient.setLastName("Revised");
			assertTrue(patientDAO.updatePatientValues(patient));
			patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void testUpdatesFromMultipleDAOObjects(){
		assertEquals("First name not set correctly:", "TestFirst", patient.getFirstName());
		assertEquals("Last name not set correctly:", "TestLast", patient.getLastName());
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			patient.setFirstName("Sammy");
			IPatientDAO patientDAO1 = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			IPatientDAO patientDAO2 = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO1.updatePatientValues(patient);
			patient.setLastName("Revised");
			patientDAO2.updatePatientValues(patient);
			assertEquals("Update of first name failed:", "Sammy", patient.getFirstName());
			assertEquals("Update of last name failed:", "Revised", patient.getLastName());
			patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void testMultiplePatientUpdates(){
		assertEquals("First name not set correctly:", "TestFirst", patient.getFirstName());
		assertEquals("Last name not set correctly:", "TestLast", patient.getLastName());
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			patient.setFirstName("Sammy");
			patientDAO.updatePatientValues(patient);
			patient.setLastName("Revised");
			patientDAO.updatePatientValues(patient);
			assertEquals("Update of first name failed:", "Sammy", patient.getFirstName());
			assertEquals("Update of last name failed:", "Revised", patient.getLastName());
			patientDAO.getSession().getTransaction().commit();
	}

	@Before
	public void setUp() throws Exception {
		ResetTestingDatabaseSchema reset = new ResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();

		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		personValidator = new ClassValidator<Patient>(Patient.class);

	}
	

}
