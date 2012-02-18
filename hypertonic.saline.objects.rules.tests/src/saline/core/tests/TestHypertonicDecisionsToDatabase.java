package saline.core.tests;

import static org.junit.Assert.assertEquals;
import hypertonic.decision.object.HypertonicSalineDecision;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.patient.object.Patient;

public class TestHypertonicDecisionsToDatabase {

	private Patient patient;
	private HypertonicSalineDecision decision1, decision2, decision3;
	private ClassValidator<HypertonicSalineDecision> salineValidator;
	private InvalidValue[] validationMessages;
	private Long recordCount;
	private GregorianCalendar birthdate;
	
	@Test
	public void retrievePatients(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		List<Patient> results = patientDAO.getAllPatients();
		patientDAO.getSession().getTransaction().commit();
		assertEquals("There should be one patient",1, results.size());
		assertEquals("First name not correct","TestFirst", results.get(0).getFirstName());
		assertEquals("Last name not correct","TestLast", results.get(0).getLastName());
	}
	
	@Test
	public void changePatient(){
		patient.setFirstName("Michael");
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		patientDAO.getSession().beginTransaction();
		List<Patient> results = patientDAO.getAllPatients();
		patientDAO.getSession().getTransaction().commit();
		assertEquals("There should be one patient",1, results.size());
		assertEquals("First name not correct","Michael", results.get(0).getFirstName());
		assertEquals("Last name not correct","TestLast", results.get(0).getLastName());	
	}
	

	@Test
	public void addDecisionToPatient(){
		decision1 = new HypertonicSalineDecision();
		patient.addDecision(decision1);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		patientDAO.getSession().beginTransaction();		
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		assertEquals("Single decision not counted correctly", 1, recordCount.intValue());
	}
	

	@Test
	public void addMultipleDecisionsToPatient(){
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		decision2 = new HypertonicSalineDecision(new GregorianCalendar(2005, 9, 25));
		decision3 = new HypertonicSalineDecision(new GregorianCalendar(2005, 10, 25));
		patient.addDecision(decision1);
		patient.addDecision(decision2);
		patient.addDecision(decision3);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		patientDAO.getSession().beginTransaction();
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		assertEquals("Single decision not counted correctly", 3, recordCount.intValue());
	}
	
	@Ignore
	@Test
	public void removeDecisionsFromPatient(){
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		decision2 = new HypertonicSalineDecision(new GregorianCalendar(2005, 9, 25));
		decision3 = new HypertonicSalineDecision(new GregorianCalendar(2005, 10, 25));
		patient.addDecision(decision1);
		patient.addDecision(decision2);
		patient.addDecision(decision3);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		patientDAO.getSession().beginTransaction();
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Three valid decisions not counted correctly", 3, recordCount.intValue());
		
		patient.deleteDecision(decision1);
		assertEquals("Decision not deleted from patient prior to persisting",2,patient.getDecisions().size());
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		patientDAO.getSession().beginTransaction();
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Decision deletion not persisted", 2, recordCount.intValue());		
	}
	

	@Test
	public void invalidateDecisionInPatient(){
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		decision2 = new HypertonicSalineDecision(new GregorianCalendar(2005, 9, 25));
		decision3 = new HypertonicSalineDecision(new GregorianCalendar(2005, 10, 25));
		patient.addDecision(decision1);
		patient.addDecision(decision2);
		patient.addDecision(decision3);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		patientDAO.getSession().beginTransaction();
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Three valid decisions not counted correctly", 3, recordCount.intValue());
		
		decision1.setValid(false);
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		patientDAO.getSession().beginTransaction();
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		assertEquals("Decision invalidation not persisted correctly", 2, recordCount.intValue());	
	}
	

	@Test
	public void testCascadeDeletion(){
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		decision2 = new HypertonicSalineDecision(new GregorianCalendar(2005, 9, 25));
		decision3 = new HypertonicSalineDecision(new GregorianCalendar(2005, 10, 25));
		patient.addDecision(decision1);
		patient.addDecision(decision2);
		patient.addDecision(decision3);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		assertEquals("Addition of three decisions failed", 3, recordCount.intValue());
		patientDAO.getSession().beginTransaction();
		patientDAO.deletePatientWithoutWarning(patient);
		recordCount = 0L;
		recordCount = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		patientDAO.getSession().getTransaction().commit();
		assertEquals("Cascade deletion of decisions failed", 0, recordCount.intValue());
	}
	
	@Test
	public void testSalineValidatorOnDefaultValues(){
		decision1 = new HypertonicSalineDecision();
		salineValidator = new ClassValidator<HypertonicSalineDecision>(HypertonicSalineDecision.class);
		validationMessages = salineValidator.getInvalidValues(decision1);
		assertEquals("Default object has null patient",1,validationMessages.length);
		patient.addDecision(decision1);
		validationMessages = salineValidator.getInvalidValues(decision1);
		assertEquals("Added object has non-null patient",0,validationMessages.length);	
	}
	
	@Before
	public void setUp(){
		SalineResetTestingDatabaseSchema reset = new SalineResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();
		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate,12.34, 25.34);
		new ClassValidator<Patient>(Patient.class);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		patientDAO.getSession().getTransaction().commit();
	}
}
