package edu.utah.cdmcc.jface.viewers;

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

import vasoactive.decision.object.GlucoseDecisionLabelProvider;
import vasoactive.decision.object.GlucoseTable;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.decision.object.GetInvalidFilter;
import core.decision.object.IDecisionListener;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.decisionsupport.viewers.DecisionContentProvider;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;

/**
 * 
 * DecisionTableView is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Purpose of the class: List decisions for a specific patient in a
 *         table.
 *         
 *          * 
 * IN CASE I THINK ABOUT MOVING THIS AGAIN:
 * 	Currently application.core is independent of glucose.core and other
 *  domain specific plugins, and vice versa.  Since this class need to have
 *  access to the application controllers and other things inside application.core,
 *  and since this class is necessarily specific to a domain (glucose in this case),
 *  then it is best kept in the main application plugin for each domain. 
 *  
 */
public class DecisionTableView extends ViewPart implements IDecisionListener, IPatientsListener {

	public static final String ID = "edu.utah.cdmcc.decisionsupport.decision.table.viewer";
	private CheckboxTableViewer decisionTableViewer;
	private Table table;
	private final GetInvalidFilter getInvalidFilter = new GetInvalidFilter();
	
	public DecisionTableView() {
		super();
	}

	public CheckboxTableViewer getDecisionTableViewer() {
		return decisionTableViewer;
	}

	@Override
	public void createPartControl(final Composite parent) {
		// With other domains, change the next line to bring in the correct table
		// For example, VasoactiveTable or VentilatorTable or SalineTable.
		// Eventually I may be able to use Spring to wire these up and eliminate
		// all coupling between these classes.  That would actually be slick.
		table = new GlucoseTable(parent).getTable();
		decisionTableViewer = new CheckboxTableViewer(table);
		decisionTableViewer.setContentProvider(new DecisionContentProvider());
		decisionTableViewer.setLabelProvider(new GlucoseDecisionLabelProvider());
		// With other domains, change the previous line to select the correct label provider.
		// As above, Spring might allow us to completely decouple the code.
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
					patientDAO.updatePatient(decision.getPatient());
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
