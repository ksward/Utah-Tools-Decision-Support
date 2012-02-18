package ventilator.decision.object;


import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * VentilatorPediatricDecisionPropertySource is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         November 26, 2006 15:30 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2006. All rights reserved.
 *         <P>
 *         Purpose of the class: Provides hypertonic decision object to properties
 *         view. Keeps decision object clean of this code.
 *         <P>
 * 
 */
public class VentilatorPediatricDecisionPropertySource implements IPropertySource2 {

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
	private static final String PROPERTY_PREVIOUS_DECISION_DATE = "decision.previousObservationTime";
	private static final String PROPERTY_VALID = "decision.valid";
	private static final String PROPERTY_PREVIOUS_SODIUM = "decision.previousSodiumValue";
	private static final String PROPERTY_PREVIOUS_SODIUM_DATE = "decision.previousSodiumDateTime";
	private static final String PROPERTY_CURRENT_SODIUM = "decision.currentSodiumValue";
	private static final String PROPERTY_CURRENT_SODIUM_DATE = "decision.currentSodiumDateTime";
	private static final String PROPERTY_PREVIOUS_OSMOLALITY = "decision.previousOsmolalityValue";
	private static final String PROPERTY_PREVIOUS_OSMOLALITY_DATE = "decision.previousOsmolalityDateTime";
	private static final String PROPERTY_CURRENT_OSMOLALITY = "decision.currentOsmolalityValue";
	private static final String PROPERTY_CURRENT_OSMOLALITY_DATE = "decision.currentOsmolalityDateTime";
	private static final String PROPERTY_ICP = "decision.intracranialPressure";
	private static final String PROPERTY_MAP = "decision.meanArterialPressure";
	private static final String PROPERTY_CVP = "decision.centralVenousPressure";
	//private static final String PROPERTY_CURRENT_SALINE_RATE = "decision.currentHypertonicSalineDripRate";
	private static final String PROPERTY_REC_SALINE_RATE = "decision.recommendedHypertonicSalineDripRate";
	private static final String PROPERTY_REC_SALINE_BOLUS = "decision.recommendedHypertonicSalineBolus";
	private static final String PROPERTY_REC_MANNITOL_BOLUS = "decision.recommendedMannitolBolus";
	private static final String DECISIONTIME = "1.  Decision Time";
	private static final String PATIENT = "2.  Patient Information";
	private static final String CLINICAL = "3.  Clinical Information";
	private static final String LABORATORY = "4.  Laboratory Values";
	private static final String DECISION = "5.  Decision Generation";
	private static final String USER = "6.  User Comments";
	private static final String FLAGS = "7.  Validity Flag";

	private IPropertyDescriptor[] propertyDescriptors;
	private VentilatorPediatricDecision decision;

