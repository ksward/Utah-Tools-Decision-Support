package saline.core.tests;

import static org.junit.Assert.assertEquals;
import hypertonic.decision.object.HypertonicSalineDecision;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import patient.laboratory.controller.HypertonicPatientDecisions;
import patient.laboratory.controller.HypertonicPatientLaboratories;
import patient.laboratory.controller.IPatientLaboratoryController;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.multiple.object.controllers.IPatientDecisionController;
import core.patient.object.Patient;

public class TestHypertonicDecisionRetrievalFunctions {
	private Patient patient;
	private HypertonicSalineDecision decision1, decision2, decision3;
	private IPatientDecisionController decisionController = new HypertonicPatientDecisions();
	

	@Test
	public void testRetrievePreviousObservationDate(){
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
	
		patientDAO.getSession().beginTransaction();
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		patient.addDecision(decision1);	
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		decision2 = new HypertonicSalineDecision(new GregorianCalendar(2005, 9, 25));
		decision2.setPreviousObservationTime(decisionController.retrievePreviousObservationDateTime(patient));
		patient.addDecision(decision2);
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		decision3 = new HypertonicSalineDecision(new GregorianCalendar(2005, 10, 25));
		decision3.setPreviousObservationTime(decisionController.retrievePreviousObservationDateTime(patient));
		patient.addDecision(decision3);
		
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("First decision has no predecessor date",HypertonicSalineDecision.NOPREVIOUSOBSERVATIONDATE, decision1.getPreviousObservationTime());
		assertEquals("Second decision should have predecessor date", decision1.getObservationDate(), decision2.getPreviousObservationTime());
		assertEquals("Third decision should have predecessor date", decision2.getObservationDate(), decision3.getPreviousObservationTime());
	}
	
	@Test
	public void testPopulatingPreviousLabsWhenPresent(){		
		GregorianCalendar prevSodiumDate, currSodiumDate, prevOsmDate, currOsmDate;
		prevSodiumDate = new GregorianCalendar(2010,11,11,12,50);
		currSodiumDate = new GregorianCalendar(2010,11,11,13,50);
		prevOsmDate = new GregorianCalendar(2010,11,11,10,50);
		currOsmDate = new GregorianCalendar(2010,11,11,13,50);

		SerumSodiumLaboratoryResult prevSodium = new SerumSodiumLaboratoryResult(prevSodiumDate, "145");
		SerumSodiumLaboratoryResult currSodium = new SerumSodiumLaboratoryResult(currSodiumDate, "165");
		SerumOsmolalityLaboratoryResult prevOsm = new SerumOsmolalityLaboratoryResult(prevOsmDate, "265");
		SerumOsmolalityLaboratoryResult currOsm = new SerumOsmolalityLaboratoryResult(currOsmDate, "275");
		
		patient.addExperimentalLabResult(currSodium);
		patient.addExperimentalLabResult(prevSodium);
		patient.addExperimentalLabResult(currOsm);
		patient.addExperimentalLabResult(prevOsm);
		patient.addExperimentalLabResult(currSodium);
		patient.addExperimentalLabResult(prevSodium);
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		assertEquals("Current sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousOsmolalityValue());
		populateLabValuesFromPatient(decision1);
		patient.addDecision(decision1);
				
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Current sodium should be available", (Integer) 165, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be available", (Integer) 145, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be available", (Integer) 275, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be available", (Integer) 265, decision1.getPreviousOsmolalityValue());
	}

	@Test
	public void testPopulatingPreviousLabsWhenOnePresent(){		
		GregorianCalendar currSodiumDate, currOsmDate;

		currSodiumDate = new GregorianCalendar(2010,11,11,13,50);
		currOsmDate = new GregorianCalendar(2010,11,11,13,50);

		SerumSodiumLaboratoryResult currSodium = new SerumSodiumLaboratoryResult(currSodiumDate, "165");
		SerumOsmolalityLaboratoryResult currOsm = new SerumOsmolalityLaboratoryResult(currOsmDate, "275");
		
		patient.addExperimentalLabResult(currSodium);
		patient.addExperimentalLabResult(currOsm);
		patient.addExperimentalLabResult(currSodium);
		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		assertEquals("Current sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousOsmolalityValue());
		populateLabValuesFromPatient(decision1);
		patient.addDecision(decision1);
				
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Current sodium should be available", (Integer) 165, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be available", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be available", (Integer) 275, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be available",(Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousOsmolalityValue());
	}

	@Test
	public void testPopulatingPreviousLabsWhenNonePresent(){		
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		decision1 = new HypertonicSalineDecision(new GregorianCalendar(2005, 8, 25));
		assertEquals("Current sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be unavailable", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousOsmolalityValue());
		
		populateLabValuesFromPatient(decision1);
		patient.addDecision(decision1);
				
		patientDAO.getSession().beginTransaction();
		patientDAO.updatePatientValues(patient);
		patientDAO.getSession().getTransaction().commit();
		
		assertEquals("Current sodium should be available", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentSodiumValue());
		assertEquals("Previous sodium should be available", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousSodiumValue());
		assertEquals("Current osm should be available", (Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getCurrentOsmolalityValue());
		assertEquals("Previous osm should be available",(Integer) HypertonicSalineDecision.NOPREVIOUSVALUE, decision1.getPreviousOsmolalityValue());
	}
	
	private void populateLabValuesFromPatient(HypertonicSalineDecision decision) {
		SerumOsmolalityLaboratoryResult recentOsm, previousOsm;
		SerumSodiumLaboratoryResult recentNA, previousNA;
		IPatientLaboratoryController patientLabController = new HypertonicPatientLaboratories();

		recentOsm = patientLabController.retrieveCurrentOsmolalityLabResult(patient);
		previousOsm = patientLabController.retrievePreviousOsmolalityLabResult(patient);
		recentNA = patientLabController.retrieveCurrentSodiumLabResult(patient);
		previousNA = patientLabController.retrievePreviousSodiumLabResult(patient);
		if (recentNA != HypertonicSalineDecision.NOCURRENTSODIUM) {
			decision.setCurrentSodiumValue(recentNA.getNumericResult());
			decision.setCurrentSodiumDateTime(recentNA.getTimeOfSpecimenCollection());
		}
		if (previousNA != HypertonicSalineDecision.NOPREVIOUSSODIUM) {
			decision.setPreviousSodiumValue(previousNA.getNumericResult());
			decision.setPreviousSodiumDateTime(previousNA.getTimeOfSpecimenCollection());
		}
		if (recentOsm != HypertonicSalineDecision.NOCURRENTOSMOLALITY) {
			decision.setCurrentOsmolalityValue(recentOsm.getNumericResult());
			decision.setCurrentOsmolalityDateTime(recentOsm.getTimeOfSpecimenCollection());
		}
		if (previousOsm != HypertonicSalineDecision.NOPREVIOUSOSMOLALITY) {
			decision.setPreviousOsmolalityValue(previousOsm.getNumericResult());
			decision.setPreviousOsmolalityDateTime(previousOsm
					.getTimeOfSpecimenCollection());
		}
	}
	
	@Before
	public void setUp(){
		SalineResetTestingDatabaseSchema reset = new SalineResetTestingDatabaseSchema();
		reset.resetHSQLDBDatabase();
		GregorianCalendar birthdate = new GregorianCalendar(1999, 12, 12);
		patient = new Patient("TestLast", "TestFirst", "12-34-56",
				"ST03CHOM0002", birthdate,12.34, 25.34);
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.createPatient(patient);
		patientDAO.getSession().getTransaction().commit();

	}
}
