package hypertonic.decision.object;

import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import core.decision.object.ClinicalDecision;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;

/**
 * 
 * HypertonicSalineDecision is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         June 24, 2010
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2010. All rights reserved.
 *         <P>
 *         Purpose of the class: Extends clinical decision. Specific for
 *         hypertonic decisions.
 *         <P>
 * 
 */
@Entity
@DiscriminatorValue("hypertonic")
public class HypertonicSalineDecision extends ClinicalDecision {
	public static final SerumSodiumLaboratoryResult NOPREVIOUSSODIUM = null;
	public static final SerumOsmolalityLaboratoryResult NOPREVIOUSOSMOLALITY = null;
	public static final SerumSodiumLaboratoryResult NOCURRENTSODIUM = null;
	public static final SerumOsmolalityLaboratoryResult NOCURRENTOSMOLALITY = null;
	public static final int NOPREVIOUSVALUE = -1;
	public static final Integer NOVALUEENTERED = null;
	public static final GregorianCalendar NOPREVIOUSOBSERVATIONDATE = new GregorianCalendar(
			1900, 0, 0, 0, 0);
	public static final GregorianCalendar NOPREVIOUSVALUEDATE = new GregorianCalendar(
			1900, 0, 0, 0, 0);
	private GregorianCalendar previousObservationTime;
	private Integer previousSodiumValue;
	private GregorianCalendar previousSodiumDateTime;
	private Integer currentSodiumValue;
	private GregorianCalendar currentSodiumDateTime;
	private Integer previousOsmolalityValue;
	private GregorianCalendar previousOsmolalityDateTime;
	private Integer currentOsmolalityValue;
	private GregorianCalendar currentOsmolalityDateTime;
	private Integer intracranialPressure;
	private Integer meanArterialPressure;
	private Integer centralVenousPressure;
	private Double currentHypertonicSalineDripRate;
	private Double recommendedHypertonicSalineDripRate;
	private Double recommendedHypertonicSalineBolus;
	private Double recommendedMannitolBolus;

	/**
	 * This constructor is designed to be called by a user that can supply all
	 * the parameters. Typical use case will be from routines that populate the
	 * decision object in the decision editor prior to the nurse clicking on the
	 * Calculate Decision button.
	 * 
	 * @param currentHypertonicSalineDripRate
	 * @param intracranialPressure
	 * @param meanArterialPressure
	 * @param centralVenousPressure
	 */
	public HypertonicSalineDecision(GregorianCalendar observationDate, Double currentHypertonicSalineDripRate,
			Integer intracranialPressure, Integer meanArterialPressure,
			Integer centralVenousPressure) {
		super(observationDate, ClinicalDecision.PENDING);
		setBlankValues();
		this.currentHypertonicSalineDripRate = currentHypertonicSalineDripRate;
		this.intracranialPressure = intracranialPressure;
		this.meanArterialPressure = meanArterialPressure;
		this.centralVenousPressure = centralVenousPressure;
	}

	/**
	 * This constructor is a convenience method that requires no parameters.
	 * Typical use case is Hibernate (which requires a constructor without
	 * arguments) or programmer testing routines that do not care about the
	 * date.
	 */
	public HypertonicSalineDecision() {
		super(new GregorianCalendar(), ClinicalDecision.PENDING);
		setBlankValues();

	}

	/**
	 * This constructor is a convenience method that requires the specific date
	 * of the decision. Typical use case is programmer testing routines where
	 * the date is important.
	 * 
	 * @param observationDate
	 */
	public HypertonicSalineDecision(GregorianCalendar observationDate) {
		super(observationDate, ClinicalDecision.PENDING);
		setBlankValues();
	}

	private void setBlankValues() {
		this.previousObservationTime = NOPREVIOUSOBSERVATIONDATE;
		this.previousSodiumValue = NOPREVIOUSVALUE;
		this.previousSodiumDateTime = NOPREVIOUSVALUEDATE;
		this.previousOsmolalityValue = NOPREVIOUSVALUE;
		this.previousOsmolalityDateTime = NOPREVIOUSVALUEDATE;
		this.currentSodiumValue = NOPREVIOUSVALUE;
		this.currentSodiumDateTime = NOPREVIOUSVALUEDATE;
		this.currentOsmolalityValue = NOPREVIOUSVALUE;
		this.currentOsmolalityDateTime = NOPREVIOUSVALUEDATE;
		this.currentHypertonicSalineDripRate = 0.0;
		this.intracranialPressure = NOVALUEENTERED;
		this.meanArterialPressure = NOVALUEENTERED;
		this.centralVenousPressure = NOVALUEENTERED;
		this.recommendedHypertonicSalineBolus = 0.0;
		this.recommendedHypertonicSalineDripRate = 0.0;
		this.recommendedMannitolBolus = 0.0;
	}

