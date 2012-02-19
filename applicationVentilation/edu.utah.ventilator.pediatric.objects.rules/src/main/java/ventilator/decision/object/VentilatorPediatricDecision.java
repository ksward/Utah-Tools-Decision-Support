package ventilator.decision.object;

import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import core.decision.object.ClinicalDecision;
import core.laboratory.object.ArterialBloodGasLaboratoryResult;
import core.laboratory.object.SerumOsmolalityLaboratoryResult;
import core.laboratory.object.SerumSodiumLaboratoryResult;

/**
 * 
 * VentilatorPediatricDecision is part of the Utah Decision Support Tools.
 * 
 * @author J. Michael Dean, M.D., M.B.A.
 *         <P>
 *         February 20, 2012
 *         <P>
 *         University of Utah School of Medicine, Salt Lake City, Utah
 *         <P>
 *         Copyright 2005 - 2012. All rights reserved.
 *         <P>
 *         Purpose of the class: Extends clinical decision. Specific for
 *         pediatric ventilator decisions.
 *         <P>
 * 
 */
@Entity
@DiscriminatorValue("ventilator")
public class VentilatorPediatricDecision extends ClinicalDecision {
	public static final ArterialBloodGasLaboratoryResult NOCURRENTABG = null;
	public static final ArterialBloodGasLaboratoryResult NOPREVIOUSABG = null;
	public static final int NOPREVIOUSVALUE = -1;
	public static final Integer NOVALUEENTERED = null;
	public static final GregorianCalendar NOPREVIOUSOBSERVATIONDATE = new GregorianCalendar(
			1900, 0, 0, 0, 0);
	public static final GregorianCalendar NOPREVIOUSVALUEDATE = new GregorianCalendar(
			1900, 0, 0, 0, 0);
	
	private GregorianCalendar previousObservationTime;

	/**
	 * This constructor is a convenience method that requires no parameters.
	 * Typical use case is Hibernate (which requires a constructor without
	 * arguments) or programmer testing routines that do not care about the
	 * date.
	 */
	public VentilatorPediatricDecision() {
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
	public VentilatorPediatricDecision(GregorianCalendar observationDate) {
		super(observationDate, ClinicalDecision.PENDING);
		setBlankValues();
	}

	private void setBlankValues() {
		this.previousObservationTime = NOPREVIOUSOBSERVATIONDATE;
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

}
