package edu.utah.cdmcc.decisionsupport.controller.core;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.ListenerList;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

/**
 * This object handles all listening that relates to patients, stores the active patient,
 * fires patient changed events, etc. 
 * 
 * @author J. Michael Dean, M.D.
 *
 */
public class PatientController extends PropertyChangeController {
	private List<Patient> patients = new ArrayList<Patient>();
	private ListenerList patientListeners;
	private Patient activePatient;
	
	public PatientController() {
	}

	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}

	public ListenerList getPatientListeners() {
		return patientListeners;
	}

	public void setPatientListeners(ListenerList patientListeners) {
		this.patientListeners = patientListeners;
	}

	public Patient getActivePatient() {
		return activePatient;
	}

	public void setActivePatient(Patient activePatient) {
		this.activePatient = activePatient;
	}

	public void addPatientsListener(final IPatientsListener listener) {

		if (patientListeners == null) {
			patientListeners = new ListenerList();
		}
		patientListeners.add(listener);
	}
	
	public void removePatientsListener(final IPatientsListener listener) {

		if (patientListeners != null) {
			patientListeners.remove(listener);
			if (patientListeners.isEmpty())
				patientListeners = null;
		}
	}	
	
	public void updatePatientList() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
			patientDAO.getSession().beginTransaction();
			patients = (ArrayList<Patient>) patientDAO.getAllPatients();
			patientDAO.getSession().getTransaction().commit();
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
		} catch (HibernateException e) {
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}
	}
	
	public void firePatientsChanged(final Patient patient) {
		updatePatientList(); // has begin and commit wrapper
		ApplicationControllers.getDecisionController().updateDecisionList(patient); // has begin and commit wrapper
		ApplicationControllers.getLaboratoryController().updateLabList(patient);
		if (patientListeners == null)
			return;
		Object[] rls = patientListeners.getListeners();
		for (Object o : rls) {
			IPatientsListener listener = (IPatientsListener) o;
			listener.patientsChanged(patient);
		}
	}	
}
