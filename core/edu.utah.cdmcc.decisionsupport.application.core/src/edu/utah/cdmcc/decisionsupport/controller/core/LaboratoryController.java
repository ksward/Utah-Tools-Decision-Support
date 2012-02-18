package edu.utah.cdmcc.decisionsupport.controller.core;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.ListenerList;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.Patient;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.listeners.ILaboratoryListener;;

public class LaboratoryController extends PropertyChangeController {
	private ListenerList laboratoryChangeListeners;
	private Boolean invalidRecordsShown = false;
	private List<BasicLaboratoryObject> labsForPatient = new ArrayList<BasicLaboratoryObject>();
	
	public LaboratoryController(){
	}

	public ListenerList getLaboratoryChangeListeners() {
		return laboratoryChangeListeners;
	}

	public void setLaboratoryChangeListeners(ListenerList laboratoryChangeListeners) {
		this.laboratoryChangeListeners = laboratoryChangeListeners;
	}
	
	public void addLaboratoryChangedListener(final ILaboratoryListener listener) {
		if (laboratoryChangeListeners == null) {
			laboratoryChangeListeners = new ListenerList();
		}
		laboratoryChangeListeners.add(listener);
	}

	public void removeLaboratoryChangedListener(final ILaboratoryListener listener) {

		if (laboratoryChangeListeners != null) {
			laboratoryChangeListeners.remove(listener);
			if (laboratoryChangeListeners.isEmpty())
				laboratoryChangeListeners = null;
		}
	}
	
	public void updateLabList(Patient patient) {
		System.out.println("Updating the laboratory list");
		if (patient == null){
			labsForPatient.clear();
		} else {
			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			try {
				patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
				patientDAO.getSession().beginTransaction();
				System.out.println("Getting the labs for patient: " + patient);
					labsForPatient = (ArrayList<BasicLaboratoryObject>) patientDAO.getAllLabsIncludingInvalid(patient);
				patientDAO.getSession().getTransaction().commit();
				patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
			} catch (HibernateException e){
				System.out.println("Failed getting labsForPatient: "+ labsForPatient);
				throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
			}
		}		
	}
	
	public void fireLaboratoryChanged() {
		Patient patient = ApplicationControllers.getPatientController().getActivePatient();
		updateLabList(patient);
		if (laboratoryChangeListeners == null)
			return;
		Object[] rls = laboratoryChangeListeners.getListeners();
		for (Object o : rls) {
			ILaboratoryListener listener = (ILaboratoryListener) o;
			listener.laboratoryChanged();
		}
	}

	public List<BasicLaboratoryObject> getLabsForPatient() {
		return labsForPatient;
	}

	public void setLabsForPatient(List<BasicLaboratoryObject> labsForPatient) {
		this.labsForPatient = labsForPatient;
	}

	public void setInvalidRecordsShown(Boolean invalidRecordsShown) {
		this.invalidRecordsShown = invalidRecordsShown;
	}

	public Boolean getInvalidRecordsShown() {
		return invalidRecordsShown;
	}




}
