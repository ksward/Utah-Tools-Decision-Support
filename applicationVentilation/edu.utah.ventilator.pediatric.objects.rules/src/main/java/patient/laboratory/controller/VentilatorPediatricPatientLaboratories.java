package patient.laboratory.controller;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import ventilator.decision.object.VentilatorPediatricDecision;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;

public class VentilatorPediatricPatientLaboratories implements IPatientLaboratoryController {
	static Logger logger = Logger.getLogger(VentilatorPediatricPatientLaboratories.class);

	public ArterialBloodGasLaboratoryResult retrieveCurrentArterialBloodGasResult(
			Patient patient) {
		ArrayList<?> results = retrieveArterialBloodGasArray(patient);
		if (results.size() == 0){
			return VentilatorPediatricDecision.NOCURRENTABG;
		} else {
			return (ArterialBloodGasLaboratoryResult) results.get(0);
		}
	}

	public ArterialBloodGasLaboratoryResult retrievePreviousArterialBloodGasResult(
			Patient patient) {
		ArrayList<?> results = retrieveArterialBloodGasArray(patient);
		if (results.size() > 1){
			return (ArterialBloodGasLaboratoryResult) results.get(1);
		} else {
			return VentilatorPediatricDecision.NOPREVIOUSABG;
		}
	}
	
	private ArrayList<?> retrieveArterialBloodGasArray(Patient patient) {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(BasicLaboratoryObject.GETALLLABRESULTSBYPATIENTANDLOINC);
		q.setParameter("patient", patient);
		q.setParameter("loincCode", ArterialBloodGasLaboratoryResult.LOINC_CODE);
		ArrayList<?> results = (ArrayList<?>) q.list();
		patientDAO.getSession().getTransaction().commit();
		return results;
	}
}
