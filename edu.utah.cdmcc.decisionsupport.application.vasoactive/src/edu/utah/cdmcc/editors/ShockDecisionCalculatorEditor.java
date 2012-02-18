package edu.utah.cdmcc.editors;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import vasoactive.decision.object.VasoactiveDecision;
import vasoactive.decision.object.VasoactiveDecisionState;
import vasoactive.decision.object.VasoactiveFieldsAddedComposite2;
import vasoactive.decision.object.ShockDecision;
import vasoactive.decision.object.ShockDecisionState;
import vasoactive.decision.object.PreferenceObject;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.hibernate.HibernateValidationHandler;
import core.patient.object.IPatientsListener;
import core.patient.object.Patient;
import core.patient.object.PatientEditorInput;
import drools.engine.KnowledgeEngine;
import drools.engine.RulesEngineException;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.exceptions.UtahToolboxException;
import edu.utah.cdmcc.exceptions.UtahToolboxException.ErrorCode;
import edu.utah.cdmcc.listeners.IDatabaseListener;

/**
 * 
 * GlucoseDecisionCalculatorEditor is part of the Utah Decision Support Tools.
 * This is specific to glucose; it currently contains code to fire the rules
 * engine (Drools).
 * 
 * IN CASE I THINK ABOUT MOVING THIS AGAIN:
 * 	Currently application.core is independent of glucose.core and other
 *  domain specific plugins, and vice versa.  Since this class need to have
 *  access to the application controllers and other things inside application.core,
 *  and since this class is necessarily specific to a domain (glucose in this case),
 *  then it is best kept in the main application plugin for each domain. 
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 * 
 */
