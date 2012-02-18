package core.patient.object;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Range;
import core.decision.object.ClinicalDecision;
import core.laboratory.object.BasicLaboratoryObject;
import core.model.object.ModelObject;

/**
 * 
 * Patient is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Aug 4, 2006 8:25:38 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2010. All rights reserved.
 *         <P>
 *         Purpose of the class: Represents a patient in a specific study in a
 *         specific clinical site. 
 * 
 */
@Entity
@org.hibernate.annotations.GenericGenerator(name="hibernate-uuid", strategy = "uuid")
@NamedQueries( {
		@NamedQuery(name = "patientCount", query = "select count(*) from Patient"),
		@NamedQuery(name = "getLastPatient", query = "from Patient where id=(select max(id) from Patient)"),
		@NamedQuery(name = "getAllPatientsByStudyID", query = "from Patient where studyID = :studyID") })
public class Patient extends ModelObject{
	public static final String PATIENTCOUNT = "patientCount";
	public static final String GETLASTPATIENT = "getLastPatient";
	public static final String GETALLPATIENTSBYSTUDYID = "getAllPatientsByStudyID";
	private static final String DEFAULT = "Default miscellaneous";
	private String lastName;
	private String firstName;
	private String medRecNum;
	private String studyID;
	private GregorianCalendar birthdate;
	private Double weight;
	private Double height;
	private String miscellaneous = DEFAULT; // Used for testing 
	private List<ClinicalDecision> decisions;
	private List<BasicLaboratoryObject> experimentalLabResults;
	private String accountName;
	static Logger logger = Logger.getLogger(Patient.class);