	/**
	 * Set previous observation time. Normally the user does not call this
	 * method directly.
	 * 
	 * @param previousObservationTime
	 */
	public void setPreviousObservationTime(
			GregorianCalendar previousObservationTime) {
		this.previousObservationTime = previousObservationTime;
	}

	/**
	 * Returns the Date and Time of the previous clinical decision. Typical use
	 * case is rule writer who wants to determine how long it has been since
	 * there was a previous decision recommendation.
	 * 
	 * @return previousObservationTime
	 */
	@Column(nullable = true)
	public GregorianCalendar getPreviousObservationTime() {
		return previousObservationTime;
	}

	/**
	 * Returns the previous sodium value.
	 * 
	 * @return previousSodiumValue
	 */
	@Column(nullable = true)
	public Integer getPreviousSodiumValue() {
		return previousSodiumValue;
	}

	/**
	 * Returns the time of specimen collection of previous sodium value.
	 * 
	 * @return previousSodiumDateTime
	 */
	@Column(nullable = true)
	public GregorianCalendar getPreviousSodiumDateTime() {
		return previousSodiumDateTime;
	}

	/**
	 * Returns the current (most recent) sodium value.
	 * 
	 * @return currentSodiumValue
	 */
	public Integer getCurrentSodiumValue() {
		return currentSodiumValue;
	}

	/**
	 * Returns the time of specimen collection of current (most recent) sodium
	 * value.
	 * 
	 * @return currentSodiumDateTime
	 */
	public GregorianCalendar getCurrentSodiumDateTime() {
		return currentSodiumDateTime;
	}

	/**
	 * Returns the previous osmolality value.
	 * 
	 * @return previousOsmolalityValue
	 */
	@Column(nullable = true)
	public Integer getPreviousOsmolalityValue() {
		return previousOsmolalityValue;
	}

	/**
	 * Returns the time of specimen collection of previous osmolality value.
	 * 
	 * @return previousOsmolalityDateTime
	 */
	@Column(nullable = true)
	public GregorianCalendar getPreviousOsmolalityDateTime() {
		return previousOsmolalityDateTime;
	}

	/**
	 * Returns the current (most recent) osmolality value.
	 * 
	 * @return currentOsmolalityValue
	 */
	@Column(nullable = true)
	public Integer getCurrentOsmolalityValue() {
		return currentOsmolalityValue;
	}

	/**
	 * Returns the time of specimen collection of the current (most recent)
	 * osmolality value.
	 * 
	 * @return currentOsmolalityDateTime
	 */
	@Column(nullable = true)
	public GregorianCalendar getCurrentOsmolalityDateTime() {
		return currentOsmolalityDateTime;
	}

	/**
	 * Returns the ICP.
	 * 
	 * @return intracranialPressure
	 */
	@Column(nullable = true)
	@Range(min = 0, max = 120)
	public Integer getIntracranialPressure() {
		return intracranialPressure;
	}

	/**
	 * Set the intracranial pressure field. Normally called by an application
	 * editor object when the decision is instantiated.
	 * 
	 * @param intracranialPressure
	 */
	public void setIntracranialPressure(Integer intracranialPressure) {
		this.intracranialPressure = intracranialPressure;
	}

	/**
	 * Returns the mean arterial pressure.
	 * 
	 * @return meanArterialPressure
	 */
	@NotNull
	@Range(min = 30, max = 200)
	public Integer getMeanArterialPressure() {
		return meanArterialPressure;
	}

	/**
	 * Set the mean arterial pressure field. Normally called by an application
	 * editor object when the decision is instantiated.
	 * 
	 * @param meanArterialPressure
	 */
	public void setMeanArterialPressure(Integer meanArterialPressure) {
		this.meanArterialPressure = meanArterialPressure;
	}

	@Column(nullable = true)
	@Range(min = 0, max = 50)
	public Integer getCentralVenousPressure() {
		return centralVenousPressure;
	}

	public void setCentralVenousPressure(Integer centralVenousPressure) {
		this.centralVenousPressure = centralVenousPressure;
	}

	@NotNull
	@Range(min = 0, max = 10)
	public Double getCurrentHypertonicSalineDripRate() {
		return currentHypertonicSalineDripRate;
	}

	public void setCurrentHypertonicSalineDripRate(
			Double currentHypertonicSalineDripRate) {
		this.currentHypertonicSalineDripRate = currentHypertonicSalineDripRate;
	}


