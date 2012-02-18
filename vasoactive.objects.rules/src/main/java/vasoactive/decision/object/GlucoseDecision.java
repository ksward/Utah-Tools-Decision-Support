package vasoactive.decision.object;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import core.decision.object.ClinicalDecision;


/**
 * 
 * GlucoseDecision is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         Aug 4, 2006 8:25:18 PM
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2008. All rights reserved.
 *         <P>
 *         Purpose of the class: Extends clinical decision. Specific for glucose
 *         decisions.
 *         <P>
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "validGlucoseDecisionCount", query = "select count(*) from "
				+ "GlucoseDecision where valid = true"),
		@NamedQuery(name = "getAllValidGlucoseDecisionsByPatient", 
				query = "from GlucoseDecision where patient = :patient and valid = true order by observationDate desc")})
//				,
//		@NamedQuery(name = "getLastGlucoseDecision", query = "from GlucoseDecision "
//				+ "where id=(select max(id) from GlucoseDecision where valid = true and patient = :patient)") })
@DiscriminatorValue("glucose")
public class GlucoseDecision extends ClinicalDecision {
	public static final String VALIDGLUCOSEDECISIONCOUNT = "validGlucoseDecisionCount";
	//public static final String GETLASTGLUCOSEDECISION = "getLastGlucoseDecision";
	public static final String GETALLVALIDGLUCOSEDECISIONSBYPATIENT = "getAllValidGlucoseDecisionsByPatient";
	public static final int NOPREVIOUSGLUCOSE = -1; 
	public static final GregorianCalendar NOPREVIOUSOBSERVATIONDATE = new GregorianCalendar(1900,0,0,0,0);
	private Integer previousGlucoseConcentration;
	private GregorianCalendar previousObservationTime;
	private Integer serumGlucoseConcentration;
	private Double currentInsulinDripRate;
	private Double recommendedInsulinDripRate;
	private Double recommendedInsulinBolus;
	private Double recommendedGlucoseBolus;
	
	private Integer systolicBloodPressure;

	public GlucoseDecision() {	
		super(new GregorianCalendar(), ClinicalDecision.PENDING);
		this.serumGlucoseConcentration = 0;
		this.currentInsulinDripRate = 0.0;
		this.recommendedInsulinDripRate = 0.0;
		this.recommendedInsulinBolus = 0.0;
		this.recommendedGlucoseBolus = 0.0;
		
		this.systolicBloodPressure = 0;
		
		this.previousGlucoseConcentration = NOPREVIOUSGLUCOSE;
		this.previousObservationTime = NOPREVIOUSOBSERVATIONDATE;
	}

	public GlucoseDecision(GregorianCalendar decisionTime, String userAction,
			int serumGlucoseConcentration, double currentInsulinDripRate, int systolicBloodPressure) {
		super(decisionTime, userAction);
		this.serumGlucoseConcentration = serumGlucoseConcentration;
		this.currentInsulinDripRate = currentInsulinDripRate;
		this.systolicBloodPressure = systolicBloodPressure;
		this.recommendedInsulinDripRate = 0.0;
		this.recommendedInsulinBolus = 0.0;
		this.recommendedGlucoseBolus = 0.0;
		
		this.systolicBloodPressure = 0;
		
		this.previousGlucoseConcentration = NOPREVIOUSGLUCOSE;
		this.previousObservationTime = NOPREVIOUSOBSERVATIONDATE;
	}

	/**
	 * @return Returns the systolicBloodPressure.
	 */
	@Min(value = 0)
	@Max(value = 300)
	public Integer getSystolicBloodPressure() {
		return systolicBloodPressure;
	}

	public void setSystolicBloodPressure(Integer systolicBloodPressure) {
		this.systolicBloodPressure = systolicBloodPressure;
	}

	
	/**
	 * @return Returns the currentInsulinDripRate.
	 */
	@Min(value = 0)
	@Max(value = 50)
	public Double getCurrentInsulinDripRate() {
		return currentInsulinDripRate;
	}

	/**
	 * @param currentInsulinDripRate
	 *            The currentInsulinDripRate to set.
	 */
	public void setCurrentInsulinDripRate(Double currentInsulinDripRate) {
		this.currentInsulinDripRate = currentInsulinDripRate;
	}

	/**
	 * @return Returns the recommendedGlucoseBolus.
	 */
	@Min(value = 0)
	@Max(value = 100)
	public Double getRecommendedGlucoseBolus() {
		return recommendedGlucoseBolus;
	}

	/**
	 * @param recommendedGlucoseBolus
	 *            The recommendedGlucoseBolus to set.
	 */
	public void setRecommendedGlucoseBolus(Double recommendedGlucoseBolus) {
		this.recommendedGlucoseBolus = recommendedGlucoseBolus;
	}

	/**
	 * @return Returns the recommendedInsulinBolus.
	 */
	public Double getRecommendedInsulinBolus() {
		return recommendedInsulinBolus;
	}

	/**
	 * @param recommendedInsulinBolus
	 *            The recommendedInsulinBolus to set.
	 */
	public void setRecommendedInsulinBolus(Double recommendedInsulinBolus) {
		this.recommendedInsulinBolus = recommendedInsulinBolus;
	}

	/**
	 * @return Returns the recommendedInsulinDripRate.
	 */
	@Min(value = 0)
	@Max(value = 50)
	public Double getRecommendedInsulinDripRate() {
		return recommendedInsulinDripRate;
	}

	/**
	 * @param recommendedInsulinDripRate
	 *            The recommendedInsulinDripRate to set.
	 */
	public void setRecommendedInsulinDripRate(Double recommendedInsulinDripRate) {
		this.recommendedInsulinDripRate = recommendedInsulinDripRate;
	}

	/**
	 * @return Returns the serumGlucoseConcentration.
	 */
	@Min(value = 0)
	@Max(value = 2000)
	public Integer getSerumGlucoseConcentration() {
		return serumGlucoseConcentration;
	}

	/**
	 * @param serumGlucoseConcentration
	 *            The serumGlucoseConcentration to set.
	 */
	public void setSerumGlucoseConcentration(Integer serumGlucoseConcentration) {
		this.serumGlucoseConcentration = serumGlucoseConcentration;;
	}

	/**
	 * Returns the previousGlucoseConcentration
	 * 
	 * @return the previousGlucoseConcentration
	 */
	@Column(nullable = true)
	public Integer getPreviousGlucoseConcentration() {
		return previousGlucoseConcentration;
	}

	/**
	 * Sets the previousGlucoseConcentration to the parameter
	 * previousGlucoseConcentration
	 * 
	 * @param previousGlucoseConcentration
	 *            the previousGlucoseConcentration to set
	 */
	public void setPreviousGlucoseConcentration(Integer previousGlucoseConcentration) {
		this.previousGlucoseConcentration = previousGlucoseConcentration;
	}

	/**
	 * Returns the previousObservationTime
	 * 
	 * @return the previousObservationTime
	 */
	@Column(nullable = true)
	public GregorianCalendar getPreviousObservationTime() {
		return previousObservationTime;
	}

	/**
	 * Sets the previousObservationTime to the parameter previousObservationTime
	 * 
	 * @param previousObservationTime
	 *            the previousObservationTime to set
	 */
	public void setPreviousObservationTime(
			GregorianCalendar previousObservationTime) {
		this.previousObservationTime = previousObservationTime;
	}

	/**
	 * Returns the number of minutes between the current and previous dates.
	 * 
	 * @param currentDate -
	 *            date of the current observation
	 * @param previousDate -
	 *            date of the previous observation
	 * @return number of minutes between current and previous dates
	 */
	@Transient
	public Double getDeltaMinutes(GregorianCalendar currentDate,
			GregorianCalendar previousDate) {
		if (previousDate == NOPREVIOUSOBSERVATIONDATE || previousDate == null) {
			return null;
		}
		long millisBetween = currentDate.getTimeInMillis()
				- previousDate.getTimeInMillis();
		return (double) Math.round(millisBetween / (1000 * 60));

	}

	/**
	 * Returns the differential of glucose per hour, based on previous
	 * measurement and current measurement.
	 * 
	 * @return glucoseChangePerHour
	 */
	@Transient
	public Double getGlucoseChangePerHour() {
		final int MINUTESPERHOUR = 60;
		if (this.getPreviousGlucoseConcentration() == NOPREVIOUSGLUCOSE) {
			return null;
		} else {
			int deltaGlucose = this.getSerumGlucoseConcentration() - this.getPreviousGlucoseConcentration();
			double deltaMinutes = getDeltaMinutes(getObservationDate(),
					getPreviousObservationTime());
			double deltaHours = deltaMinutes / MINUTESPERHOUR;
			double glucoseChangePerHour = deltaGlucose / deltaHours;
			return glucoseChangePerHour;
		}
	}

	/**
	 * Returns the previous value of glucose at index value.
	 * 
	 * @param start 
	 * @param backwards 
	 * @return glucose value at Nth position backwards from present
	 */
	@Transient
	public int retrieveNthGlucoseValue(ArrayList<GlucoseDecision> q, int start,
			int backwards) {
		return q.get(start + backwards).serumGlucoseConcentration;
	}
