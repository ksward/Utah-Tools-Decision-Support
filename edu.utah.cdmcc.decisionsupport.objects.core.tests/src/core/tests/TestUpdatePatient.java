package core.tests;

import static org.junit.Assert.assertEquals;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.Patient;

public class TestUpdatePatient {
	private Patient patient, patient2;
	private GregorianCalendar birthdate;



	@Test
	public void updatesSamePatientWithTwoSequentialChanges(){
	
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
	
	@Test
	public void updatesTwoDifferentPatientsInSequence(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		patient.setFirstName("Sammy");
		patientDAO.updatePatientValues(patient);
		patientDAO.createPatient(patient2);
		patient2.setFirstName("New Second");
		patientDAO.updatePatientValues(patient2);
		assertEquals("Update of first patient failed:", "Sammy", patient.getFirstName());
		assertEquals("Update of second patient failed:", "New Second", patient2.getFirstName());
		patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void updatePatientCallsSucceedOverAndOver(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		patient.setFirstName("Sammy");
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void updateDifferentPatientCallsSucceedOverAndOver(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		patient.setFirstName("Sammy");
		patientDAO.createPatient(patient2);
		patient2.setFirstName("New Second");
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient2);
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient2);
		patientDAO.updatePatientValues(patient);
		patientDAO.updatePatientValues(patient2);
		patientDAO.getSession().getTransaction().commit();
	}


	
	@Before
	public void setUp() throws Exception {
		ResetTestingDatabaseSchema reset = new ResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();
		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		patient2 = new Patient("SecondLast", "SecondFirst", "92-34-56",
				"ST03CHOM0003", birthdate, 72.34, 75.34);
	}
}
