package patient.laboratory.controller;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;
import hypertonic.decision.object.HypertonicSalineDecision;

public class HypertonicPatientLaboratories implements IPatientLaboratoryController {
	static Logger logger = Logger.getLogger(HypertonicPatientLaboratories.class);
	
	public SerumOsmolalityLaboratoryResult retrieveCurrentOsmolalityLabResult(Patient patient) {
		ArrayList<?> results = retrieveOsmolalityLabResultArray(patient);
		if (results.size() == 0) {
			return HypertonicSalineDecision.NOCURRENTOSMOLALITY;
		} else {
			return (SerumOsmolalityLaboratoryResult) results.get(0);
		}
	}

	public SerumSodiumLaboratoryResult retrieveCurrentSodiumLabResult(Patient patient) {
		ArrayList<?> results = retrieveSodiumLabResultArray(patient);
		if (results.size() == 0) {
			return HypertonicSalineDecision.NOCURRENTSODIUM;
		} else {
			return (SerumSodiumLaboratoryResult) results.get(0);
		}
	}

	public SerumOsmolalityLaboratoryResult retrievePreviousOsmolalityLabResult(Patient patient) {
		ArrayList<?> results = retrieveOsmolalityLabResultArray(patient);
		if (results.size() > 1) {
			return (SerumOsmolalityLaboratoryResult) results.get(1);
		} else {
			return HypertonicSalineDecision.NOPREVIOUSOSMOLALITY;
		}
	}

	public SerumSodiumLaboratoryResult retrievePreviousSodiumLabResult(Patient patient) {
		ArrayList<?> results = retrieveSodiumLabResultArray(patient);
		if (results.size() > 1) {
			return (SerumSodiumLaboratoryResult) results.get(1);
		} else {
			return HypertonicSalineDecision.NOPREVIOUSSODIUM;
		}
	}

	private ArrayList<?> retrieveOsmolalityLabResultArray(Patient patient) {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO
		.getSession()
		.getNamedQuery(
				SerumOsmolalityLaboratoryResult.GETALLLABRESULTSBYPATIENTANDLOINC);
		q.setParameter("patient", patient);
		q.setParameter("loincCode", SerumOsmolalityLaboratoryResult.LOINC_CODE);
		ArrayList<?> results = (ArrayList<?>) q.list();
		patientDAO.getSession().getTransaction().commit();
		return results;
	}
	
	private ArrayList<?> retrieveSodiumLabResultArray(Patient patient) {
		System.out.println("Entering retrieve sodium result");
		logger.debug("Entering retrieve sodium result array");
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(
				BasicLaboratoryObject.GETALLLABRESULTSBYPATIENTANDLOINC);
		q.setParameter("patient", patient);
		q.setParameter("loincCode", SerumSodiumLaboratoryResult.LOINC_CODE);
		ArrayList<?> results = (ArrayList<?>) q.list();
		patientDAO.getSession().getTransaction().commit();
		logger.debug(results.toString());
		return results;
	}
	
}
