package edu.utah.cdmcc.editors;

import java.text.DecimalFormat;
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
import patient.laboratory.controller.IPatientLaboratoryController;
import patient.laboratory.controller.VentilatorPediatricPatientDecisions;
import patient.laboratory.controller.VentilatorPediatricPatientLaboratories;
import ventilator.decision.object.VentilatorFieldsAddedToDomainComposite;
import ventilator.decision.object.VentilatorPediatricDecision;
import ventilator.decision.object.VentilatorPediatricGetPassiveDecisionDialog;
import core.dao.DAOFactory;
import core.dao.IPatientDAO;
import core.decision.object.ClinicalDecision;
import core.drools.utilities.RulesEngineException;
import core.hibernate.HibernateValidationHandler;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.listener.interfaces.IGuiListener;
import core.multiple.object.controllers.IPatientDecisionController;
import drools.engine.KnowledgeEngine;
import edu.utah.cdmcc.decisionsupport.application.core.Activator;
import edu.utah.cdmcc.decisionsupport.controller.core.ApplicationControllers;
import edu.utah.cdmcc.listeners.ILaboratoryListener;
import edu.utah.cdmcc.preferences.StudyPreferenceConstants;
import edu.utah.cdmcc.views.DecisionCalculatorEditorTemplate;

/**
 * 
 * VentilatorDecisionCalculatorEditor is part of the Utah Decision Support
 * Tools. This is specific to pediatric ventilators; it currently contains code
 * to fire the rules engine (Drools). This now extends a generic template.
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
public class VentilatorDecisionCalculatorEditorBasedOnTemplate extends DecisionCalculatorEditorTemplate implements
		ILaboratoryListener, IGuiListener {
	public KnowledgeEngine decisionEngine;
	private GregorianCalendar decisionTime;
	protected StyledText emptyTrace;
	public static String ID = "edu.utah.cdmcc.decisionsupport.ventilator.pediatric.editor.decisionCalculator";
	private static Logger log = Logger.getLogger(VentilatorDecisionCalculatorEditorBasedOnTemplate.class);
	private VentilatorFieldsAddedToDomainComposite editorComposite;
	private ClassValidator<VentilatorPediatricDecision> ventilationValidator;
	private InvalidValue[] validationMessages;
	private IPatientLaboratoryController patientLabController = new VentilatorPediatricPatientLaboratories();

	public VentilatorDecisionCalculatorEditorBasedOnTemplate() {
		super();
	}

	/**
	 * Create the editor for ventilator decisions, based on the appropriate
	 * composite. Add a whole shitload of listeners, etc. which I hope to
	 * replace with data binding routines to simplify the code.
	 * <p>
	 * But this works, so hey, why complain?
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setupGeneralDecisionPart(parent);
		setupDomainSpecificPart();
		populateBloodGasFields();
		populateTidalVolumeCalculatedLabels(); //TODO this is NOT the right place for this because will not ever exist
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
		editorComposite.addGuiFieldsChangeListener(this);
		addContextHelp();
	}

	private void createEditorComposite(final Composite parent) {
		editorComposite = new VentilatorFieldsAddedToDomainComposite(parent, SWT.NULL);
	}

	private void addContextHelp() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(editorComposite, "edu.utah.cdmcc.help.glucose.editorView");
		// TODO need to fix this plug in ID
	}

	private void setupDomainSpecificPart() {

		editorComposite.getBtnNewAbg().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
				try {
					handlerService.executeCommand("edu.utah.cdmcc.commands.abglab", null);
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotDefinedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotEnabledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotHandledException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
	}

	private void listenToObservationDateTimeChanges() {
		editorComposite.getObservationDate().addSelectionListener(this);
		editorComposite.getObservationTime().addSelectionListener(this);
	}

	private void listenToButtonSelection() {
		editorComposite.getDecisionButton().addSelectionListener(this);
		editorComposite.getChartButton().addSelectionListener(this);
		editorComposite.getAcceptButton().addSelectionListener(this);
		editorComposite.getDeclineButton().addSelectionListener(this);
		editorComposite.getBtnNewAbg().addSelectionListener(this);
	}
	
	private void listenToTidalVolumeChanges(){
		//TODO need to figure out how to listen for changes in tidal volume on the composite
		// so that the editor can fix the calculated cc/kg labels on the composite which
		// requires the patient's weight.
	}

	@Override
	protected void setDecisionFiredFlagOff() {
		editorComposite.setDecisionFiredFlag(false);
	}

	// @Override
	// public void setFocus() {
	// if (editorComposite != null && !editorComposite.isDisposed()) {
	// editorComposite.getObsDateWidget().setFocus();
	// }
	// }

	public void dispose() {
		ApplicationControllers.getPatientController().removePatientsListener(this);
		ApplicationControllers.getDecisionController().removeDecisionFiredListener(editorComposite);
		ApplicationControllers.getDatabaseController().removeDatabaseChangedListener(this);
		ApplicationControllers.getLaboratoryController().removeLaboratoryChangedListener(this);
		editorComposite.removeGuiFieldsChangeListener(this);
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
			VentilatorPediatricGetPassiveDecisionDialog dialog = new VentilatorPediatricGetPassiveDecisionDialog(
					getSite().getShell(), (VentilatorPediatricDecision) decision);
			dialog.open();
			ApplicationControllers.getDecisionController().fireDecisionChanged(decision);
		} else {
			if (mode == true && instantiateDecisionObject()) {
				// Fire rules engine for decision
				log.debug("Decision object successfully instantiated");
				this.getEditorSite().getActionBars().getStatusLineManager()
						.setMessage("Decision object instantiated ...");
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
		// // This is a kludge because I cannot figure out how to make Hibernate
		// validation
		// // work on the HypertonicDecisionObject.
		// if(((VentilatorDecision)
		// decision).getCurrentHypertonicSalineDripRate() > 10){
		// String errorMessage =
		// "Hypertonic saline drip rate may not exceed 10.0";
		// MessageDialog.openError(null, "Error in decision data entry",
		// errorMessage);
		// editorComposite.getCurrentHypertonicSalineDripRateText().setFocus();
		// return false;
		// }
		// if(((VentilatorDecision) decision).getMeanArterialPressure() > 200){
		// String errorMessage = "Mean arterial pressure may not exceed 200.";
		// MessageDialog.openError(null, "Error in decision data entry",
		// errorMessage);
		// editorComposite.getMAPText().setFocus();
		// return false;
		// }
		// if (((VentilatorDecision) decision).getIntracranialPressure() !=
		// null){
		// if(((VentilatorDecision) decision).getIntracranialPressure() > 100){
		// String errorMessage = "Intracranial pressure may not exceed 100.";
		// MessageDialog.openError(null, "Error in decision data entry",
		// errorMessage);
		// editorComposite.getICPText().setFocus();
		// return false;
		// }}
		// if (((VentilatorDecision) decision).getCentralVenousPressure() !=
		// null){
		// if(((VentilatorDecision) decision).getCentralVenousPressure() > 25){
		// String errorMessage = "Central venous pressure may not exceed 25.";
		// MessageDialog.openError(null, "Error in decision data entry",
		// errorMessage);
		// editorComposite.getCVPText().setFocus();
		// return false;
		// }}
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
		getDecisionEngine().fireRules((VentilatorPediatricDecision) decision);
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
		}
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

			// prepareObservationDateTimeValues();
			decision = new VentilatorPediatricDecision(decisionTime);
			// populateDomainSpecificDecisionFieldsFromEditor();
			populateClinicalDecisionFromPatient();
			if (ApplicationControllers.getUserController().getCurrentUser() != null) {
				decision.setAccountName(ApplicationControllers.getUserController().getCurrentUser().getAccountName());
			}
			// return validateHypertonicSalineDecisionWithHibernate();
			return checkValuesKludgeRoutine();
		}
		return checkValuesKludgeRoutine();
	}

	// private void prepareObservationDateTimeValues() {
	// decisionTime = new
	// GregorianCalendar(editorComposite.getObsDateWidget().getYear(),
	// editorComposite
	// .getObsDateWidget().getMonth(),
	// editorComposite.getObsDateWidget().getDay(), editorComposite
	// .getObsTimeWidget().getHours(),
	// editorComposite.getObsTimeWidget().getMinutes());
	// }
	//
	// private void populateDomainSpecificDecisionFieldsFromEditor() {
	// if(editorComposite.getCurrentHypertonicSalineDripRateText().getText().length()>0){
	// ((VentilatorDecision)
	// decision).setCurrentHypertonicSalineDripRate(Double.valueOf(editorComposite
	// .getCurrentHypertonicSalineDripRateText().getText()));
	// }
	// if(editorComposite.getCVPText().getText().length()>0){
	// ((VentilatorDecision)
	// decision).setCentralVenousPressure(Integer.valueOf(editorComposite.getCVPText()
	// .getText()));}
	// if(editorComposite.getMAPText().getText().length()>0){
	// ((VentilatorDecision)
	// decision).setMeanArterialPressure(Integer.valueOf(editorComposite.getMAPText()
	// .getText()));}
	// if(editorComposite.getICPText().getText().length()>0){
	// ((VentilatorDecision)
	// decision).setIntracranialPressure(Integer.valueOf(editorComposite.getICPText()
	// .getText()));}
	// }

	private void populateClinicalDecisionFromPatient() {
		decision.setPatient(getPatient());
		decision.setPatientAgeDays(getPatient().getDeltaAgeDays(decisionTime));
		decision.setPatientHeight(getPatient().getHeight());
		decision.setPatientWeight(getPatient().getWeight());
		decision.userAction = ClinicalDecision.PENDING;
		populateDecisionWithLabValuesFromPatient();
		// ((VentilatorDecision)
		// decision).setPreviousObservationTime(populatePreviousObservationTime());
	}

	private void populateBloodGasFields() {
		ArterialBloodGasLaboratoryResult result = patientLabController
				.retrieveCurrentArterialBloodGasResult(getPatient());
		populatePhFields(result);
		populatePco2Fields(result);
		populatePo2Fields(result);
	}

	private void populatePo2Fields(ArterialBloodGasLaboratoryResult result) {
		if (result != null) {
			editorComposite.getLblLastDate_PaO2().setText(result.getTimeOfABG());
			editorComposite.getPaO2Text().setText(result.getPo2String());
			editorComposite.getLblLastDate_PaO2_2().setText(result.getTimeOfABG());
			editorComposite.getPaO2Text_2().setText(result.getPo2String());
			editorComposite.getLblLastDate_PaO2_3().setText(result.getTimeOfABG());
			editorComposite.getPaO2Text_3().setText(result.getPo2String());
			editorComposite.getLblLastDate_PaO2_4().setText(result.getTimeOfABG());
			editorComposite.getPaO2Text_4().setText(result.getPo2String());
		} else {
			editorComposite.getLblLastDate_PaO2().setText("Not available");
			editorComposite.getPaO2Text().setText("Not available");
			editorComposite.getLblLastDate_PaO2_2().setText("Not available");
			editorComposite.getPaO2Text_2().setText("Not available");
			editorComposite.getLblLastDate_PaO2_3().setText("Not available");
			editorComposite.getPaO2Text_3().setText("Not available");
			editorComposite.getLblLastDate_PaO2_4().setText("Not available");
			editorComposite.getPaO2Text_4().setText("Not available");
		}

	}

	private void populatePco2Fields(ArterialBloodGasLaboratoryResult result) {
		if (result != null) {
			editorComposite.getLblLastDate_PCO2().setText(result.getTimeOfABG());
			editorComposite.getpCO2Text().setText(result.getPco2String());
			editorComposite.getLblLastDate_PCO2_2().setText(result.getTimeOfABG());
			editorComposite.getpCO2Text_2().setText(result.getPco2String());
			editorComposite.getLblLastDate_PCO2_3().setText(result.getTimeOfABG());
			editorComposite.getpCO2Text_3().setText(result.getPco2String());
			editorComposite.getLblLastDate_PCO2_4().setText(result.getTimeOfABG());
			editorComposite.getpCO2Text_4().setText(result.getPco2String());
		} else {
			editorComposite.getLblLastDate_PCO2().setText("Not available");
			editorComposite.getpCO2Text().setText("Not available");
			editorComposite.getLblLastDate_PCO2_2().setText("Not available");
			editorComposite.getpCO2Text_2().setText("Not available");
			editorComposite.getLblLastDate_PCO2_3().setText("Not available");
			editorComposite.getpCO2Text_3().setText("Not available");
			editorComposite.getLblLastDate_PCO2_4().setText("Not available");
			editorComposite.getpCO2Text_4().setText("Not available");
		}

	}

	private void populatePhFields(ArterialBloodGasLaboratoryResult result) {
		if (result != null) {
			editorComposite.getLblLastDate_pH().setText(result.getTimeOfABG());
			editorComposite.getpHText().setText(result.getPhString());
			editorComposite.getLblLastDate_pH_2().setText(result.getTimeOfABG());
			editorComposite.getpHText_2().setText(result.getPhString());
			editorComposite.getLblLastDate_pH_3().setText(result.getTimeOfABG());
			editorComposite.getpHText_3().setText(result.getPhString());
			editorComposite.getLblLastDate_pH_4().setText(result.getTimeOfABG());
			editorComposite.getpHText_4().setText(result.getPhString());
		} else {
			editorComposite.getLblLastDate_pH().setText("Not available");
			editorComposite.getpHText().setText("Not available");
			editorComposite.getLblLastDate_pH_2().setText("Not available");
			editorComposite.getpHText_2().setText("Not available");
			editorComposite.getLblLastDate_pH_3().setText("Not available");
			editorComposite.getpHText_3().setText("Not available");
			editorComposite.getLblLastDate_pH_4().setText("Not available");
			editorComposite.getpHText_4().setText("Not available");
		}
	}

	private void populateTidalVolumeCalculatedLabels(){
		Double patientWeight = getPatient().getWeight();
		DecimalFormat formatter = new DecimalFormat("###.##");
		if (editorComposite.getTidalVolumeText().getText().length()>0){
			Double tidalPerKg = new Double(editorComposite.getTidalVolumeText().getText())/patientWeight;
			editorComposite.getTidalVolumePerKgLabel().setText((formatter.format(tidalPerKg)) + " ml/kg");
			editorComposite.getTidalVolumePerKgLabel2().setText((formatter.format(tidalPerKg)) + " ml/kg");
			editorComposite.getTidalVolumePerKgLabel4().setText((formatter.format(tidalPerKg)) + " ml/kg");
		} else {
			editorComposite.getTidalVolumePerKgLabel().setText("Calculated ml/kg");
			editorComposite.getTidalVolumePerKgLabel2().setText("Calculated ml/kg");
			editorComposite.getTidalVolumePerKgLabel4().setText("Calculated ml/kg");
		}
	}
	
	private GregorianCalendar populatePreviousObservationTime() {
		IPatientDecisionController patientDecisionController = new VentilatorPediatricPatientDecisions();
		return patientDecisionController.retrievePreviousObservationDateTime(getPatient());
	}

	private void populateDecisionWithLabValuesFromPatient() {
		// SerumOsmolalityLaboratoryResult recentOsm, previousOsm;
		// SodiumLaboratoryResult recentNA, previousNA;
		// IPatientLaboratoryController patientLabController = new
		// VentilatorPatientLaboratories();

		// recentOsm =
		// patientLabController.retrieveCurrentOsmolalityLabResult(getPatient());
		// previousOsm =
		// patientLabController.retrievePreviousOsmolalityLabResult(getPatient());
		// recentNA =
		// patientLabController.retrieveCurrentSodiumLabResult(getPatient());
		// previousNA =
		// patientLabController.retrievePreviousSodiumLabResult(getPatient());
		// if (recentNA != VentilatorDecision.NOCURRENTSODIUM) {
		// ((VentilatorDecision)
		// decision).setCurrentSodiumValue(recentNA.getNumericResult());
		// ((VentilatorDecision)
		// decision).setCurrentSodiumDateTime(recentNA.getTimeOfSpecimenCollection());
		// }
		// if (previousNA != VentilatorDecision.NOPREVIOUSSODIUM) {
		// ((VentilatorDecision)
		// decision).setPreviousSodiumValue(previousNA.getNumericResult());
		// ((VentilatorDecision)
		// decision).setPreviousSodiumDateTime(previousNA.getTimeOfSpecimenCollection());
		// }
		// if (recentOsm != VentilatorDecision.NOCURRENTOSMOLALITY) {
		// ((VentilatorDecision)
		// decision).setCurrentOsmolalityValue(recentOsm.getNumericResult());
		// ((VentilatorDecision)
		// decision).setCurrentOsmolalityDateTime(recentOsm.getTimeOfSpecimenCollection());
		// }
		// if (previousOsm != VentilatorDecision.NOPREVIOUSOSMOLALITY) {
		// ((VentilatorDecision)
		// decision).setPreviousOsmolalityValue(previousOsm.getNumericResult());
		// ((VentilatorDecision)
		// decision).setPreviousOsmolalityDateTime(previousOsm
		// .getTimeOfSpecimenCollection());
		// }
	}

	@SuppressWarnings("unused")
	private boolean validateHypertonicSalineDecisionWithHibernate() {
		// TODO This routine fails - no invalid messages come back
		// Need to figure this out.
		ventilationValidator = new ClassValidator<VentilatorPediatricDecision>(VentilatorPediatricDecision.class);
		validationMessages = ventilationValidator.getInvalidValues((VentilatorPediatricDecision) decision);
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
		if (e.widget.equals(editorComposite.getObservationDate())
				|| e.widget.equals(editorComposite.getObservationTime())) {
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
		if (e.widget.equals(editorComposite.getObservationDate())) {
			// TODO Check to make sure birthdate preceded the observation date
			// and put up a dialog if this is not the case, and then clear
			// the observation date field so buttons will behave correctly.
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void laboratoryChanged() {
		System.out.println("Laboratory changed reached");
		populateBloodGasFields();
	}

	@Override
	public void setFocus() {
		if (editorComposite != null && !editorComposite.isDisposed()) {
			editorComposite.getObservationDate().setFocus();

		}
	}


	public void guiFieldsChanged(Composite composite) {
		populateTidalVolumeCalculatedLabels();
		
	}

}