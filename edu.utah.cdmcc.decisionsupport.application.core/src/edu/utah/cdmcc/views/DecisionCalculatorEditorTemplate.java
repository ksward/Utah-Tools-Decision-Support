package edu.utah.cdmcc.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.hibernate.HibernateException;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.decision.object.DeclineDecisionCommentDialog;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import core.patient.object.PatientEditorInput;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.listeners.IDatabaseListener;
import edu.utah.cdmcc.preferences.StudyPreferenceConstants;

/**
 * 
 * The domain specific calculators must extend this abstract class.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 * 
 */
public abstract class DecisionCalculatorEditorTemplate extends EditorPart implements 
		IPatientsListener, 
		SelectionListener,
		ModifyListener,
		FocusListener, 
		IDatabaseListener {

	Boolean activeMode;
	
	protected ClinicalDecision decision;

	public DecisionCalculatorEditorTemplate() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getPatient().getName());
	}

	protected Patient getPatient() {
		Patient returnPatient = ((PatientEditorInput) getEditorInput()).getPatient();
		return returnPatient;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * When patient selection is changed, then need to get new patient, make
	 * sure the Study ID number already exists, and then set up the editor.
	 * 
	 */
	public void patientsChanged(final Patient patient) {
		if (patient != null) {
			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			try {
				patientDAO.getSession().beginTransaction();
				if (patientDAO.studyIDAlreadyExists(patient.getStudyID())) {
					setPartName(((PatientEditorInput) getEditorInput()).getPatient().getName());
					setDecisionFiredFlagOff();
				} else {
					if (getEditorInput().getName().equals(patient.getName()))
						getEditorSite().getPage().closeEditor(this, false);
				}
				patientDAO.getSession().getTransaction().commit();
			} catch (HibernateException e) {
				throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
			}
		}
	}

	protected abstract void setDecisionFiredFlagOff();

	/**
	 * If the database is changed, completely, then all the editors need to
	 * close.
	 */
	public void databaseChanged() {
		getEditorSite().getPage().closeAllEditors(true);
	}

	/**
	 * Sets the chart to indicate user declined the advice.  If mode
	 * is passive, this does not apply.
	 */
	protected void declineDecision() {
		activeMode = Activator.getDefault().getPreferenceStore().getBoolean(StudyPreferenceConstants.ACTIVE_MODE);
		if (activeMode) {
			decision.setUserAction(ClinicalDecision.DECLINE);
			DeclineDecisionCommentDialog dialog = new DeclineDecisionCommentDialog(
					getSite().getShell(), decision);
			dialog.open();
		} else {
			decision.setUserAction(ClinicalDecision.PASSIVE);
		}
		chartDecision();
	}


	/**
	 * Sets the chart to indicate user accepted the advice. If study mode
	 * is passive, then it will set flag to passive.
	 */
	protected void acceptDecision() {
		activeMode = Activator.getDefault().getPreferenceStore().getBoolean(StudyPreferenceConstants.ACTIVE_MODE);
		if (activeMode) {
			decision.setUserAction(ClinicalDecision.ACCEPT);
		} else {
			decision.setUserAction(ClinicalDecision.PASSIVE);
		}
		chartDecision();
	}
	
	protected abstract void chartDecision();
	public abstract void modifyText(final ModifyEvent e);
	public abstract void widgetSelected(final SelectionEvent e);
}
