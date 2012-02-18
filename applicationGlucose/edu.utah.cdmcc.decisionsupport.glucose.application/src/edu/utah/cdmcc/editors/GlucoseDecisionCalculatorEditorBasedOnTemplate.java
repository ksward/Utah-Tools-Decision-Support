package edu.utah.cdmcc.editors;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.drools.utilities.RulesEngineException;
import core.hibernate.HibernateValidationHandler;
import drools.engine.KnowledgeEngine;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.views.DecisionCalculatorEditorTemplate;
import glucose.decision.object.GlucoseDecision;
import glucose.decision.object.GlucoseFieldsAddedToDomainComposite;

/**
 * 
 * GlucoseDecisionCalculatorEditor is part of the Utah Decision Support Tools.
 * This is specific to glucose; it currently contains code to fire the rules
 * engine (Drools).  This now extends a generic template.
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
public class GlucoseDecisionCalculatorEditorBasedOnTemplate extends DecisionCalculatorEditorTemplate {
	public  KnowledgeEngine decisionEngine;
	private GregorianCalendar decisionTime;
	protected StyledText emptyTrace;
	public static String ID = "edu.utah.cdmcc.decisionsupport.glucose.editor.decisionCalculator";
	private static Logger log = Logger.getLogger(GlucoseDecisionCalculatorEditorBasedOnTemplate.class);
	private GlucoseFieldsAddedToDomainComposite editorComposite;

	public GlucoseDecisionCalculatorEditorBasedOnTemplate() {
		super();
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
		editorComposite = new GlucoseFieldsAddedToDomainComposite(parent, SWT.NULL);
	}

	private void addContextHelp() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(editorComposite,
				"edu.utah.cdmcc.help.glucose.editorView");
	}

	private void setupDomainSpecificPart() {
		editorComposite.getCurrentGlucoseText().addModifyListener(this);
		editorComposite.getCurrentInsulinText().addModifyListener(this);
		editorComposite.getCarbohydrateStatusCombo().addSelectionListener(this);
	}

	private void listenToObservationDateTimeChanges() {
		editorComposite.getObsDateWidget().addSelectionListener(this);
		editorComposite.getObsTimeWidget().addSelectionListener(this);
	}

	private void listenToButtonSelection() {
		editorComposite.getDecisionButton().addSelectionListener(this);
		editorComposite.getChartButton().addSelectionListener(this);
		editorComposite.getAcceptButton().addSelectionListener(this);
		editorComposite.getDeclineButton().addSelectionListener(this);
		editorComposite.getRefreshDataButton().addSelectionListener(this);
	}

	@Override
	protected void setDecisionFiredFlagOff() {
		editorComposite.setDecisionFiredFlag(false);
	}

	@Override
	public void setFocus() {
		if (editorComposite != null && !editorComposite.isDisposed()) {
			editorComposite.getCurrentGlucoseText().setFocus();
		}
	}

	public void dispose() {
		ApplicationControllers.getPatientController().removePatientsListener(this);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(editorComposite);
		ApplicationControllers.getDatabaseController().removeDatabaseChangedListener(this);
		super.dispose();
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
			log.debug("Decision object successfully instantiated");
			this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision object instantiated ...");
				try {
					log.debug("About to fire the rules engine");
					fireDroolsRulesEngine();
				} catch (Exception e) {
					log.debug("Firing the rules engine failed");
					log.debug("Exception: " + e);
					e.printStackTrace();
				}

			editorComposite.setDecisionFiredFlag(true);
			editorComposite.enableCorrectButtonCombination();
		}
	}

	public void setDecisionEngine(KnowledgeEngine decisionEngine) {
		this.decisionEngine = decisionEngine;
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
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Firing the rules engine ...");
		getDecisionEngine().fireRules( (GlucoseDecision) decision);
		decision.setRulesFiredText(getDecisionEngine().getTraceRulesLogger().getEvents());
		// If no advice resulted from running the inference engine
		if (decision.getAdviceText().length() == 0) {
			decision.setAdviceText("No change at this time.");
		}

		// Fire property change so listeners can act
		ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision calculated ...");
	}

	public KnowledgeEngine getDecisionEngine() {
		if (decisionEngine == null) {
			return createDecisionEngine();
		} else {
			return decisionEngine;
		}

	}

	private KnowledgeEngine createDecisionEngine() {
		// decisionEngine = new KnowledgeEngine("GlucoseDroolRules00.drl");
		decisionEngine = new KnowledgeEngine();
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Creating the knowledge engine ...");
		return decisionEngine;
	}

	/**
	 * Add the decision to the patient object so that we can consider the
	 * decision retrospectively. This would be the routine that preserves all
	 * ludicrous human behavior for posterity to enjoy.
	 */
	protected void chartDecision() {
		System.out.println("Preparing to chart the decision");
		if (instantiateDecisionObject()) {
			if (decision.getUserAction().equals(ClinicalDecision.PENDING)) {
				decision.setUserAction(ClinicalDecision.BACKCHART);
			}
			this.getPatient().addDecision(decision);
			System.out.println("Decision added to decisions collection object in the patient");
		}

		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			System.out.println("About to get a hibernate transaction to start the charting");
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(this.getPatient());
			patientDAO.getSession().flush();
			patientDAO.getSession().getTransaction().commit();
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
			System.out.println("Transaction committed");
		} catch (HibernateException e) {
			System.out.println("Charting failed with Hibernate exception: " + e);
			e.printStackTrace();
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

			Integer carbohydrateSource = editorComposite.getCarbohydrateStatusCombo().getSelectionIndex();
			Integer currentGlucose = Integer.valueOf(editorComposite.getCurrentGlucoseText().getText());
			Double currentInsulinDripRate = Double.valueOf(editorComposite.getCurrentInsulinText().getText());
			populateDomainSpecificDecision(carbohydrateSource, currentGlucose, currentInsulinDripRate);
			populateClinicalDecision();
			if (ApplicationControllers.getUserController().getCurrentUser() != null) {
				decision.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());
			}
			return validateGlucoseDecisionWithHibernate();
		}
		return true;
	}

	private void prepareObservationDateTimeValues() {
			decisionTime = new GregorianCalendar(editorComposite.getObsDateWidget().getYear(),
					editorComposite.getObsDateWidget().getMonth(),
					editorComposite.getObsDateWidget().getDay(),
					editorComposite.getObsTimeWidget().getHours(),
					editorComposite.getObsTimeWidget().getMinutes());
	}

	private void populateDomainSpecificDecision(Integer carbohydrateSource, Integer currentGlucose,
			Double currentInsulinDripRate) {
		String userAction = ClinicalDecision.PENDING;
		decision = new GlucoseDecision(decisionTime, userAction, currentGlucose, currentInsulinDripRate,
				carbohydrateSource);
		 ((GlucoseDecision) decision).setPreviousGlucoseConcentration(retrievePreviousDecisionGlucose());
		((GlucoseDecision) decision).setPreviousObservationTime(retrievePreviousDecisionTime());
	}

	private void populateClinicalDecision() {
		decision.setPatient(getPatient());
		decision.setPatientAgeDays(getPatient().getDeltaAgeDays(decisionTime));
		decision.setPatientHeight(getPatient().getHeight());
		decision.setPatientWeight(getPatient().getWeight());
	}

	private boolean validateGlucoseDecisionWithHibernate() {
		ClassValidator<GlucoseDecision> glucoseValidator = new ClassValidator<GlucoseDecision>(GlucoseDecision.class);
		InvalidValue[] validationMessages = glucoseValidator.getInvalidValues((GlucoseDecision) decision);
		if (validationMessages.length == 0) {
			return true;
		} else {
			String errorMessage = HibernateValidationHandler.handleValidationMessages(validationMessages);
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			decision = null;
			return false;
		}
	}

	public Integer retrievePreviousDecisionGlucose() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", this.getPatient());
		ArrayList<?> results = (ArrayList<?>) q.list();
		patientDAO.getSession().getTransaction().commit();
		if (results.size() == 0) {
			return GlucoseDecision.NOPREVIOUSGLUCOSE;
		} else {
			return ((GlucoseDecision) results.get(0)).getSerumGlucoseConcentration();
		}
	}


	public GregorianCalendar retrievePreviousDecisionTime() {
		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		patientDAO.getSession().beginTransaction();
		Query q = patientDAO.getSession().getNamedQuery(ClinicalDecision.GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID);
		q.setParameter("patient", this.getPatient());
		ArrayList<?> results = (ArrayList<?>) q.list();
		patientDAO.getSession().getTransaction().commit();
		if (results.size() == 0) {
			return GlucoseDecision.NOPREVIOUSOBSERVATIONDATE;
		} else {
			return ((ClinicalDecision) results.get(0)).getObservationDate();
		}
	}

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
		if (e.widget.equals(editorComposite.getCarbohydrateStatusCombo())) {
			clearDecision();
			return;
		}
		if (e.widget.equals(editorComposite.getRefreshDataButton())){
			clearDecision();
			return;
		}
		if (e.widget.equals(editorComposite.getObsDateWidget()) || e.widget.equals(editorComposite.getObsTimeWidget())){
			clearDecision();
		}
	}

	public void modifyText(final ModifyEvent e) {
		clearDecision();
	}

	private void clearDecision() {
		decision = null;
		editorComposite.clearAdvice();
	}
	
	public void focusGained(final FocusEvent e) {
		
	}

	public void focusLost(final FocusEvent e) {
		
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}
}
