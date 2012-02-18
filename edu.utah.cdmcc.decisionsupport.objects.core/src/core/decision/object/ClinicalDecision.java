package core.decision.object;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.validator.NotNull;
import core.model.object.BaseEntity;
import core.patient.object.Patient;


/**
 * 
 * ClinicalDecision is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Aug 4, 2006 8:24:01 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2008. All rights reserved.
 *         <P>
 *         Purpose of the class: Base class for all decision objects. Must be
 *         extended.
 *         <P>
 * 
 */
@Entity
@org.hibernate.annotations.GenericGenerator(name="hibernate-uuid", strategy="uuid")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "decisionType", discriminatorType = javax.persistence.DiscriminatorType.STRING)
@DiscriminatorValue("clinicalDecision")
@NamedQueries( {
		@NamedQuery(name = "validClinicalDecisionCount", query = "select count(*) from ClinicalDecision where valid = true"),
		@NamedQuery(name = "getAllClinicalDecisionsByPatientIncludingInvalid", query = "from ClinicalDecision where patient = :patient order by observationDate desc")})
public class ClinicalDecision extends BaseEntity{

	public static final String VALIDCLINICALDECISIONCOUNT = "validClinicalDecisionCount"; 
	public static final String GETALLCLINICALDECISIONSBYPATIENTINCLUDINGINVALID = "getAllClinicalDecisionsByPatientIncludingInvalid"; 
	public static final String EMPTY_STRING = "";
	public static final int UNASSIGNED_TIME = 999999;
	public static final int ONE_HOUR = 60;
	public static final int HALF_HOUR = 30;
	public static final int TWO_HOUR = 120;
	public static final int THREE_HOUR = 180;
	public static final int FOUR_HOUR = 240;
	public static final String ACCEPT = "Accept";
	public static final String DECLINE = "Decline";
	public static final String BACKCHART = "Backchart";
	public static final String PENDING = "Pending";
	public static final String PASSIVE = "Passive";		// used when user does not receive advice

	private GregorianCalendar decisionTimeStamp;
	private GregorianCalendar observationDate;
	private Double patientWeight;
	private Double patientHeight;
	private int patientAgeDays;
	private String adviceText;
	private String rulesFiredText;
	private String rationaleText;
	private String declineComment;
	private String acceptComment;
	private String otherComment;
	public String userAction; // ACCEPT, DECLINE, BACKCHART, PASSIVE
	private int minutesToNextEvaluation;
	private Patient patient;
	private String accountName;

	protected ClinicalDecision() {
		super();
	}
	
	public ClinicalDecision(GregorianCalendar observationTime, String userAction) {
		super();
		this.decisionTimeStamp = new GregorianCalendar();
		this.observationDate = observationTime;
		this.userAction = userAction;
		this.adviceText = EMPTY_STRING;
		this.rulesFiredText = EMPTY_STRING;
		this.rationaleText = EMPTY_STRING;
		this.declineComment = EMPTY_STRING;
		this.acceptComment = EMPTY_STRING;
		this.otherComment = EMPTY_STRING;
		this.minutesToNextEvaluation = UNASSIGNED_TIME;
	}

	@ManyToOne
	@NotNull
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Column(nullable = true)
	public String getAcceptComment() {
		return acceptComment;
	}

	public void setAcceptComment(String acceptComment) {
		this.acceptComment = acceptComment;
	}

	@Column(nullable = true, length=500)
	public String getAdviceText() {
		return adviceText;
	}

	public void setAdviceText(String adviceText) {
		this.adviceText = adviceText;
	}

	@NotNull
	public GregorianCalendar getDecisionTimeStamp() {
		return decisionTimeStamp;
	}

	public void setDecisionTimeStamp(GregorianCalendar decisionTime) {
		this.decisionTimeStamp = decisionTime;
	}

	@Column(nullable = true, length=500)
	public String getDeclineComment() {
		return declineComment;
	}

