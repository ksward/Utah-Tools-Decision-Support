package patient.laboratory.controller;

import hypertonic.decision.object.GenericDecision;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.hibernate.Query;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.multiple.object.controllers.IPatientDecisionController;
import core.patient.object.Patient;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class GenericPatientDecisions implements IPatientDecisionController{

	public GregorianCalendar retrievePreviousObservationDateTime(Patient patient) {
		return retrievePreviousObservationTime(patient);
	}
	
	
	private final GregorianCalendar retrievePreviousObservationTime(Patient patient) {
	if (patient == null) {
		throw new UtahToolboxException(ErrorCode.NULL_PATIENT_ERROR,
				"retrievePreviousObservationTime called with null patient");
	}
	IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
			.getPatientDAO();
	patientDAO.getSession().beginTransaction();
	Query q = patientDAO
			.getSession()
			.getNamedQuery(
					ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
	q.setParameter("patient", patient);
	ArrayList<?> results = (ArrayList<?>) q.list();
	patientDAO.getSession().getTransaction().commit();
	if (results.size() == 0) {
		return GenericDecision.NOPREVIOUSOBSERVATIONDATE;
	} else {
		return ((ClinicalDecision) results.get(0))
				.getObservationDate();
	}
}
}