public class ShockDecisionCalculatorEditor extends EditorPart implements IPatientsListener, SelectionListener,
		ModifyListener, FocusListener, IDatabaseListener {

	//public static DecisionEngine decisionEngine = new DecisionEngine();
	//public DroolsProperties droolsProperties = new DroolsProperties();
	public  KnowledgeEngine decisionEngine;
	public static String ID = "edu.utah.cdmcc.decisionsupport.glucose.editor.decisionCalculator";
	VasoactiveFieldsAddedComposite2 editorComposite;
	private ShockDecision decision;
	Date obsDate;
	GregorianCalendar decisionTime;
	StyledText emptyTrace;

	public ShockDecisionCalculatorEditor() {
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

	/**
	 * Get the patient related to the editor that is active.
	 * 
	 * @return patient
	 */
	private Patient getPatient() {
		Patient returnPatient = ((PatientEditorInput) getEditorInput()).getPatient();
		return returnPatient;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	public  KnowledgeEngine getDecisionEngine() {
		if (decisionEngine == null){
			return createDecisionEngine();
		} else {
		return decisionEngine;
	}

	}
	private KnowledgeEngine createDecisionEngine() {
		decisionEngine = new KnowledgeEngine();
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Creating the knowledge engine ...");
		return decisionEngine;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Create the editor for glucose decisions, based on the appropriate
	 * composite. Add a whole shitload of listeners, etc. which I hope to
	 * replace with data binding routines to simplify the code.
	 * <p>
	 * But this works, so hey, why complain?
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setupGeneralDecisionPart(parent);
		setupDomainSpecificPart();
	}

	private void setupGeneralDecisionPart(final Composite parent) {
		createEditorComposite(parent);
		listenToButtonSelection();	
		listenToObservationDateTimeChanges();
		emptyTrace = new StyledText(editorComposite, 0);
		emptyTrace.setVisible(false);
		setDecisionFiredFlagOff();
		ApplicationControllers.getPatientController().addPatientsListener(this);
		ApplicationControllers.getDecisionController().addDecisionFiredListener(editorComposite);
		ApplicationControllers.getDatabaseController().addDatabaseChangedListener(this);
		addContextHelp();
	}

	private void createEditorComposite(final Composite parent) {
		editorComposite = new VasoactiveFieldsAddedComposite2(parent, SWT.NULL);
	}

	private void addContextHelp() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(editorComposite,
				"edu.utah.cdmcc.help.glucose.editorView");
	}

	private void setupDomainSpecificPart() {
		editorComposite.getSystolicBloodPressureText().addModifyListener(this);
	}

	private void listenToObservationDateTimeChanges() {
		editorComposite.getObsDateText().addModifyListener(this);
		editorComposite.getObsTimeText().addModifyListener(this);
	}

	private void listenToButtonSelection() {
		editorComposite.getDecisionButton().addSelectionListener(this);
		editorComposite.getChartButton().addSelectionListener(this);
		editorComposite.getAcceptButton().addSelectionListener(this);
		editorComposite.getDeclineButton().addSelectionListener(this);
	}

	private void setDecisionFiredFlagOff() {
		editorComposite.setDecisionFiredFlag(false);
	}

	@Override
	public void setFocus() {
		if (editorComposite != null && !editorComposite.isDisposed()) {
			editorComposite.getObsDateText().setFocus();
		}
	}

	public void dispose() {
		ApplicationControllers.getPatientController().removePatientsListener(this);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(editorComposite);
		ApplicationControllers.getDatabaseController().removeDatabaseChangedListener(this);
		super.dispose();
	}

	/**
	 * When patient selection is changed, then need to get new patient, make
	 * sure the TrialDB number already exists, and then set up the editor.
	 * 
	 */
	public void patientsChanged(final Patient patient) {
		if (patient != null) {
			IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
			try {
				patientDAO.getSession().beginTransaction();
				if (patientDAO.studyIDAlreadyExists(patient.getStudyID())) {
					setPartName(((PatientEditorInput) getEditorInput()).getPatient().getName());
					//refreshDemographicFields();
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

	/**
	 * If the database is changed, completely, then all the editors need to
	 * close.
	 */
	public void databaseChanged() {
		getEditorSite().getPage().closeAllEditors(true);
	}

	/**
	 * 
	 * This calculates a decision. It checks to make sure which inference engine
	 * is used. This is because this development effort began with Jess instead
	 * of Drools, and to allow the potential to add or change inference engines
	 * in the future. Currently, only Drools is used.
	 * <p>
	 * First, instantiate a decision object (which populates everything) and
	 * then call the routine to fire the engine.
	 * 
	 * @throws Exception
	 */
	private void calculateDecision() {

		if (instantiateDecisionObject()) {
			this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision object instantiated ...");
				try {
					
					fireDroolsRulesEngine();
				} catch (Exception e) {
					throw new UtahToolboxException(ErrorCode.DROOLS_FIRE_ENGINE_ERROR, e);
				}

			editorComposite.setDecisionFiredFlag(true);
			editorComposite.enableCorrectButtonCombination();
		}
	}

	/**
	 * This routine gets a decision engine and fires it. I have buried all
	 * details in the core Drools engine code that is in an external plugin.
	 * <p>
	 * Then the routine fires the rules engine.
	 * <p>
	 * Then the decision object is updated and the main plugin has its listeners
	 * notified.
	 * 
	 * @throws RulesEngineException
	 */
	private void fireDroolsRulesEngine() throws RulesEngineException {

		ShockDecisionState decisionState = new ShockDecisionState();
		PreferenceObject preferences = new PreferenceObject();
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Firing the rules engine ...");
		getDecisionEngine().fireRules( decision, decisionState, preferences);
		decision.setRulesFiredText(getDecisionEngine().getGlucoseLogger().getEvents());
		// If no advice resulted from running the inference engine
		if (decision.getAdviceText().length() == 0) {
			decision.setAdviceText("No change at this time.");
		}

		// Fire property change so listeners can act
		ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision calculated ...");
	}
	
	private void declineDecision() {
		//TODO We need a comment entry method
		decision.setUserAction(ClinicalDecision.DECLINE);
		chartDecision();
	}

	/**
	 * Sets the chart to indicate user accepted the advice. May want to add
	 * routines to permit adding comments.
	 */
	private void acceptDecision() {
		decision.setUserAction(ClinicalDecision.ACCEPT);
		chartDecision();
	}

	/**
	 * Add the decision to the patient object so that we can consider the
	 * decision retrospectively. This would be the routine that preserves all
	 * ludicrous human behavior for posterity to enjoy.
	 */
	private void chartDecision() {
		if (instantiateDecisionObject()) {
			if (decision.getUserAction().equals(ClinicalDecision.PENDING)) {
				decision.setUserAction(ClinicalDecision.BACKCHART);
			}
			this.getPatient().addDecision(decision);
		}

		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatient(this.getPatient());
			patientDAO.getSession().getTransaction().commit();
		} catch (HibernateException e) {
			throw new UtahToolboxException(ErrorCode.HIBERNATE_ERROR, e);
		}

		ApplicationControllers.getDecisionController().fireDecisionStored(decision);
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision stored in database ...");
		decision = null;
		setDecisionFiredFlagOff();
		editorComposite.enableCorrectButtonCombination();
	}

	/**
	 * Populates a decision object so that it can be fed to the rules engine
	 * shredder (currently Drools) and then processed.
	 * 
	 * @return status of whether the decision object was successfully created.
	 */
	private boolean instantiateDecisionObject() {
		// Check if a decision already instantiated because do not want to
		// overwrite this. If rules were fired, then there is a decision object.
		if (decision == null) {

			prepareObservationDateTimeValues();

			Integer systolicBloodPressure = Integer.valueOf(editorComposite.getSystolicBloodPressureText().getText());
			Integer diastolicBloodPressure = Integer.valueOf(editorComposite.getDiastolicBloodPressureText().getText());
			Double dopamineInfusionRate = Double.valueOf(editorComposite.getDopamineInfusionRateText().getText());
			
//			These lines were added to remove the errors for now -- until the editor is completed... ***
			Double epinephrineInfusionRate = 0.0;
			Double norEpinephrineInfusionRate = 0.0;		
//			Double epinephrineInfusionRate = Double.valueOf(editorComposite.getEpinephrineInfusionRateText().getText());
//			Double norEpinephrineInfusionRate = Double.valueOf(editorComposite.getNorEpinephrineInfusionRateText().getText());

			populateDomainSpecificDecision(/*currentGlucose, currentInsulinDripRate,*/ systolicBloodPressure, 
					diastolicBloodPressure, dopamineInfusionRate, epinephrineInfusionRate, norEpinephrineInfusionRate);
			populateClinicalDecision();
			return validateGlucoseDecisionWithHibernate();
		}
		return true;
	}

	private void prepareObservationDateTimeValues() {
		String ds = editorComposite.getObsDateText().getText();
		String hm = editorComposite.getObsTimeText().getText();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		try {
			obsDate = df.parse(ds);
			decisionTime = new GregorianCalendar();
			decisionTime.setTime(obsDate);
			addTimeFields(hm);
		} catch (ParseException e) {
			throw new UtahToolboxException(ErrorCode.DATE_PARSE_ERROR, e);
		}
	}

	private void populateDomainSpecificDecision(//Integer currentGlucose, Double currentInsulinDripRate, 
		Integer systolicBloodPressure, Integer diastolicBloodPressure, 
			Double dopamineInfusionRate, Double epinephrineInfusionRate, Double norEpinephrineInfusionRate) {
		String userAction = ClinicalDecision.PENDING;
		decision = new ShockDecision(decisionTime, userAction,
				systolicBloodPressure, diastolicBloodPressure, 
				dopamineInfusionRate, epinephrineInfusionRate, norEpinephrineInfusionRate);
		decision.setPreviousObservationTime(retrievePreviousDecisionTime());
	}

	private void populateClinicalDecision() {
		decision.setPatient(getPatient());
		decision.setPatientAgeDays(getPatient().getDeltaAgeDays(decisionTime));
		decision.setPatientHeight(getPatient().getHeight());
		decision.setPatientWeight(getPatient().getWeight());
	}

	private boolean validateGlucoseDecisionWithHibernate() {
		ClassValidator<ShockDecision> glucoseValidator = new ClassValidator<ShockDecision>(ShockDecision.class);
		InvalidValue[] validationMessages = glucoseValidator.getInvalidValues(decision);
		if (validationMessages.length == 0) {
			return true;
		} else {
			String errorMessage = HibernateValidationHandler.handleValidationMessages(validationMessages);
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			decision = null;
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Integer retrievePreviousDecisionGlucose() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(VasoactiveDecision.GETALLVALIDGLUCOSEDECISIONSBYPATIENT);
		q.setParameter("patient", this.getPatient());
		ArrayList<VasoactiveDecision> results = (ArrayList<VasoactiveDecision>) q.list();
		patientDAO.getSession().getTransaction().commit();
		if (results.size() == 0) {
			return 0;//VasoactiveDecision.NOPREVIOUSGLUCOSE;
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public GregorianCalendar retrievePreviousDecisionTime() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(VasoactiveDecision.GETALLVALIDGLUCOSEDECISIONSBYPATIENT);
		q.setParameter("patient", this.getPatient());
		ArrayList<VasoactiveDecision> results = (ArrayList<VasoactiveDecision>) q.list();
		patientDAO.getSession().getTransaction().commit();
		if (results.size() == 0) {
			return VasoactiveDecision.NOPREVIOUSOBSERVATIONDATE;
		} else {
			return results.get(0).getObservationDate();
		}
	}

	/**
	 * Figure it out. This adds the time fields to the decision so that it can
	 * be properly fed to the rules engine.
	 * 
	 * @param hm
	 */
	private void addTimeFields(final String hm) {
		boolean isPM = false;
		boolean isAM = false;
		String hm1 = hm.toLowerCase();
		int pm = hm1.indexOf("pm");
		if (pm > 0) {
			hm1 = hm1.substring(0, pm).trim();
			isPM = true;
		}
		// just in case user put in am
		int am = hm1.indexOf("am");
		if (am > 0) {
			hm1 = hm1.substring(0, am).trim();
			isAM = true; // Added jmd for calc below
		}

		int colon = hm1.indexOf(":");
		if (colon < 0) {
			System.out.println("Time that would be passed to decisionTime does not have \":\" in it. Wrong format");
			return;
		}
		try {
			int hour = Integer.parseInt(hm1.substring(0, colon));
			if (isPM && hour < 12)
				hour = 12 + hour;
			//if (!isPM && hour >= 12)
				//hour = 0; MD: THIS IS WRONG - all my hours are being zeroed out!
			if (isAM && hour == 12)
				hour = hour - 12; // While dumb, user behavior would expect this
			decisionTime.set(Calendar.HOUR_OF_DAY, hour);
			int minute = Integer.parseInt(hm1.substring(colon + 1));
			decisionTime.set(Calendar.MINUTE, minute);
		} catch (NumberFormatException ne) {
			System.out.println("Time that would be passed to decisionTime does not have correct format");
			return;
		}
		/*
		 * String[] tokens = hm.split("[:]"); if ((tokens[1].contains("pm") ||
		 * tokens[1].contains("PM")) && Integer.parseInt(tokens[0]) < 12) {
		 * decisionTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens[0]) +
		 * 12); } else { decisionTime.set(Calendar.HOUR_OF_DAY,
		 * Integer.parseInt(tokens[0])); } decisionTime.set(Calendar.MINUTE,
		 * Integer.parseInt(tokens[1].substring(0, 2)));
		 */
	}

	public void widgetDefaultSelected(final SelectionEvent e) {
	}

	/**
	 * Handles which button was pushed. Calls the proper routine. -- 11/04/2008:
	 * ben changed the format of this method. Removed all the enclosing else
	 * statments by added "return" after each methods get called.
	 */
	public void widgetSelected(final SelectionEvent e) {
		if (e.widget.equals(editorComposite.getDecisionButton())) {
			calculateDecision();
			return;
		}
		if (e.widget.equals(editorComposite.getChartButton())) {
			chartDecision();
			return;
		}
		if (e.widget.equals(editorComposite.getAcceptButton())) {
			acceptDecision();
			return;
		}
		if (e.widget.equals(editorComposite.getDeclineButton())) {
			declineDecision();
			return;
		}
	}

	public void modifyText(final ModifyEvent e) {
		decision = null;
		editorComposite.clearAdvice();
	}

	public void focusGained(final FocusEvent e) {
	}

	public void focusLost(final FocusEvent e) {
		if (e.widget.equals(editorComposite.getObsDateText())) {
			// TODO Check to make sure birthdate preceded the observation date
			// and put up a dialog if this is not the case, and then clear
			// the observation date field so buttons will behave correctly.
		}
	}

}
