package glucose.decision.object;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * GlucoseDecisionPropertySource is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         November 26, 2006 15:30 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2006. All rights reserved.
 *         <P>
 *         Purpose of the class: Provides glucose decision object to properties
 *         view. Keeps decision object clean of this code.
 *         <P>
 * 
 */
public class GlucoseDecisionPropertySource implements IPropertySource2 {

	private static final String PROPERTY_TIMESTAMP = "decision.decisionTimeStamp";
	private static final String PROPERTY_OBSERVATION_DATE = "decision.observationDate";
	private static final String PROPERTY_PATIENT_WEIGHT = "decision.patientWeight";
	private static final String PROPERTY_PATIENT_HEIGHT = "decision.patientHeight";
	private static final String PROPERTY_PATIENT_AGE_DAYS = "decision.patientAgeDays";
	private static final String PROPERTY_ADVICE_TEXT = "decision.adviceText";
	private static final String PROPERTY_RULES_FIRED_TEXT = "decision.rulesFiredText";
	private static final String PROPERTY_RATIONALE_TEXT = "decision.rationaleText";
	private static final String PROPERTY_DECLINE_COMMENT = "decision.declineComment";
	private static final String PROPERTY_ACCEPT_COMMENT = "decision.acceptComment";
	private static final String PROPERTY_OTHER_COMMENT = "decision.otherComment";
	private static final String PROPERTY_USER_ACTION = "decision.userAction";
	private static final String PROPERTY_MINUTES_NEXT_EVAL = "decision.minutesToNextEvaluation";
	private static final String PROPERTY_VALID = "decision.valid";
	private static final String PROPERTY_PREVIOUS_GLUCOSE = "decision.previousGlucoseConcentration";
	private static final String PROPERTY_PREVIOUS_GLUCOSE_TIME = "decision.previousObservationTime";
	private static final String PROPERTY_GLUCOSE = "decision.serumGlucoseConcentration";
	private static final String PROPERTY_INSULIN = "decision.currentInsulinDripRate";
	private static final String PROPERTY_CARBOHYDRATE_STATUS = "decision.carbohydrateStatus";
	private static final String PROPERTY_REC_INSULIN_DRIP = "decision.recommendedInsulinDripRate";
	private static final String PROPERTY_REC_INSULIN_BOLUS = "decision.recommendedInsulinBolus";
	private static final String PROPERTY_REC_GLUCOSE_BOLUS = "decision.recommendedGlucoseBolus";
	private static final String DECISIONTIME = "1.  Decision Time";
	private static final String PATIENT = "2.  Patient Information";
	private static final String CLINICAL = "3.  Clinical Information";
	private static final String DECISION = "4.  Decision Generation";
	private static final String USER = "5.  User Comments";
	private static final String FLAGS = "6.  Validity Flag";
	private static final String[] CARBOHYDRATE_STATUS = { "",
		"No decrease",
		"Decreased or off",
		"Increased or started" };
	private IPropertyDescriptor[] propertyDescriptors;
	private GlucoseDecision decision;

