package core.tests;

import java.util.GregorianCalendar;
import org.hibernate.Query;
import org.junit.Test;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;

public class TestNamedQueriesAccessible {
	protected Query query;
	protected GregorianCalendar birthdate, decisionDate;
	private IPatientDAO patientDAO;
	
	@Test
	public void testValidClinicalDecisionCount(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(ClinicalDecision.VALIDCLINICALDECISIONCOUNT);
		patientDAO.getSession().getTransaction().commit();
	}
	

	@Test
	public void testGetAllClinicalDecisionsByPatientIncludingInvalid(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		patientDAO.getSession().getTransaction().commit();
	}
	
	
	@Test
	public void testPatientCount(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(Patient.PATIENTCOUNT);
		patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void testGetLastPatient(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(Patient.GETLASTPATIENT);
		patientDAO.getSession().getTransaction().commit();
	}
	
	@Test
	public void testGetAllPatientsByStudyID(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(Patient.GETALLPATIENTSBYSTUDYID);
		patientDAO.getSession().getTransaction().commit();
	}
	

	@Test
	public void testGetAllLabResultsByPatientIncludingInvalid(){
		patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		patientDAO.getSession().getNamedQuery(BasicLaboratoryObject.GETALLLABRESULTSBYPATIENTINCLUDINGINVALIDEXP);
		patientDAO.getSession().getTransaction().commit();
	}
	
}