	/**
	 * Creates PropertySource for the VentilatorPediatricDecision object. Called by the
	 * VentilatorPediatricDecision object method getAdaptor().
	 * 
	 * @param decision
	 */
	public VentilatorPediatricDecisionPropertySource(final VentilatorPediatricDecision decision) {
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
					PROPERTY_OBSERVATION_DATE, "Decision date:");
			obsDateDescriptor.setCategory(DECISIONTIME);
			obsDateDescriptor.setDescription("Decision date (clinical)");
			PropertyDescriptor prevDateDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_DECISION_DATE, "Previous decision date:");
			prevDateDescriptor.setCategory(DECISIONTIME);
			prevDateDescriptor.setDescription("Date of previous decision.");

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

			PropertyDescriptor mapDescriptor = new PropertyDescriptor(
					PROPERTY_MAP, "Mean arterial pressure:");
			mapDescriptor.setCategory(CLINICAL);
			mapDescriptor
					.setDescription("Mean arterial pressure at time of decision.");
			PropertyDescriptor icpDescriptor = new PropertyDescriptor(
					PROPERTY_ICP, "Intracranial pressure:");
			icpDescriptor.setCategory(CLINICAL);
			icpDescriptor
					.setDescription("Intracranial pressure at time of decision.");
			PropertyDescriptor cvpDescriptor = new PropertyDescriptor(
					PROPERTY_CVP, "Central venous pressure:");
			cvpDescriptor.setCategory(CLINICAL);
			cvpDescriptor
					.setDescription("Central venous pressure at time of decision.");

			PropertyDescriptor currentSodiumDescriptor = new PropertyDescriptor(
					PROPERTY_CURRENT_SODIUM, "Most recent sodium:");
			currentSodiumDescriptor.setCategory(LABORATORY);
			currentSodiumDescriptor
					.setDescription("Most recent value of serum sodium contributing to decision.");

			PropertyDescriptor currentSodiumDateDescriptor = new PropertyDescriptor(
					PROPERTY_CURRENT_SODIUM_DATE, "Date of most recent sodium:");
			currentSodiumDateDescriptor.setCategory(LABORATORY);
			currentSodiumDateDescriptor
					.setDescription("Date of most recent value of serum sodium contributing to decision.");

			PropertyDescriptor currentOsmDescriptor = new PropertyDescriptor(
					PROPERTY_CURRENT_OSMOLALITY, "Most recent osmolality:");
			currentOsmDescriptor.setCategory(LABORATORY);
			currentOsmDescriptor
					.setDescription("Most recent value of serum osmolality contributing to decision.");

			PropertyDescriptor currentOsmDateDescriptor = new PropertyDescriptor(
					PROPERTY_CURRENT_OSMOLALITY_DATE,
					"Date of most recent osmolality:");
			currentOsmDateDescriptor.setCategory(LABORATORY);
			currentOsmDateDescriptor
					.setDescription("Date of most recent value of serum osmolality contributing to decision.");

			PropertyDescriptor prevSodiumDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_SODIUM, "Previous sodium:");
			prevSodiumDescriptor.setCategory(LABORATORY);
			prevSodiumDescriptor
					.setDescription("Previous value of serum sodium contributing to decision.");

			PropertyDescriptor prevSodiumDateDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_SODIUM_DATE, "Date of previous sodium:");
			prevSodiumDateDescriptor.setCategory(LABORATORY);
			prevSodiumDateDescriptor
					.setDescription("Date of previous value of serum sodium contributing to decision.");

			PropertyDescriptor prevOsmDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_OSMOLALITY, "Previous osmolality:");
			prevOsmDescriptor.setCategory(LABORATORY);
			prevOsmDescriptor
					.setDescription("Previous value of serum osmolality contributing to decision.");

			PropertyDescriptor prevOsmDateDescriptor = new PropertyDescriptor(
					PROPERTY_PREVIOUS_OSMOLALITY_DATE,
					"Date of previous osmolality:");
			prevOsmDateDescriptor.setCategory(LABORATORY);
			prevOsmDateDescriptor
					.setDescription("Date of previous value of serum osmolality contributing to decision.");
			
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

			PropertyDescriptor recSalineDripDescriptor = new PropertyDescriptor(
					PROPERTY_REC_SALINE_RATE, "Recommended 3% saline rate:");
			recSalineDripDescriptor.setCategory(DECISION);
			recSalineDripDescriptor
					.setDescription("Recommended new rate of 3% saline:");

			PropertyDescriptor recSalineBolusDescriptor = new PropertyDescriptor(
					PROPERTY_REC_SALINE_BOLUS, "Recommended 3% saline bolus:");
			recSalineBolusDescriptor.setCategory(DECISION);
			recSalineBolusDescriptor
					.setDescription("Recommended amount of 3% saline bolus.");

			PropertyDescriptor recMannitolBolusDescriptor = new PropertyDescriptor(
					PROPERTY_REC_MANNITOL_BOLUS, "Recommended mannitol bolus:");
			recMannitolBolusDescriptor.setCategory(DECISION);
			recMannitolBolusDescriptor
					.setDescription("Recommended amount of mannitol bolus.");

			PropertyDescriptor recTimeNextDescriptor = new PropertyDescriptor(
					PROPERTY_MINUTES_NEXT_EVAL,
					"Recommended minutes before next decision:");
			recTimeNextDescriptor.setCategory(DECISION);
			recTimeNextDescriptor
					.setDescription("Recommended time to do the next decision (minutes from time of current hypertonic saline decision).");

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
					timestampDescriptor, 
					obsDateDescriptor, 
					prevDateDescriptor,
					ageDescriptor,
					weightDescriptor, 
					heightDescriptor,
					mapDescriptor,
					icpDescriptor,
					cvpDescriptor,
					currentSodiumDescriptor,
					currentSodiumDateDescriptor,
					currentOsmDescriptor,
					currentOsmDateDescriptor,
					prevSodiumDescriptor,
					prevSodiumDateDescriptor,
					prevOsmDescriptor,
					prevOsmDateDescriptor,
					adviceDescriptor, 
					reasonDescriptor,
					rulesDescriptor,
					recSalineDripDescriptor,
					recSalineBolusDescriptor,
					recMannitolBolusDescriptor,
					recTimeNextDescriptor, 
					userActionDescriptor,
					declineDescriptor, 
					acceptDescriptor, 
					otherDescriptor,
					validDescriptor };
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
		if (id.equals(PROPERTY_PREVIOUS_DECISION_DATE)) {
			return decision.getPreviousObservationTime().getTime().toString();
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
		if (id.equals(PROPERTY_MAP)){
			return decision.getMeanArterialPressure().toString();
		}
		if (id.equals(PROPERTY_ICP)){
			if(decision.getIntracranialPressure()== VentilatorPediatricDecision.NOVALUEENTERED){
				return("No value entered");
			} else {
				return decision.getIntracranialPressure().toString();
			}
		}
		if (id.equals(PROPERTY_CVP)){
			if(decision.getCentralVenousPressure()==VentilatorPediatricDecision.NOVALUEENTERED){
				return("No value entered");
			} else {
				return decision.getCentralVenousPressure().toString();
			}
		}
		if (id.equals(PROPERTY_CURRENT_SODIUM)){
			return decision.getCurrentSodiumValue().toString();
		}
		if (id.equals(PROPERTY_CURRENT_SODIUM_DATE)){
			return decision.getCurrentSodiumDateTime().getTime().toString();
		}
		if (id.equals(PROPERTY_CURRENT_OSMOLALITY)){
			return decision.getCurrentOsmolalityValue().toString();
		}
		if (id.equals(PROPERTY_CURRENT_OSMOLALITY_DATE)){
			return decision.getCurrentOsmolalityDateTime().getTime().toString();
		}
		if (id.equals(PROPERTY_PREVIOUS_SODIUM)){
			return decision.getPreviousSodiumValue().toString();
		}
		if (id.equals(PROPERTY_PREVIOUS_SODIUM_DATE)){
			return decision.getPreviousSodiumDateTime().getTime().toString();
		}
		if (id.equals(PROPERTY_PREVIOUS_OSMOLALITY)){
			return decision.getPreviousOsmolalityValue().toString();
		}
		if (id.equals(PROPERTY_PREVIOUS_OSMOLALITY_DATE)){
			return decision.getPreviousOsmolalityDateTime().getTime().toString();
		}

		if (id.equals(PROPERTY_REC_SALINE_RATE)){
			return decision.getRecommendedHypertonicSalineDripRate().toString();
		}
		if (id.equals(PROPERTY_REC_SALINE_BOLUS)){
			return decision.getRecommendedHypertonicSalineBolus().toString();
		}
		if (id.equals(PROPERTY_REC_MANNITOL_BOLUS)){
			return decision.getRecommendedMannitolBolus().toString();
		}
//		if (id.equals(PROPERTY_INSULIN)) {
//			return ((Double)decision.getCurrentInsulinDripRate()).toString();
//		}
//		if (id.equals(PROPERTY_PREVIOUS_GLUCOSE)) {
//			if (decision.getPreviousGlucoseConcentration() != VentilatorPediatricDecision.NOPREVIOUSGLUCOSE){
//				return (decision.getPreviousGlucoseConcentration()).toString();
//			} else return "No previous value";			
//		}

		if (id.equals(PROPERTY_ADVICE_TEXT)) {
			return decision.getAdviceText();
		}
		if (id.equals(PROPERTY_RATIONALE_TEXT)) {
			return decision.getRationaleText();
		}
		if (id.equals(PROPERTY_RULES_FIRED_TEXT)) {
			return decision.getRulesFiredText();
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
