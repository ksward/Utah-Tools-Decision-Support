package edu.utah.cdmcc.views;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.laboratory.object.GetInvalidLaboratoryFilter;
import core.laboratory.object.BasicLaboratoryObject;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.decisionsupport.viewers.LaboratoryContentProvider;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.listeners.ILaboratoryListener;

public abstract class LaboratoryTableViewTemplate extends ViewPart implements
		IPatientsListener, ILaboratoryListener {

	protected CheckboxTableViewer labTableViewer;
	protected Table table;
	protected final GetInvalidLaboratoryFilter getInvalidFilter = new GetInvalidLaboratoryFilter();
	final List<BasicLaboratoryObject> EMPTY = new ArrayList<BasicLaboratoryObject>();
	
	public void laboratoryChanged() {
		System.out.println("Laboratory changed in lab viewer reached");
		labTableViewer.setInput(EMPTY);
		labTableViewer.refresh();
		labTableViewer.setInput(ApplicationControllers.getLaboratoryController().getLabsForPatient());
		checkInvalidLabs(ApplicationControllers.getLaboratoryController().getLabsForPatient());	
	}

	public void patientsChanged(Patient patient) {
		labTableViewer.setInput(EMPTY);
		labTableViewer.refresh();
		if (patient != null){
			labTableViewer.setInput(ApplicationControllers.getLaboratoryController().getLabsForPatient());
			checkInvalidLabs(ApplicationControllers.getLaboratoryController().getLabsForPatient());
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		labTableViewer.setContentProvider(new LaboratoryContentProvider());
		labTableViewer.setInput(ApplicationControllers.getLaboratoryController().getLabsForPatient());
		checkInvalidLabs(ApplicationControllers.getLaboratoryController().getLabsForPatient());

		labTableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				BasicLaboratoryObject result = (BasicLaboratoryObject) event.getElement();
				if (event.getChecked()==true){
					result.setValid(false);
				} else {
					result.setValid(true);
				}
				IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
				try {
					patientDAO.getSession().beginTransaction();
					patientDAO.updatePatientValues(result.getPatient());
					patientDAO.getSession().getTransaction().commit();
				} catch (HibernateException e) {
					throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
				}
				laboratoryChanged();
			}
		});
		
		addListenerHookups();
		ApplicationControllers.getLaboratoryController().setInvalidRecordsShown(false);
		labTableViewer.addFilter(getInvalidFilter);
	}
	
	private void checkInvalidLabs(List<BasicLaboratoryObject> labsForPatient) {
		for (BasicLaboratoryObject result:labsForPatient){
			if (result.getValid()){
				this.labTableViewer.setChecked(result, false);
			} else {
				this.labTableViewer.setChecked(result, true);
			}
		}		
	}

	public GetInvalidLaboratoryFilter getGetInvalidFilter() {
		return getInvalidFilter;
	}

	public void setLabTableViewer(CheckboxTableViewer labTableViewer) {
		this.labTableViewer = labTableViewer;
	}

	private void addListenerHookups() {
		ApplicationControllers.getLaboratoryController().addLaboratoryChangedListener(this);
		ApplicationControllers.getPatientController().addPatientsListener(this);		
	}
	
	@Override
	public void dispose() {
		ApplicationControllers.getLaboratoryController().removeLaboratoryChangedListener(this);
		ApplicationControllers.getPatientController().removePatientsListener(this);
		super.dispose();
	};
	
	public CheckboxTableViewer getLabTableViewer() {
		return labTableViewer;
	}

	@Override
	public void setFocus() {
		labTableViewer.getControl().setFocus();
	}
}