	/**
	 * Creates PropertySource for the GlucoseDecision object. Called by the
	 * GlucoseDecision object method getAdaptor().
	 * 
	 * @param decision
	 */
	public GlucoseDecisionPropertySource(final GlucoseDecision decision) {
		this.decision = decision;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			PropertyDescriptor timestampDescriptor = new PropertyDescriptor(
					PROPERTY_TIMESTAMP, "Record timestamp:");
			timestampDescriptor.setCategory(DECISIONTIME);
			timestampDescriptor
					.setDescription("Timestamp of the record in the database.");
			PropertyDescriptor obsDateDescriptor = new PropertyDescriptor(
					PROPERTY_OBSERVATION_DATE, "Observation date:");
			obsDateDescriptor.setCategory(DECISIONTIME);
			obsDateDescriptor.setDescription("Observation date (clinical)");
			PropertyDescriptor ageDescriptor = new PropertyDescriptor(
					PROPERTY_PATIENT_AGE_DAYS, "Age (days):");
			ageDescriptor.setCategory(PATIENT);
			ageDescriptor.setDescription("Age of patient at time of decision.");
			PropertyDescriptor weightDescriptor = new PropertyDescriptor(
					PROPERTY_PATIENT_WEIGHT, "Weight (kg):");
			weightDescriptor.setCategory(PATIENT);
			weightDescriptor
					.setDescription("Weight of patient at time of decision.");
			PropertyDescriptor heightDescriptor = new PropertyDescriptor(
					PROPERTY_PATIENT_HEIGHT, "Height (cm):");
			heightDescriptor.setCategory(PATIENT);
			heightDescriptor
					.setDescription("Height of patient at time of decision.");
			PropertyDescriptor glucoseDescriptor = new PropertyDescriptor(
					PROPERTY_GLUCOSE, "Current glucose:");
			glucoseDescriptor.setCategory(CLINICAL);
			glucoseDescriptor
					.setDescription("Current serum glucose concentration.");
			PropertyDescriptor insulinDescriptor = new PropertyDescriptor(
					PROPERTY_INSULIN, "Insulin drip:");
			insulinDescriptor.setCategory(CLINICAL);
			insulinDescriptor.setDescription("Current insulin drip rate.");
			PropertyDescriptor prevGlucoseDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_GLUCOSE, "Previous glucose:");
			prevGlucoseDescriptor.setCategory(CLINICAL);
			prevGlucoseDescriptor
					.setDescription("Previous serum glucose concentration.");
			PropertyDescriptor prevDateDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_GLUCOSE_TIME, "Previous date:");
			prevDateDescriptor.setCategory(CLINICAL);
			prevDateDescriptor
					.setDescription("Date and time of previous serum glucose concentration.");
			PropertyDescriptor carboDescriptor = new PropertyDescriptor(
					PROPERTY_CARBOHYDRATE_STATUS, "Carbohydrate status:");
			carboDescriptor.setCategory(CLINICAL);
			carboDescriptor
					.setDescription("Carbohydrate status in previous hour.");
			PropertyDescriptor adviceDescriptor = new PropertyDescriptor(
					PROPERTY_ADVICE_TEXT, "Advice given:");
			adviceDescriptor.setCategory(DECISION);
			adviceDescriptor
					.setDescription("Advice generated by the rules engine.");
			PropertyDescriptor reasonDescriptor = new PropertyDescriptor(
					PROPERTY_RATIONALE_TEXT, "Explanation given:");
			reasonDescriptor.setCategory(DECISION);
			reasonDescriptor
					.setDescription("Explanation of the advice generated by the rules engine.");
			PropertyDescriptor rulesDescriptor = new PropertyDescriptor(
					PROPERTY_RULES_FIRED_TEXT, "Rules activated:");
			rulesDescriptor.setCategory(DECISION);
			rulesDescriptor
					.setDescription("Rules activated by the rules engine.");
			PropertyDescriptor recInsulinDripDescriptor = new PropertyDescriptor(
					PROPERTY_REC_INSULIN_DRIP, "Recommended insulin drip:");
			recInsulinDripDescriptor.setCategory(DECISION);
			recInsulinDripDescriptor
					.setDescription("Recommended dose of insulin drip.");
			PropertyDescriptor recInsulinBolusDescriptor = new PropertyDescriptor(
					PROPERTY_REC_INSULIN_BOLUS, "Recommended insulin bolus:");
			recInsulinBolusDescriptor.setCategory(DECISION);
			recInsulinBolusDescriptor
					.setDescription("Recommended dose of insulin bolus.");
			PropertyDescriptor recGlucoseBolusDescriptor = new PropertyDescriptor(
					PROPERTY_REC_GLUCOSE_BOLUS, "Recommended glucose bolus:");
			recGlucoseBolusDescriptor.setCategory(DECISION);
			recGlucoseBolusDescriptor
					.setDescription("Recommended dose of glucose bolus.");
			PropertyDescriptor recTimeNextDescriptor = new PropertyDescriptor(
					PROPERTY_MINUTES_NEXT_EVAL,
					"Recommended minutes before next measurement:");
			recTimeNextDescriptor.setCategory(DECISION);
			recTimeNextDescriptor
					.setDescription("Recommended time to do the next measurement (minutes from time of current glucose measurement).");
			PropertyDescriptor userActionDescriptor = new PropertyDescriptor(
					PROPERTY_USER_ACTION, "User action:");
			userActionDescriptor.setCategory(USER);
			userActionDescriptor
					.setDescription("User action in response to decision.");
			PropertyDescriptor declineDescriptor = new PropertyDescriptor(
					PROPERTY_DECLINE_COMMENT, "Declination comment:");
			declineDescriptor.setCategory(USER);
			declineDescriptor
					.setDescription("Comment entered by user when declining instruction.");
			PropertyDescriptor acceptDescriptor = new PropertyDescriptor(
					PROPERTY_ACCEPT_COMMENT, "Acceptance comment:");
			acceptDescriptor.setCategory(USER);
			acceptDescriptor
					.setDescription("Comment entered by user when accepting instruction.");
			PropertyDescriptor otherDescriptor = new PropertyDescriptor(
					PROPERTY_OTHER_COMMENT, "Other comment:");
			otherDescriptor.setCategory(USER);
			otherDescriptor.setDescription("Other comment entered by user.");
			PropertyDescriptor validDescriptor = new PropertyDescriptor(
					PROPERTY_VALID, "Record valid flag:");
			validDescriptor.setCategory(FLAGS);
			validDescriptor
					.setDescription("Denotes if record has been marked as invalid.");
			propertyDescriptors = new IPropertyDescriptor[] {
					timestampDescriptor, obsDateDescriptor, ageDescriptor,
					weightDescriptor, heightDescriptor, glucoseDescriptor,
					insulinDescriptor, prevGlucoseDescriptor,
					prevDateDescriptor, carboDescriptor, adviceDescriptor,
					reasonDescriptor, rulesDescriptor,
					recInsulinDripDescriptor, recInsulinBolusDescriptor,
					recGlucoseBolusDescriptor, recTimeNextDescriptor,
					userActionDescriptor, declineDescriptor, acceptDescriptor,
					otherDescriptor, validDescriptor };
		}
		return propertyDescriptors;
	}

	public Object getPropertyValue(final Object id) {
		if (id.equals(PROPERTY_TIMESTAMP)) {
			return decision.getDecisionTimeStamp().getTime().toString();
		}
		if (id.equals(PROPERTY_OBSERVATION_DATE)) {
			return decision.getObservationDate().getTime().toString();
		}
		if (id.equals(PROPERTY_PATIENT_AGE_DAYS)) {
			return ((Integer) decision.getPatientAgeDays()).toString();
		}
		if (id.equals(PROPERTY_PATIENT_WEIGHT)) {
			return decision.getPatientWeight().toString();
		}
		if (id.equals(PROPERTY_PATIENT_HEIGHT)) {
			return decision.getPatientHeight().toString();
		}
		if (id.equals(PROPERTY_GLUCOSE)) {
			return (decision.getSerumGlucoseConcentration()).toString();
		}
		if (id.equals(PROPERTY_INSULIN)) {
			return ((Double)decision.getCurrentInsulinDripRate()).toString();
		}
		if (id.equals(PROPERTY_PREVIOUS_GLUCOSE)) {
			if (decision.getPreviousGlucoseConcentration() != GlucoseDecision.NOPREVIOUSGLUCOSE){
				return (decision.getPreviousGlucoseConcentration()).toString();
			} else return "No previous value";			
		}
		if (id.equals(PROPERTY_PREVIOUS_GLUCOSE_TIME)) {
			if (!decision.getPreviousObservationTime().equals(GlucoseDecision.NOPREVIOUSOBSERVATIONDATE)){
				return decision.getPreviousObservationTime().getTime().toString();
			} else return "No previous date";		
		}
		if (id.equals(PROPERTY_CARBOHYDRATE_STATUS)) {
			return (CARBOHYDRATE_STATUS[decision.getCarbohydrateStatus()]);
		}
		if (id.equals(PROPERTY_ADVICE_TEXT)) {
			return decision.getAdviceText();
		}
		if (id.equals(PROPERTY_RATIONALE_TEXT)) {
			return decision.getRationaleText();
		}
		if (id.equals(PROPERTY_RULES_FIRED_TEXT)) {
			return decision.getRulesFiredText();
		}
		if (id.equals(PROPERTY_REC_INSULIN_DRIP)) {
			return decision.getRecommendedInsulinDripRate().toString();
		}
		if (id.equals(PROPERTY_REC_INSULIN_BOLUS)) {
			return decision.getRecommendedInsulinBolus().toString();
		}
		if (id.equals(PROPERTY_REC_GLUCOSE_BOLUS)) {
			return decision.getRecommendedGlucoseBolus().toString();
		}
		if (id.equals(PROPERTY_MINUTES_NEXT_EVAL)) {
			return decision.getMinutesToNextEvaluation();
		}
		if (id.equals(PROPERTY_USER_ACTION)) {
			return decision.getUserAction();
		}
		if (id.equals(PROPERTY_DECLINE_COMMENT)) {
			return decision.getDeclineComment();
		}
		if (id.equals(PROPERTY_ACCEPT_COMMENT)) {
			return decision.getAcceptComment();
		}
		if (id.equals(PROPERTY_OTHER_COMMENT)) {
			return decision.getOtherComment();
		}
		if (id.equals(PROPERTY_VALID)) {
			return decision.getValid().toString();
		}
		return null;
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public boolean isPropertyResettable(final Object id) {
		return false;
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public boolean isPropertySet(final Object id) {
		return false;
	}

	/**
	 * Irrelevant at present. Do not call.
	 */
	public void resetPropertyValue(final Object id) {
	}

	/**
	 * Do not call. The decision objects are not editable from
	 * the property view.
	 */
	public void setPropertyValue(final Object id, final Object value) {
	}
}
