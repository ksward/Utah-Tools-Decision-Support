package core.laboratory.object;

import java.util.GregorianCalendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("osmolality")
public class SerumOsmolalityLaboratoryResult extends
		SingleValueLaboratoryObject  {
	public static final String LOINC_CODE = "2692-2";
	public static final String CONVENTIONAL_UNITS = "mmol/L";
	
	public SerumOsmolalityLaboratoryResult() {
		super();
		this.setLabelName("serum osmolality");
		this.setLoincCode(LOINC_CODE);
		this.setConventionalUnits(CONVENTIONAL_UNITS);
		this.setTimeOfSpecimenCollection(new GregorianCalendar());
		this.setConventionalTextResult("");
		this.setValid(true);
	}
	
	public SerumOsmolalityLaboratoryResult(GregorianCalendar timeOfSpecimenCollection,
			String conventionalTextResult) {
		this.setLabelName("serum osmolality");
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

}
