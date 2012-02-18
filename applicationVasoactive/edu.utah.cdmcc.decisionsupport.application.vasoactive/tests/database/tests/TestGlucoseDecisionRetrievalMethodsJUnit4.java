//package database.tests;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.GregorianCalendar;
//
//import org.hibernate.Query;
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
//
//
//
///**
// * This set of tests illustrates the use of the PatientDAO interface, created
// * from a factory call.  It is interesting because I have successfully eliminated
// * all references to HibernateUtil and Session;  the DAO takes care of everything.
// * 
// * @author mdean
// *
// */
//public class TestGlucoseDecisionRetrievalMethodsJUnit4 {
//
//	protected Patient patient;
//	protected VasoactiveDecision decision;
//	protected ClinicalDecision otherDecision;
//	protected GregorianCalendar birthdate, decisionDate;
//
//	@Test
//	public void testPatientAllGlucoseDecisionCount() {
//		Long temp;
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		temp = (Long) patientDAO.getSession().getNamedQuery(VasoactiveDecision.VALIDGLUCOSEDECISIONCOUNT).uniqueResult();
//		
//		patientDAO.getSession().getTransaction().commit();
//		
//		assertEquals("Glucose decision count incorrect:", temp.intValue(), 6);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testPatientAllGlucoseDecisionQuery(){
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONSBYPATIENT);
//		q.setParameter("patient", patient);
//		ArrayList<VasoactiveDecision> results = (ArrayList<VasoactiveDecision>) q.list();
//		patientDAO.getSession().getTransaction().commit();
//		
//		assertEquals("Size of decision query list incorrect:", 6, results.size());
//		assertEquals("Failed retrieval of latest decision:", 500, (long) results.get(0).getSerumGlucoseConcentration());
//		assertEquals("Failed retrieval of second decision:", 600, (long) results.get(1).getSerumGlucoseConcentration());
//		assertEquals("Failed retrieval of third decision:", 700, (long) results.get(2).getSerumGlucoseConcentration());
//		assertEquals("Failed retrieval of fourth decision:", 800, (long) results.get(3).getSerumGlucoseConcentration());
//		assertEquals("Failed retrieval of fifth decision:", 900, (long) results.get(4).getSerumGlucoseConcentration());
//		assertEquals("Failed retrieval of earliest decision:", 1000, (long) results.get(5).getSerumGlucoseConcentration());
//
//		assertEquals("Failed retrieval of latest decision insulin:", (double) results.get(0).getCurrentInsulinDripRate(), 30.,0);
//		assertEquals("Failed retrieval of second decision insulin:", (double) results.get(1).getCurrentInsulinDripRate(), 20.,0);
//		assertEquals("Failed retrieval of third decision insulin:", (double) results.get(2).getCurrentInsulinDripRate(), 15.,0);
//		assertEquals("Failed retrieval of fourth decision insulin:", (double) results.get(3).getCurrentInsulinDripRate(), 10.,0);
//		assertEquals("Failed retrieval of fifth decision insulin:", (double) results.get(4).getCurrentInsulinDripRate(), 5.,0);
//		assertEquals("Failed retrieval of earliest decision insulin:", (double) results.get(5).getCurrentInsulinDripRate(), 0.,0);
//	
//		assertEquals("Failed retrieval of latest systolic blood pressure:", 110,(long) results.get(0).getSystolicBloodPressure());
//		assertEquals("Failed retrieval of second systolic blood pressure:", 120,(long) results.get(1).getSystolicBloodPressure());
//		assertEquals("Failed retrieval of third systolic blood pressure:", 130,(long) results.get(2).getSystolicBloodPressure());
//		assertEquals("Failed retrieval of fourth systolic blood pressure:", 140,(long) results.get(3).getSystolicBloodPressure());
//		assertEquals("Failed retrieval of fifth systolic blood pressure:", 150,(long) results.get(4).getSystolicBloodPressure());
//		assertEquals("Failed retrieval of earliest systolic blood pressure:", 160,(long) results.get(5).getSystolicBloodPressure());
//	
//		/* need to add tests later */
//	
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testRetrieveNthGlucoseValue(){
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLVALIDCLINICALDECISIONSBYPATIENT);
//		q.setParameter("patient", patient);
//		ArrayList<VasoactiveDecision> results = (ArrayList<VasoactiveDecision>) q.list();
//		patientDAO.getSession().getTransaction().commit();
//
//		assertEquals("Failed to retrieve backward 2 values:", 700, results.get(0).retrieveNthGlucoseValue(results,0,2));
//		assertEquals("Failed to retrieve backward 2 values second time:", 800, results.get(1).retrieveNthGlucoseValue(results,1,2));
//		assertEquals("Failed to retrieve backward 3 values:", 800, results.get(0).retrieveNthGlucoseValue(results,0,3));
//		assertEquals("Failed to retrieve backward 3 values second time:", 900, results.get(1).retrieveNthGlucoseValue(results,1,3));
//		
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
//		
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		patientDAO.createPatient(patient);
//		
//		decisionDate = new GregorianCalendar(2007,0,5,14,0,0); //Jan 5 14:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 600,20,110,70,1.0);
//		patient.addDecision(decision);
//		decisionDate = new GregorianCalendar(2007,0,5,15,0,0); //Jan 5 15:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 500,30, 120,80,2.0);
//		patient.addDecision(decision);
//		decisionDate = new GregorianCalendar(2007,0,5,10,0,0); //Jan 5 10:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 1000,0, 130,90,3.0);
//		patient.addDecision(decision);
//		decisionDate = new GregorianCalendar(2007,0,5,11,0,0); //Jan 5 11:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 900,5, 140,100,4.0);
//		patient.addDecision(decision);
//		decisionDate = new GregorianCalendar(2007,0,5,12,0,0); //Jan 5 12:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 800,10, 150,110,5.0);
//		patient.addDecision(decision);
//		decisionDate = new GregorianCalendar(2007,0,5,13,0,0); //Jan 5 13:00:00 am
//		decision = new VasoactiveDecision(decisionDate, "ACCEPT", 700,15, 160,120,6.0);
//		patient.addDecision(decision);
//		
//		patientDAO.updatePatientValues(patient);
//		patientDAO.getSession().getTransaction().commit();
//	}
//
//
//
//}
