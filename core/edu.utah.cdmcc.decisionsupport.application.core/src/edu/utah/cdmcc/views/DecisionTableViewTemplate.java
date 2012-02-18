package edu.utah.cdmcc.views;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.decision.object.GetInvalidDecisionFilter;
import core.decision.object.IDecisionListener;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.decisionsupport.viewers.DecisionContentProvider;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

public abstract class DecisionTableViewTemplate extends ViewPart implements
		IPatientsListener, IDecisionListener {

	protected CheckboxTableViewer decisionTableViewer;
	protected Table table;
	protected final GetInvalidDecisionFilter getInvalidFilter = new GetInvalidDecisionFilter();
	
	public CheckboxTableViewer getDecisionTableViewer() {
		return decisionTableViewer;
	}

	@Override
	public void createPartControl(final Composite parent) {
		decisionTableViewer.setContentProvider(new DecisionContentProvider());
		decisionTableViewer.setInput(ApplicationControllers.getDecisionController().getDecisionsForPatient());
		checkInvalidDecisions(ApplicationControllers.getDecisionController().getDecisionsForPatient());
		decisionTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection) event.getSelection();
					ClinicalDecision decision = (ClinicalDecision) ss.getFirstElement();
					ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
				}
			}
		});
		
		decisionTableViewer.addCheckStateListener(new ICheckStateListener(){  
			public void checkStateChanged(CheckStateChangedEvent event) {
				ClinicalDecision decision = (ClinicalDecision) event.getElement();
				if (event.getChecked()==true){
					decision.setValid(false);
				} else {
					decision.setValid(true);
				}
				IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
				try {
					patientDAO.getSession().beginTransaction();
					patientDAO.updatePatientValues(decision.getPatient());
					patientDAO.getSession().getTransaction().commit();
				} catch (HibernateException e) {
					throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
				}
				ApplicationControllers.getDecisionController().fireDecisionStored(decision);
				// Firing decision stored makes a check change refresh the viewer instantly
			}		
		});
			
		addListenerHookups();
		ApplicationControllers.getDecisionController().setInvalidRecordsShown(false);
		decisionTableViewer.addFilter(getInvalidFilter);

	}

	private void checkInvalidDecisions(List<ClinicalDecision> decisionsForPatient) {
		 for (ClinicalDecision decision : decisionsForPatient){
			 if (decision.getValid()){
				 this.decisionTableViewer.setChecked(decision, false);
			 } else {
				 this.decisionTableViewer.setChecked(decision, true);
			 }
		 }
	}
	
	private void addListenerHookups() {
		getSite().setSelectionProvider(decisionTableViewer);
		ApplicationControllers.getDecisionController().addDecisionFiredListener(this);
		ApplicationControllers.getPatientController().addPatientsListener(this);
	}
	
	@Override
	public void setFocus() {
		decisionTableViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(this);
		ApplicationControllers.getPatientController().removePatientsListener(this);
		super.dispose();
	}
	
	public void decisionChanged(final ClinicalDecision decision) {
	}

	public void decisionStored(final ClinicalDecision decision) {
		decisionTableViewer.refresh();
		decisionTableViewer.reveal(decision);
		decisionTableViewer.setSelection(new StructuredSelection(decision));
	}

	public void patientsChanged(final Patient patient) {
		final List<ClinicalDecision> EMPTY = new ArrayList<ClinicalDecision>();
		decisionTableViewer.setInput(EMPTY);
		decisionTableViewer.refresh();
		if (patient != null)
		decisionTableViewer.setInput(ApplicationControllers.getDecisionController()
				.getDecisionsForPatient());
		checkInvalidDecisions(ApplicationControllers.getDecisionController().getDecisionsForPatient());
	}

	public ViewerFilter getInvalidFilter() {
		return getInvalidFilter;
	}
	
}
