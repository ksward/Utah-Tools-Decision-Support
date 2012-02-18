package core.laboratory.object;

import java.util.GregorianCalendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("sodium")
public class SerumSodiumLaboratoryResult extends
		SingleValueLaboratoryObject {
	
	public static final String LOINC_CODE = "2951-2";
	public static final String CONVENTIONAL_UNITS = "mEq/dL";
	
	public SerumSodiumLaboratoryResult() {
		super();
		this.setLabelName("serum sodium");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setTimeOfSpecimenCollection(new GregorianCalendar());
		this.setConventionalTextResult("");
		this.setValid(true);
	}
	
	public SerumSodiumLaboratoryResult(GregorianCalendar timeOfSpecimenCollection,
			String conventionalTextResult) {
		this.setLabelName("serum sodium");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setTimeOfSpecimenCollection(timeOfSpecimenCollection);
		this.setConventionalTextResult(conventionalTextResult);
		this.setValid(true);
	}
	
	/* (non-Javadoc)
	 * @see core.laboratory.object.IntegerResult#getNumericResult()
	 */
	@Transient
	public Integer getNumericResult() {
		return Integer.parseInt(getConventionalTextResult());
	}
	
//	public void setNumericResult(Integer result){	
//	}
}
