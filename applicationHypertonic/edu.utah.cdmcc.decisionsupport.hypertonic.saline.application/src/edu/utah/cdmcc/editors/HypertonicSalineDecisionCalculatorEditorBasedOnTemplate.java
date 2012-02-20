package edu.utah.cdmcc.editors;

import hypertonic.decision.object.HypertonicSalineDecision;
import hypertonic.decision.object.HypertonicSalineFieldsAddedToDomainComposite;
import hypertonic.decision.object.HypertonicSalineGetPassiveDecisionDialog;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.hibernate.HibernateException;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import patient.laboratory.controller.HypertonicPatientDecisions;
import patient.laboratory.controller.HypertonicPatientLaboratories;
import patient.laboratory.controller.IPatientLaboratoryController;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.drools.utilities.RulesEngineException;
import core.hibernate.HibernateValidationHandler;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;
import core.multiple.object.controllers.IPatientDecisionController;
import drools.engine.KnowledgeEngine;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.listeners.ILaboratoryListener;
import edu.utah.cdmcc.preferences.StudyPreferenceConstants;
import edu.utah.cdmcc.views.DecisionCalculatorEditorTemplate;

/**
 * 
 * HypertonicSalineDecisionCalculatorEditor is part of the Utah Decision Support
 * Tools. This is specific to hypertonic saline in ICP; it currently contains
 * code to fire the rules engine (Drools). This now extends a generic template.
 * 
 * IN CASE I THINK ABOUT MOVING THIS AGAIN: Currently application.core is
 * independent of hypertonic.core and other domain specific plugins, and vice
 * versa. Since this class need to have access to the application controllers
 * and other things inside application.core, and since this class is necessarily
 * specific to a domain (hypertonic in this case), then it is best kept in the
 * main application plugin for each domain.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 * 
 */