//
//	/**
//	 * Get the last observation time.  This is used for calculating
//	 * changes in glucose per unit time.
//	 * 
//	 * @return GregorianCalendar of previous observation.
//	 */
//	public GregorianCalendar retrievePreviousDecisionTime() {
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		Query q = patientDAO.getSession().getNamedQuery(GlucoseDecision.GETALLVALIDGLUCOSEDECISIONSBYPATIENT);
//		q.setParameter("patient", this.getPatient());
//		ArrayList<GlucoseDecision> results = (ArrayList<GlucoseDecision>) q.list();
//		patientDAO.getSession().getTransaction().commit();
//		if (results.size()==0){
//		return NOPREVIOUSOBSERVATIONDATE;
//		} else {
//			return results.get(0).getObservationDate();
//		}
//	}

//	/**
//	 * Get the glucose of the last observation.  This is then
//	 * used to calculate the rate of change of glucose.
//	 * 
//	 * @return previous glucose value
//	 */
//	public Integer retrievePreviousDecisionGlucose() {
//		IPatientDAO patientDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getPatientDAO();
//		patientDAO.getSession().beginTransaction();
//		Query q = patientDAO.getSession().getNamedQuery(GlucoseDecision.GETALLVALIDGLUCOSEDECISIONSBYPATIENT);
//		q.setParameter("patient", this.getPatient());
//		ArrayList<GlucoseDecision> results = (ArrayList<GlucoseDecision>) q.list();
//		patientDAO.getSession().getTransaction().commit();
//		if (results.size()==0){
//		return NOPREVIOUSGLUCOSE;
//		} else {
//			return results.get(0).serumGlucoseConcentration;
//		}
//	}

	
	public Double adjustInsulinRate(){
		Integer gluc_midpoint = 95;
		Integer g2 = getSerumGlucoseConcentration();
		if (g2 == gluc_midpoint){
			g2 = g2 + 1;
			} 
		double mf = (g2 - gluc_midpoint)/g2;
		double dgdt = getGlucoseChangePerHour();
		double desired_rate_of_change = -50 * mf;
		double adjustment = ((dgdt - desired_rate_of_change)/Math.abs(desired_rate_of_change))
		    *Math.abs(mf)/Math.sqrt(getCurrentInsulinDripRate());
		if (adjustment <= -1) return 0.0;
		return getCurrentInsulinDripRate() * (1 + adjustment);
	}
	
	
	@Transient
	public String toString(){
		return super.toString() +
		" gluc= "+ serumGlucoseConcentration +" mg/dL, " +
		"rec glu= "+ recommendedGlucoseBolus + "gms, " +
		"ins= "+ currentInsulinDripRate +" U/hr, " +
		"rec ins= "+ recommendedInsulinDripRate + " U/hr," +
		" sbp= "+systolicBloodPressure;
	}

}
