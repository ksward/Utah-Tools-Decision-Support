package vasoactive.decision.object;

import java.util.GregorianCalendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import core.laboratory.object.LaboratoryResult;

@Entity
@DiscriminatorValue("glucose")
public class GlucoseLaboratoryResult extends LaboratoryResult {
	static final String LOINC_CODE = "14749-6";
	static final String CONVENTIONAL_UNITS = "mg/dL";
	
	public GlucoseLaboratoryResult(final GregorianCalendar timeOfSpecimenCollection,
			final String conventionalTextResult) {
		super(timeOfSpecimenCollection, LOINC_CODE, CONVENTIONAL_UNITS,
				conventionalTextResult);
	}

	public final Integer getNumericResult() {
		return Integer.parseInt(getConventionalTextResult());
	}
}
