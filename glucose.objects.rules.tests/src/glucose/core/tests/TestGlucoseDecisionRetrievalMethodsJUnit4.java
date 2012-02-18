package glucose.core.tests;

import static org.junit.Assert.*;
import glucose.decision.object.GlucoseDecision;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.patient.object.Patient;

/**
 * This set of core.tests illustrates the use of the PatientDAO interface, created
 * from a factory call.  It is interesting because I have successfully eliminated
 * all references to HibernateUtil and Session;  the DAO takes care of everything.
 * 
 * @author mdean
 *
 */
public class TestGlucoseDecisionRetrievalMethodsJUnit4 {

	protected Patient patient;
	protected GlucoseDecision decision;
	protected ClinicalDecision otherDecision;
	protected GregorianCalendar birthdate, decisionDate;

	@Test
	public void testPatientAllGlucoseDecisionCount() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Long temp = (Long) patientDAO.getSession().getNamedQuery(GlucoseDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();		
		patientDAO.getSession().getTransaction().commit();	
		assertTrue("Glucose count incorrect",temp==6);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveDecisionsInDescendingDateOrder(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(GlucoseDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", patient);
		ArrayList<ClinicalDecision> results = (ArrayList<ClinicalDecision>) q.list();
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Size of decision query list incorrect:", 6, results.size());
		
		assertEquals("Failed retrieval of latest decision:", 15,  results.get(0).getObservationDate().get(Calendar.HOUR_OF_DAY));
		assertEquals("Failed retrieval of second decision:", 14, results.get(1).getObservationDate().get(Calendar.HOUR_OF_DAY));
		assertEquals("Failed retrieval of third decision:", 13, results.get(2).getObservationDate().get(Calendar.HOUR_OF_DAY));
		assertEquals("Failed retrieval of fourth decision:", 12,results.get(3).getObservationDate().get(Calendar.HOUR_OF_DAY));
		assertEquals("Failed retrieval of fifth decision:", 11, results.get(4).getObservationDate().get(Calendar.HOUR_OF_DAY));
		assertEquals("Failed retrieval of earliest decision:", 10, results.get(5).getObservationDate().get(Calendar.HOUR_OF_DAY));

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNthGlucoseValue(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(GlucoseDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", patient);
		ArrayList<GlucoseDecision> results = (ArrayList<GlucoseDecision>) q.list();
		patientDAO.getSession().getTransaction().commit();

		assertEquals("Size of decision query list incorrect:", 6, results.size());
//		assertEquals("Failed to retrieve backward 2 values:", 700, results.get(0).retrieveNthGlucoseValue(results,0,2));
//		assertEquals("Failed to retrieve backward 2 values second time:", 800, results.get(1).retrieveNthGlucoseValue(results,1,2));
//		assertEquals("Failed to retrieve backward 3 values:", 800, results.get(0).retrieveNthGlucoseValue(results,0,3));
//		assertEquals("Failed to retrieve backward 3 values second time:", 900, results.get(1).retrieveNthGlucoseValue(results,1,3));
		
	}

	@Before
	public void setUp() throws Exception {
		GlucoseResetTestingDatabaseSchema reset = new GlucoseResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();
		
		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		
		decisionDate = new GregorianCalendar(2007,0,5,14,0,0); //Jan 5 14:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 600,20,1);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,15,0,0); //Jan 5 15:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 500,30,1);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,10,0,0); //Jan 5 10:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 1000,0,1);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,11,0,0); //Jan 5 11:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 900,5,1);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,12,0,0); //Jan 5 12:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 800,10,1);
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2007,0,5,13,0,0); //Jan 5 13:00:00 am
		decision = new GlucoseDecision(decisionDate, "ACCEPT", 700,15,1);
		patient.addDecision(decision);
		
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
	}



}
