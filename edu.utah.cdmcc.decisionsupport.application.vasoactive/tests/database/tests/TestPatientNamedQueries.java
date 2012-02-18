package database.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;

import vasoactive.decision.object.VasoactiveDecision;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
//import core.patient.object.Gender;
import core.patient.object.Patient;

public class TestPatientNamedQueries {

	protected Patient patient;
	protected ClinicalDecision decision;
	protected GregorianCalendar birthdate, decisionDate;
	private Patient patient2;
	private GregorianCalendar birthdate2;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCountClinicalDecisions(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Long temp = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.TOTALCLINICALDECISIONCOUNT).uniqueResult();
		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", patient);
		ArrayList<ClinicalDecision> results = (ArrayList<ClinicalDecision>) q.list();
		int temp2= results.size();		
		q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSINCLUDINGINVALID);
		 results = (ArrayList<ClinicalDecision>) q.list();
		int temp4= results.size();					
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Clinical total decision count incorrect:", 6,temp.intValue());
		assertEquals("Clinical total decision by patient count incorrect ",3,temp2);
		assertEquals("Clinical decisions by patient count incorrect ",6,temp4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCountValidClinicalDecisions(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Long temp = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.TOTALCLINICALDECISIONCOUNT).uniqueResult();
		Long temp1 = (Long) patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT).uniqueResult();
		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", patient);
		ArrayList<ClinicalDecision> results = (ArrayList<ClinicalDecision>) q.list();
		int temp2= results.size();
		q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONS);
		 results = (ArrayList<ClinicalDecision>) q.list();
		int temp3= results.size();		
		q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSINCLUDINGINVALID);
		 results = (ArrayList<ClinicalDecision>) q.list();
		int temp4= results.size();		
		q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONSBYPATIENT);
		q.setParameter("patient", patient);
		 results = (ArrayList<ClinicalDecision>) q.list();
		int temp5= results.size();			
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Clinical total decision count incorrect:", 6,temp.intValue());
		assertEquals("Clinical valid decision count incorrect:", 4,temp1.intValue());
		assertEquals("Clinical total decision by patient count incorrect ",3,temp2);
		assertEquals("Clinical valid decision by patient count incorrect ",4,temp3);
		assertEquals("Clinical decisions by patient count incorrect ",6,temp4);
		assertEquals("Clinical valid decisions by patient count incorrect ",2,temp5);
	}	
	
	@Test
	public void testGetLastClinicalDecision(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();		
		ClinicalDecision decision = (ClinicalDecision) patientDAO.getSession().getNamedQuery(ClinicalDecision.GETLASTCLINICALDECISION).uniqueResult();
		assertEquals("Last clinical decision query incorrect ",new GregorianCalendar(2006,0,5,13,0,0),decision.getObservationDate());
		patientDAO.getSession().getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetLastValidClinicalDecisionByHand(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();	
		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONS);
		ArrayList<ClinicalDecision> results = (ArrayList<ClinicalDecision>) q.list();
		Long maxId = findMaximumId(results);
		q = patientDAO.getSession().getNamedQuery(ClinicalDecision.DECISIONBYID);
		q.setParameter("id", maxId);
		ClinicalDecision decision = (ClinicalDecision) q.uniqueResult();
		assertEquals("Last clinical decision query incorrect ",new GregorianCalendar(2005,0,5,12,0,0),decision.getObservationDate());
		patientDAO.getSession().getTransaction().commit();
	}

	private Long findMaximumId(ArrayList<ClinicalDecision> results) {
		Long maxId = 0L;
		for (ClinicalDecision decision : results){
			if (decision.getId().longValue()>maxId){
				maxId = decision.getId().longValue();
			}
		}
		return maxId;
	}
	
	@Before
	public void setUp() throws Exception {
		ResetTestingDatabaseSchema reset = new ResetTestingDatabaseSchema();
		reset.resetDatabase();
		
		birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate, 12.34, 25.34);
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		
		decisionDate = new GregorianCalendar(2001,0,5,14,0,0); //Jan 5 14:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2002,0,5,15,0,0); //Jan 5 15:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		patient.addDecision(decision);
		decisionDate = new GregorianCalendar(2003,0,5,10,0,0); //Jan 5 10:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		decision.setValid(false);
		patient.addDecision(decision);
			patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();	
		
		birthdate2 = new GregorianCalendar(1999, 12, 12);
		patient2 = new Patient("TestLast", "TestFirst", "99-34-56",
				"ST03CHOM0003", birthdate2, 22.34, 35.34);
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient2);
		
		decisionDate = new GregorianCalendar(2004,0,5,11,0,0); //Jan 5 11:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		patient2.addDecision(decision);
		decisionDate = new GregorianCalendar(2005,0,5,12,0,0); //Jan 5 12:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		patient2.addDecision(decision);
		decisionDate = new GregorianCalendar(2006,0,5,13,0,0); //Jan 5 13:00:00 am
		decision = new ClinicalDecision(decisionDate, "ACCEPT");
		decision.setValid(false);
		patient2.addDecision(decision);
		patientDAO.updatePatientValues(patient2);
		patientDAO.getSession().getTransaction().commit();

	}	
}