	public Double getRecommendedHypertonicSalineDripRate() {
		return recommendedHypertonicSalineDripRate;
	}

	public void setRecommendedHypertonicSalineDripRate(
			Double recommendedHypertonicSalineDripRate) {
		this.recommendedHypertonicSalineDripRate = recommendedHypertonicSalineDripRate;
	}


	public Double getRecommendedHypertonicSalineBolus() {
		return recommendedHypertonicSalineBolus;
	}

	public void setRecommendedHypertonicSalineBolus(
			Double recommendedHypertonicSalineBolus) {
		this.recommendedHypertonicSalineBolus = recommendedHypertonicSalineBolus;
	}


	public Double getRecommendedMannitolBolus() {
		return recommendedMannitolBolus;
	}

	public void setRecommendedMannitolBolus(Double recommendedMannitolBolus) {
		this.recommendedMannitolBolus = recommendedMannitolBolus;
	}

	/**
	 * This method returns the change of sodium value per hour. Typical user
	 * will be rule writer. If the dates entered are identical, which would
	 * cause a division by zero, the method returns null. Callers of the routine
	 * should check for null values before using the result.
	 * 
	 * @return getSodiumDerivative
	 */
	@Transient
	public Double getSodiumDerivative() {
		if (previousSodiumValue == NOPREVIOUSVALUE) {
			return null;
		}
		if (currentSodiumValue == NOPREVIOUSVALUE) {
			return null;
		}
		if (previousSodiumDateTime.equals(currentSodiumDateTime)) {
			return null;
		}
		long deltaMilliSeconds = currentSodiumDateTime.getTimeInMillis()
				- previousSodiumDateTime.getTimeInMillis();
		int deltaSodium = currentSodiumValue - previousSodiumValue;
		return changePerHour(deltaSodium, deltaMilliSeconds);
	}

	/**
	 * This method returns the change of osmolality value per hour. Typical user
	 * will be rule writer. If the dates entered are identical, which would
	 * cause a division by zero, the method returns null. Callers of the routine
	 * should check for null values before using the result.
	 * 
	 * @return getOsmolalityDerivative
	 */
	@Transient
	public Double getOsmolalityDerivative() {
		if (previousOsmolalityValue == NOPREVIOUSVALUE) {
			return null;
		}
		if (currentOsmolalityValue == NOPREVIOUSVALUE) {
			return null;
		}
		if (previousOsmolalityDateTime.equals(currentOsmolalityDateTime)) {
			return null;
		}
		long deltaMilliSeconds = currentOsmolalityDateTime.getTimeInMillis()
				- previousOsmolalityDateTime.getTimeInMillis();
		int deltaOsmolality = currentOsmolalityValue - previousOsmolalityValue;
		return changePerHour(deltaOsmolality, deltaMilliSeconds);
	}

	private Double changePerHour(int valueChange, long deltaMilliSeconds) {
		double deltaHours = (double) Math.round(deltaMilliSeconds
				/ (1000 * 60 * 60));
		return valueChange / deltaHours;
	}

	public void setPreviousSodiumValue(Integer previousSodiumValue) {
		this.previousSodiumValue = previousSodiumValue;
	}

	public void setPreviousSodiumDateTime(
			GregorianCalendar previousSodiumDateTime) {
		this.previousSodiumDateTime = previousSodiumDateTime;
	}

	public void setCurrentSodiumValue(Integer currentSodiumValue) {
		this.currentSodiumValue = currentSodiumValue;
	}

	public void setCurrentSodiumDateTime(GregorianCalendar currentSodiumDateTime) {
		this.currentSodiumDateTime = currentSodiumDateTime;
	}

	public void setPreviousOsmolalityValue(Integer previousOsmolalityValue) {
		this.previousOsmolalityValue = previousOsmolalityValue;
	}

	public void setPreviousOsmolalityDateTime(
			GregorianCalendar previousOsmolalityDateTime) {
		this.previousOsmolalityDateTime = previousOsmolalityDateTime;
	}

	public void setCurrentOsmolalityValue(Integer currentOsmolalityValue) {
		this.currentOsmolalityValue = currentOsmolalityValue;
	}

	public void setCurrentOsmolalityDateTime(
			GregorianCalendar currentOsmolalityDateTime) {
		this.currentOsmolalityDateTime = currentOsmolalityDateTime;
	}

	/**
	 * This is an overridden method to make the object description more
	 * sensible. If programmer does not like the way object is described, this
	 * routine can be changed.
	 */
	@Transient
	public String toString() {
		return // super.toString() +
		getClass().getName() + " Time = "
				+ this.getObservationDate().toString() + " Na = "
				+ this.getCurrentSodiumValue();
	}
}