public class HypertonicSalineDecisionCalculatorEditorBasedOnTemplate extends DecisionCalculatorEditorTemplate implements
ILaboratoryListener{
	public KnowledgeEngine decisionEngine;
	private GregorianCalendar decisionTime;
	protected StyledText emptyTrace;
	public static String ID = "edu.utah.cdmcc.decisionsupport.hypertonic.saline.editor.decisionCalculator";
	private static Logger log = Logger.getLogger(HypertonicSalineDecisionCalculatorEditorBasedOnTemplate.class);
	private HypertonicSalineFieldsAddedToDomainComposite editorComposite;
	private ClassValidator<HypertonicSalineDecision> salineValidator;
	private InvalidValue[] validationMessages;
	private IPatientLaboratoryController patientLabController = new HypertonicPatientLaboratories();
	public HypertonicSalineDecisionCalculatorEditorBasedOnTemplate() {
		super();
	}

	/**
	 * Create the editor for hypertonic decisions, based on the appropriate
	 * composite. Add a whole shitload of listeners, etc. which I hope to
	 * replace with data binding routines to simplify the code.
	 * <p>
	 * But this works, so hey, why complain?
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setupGeneralDecisionPart(parent);
		setupDomainSpecificPart();
		populateSerumSodiumFields();
		populateOsmolalityFields();
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
		ApplicationControllers.getLaboratoryController().addLaboratoryChangedListener(this);
		addContextHelp();
	}

	private void createEditorComposite(final Composite parent) {
		editorComposite = new HypertonicSalineFieldsAddedToDomainComposite(parent, SWT.NULL);
	}

	private void addContextHelp() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(editorComposite, "edu.utah.cdmcc.help.glucose.editorView");
		//TODO need to fix this plug in ID
	}

	private void setupDomainSpecificPart() {

		editorComposite.getAddSodiumButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
				try {
					handlerService.executeCommand("edu.utah.cdmcc.commands.sodiumlab", null);
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				} catch (NotDefinedException e1) {
					e1.printStackTrace();
				} catch (NotEnabledException e1) {
					e1.printStackTrace();
				} catch (NotHandledException e1) {
					e1.printStackTrace();
				}

				//populateSerumSodiumFields();
				editorComposite.resetFocus();
				editorComposite.enableCorrectButtonCombination();
			}

		});

		editorComposite.getAddOsmolalityButton().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
				try {
					handlerService.executeCommand("edu.utah.cdmcc.commands.osmolalitylab", null);
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				} catch (NotDefinedException e1) {
					e1.printStackTrace();
				} catch (NotEnabledException e1) {
					e1.printStackTrace();
				} catch (NotHandledException e1) {
					e1.printStackTrace();
				}

				//populateOsmolalityFields();
				editorComposite.resetFocus();
				editorComposite.enableCorrectButtonCombination();
			}

		});
	
	editorComposite.getCurrentHypertonicSalineDripRateText().addModifyListener(this);
	editorComposite.getMAPText().addModifyListener(this);
	editorComposite.getICPText().addModifyListener(this);
	editorComposite.getCVPText().addModifyListener(this);
	}

	private void populateSerumSodiumFields() {
		SerumSodiumLaboratoryResult result = patientLabController.retrieveCurrentSodiumLabResult(getPatient());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		if (result != null) {
			editorComposite.getSodiumDateTimeText().setText(df.format(result.getTimeOfSpecimenCollection().getTime()));
			editorComposite.getCurrentSerumSodiumText().setText(result.getNumericResult().toString());
		} else {
			editorComposite.getSodiumDateTimeText().setText("(Not available)");
			editorComposite.getCurrentSerumSodiumText().setText("Not available");
		}
	}

	private void populateOsmolalityFields() {
		System.out.println("In populateOsmolalityFields");
		SerumOsmolalityLaboratoryResult result = patientLabController.retrieveCurrentOsmolalityLabResult(getPatient());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		if (result != null) {
			editorComposite.getOsmolalityDateTimeText().setText(
					df.format(result.getTimeOfSpecimenCollection().getTime()));
			editorComposite.getSerumOsmText().setText(result.getNumericResult().toString());
		} else {
			editorComposite.getOsmolalityDateTimeText().setText("(Not available)");
			editorComposite.getSerumOsmText().setText("Not available");
		}
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
		editorComposite.getAddOsmolalityButton().addSelectionListener(this);
		editorComposite.getAddSodiumButton().addSelectionListener(this);
	}

	@Override
	protected void setDecisionFiredFlagOff() {
		editorComposite.setDecisionFiredFlag(false);
	}

	@Override
	public void setFocus() {
		if (editorComposite != null && !editorComposite.isDisposed()) {
			editorComposite.getObsDateWidget().setFocus();
		}
	}

	public void dispose() {
		ApplicationControllers.getPatientController().removePatientsListener(this);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(editorComposite);
		ApplicationControllers.getDatabaseController().removeDatabaseChangedListener(this);
		ApplicationControllers.getLaboratoryController().removeLaboratoryChangedListener(this);
		super.dispose();
	}

	/**
	 * 
	 * If the application is in active mode, then it calculates a decision. If
	 * it is in passive mode, then it intercepts this with a dialog that asks
	 * the user what actions they actually took.
	 * <p>
	 * First, instantiate a decision object (which populates everything) and
	 * then call the routine to fire the engine.
	 * 
	 * @throws Exception
	 */
	private void calculateDecision() {
		
		Boolean mode = Activator.getDefault().getPreferenceStore().getBoolean(StudyPreferenceConstants.ACTIVE_MODE);
		if (mode == false && instantiateDecisionObject()) {
			// Intercept without firing rule engine
			decision.setUserAction(ClinicalDecision.PASSIVE);
			this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Asking clinician for actions ...");
			HypertonicSalineGetPassiveDecisionDialog dialog = new HypertonicSalineGetPassiveDecisionDialog(getSite()
					.getShell(), (HypertonicSalineDecision) decision);
			dialog.open();
			ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
		} else {
			if (mode == true && instantiateDecisionObject()) {
				// Fire rules engine for decision
				log.debug("Decision object successfully instantiated");
				this.getEditorSite().getActionBars().getStatusLineManager().setMessage(
						"Decision object instantiated ...");
				try {
					log.debug("About to fire the rules engine");
					fireDroolsRulesEngine();
				} catch (Exception e) {
					log.debug("Firing the rules engine failed");
					log.debug("Exception: " + e);
					e.printStackTrace();
				}
			}
		}
		editorComposite.setDecisionFiredFlag(true);
		editorComposite.enableCorrectButtonCombination();
	}
	
	
	private boolean checkValuesKludgeRoutine() {
		// This is a kludge because I cannot figure out how to make Hibernate validation
		// work on the HypertonicDecisionObject.
		if(((HypertonicSalineDecision) decision).getCurrentHypertonicSalineDripRate() > 10){
			String errorMessage = "Hypertonic saline drip rate may not exceed 10.0";
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			editorComposite.getCurrentHypertonicSalineDripRateText().setFocus();
			return false;
		}
		if(((HypertonicSalineDecision) decision).getMeanArterialPressure() > 200){
			String errorMessage = "Mean arterial pressure may not exceed 200.";
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			editorComposite.getMAPText().setFocus();
			return false;
		}
		if (((HypertonicSalineDecision) decision).getIntracranialPressure() != null){
		if(((HypertonicSalineDecision) decision).getIntracranialPressure() > 100){
			String errorMessage = "Intracranial pressure may not exceed 100.";
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			editorComposite.getICPText().setFocus();
			return false;
		}}
		if (((HypertonicSalineDecision) decision).getCentralVenousPressure() != null){
		if(((HypertonicSalineDecision) decision).getCentralVenousPressure() > 25){
			String errorMessage = "Central venous pressure may not exceed 25.";
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			editorComposite.getCVPText().setFocus();
			return false;
		}}
		return true;
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
		getDecisionEngine().fireRules((HypertonicSalineDecision) decision);
		decision.setRulesFiredText(getDecisionEngine().getTraceRulesLogger().getEvents());

		// If no advice resulted from running the inference engine
		if (decision.getAdviceText().length() == 0) {
			decision.setAdviceText("No change at this time.");
		}

		// Fire property change so listeners can act
		ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision calculated ...");
	}

	/**
	 * Add the decision to the patient object so that we can consider the
	 * decision retrospectively. This would be the routine that preserves all
	 * ludicrous human behavior for posterity to enjoy.
	 */
	protected void chartDecision() {
		log.info("Preparing to chart the decision");
		if (instantiateDecisionObject()) {
			if (decision.getUserAction().equals(ClinicalDecision.PENDING)) {
				decision.setUserAction(ClinicalDecision.BACKCHART);
			}
			this.getPatient().addDecision(decision);
			log.debug("Decision added to decisions collection object in the patient");
		

		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
		try {
			log.debug("About to get a hibernate transaction to start the charting");
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL);
			patientDAO.getSession().beginTransaction();
			patientDAO.updatePatientValues(this.getPatient());
			patientDAO.getSession().flush();
			patientDAO.getSession().getTransaction().commit();
			patientDAO.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
			log.debug("Transaction committed");
		} catch (HibernateException e) {
			log.debug("Charting failed with Hibernate exception: " + e);
			e.printStackTrace();
		}

		ApplicationControllers.getDecisionController().fireDecisionStored(decision);
		this.getEditorSite().getActionBars().getStatusLineManager().setMessage("Decision stored in database ...");
		decision = null;
		setDecisionFiredFlagOff();
		editorComposite.enableCorrectButtonCombination();
	}}

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
			decision = new HypertonicSalineDecision(decisionTime);
			populateDomainSpecificDecisionFieldsFromEditor();
			populateClinicalDecisionFromPatient();
			if (ApplicationControllers.getUserController().getCurrentUser() != null) {
				decision.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());
			}
			//return validateHypertonicSalineDecisionWithHibernate();
			return checkValuesKludgeRoutine();
		}
		return checkValuesKludgeRoutine();
	}

	private void prepareObservationDateTimeValues() {
		decisionTime = new GregorianCalendar(editorComposite.getObsDateWidget().getYear(), editorComposite
				.getObsDateWidget().getMonth(), editorComposite.getObsDateWidget().getDay(), editorComposite
				.getObsTimeWidget().getHours(), editorComposite.getObsTimeWidget().getMinutes());
	}

	private void populateDomainSpecificDecisionFieldsFromEditor() {
		if(editorComposite.getCurrentHypertonicSalineDripRateText().getText().length()>0){
					((HypertonicSalineDecision) decision).setCurrentHypertonicSalineDripRate(Double.valueOf(editorComposite
				.getCurrentHypertonicSalineDripRateText().getText()));
		} 
		if(editorComposite.getCVPText().getText().length()>0){
		((HypertonicSalineDecision) decision).setCentralVenousPressure(Integer.valueOf(editorComposite.getCVPText()
				.getText()));}
		if(editorComposite.getMAPText().getText().length()>0){
		((HypertonicSalineDecision) decision).setMeanArterialPressure(Integer.valueOf(editorComposite.getMAPText()
				.getText()));}
		if(editorComposite.getICPText().getText().length()>0){
		((HypertonicSalineDecision) decision).setIntracranialPressure(Integer.valueOf(editorComposite.getICPText()
				.getText()));}
	}

	private void populateClinicalDecisionFromPatient() {
		decision.setPatient(getPatient());
		decision.setPatientAgeDays(getPatient().getDeltaAgeDays(decisionTime));
		decision.setPatientHeight(getPatient().getHeight());
		decision.setPatientWeight(getPatient().getWeight());
		decision.userAction = ClinicalDecision.PENDING;
		populateLabValuesFromPatient();
		((HypertonicSalineDecision) decision).setPreviousObservationTime(populatePreviousObservationTime());
	}

	private GregorianCalendar populatePreviousObservationTime() {
		IPatientDecisionController patientDecisionController = new HypertonicPatientDecisions();
		return patientDecisionController.retrievePreviousObservationDateTime(getPatient());
	}

	private void populateLabValuesFromPatient() {
		SerumOsmolalityLaboratoryResult recentOsm, previousOsm;
		SerumSodiumLaboratoryResult recentNA, previousNA;
		//IPatientLaboratoryController patientLabController = new HypertonicPatientLaboratories();

		recentOsm = patientLabController.retrieveCurrentOsmolalityLabResult(getPatient());
		previousOsm = patientLabController.retrievePreviousOsmolalityLabResult(getPatient());
		recentNA = patientLabController.retrieveCurrentSodiumLabResult(getPatient());
		previousNA = patientLabController.retrievePreviousSodiumLabResult(getPatient());
		if (recentNA != HypertonicSalineDecision.NOCURRENTSODIUM) {
			((HypertonicSalineDecision) decision).setCurrentSodiumValue(recentNA.getNumericResult());
			((HypertonicSalineDecision) decision).setCurrentSodiumDateTime(recentNA.getTimeOfSpecimenCollection());
		} else {
			//TODO Concerned here because the default blank value is -1 but in the rules we might forget this.
		}
		if (previousNA != HypertonicSalineDecision.NOPREVIOUSSODIUM) {
			((HypertonicSalineDecision) decision).setPreviousSodiumValue(previousNA.getNumericResult());
			((HypertonicSalineDecision) decision).setPreviousSodiumDateTime(previousNA.getTimeOfSpecimenCollection());
		} else{
			
		}
		if (recentOsm != HypertonicSalineDecision.NOCURRENTOSMOLALITY) {
			((HypertonicSalineDecision) decision).setCurrentOsmolalityValue(recentOsm.getNumericResult());
			((HypertonicSalineDecision) decision).setCurrentOsmolalityDateTime(recentOsm.getTimeOfSpecimenCollection());
		} else {
			
		}
		if (previousOsm != HypertonicSalineDecision.NOPREVIOUSOSMOLALITY) {
			((HypertonicSalineDecision) decision).setPreviousOsmolalityValue(previousOsm.getNumericResult());
			((HypertonicSalineDecision) decision).setPreviousOsmolalityDateTime(previousOsm
					.getTimeOfSpecimenCollection());
		} else {
			
		}
	}

	@SuppressWarnings("unused")
	private boolean validateHypertonicSalineDecisionWithHibernate() {
		//TODO This routine fails - no invalid messages come back
		// Need to figure this out.
		salineValidator = new ClassValidator<HypertonicSalineDecision>(
				HypertonicSalineDecision.class);
		validationMessages = salineValidator.getInvalidValues((HypertonicSalineDecision) decision);
		if (validationMessages.length == 0) {
			return true;
		} else {
			String errorMessage = HibernateValidationHandler.handleValidationMessages(validationMessages);
			MessageDialog.openError(null, "Error in decision data entry", errorMessage);
			decision = null;
			return false;
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
		if (e.widget.equals(editorComposite.getObsDateWidget()) || e.widget.equals(editorComposite.getObsTimeWidget())) {
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
		if (e.widget.equals(editorComposite.getObsDateWidget())) {
			// TODO Check to make sure birthdate preceded the observation date
			// and put up a dialog if this is not the case, and then clear
			// the observation date field so buttons will behave correctly.
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void laboratoryChanged() {
		System.out.println("Laboratory changed reached");
		populateOsmolalityFields();
		populateSerumSodiumFields();
		
	}
}
