/*package database.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;

import core.dao.DAOFactory;
import core.dao.IPatientDAO;
//import core.patient.object.Gender;
import core.patient.object.Patient;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

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
		assertEquals("TrialDB assertion: ", "ST03CHOM0002", patient
				.getTrialDbCode());
		assertEquals("Gender assertion: ", "MALE", patient.getGender().name());
		assertEquals("Gender string assertion: ", "Male", patient.getGender().toString());
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
		try {
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					patientDAO.getSession().getTransaction().rollback();
				}
			} catch (HibernateException rbex) {
			
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("The database should start out empty", 0, oldRecordCount);
	}

	@Test
	public void testDuplicatePatientDetection() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
			patient2 = patientDAO.getLastPatient();
			if (!patient.equals(patient2)) {
				patientDAO.createPatient(patient2);
			}
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					
					patientDAO.getSession().getTransaction().rollback();
				} else {
					
				}
			} catch (HibernateException rbex) {
			
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("Record addition ", 1, newRecordCount);
	}

	@Test
	public void testDuplicateTrialDbDetection(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
			patientDAO.createPatient(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
			patientDAO.createPatient(patient);
			assertEquals("TrialDB duplicates not detected", 1, patientDAO.patientCount().intValue());
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					
					patientDAO.getSession().getTransaction().rollback();
				} else {
					
				}
			} catch (HibernateException rbex) {
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	@Test
	public void testUpdateAllowsDuplicateTrialDb(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
			patientDAO.updatePatientValues(patient);
			patient = new Patient("TestLast", "TestFirst", "12-34-56",
					"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
			patientDAO.updatePatientValues(patient);
			assertEquals("TrialDB duplicates detected by update", 3, patientDAO.patientCount().intValue());
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					patientDAO.getSession().getTransaction().rollback();
				} else {
				}
			} catch (HibernateException rbex) {
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	@Test
	public void testPatientValidationMessages() {
		patient = new Patient("TestLast",
				"TestFirstName is really really too long for this field ",
				"12-34-56", "NUMBCHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		if (validationMessages.length > 0) {
			assertEquals("First name validation failed: ",
					validationMessages[0].getPropertyName(), "firstName");
			assertEquals("TrialDB code validation failed: ",
					validationMessages[1].getPropertyName(), "trialDbCode");
		} else
			fail("Should have had messages for two invalid patient values.");
	}

	@Test
	public void testPatientFirstNameTooLongValidation() {
		patient = new Patient("TestLast",
				"TestFirstName is really really too long for this field ",
				"12-34-56", "ST02CHOM0003", birthdate, Gender.MALE, 12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name long validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientLastNameTooLongValidation() {
		patient = new Patient("TestLast is really too lng for this field",
				"TestFirstName", "12-34-56", "ST02CHOM0003", birthdate, Gender.MALE,
				12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Last name long validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientLastNameTooShortValidation() {
		patient = new Patient("L", "TestFirstName", "12-34-56", "ST02CHOM0003",
				birthdate, Gender.MALE, 12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("Last name short validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientFirstNameTooShortValidation() {
		patient = new Patient("TestLast", "F", "12-34-56", "ST02CHOM0003",
				birthdate, Gender.MALE, 12.34, 25.34);
		validationMessages = personValidator.getInvalidValues(patient);
		assertEquals("First name short validation error: ", 1,
				validationMessages.length);
	}

	@Test
	public void testPatientAddition() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.createPatient(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					patientDAO.getSession().getTransaction().rollback();
				} else {
					
				}
			} catch (HibernateException rbex) {
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}

	@Test
	public void testPatientMakePersistent() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.makePersistent(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					
					patientDAO.getSession().getTransaction().rollback();
				} else {
					
				}
			} catch (HibernateException rbex) {
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}
	
	@Test
	public void testPatientUpdateDirectly() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.updatePatientValues(patient);
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					
					patientDAO.getSession().getTransaction().rollback();
				} else {
					
				}
			} catch (HibernateException rbex) {
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("Record addition ", newRecordCount, oldRecordCount + 1);
	}
	@Test
	public void testPatientDeletion() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			oldRecordCount = patientDAO.patientCount().intValue();
			patient = patientDAO.getLastPatient();
			patientDAO.deletePatient(patient);	
			newRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testPatientDeletion");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		
		assertEquals("Record deletion error:  ", 0, newRecordCount);
	}

	@Test
	public void testPatientValidateBeforeAddition() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast2", "TestFirst2", "12-34-67",
					"ST03CHOM0003", birthdate, Gender.MALE, 12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			assertEquals("Valid records inserted ", 2, oldRecordCount);

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast3",
					"TestFirst3 Name is too long to get entered", "12-34-78",
					"ST03CHOM0004", birthdate, Gender.MALE, 12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			assertEquals("Firstname too long ", 2, oldRecordCount);

			birthdate = new GregorianCalendar(1999, 12, 12);
			patient = new Patient("TestLast4", "TestFirst4", "12-34-90",
					"NUMBCHOM0005", birthdate, Gender.MALE, 12.34, 25.34);
			validationMessages = personValidator.getInvalidValues(patient);
			if (validationMessages.length == 0) {
				patientDAO.createPatient(patient);
			}
			oldRecordCount = patientDAO.patientCount().intValue();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testPatientValidateBeforeAddition");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		assertEquals("TrialDB pattern no good ", 2, oldRecordCount);
	}

	@Test
	public void testPatientUpdates() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		List<Patient> patients;
		try {
			patientDAO.getSession().beginTransaction();
			assertTrue(patientDAO.createPatient(patient));
			patients = patientDAO.getAllPatients();
			assertEquals("There should only be one patient in the list", 1,
					patients.size());
			assertEquals("The original gender should be Male: ", "MALE", patients
					.get(0).getGender().name());
			assertEquals(12.34, (double) patients
					.get(0).getWeight(),0);

			for (Patient p : patients) {
				p.setGender(Gender.FEMALE);
				p.setWeight(25.95);
				assertTrue(patientDAO.updatePatientValues(p));
			}

			patients = patientDAO.getAllPatients();
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testPatientUpdates");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		assertEquals("There should only be one patient in the list", 1,
				patients.size());
		assertEquals("The new gender should be Female: ", "FEMALE", (String) patients
				.get(0).getGender().name());
		assertEquals("The new weight should be 25.95: ", 25.95, (double) patients.get(0)
				.getWeight(),0);
			
	}
	
	@Test
	public void testPatientUpdates2(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			List<Patient> patients = patientDAO.getAllPatients();
			
			for (Patient p : patients) {
				p.setGender(Gender.FEMALE);
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
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testPatientUpdates2");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}	
	}
	

	@Test
	public void testPatientUpdateGenderWeight(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
				patientDAO.createPatient(patient);
				List<Patient> patients = patientDAO.getAllPatients();
				for (Patient p : patients) {
					p.setGender(Gender.FEMALE);
					p.setWeight(25.95);
					assertTrue(patientDAO.updatePatientValues(p));
				}
				patients = patientDAO.getAllPatients();	
				assertEquals("Error in patient updates 4 test: ",1, patients.size());
				assertEquals("Error in updating weight: ",25.95, (double) patients.get(0).getWeight(),0);
				assertEquals("Error in updating gender: ", Gender.FEMALE, patients.get(0).getGender());
				patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testPatientUpdateGenderWeight");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
		}
	
	@Test
	public void testMultipleUpdatesFromSingleDAO(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			assertTrue(patientDAO.createPatient(patient));
			patient.setFirstName("Sammy");
			assertTrue(patientDAO.updatePatientValues(patient));
			patient.setLastName("Revised");
			assertTrue(patientDAO.updatePatientValues(patient));
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testMultipleUpdatesfromSingleDAO");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	@Test
	public void testUpdatesFromMultipleDAOObjects(){
		assertEquals("First name not set correctly:", "TestFirst", patient.getFirstName());
		assertEquals("Last name not set correctly:", "TestLast", patient.getLastName());
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
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
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testUpdatesfrom MultipleDAOs");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	@Test
	public void testMultiplePatientUpdates(){
		assertEquals("First name not set correctly:", "TestFirst", patient.getFirstName());
		assertEquals("Last name not set correctly:", "TestLast", patient.getLastName());
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.createPatient(patient);
			patient.setFirstName("Sammy");
			patientDAO.updatePatientValues(patient);
			patient.setLastName("Revised");
			patientDAO.updatePatientValues(patient);
			assertEquals("Update of first name failed:", "Sammy", patient.getFirstName());
			assertEquals("Update of last name failed:", "Revised", patient.getLastName());
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			try {
				if (patientDAO.getSession().getTransaction().isActive()) {
					System.out.println("Trying to rollback active transaction");
					patientDAO.getSession().getTransaction().rollback();
				} else {
					System.out.println("Transaction no longer active");
				}
			} catch (HibernateException rbex) {
				System.out
						.println("Unable to roll back transaction after exception in testMultiplePatientUpdates");
				rbex.printStackTrace();
			}
			e.printStackTrace();
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}

	@Before
	public void setUp() throws Exception {
		ResetTestingDatabaseSchema reset = new ResetTestingDatabaseSchema();
		reset.resetDatabase();

		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, Gender.MALE, 12.34, 25.34);
		personValidator = new ClassValidator<Patient>(Patient.class);

	}
	

}
*/