	public Patient(final String lastName, final String firstName, final String medRecNum,
			final String studyID, final GregorianCalendar birthdate, 
			final Double weight, final Double height) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.medRecNum = medRecNum;
		this.studyID = studyID;
		this.birthdate = birthdate;
		this.weight = weight;
		this.height = height;
		this.decisions = new ArrayList<ClinicalDecision>();
		this.experimentalLabResults = new ArrayList<BasicLaboratoryObject>();
	}

	public Patient() {
	}
	
	public Patient(final String lastName){
		this.lastName = lastName;
		this.firstName = "test";
		this.medRecNum = "12-34-56";
		this.studyID = "ST03CHOM0002";
		this.birthdate = new GregorianCalendar();
		this.weight = 50.0;
		this.height = 100.0;
		this.decisions = new ArrayList<ClinicalDecision>();
		this.experimentalLabResults = new ArrayList<BasicLaboratoryObject>();
	}

	@NotNull
	@Past
	public GregorianCalendar getBirthdate() {
		return birthdate;
	}

	@Transient
	public String getBirthdateString() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		return df.format(birthdate.getTime());
	}
	
	/**
	 * This routine returns age in years compared to today. It is important to
	 * note that this value changes according to the date of execution and may
	 * not be suitable for storage in clinical decisions or events.
	 * 
	 * @return age (today) in years (rounded)
	 */
	@Transient
	public int getTodayAgeYears() {
		return getDeltaAgeYears(new GregorianCalendar());
	}

	/**
	 * This routine returns age in years compared to date parameter. This is
	 * more useful for storing the age at a time of decision or other clinical
	 * event, by passing in the date of the event.
	 * 
	 * @param date
	 * @return age in years (rounded)
	 */
	@Transient
	public int getDeltaAgeYears(final GregorianCalendar date) {
		long millisBetween = Math.abs(getBirthdate().getTimeInMillis()
				- date.getTimeInMillis());
		return (int) Math.round((millisBetween / (1000 * 60 * 60 * 24)) / 365);
	}

	/**
	 * This routine returns age in days compared to today. It is important to
	 * note that this value changes according to the date of execution and may
	 * not be suitable for storage in clinical decisions or events.
	 * 
	 * @return age (today) in days (rounded)
	 */
	@Transient
	public int getTodayAgeDays() {
		return getDeltaAgeDays(new GregorianCalendar());
	}

	/**
	 * This routine returns age in days compared to date parameter. This is more
	 * useful for storing the age at a time of decision or other clinical event,
	 * by passing in the date of the event.
	 * 
	 * @param date
	 * @return age in days (rounded)
	 */
	@Transient
	public int getDeltaAgeDays(final GregorianCalendar date) {
		long millisBetween = Math.abs(getBirthdate().getTimeInMillis()
				- date.getTimeInMillis());
		return (int) Math.round((millisBetween / (1000 * 60 * 60 * 24)));
	}

	public void setBirthdate(final GregorianCalendar birthdate) {
		this.birthdate = birthdate;
	}

	@NotNull
	@Length(min = 2, max = 20)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Range(min = 25, max = 200)
	public Double getHeight() {
		return height;
	}

	public void setHeight(final Double height) {
		this.height = height;
	}

	@NotNull
	@Length(min = 2, max = 20)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@NotNull
	public String getMedRecNum() {
		return medRecNum;
	}

	public void setMedRecNum(final String medRecNum) {
		this.medRecNum = medRecNum;
	}

	@NotNull
	@Pattern(regex = "[A-Z][A-Z][A-Z0-9][A-Z0-9][A-Z_][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9]")
	public String getStudyID() {
		return studyID;
	}

	public void setStudyID(final String studyID) {
		this.studyID = studyID;
	}

	@NotNull
	@Range(min = 3, max = 300)
	public Double getWeight() {
		return weight;
	}

	public void setWeight(final Double weight) {
		this.weight = weight;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
	@BatchSize(size = 5)
	@OrderBy("decisionTimeStamp")
	@org.hibernate.annotations.LazyCollection(LazyCollectionOption.FALSE)
	public List<ClinicalDecision> getDecisions() {
		return decisions;
	}

	public void setDecisions(final List<ClinicalDecision> decisions) {
		this.decisions = decisions;
	}

	/**
	 * This procedure fills in decision information related to the current state
	 * of the patient. This is done because a decision may be made with patient
	 * data that are different, whether because of a correction, or simply
	 * because the patient is older or has changed weight or height.
	 * 
	 * @param decision
	 */
	public void addDecision(ClinicalDecision decision) {
		if (checkForDuplicateDecision(decision) == false) {
			decision.setPatient(this);
			decision.setPatientAgeDays(this.getDeltaAgeDays(decision
					.getObservationDate()));
			decision.setPatientHeight(this.getHeight());
			decision.setPatientWeight(this.getWeight());
			//decision.setPatientDependentParameters(this);
			getDecisions().add(decision);
		}
	}

	private boolean checkForDuplicateDecision(final ClinicalDecision newDecision) {
		for (ClinicalDecision oldDecision : this.getDecisions()) {
			if (newDecision.equals(oldDecision)) {
				logger.debug("Duplicate decision");
				return true;
			}
		}
		return false;
	}

	public void deleteDecision(final ClinicalDecision decision) {
		getDecisions().remove(decision);
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
	@BatchSize(size = 5)
	@OrderBy("timeOfSpecimenCollection")
	@org.hibernate.annotations.LazyCollection(LazyCollectionOption.FALSE)
	public List<BasicLaboratoryObject> getExperimentalLabResults() {
		return experimentalLabResults;
	}

	public void setExperimentalLabResults(
			List<BasicLaboratoryObject> experimentalLabResults) {
		this.experimentalLabResults = experimentalLabResults;
	}

	public void addExperimentalLabResult(BasicLaboratoryObject result){
		if (checkForDuplicateExperimentalLabResult(result) == false){
			result.setPatient(this);
			getExperimentalLabResults().add(result);
			logger.debug("Lab result  added to patient because is not duplicate");
		}
	}
	private boolean checkForDuplicateExperimentalLabResult(final BasicLaboratoryObject newResult) {
		for (BasicLaboratoryObject oldResult : this.getExperimentalLabResults()) {
			if (newResult.equals(oldResult)) {
				logger.debug("Duplicate Lab result");
				return true;
			}
			logger.debug(newResult + " is not equal to "+oldResult);
		}
		return false;
	}
	
	public void deleteLabResult(final BasicLaboratoryObject result) {
		getExperimentalLabResults().remove(result);
		// Note that I am not doing the following but this
		// may be better approach in future:
		// result.setValid(false);
		// Reason: I may want to implement audits with
		// some kind of interceptor and keep the objects clean.
	}
	
	@Transient
	public String getName() {
		return getLastName().trim() + ", " + getFirstName().trim();
	}

	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Patient))
			return false;
		Patient castOther = (Patient) other;
		return ((this.getName().equals(castOther.getName()))
				&& (this.getStudyID().equals(castOther.getStudyID()))
				&& (this.getMedRecNum().equals(castOther.getMedRecNum())) 
				&& (this
				.getBirthdate().equals(castOther.getBirthdate()))
				);
	}

	public int hashcode() {
		int result = 17;
		result = 37 * result + this.getName().hashCode();
		result = 37 * result + this.getStudyID().hashCode();
		result = 37 * result + this.getMedRecNum().hashCode();
		result = 37 * result + this.getBirthdate().hashCode();
		return result;
	}

	@Column(nullable = true)
	public String getMiscellaneous() {
		return miscellaneous;
	}

	public void setMiscellaneous(final String miscellaneous) {
		this.miscellaneous = miscellaneous;
	}
	
	@Column(nullable = true)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Transient
	public String toString(){
		return getName();
	}
	
}