	public void setDeclineComment(String declineComment) {
		this.declineComment = declineComment;
	}

	@Column(nullable = true)
	public String getOtherComment() {
		return otherComment;
	}

	public void setOtherComment(String otherComment) {
		this.otherComment = otherComment;
	}

	@Column(nullable = true, length=500)
	public String getRationaleText() {
		return rationaleText;
	}

	public void setRationaleText(String rationaleText) {
		this.rationaleText = rationaleText;
	}

	@Column(nullable = true, length = 3000)
	public String getRulesFiredText() {
		return rulesFiredText;
	}

	public void setRulesFiredText(String rulesFiredText) {
		this.rulesFiredText = rulesFiredText;
	}

	@Column(nullable = true)
	public int getMinutesToNextEvaluation() {
		return minutesToNextEvaluation;
	}

	public void setMinutesToNextEvaluation(int minutesToNextEvaluation) {
		this.minutesToNextEvaluation = minutesToNextEvaluation;
	}

	@NotNull
	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((observationDate == null) ? 0 : observationDate.hashCode());
		result = prime * result
				+ ((userAction == null) ? 0 : userAction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClinicalDecision other = (ClinicalDecision) obj;
		if (observationDate == null) {
			if (other.observationDate != null)
				return false;
		} else if (!observationDate.equals(other.observationDate))
			return false;
		if (userAction == null) {
			if (other.userAction != null)
				return false;
		} else if (!userAction.equals(other.userAction))
			return false;
		return true;
	}

	/**
	 * Height is stored with the decision because the user may correct the
	 * patient patientHeight; we want the decision records to indicate the
	 * patientHeight upon which decisions were recommended.
	 * <P>
	 * 
	 * @return the patientHeight
	 */
	@Column(nullable = true)
	public Double getPatientHeight() {
		return patientHeight;
	}

	public void setPatientHeight(Double height) {
		this.patientHeight = height;
	}

	/**
	 * Date of the observations, not the timestamp of the record
	 * 
	 * @return the observationDate
	 */
	@Column(nullable = true)
	public GregorianCalendar getObservationDate() {
		return observationDate;
	}

	/**
	 * @param observationDate
	 *            the observationDate to set
	 */
	public void setObservationDate(GregorianCalendar observationDate) {
		this.observationDate = observationDate;
	}

	/**
	 * Weight is stored with the decision because the user may correct the
	 * patient patientWeight; we want the decision records to indicate the
	 * patientWeight upon which decisions were recommended.
	 * <P>
	 * 
	 * @return the patientWeight
	 */
	@Column(nullable = true)
	public Double getPatientWeight() {
		return patientWeight;
	}

	public void setPatientWeight(Double weight) {
		this.patientWeight = weight;
	}

	@Column(nullable = true)
	public int getPatientAgeDays() {
		return patientAgeDays;
	}

	public void setPatientAgeDays(int patientAgeDays) {
		this.patientAgeDays = patientAgeDays;
	}
	
	@Column(nullable = true)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void advise(final String message){
		if (this.getAdviceText().length() == 0){
			this.setAdviceText(message);
		} else {
			this.setAdviceText(this.getAdviceText() + "\n" + message);}
	}
	
	public void explain(final String message){
		if (this.getRationaleText().length() == 0){
			this.setRationaleText(message);
		} else {
			this.setRationaleText(this.getRationaleText() + "\n" + message);}
	}
	
	public String formatTwoDecimals(Double myNumber){
		DecimalFormat formatter = new DecimalFormat("###.##");
		return formatter.format(myNumber);
	}
	
	public String formatOneDecimal(Double myNumber){
		DecimalFormat formatter = new DecimalFormat("###.#");
		return formatter.format(myNumber);
	}
	
	public String FormatZeroDecimal(Double myNumber){
		DecimalFormat formatter = new DecimalFormat("###");
		return formatter.format(myNumber);
	}

}
