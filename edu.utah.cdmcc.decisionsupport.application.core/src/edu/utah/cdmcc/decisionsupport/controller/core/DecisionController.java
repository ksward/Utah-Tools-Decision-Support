package edu.utah.cdmcc.decisionsupport.controller.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ListenerList;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.decision.object.IDecisionListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public class DecisionController extends PropertyChangeController{

	private List<ClinicalDecision> decisionsForPatient = new ArrayList<ClinicalDecision>();
	private ListenerList decisionFiredListeners;
	private Boolean invalidRecordsShown = false;
	static Logger log = Logger.getLogger(DecisionController.class);
	
	public DecisionController() {
	}

	public List<ClinicalDecision> getDecisionsForPatient() {
		return decisionsForPatient;
	}

	public void setDecisionsForPatient(List<ClinicalDecision> decisionsForPatient) {
		this.decisionsForPatient = decisionsForPatient;
	}

	public ListenerList getDecisionFiredListeners() {
		return decisionFiredListeners;
	}

	public void setDecisionFiredListeners(ListenerList decisionFiredListeners) {
		this.decisionFiredListeners = decisionFiredListeners;
	}

	public void addDecisionFiredListener(final IDecisionListener listener) {
		log.debug("Adding decision listener: " + listener);
		if (decisionFiredListeners == null) {
			decisionFiredListeners = new ListenerList();
		}
		decisionFiredListeners.add(listener);
	}
	
	public void removeDecisionFiredListener(final IDecisionListener listener) {
		log.debug("Removing decision listener: "+listener);
		if (decisionFiredListeners != null) {
			decisionFiredListeners.remove(listener);
			if (decisionFiredListeners.isEmpty())
				decisionFiredListeners = null;
		}
	}
	
	public void updateDecisionList(final Patient patient) {
		log.debug("Updating the decision list");
		if (patient == null) {
			System.out.println("The patient is null so decisions will be cleared");
			decisionsForPatient.clear();
		} else {
			System.out.println("Patient not null - getting the patient DAO");
			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			try {
				//patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
				patientDAO.getSession().beginTransaction();
				System.out.println("Getting the decisions for patient: " + patient);
					decisionsForPatient = (ArrayList<ClinicalDecision>) patientDAO.getAllDecisionsIncludingInvalid(patient);
				patientDAO.getSession().getTransaction().commit();
				//patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
			} catch (HibernateException e) {
				System.out.println("Failed getting decisionsForPatient: "+ decisionsForPatient);
				throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
			}
		}
	}
	
	public void fireDecisionChanged(final ClinicalDecision decision) {
		System.out.println("Firing decision changed for decision: "+decision);
		if (decisionFiredListeners == null)
			return;
		Object[] rls = decisionFiredListeners.getListeners();
		for (Object o : rls) {
			IDecisionListener listener = (IDecisionListener) o;
			listener.decisionChanged(decision);
		}
	}
	
	public void fireDecisionStored(final ClinicalDecision decision) {
		System.out.println("Firing decision stored: "+decision);
		updateDecisionList(decision.getPatient());
		if (decisionFiredListeners == null)
			return;
		Object[] rls = decisionFiredListeners.getListeners();
		for (Object o : rls) {
			IDecisionListener listener = (IDecisionListener) o;
			listener.decisionStored(decision);
		}
	}

	public Boolean getInvalidRecordsShown() {
		return invalidRecordsShown;
	}

	public  void setInvalidRecordsShown(Boolean invalidRecordsShown) {
		this.invalidRecordsShown = invalidRecordsShown;
	}
}
