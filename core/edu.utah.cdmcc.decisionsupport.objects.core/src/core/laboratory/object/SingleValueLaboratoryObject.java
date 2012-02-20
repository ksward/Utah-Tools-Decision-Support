package core.laboratory.object;

import java.util.GregorianCalendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.validator.NotNull;

@Entity
@DiscriminatorValue("singleTest")
public class SingleValueLaboratoryObject extends
		BasicLaboratoryObject {

	private String conventionalUnits = "";
	private String conventionalTextResult = "";
	
	public SingleValueLaboratoryObject() {
	}

	public SingleValueLaboratoryObject(String labelName){
		this.timeOfSpecimenCollection = new GregorianCalendar();
		this.loincCode = "LOINC";
		this.labelName = labelName;
		this.valid = true;
	}

	@NotNull
	public String getConventionalUnits() {
		return conventionalUnits;
	}
	
	public void setConventionalUnits(String conventionalUnits) {
		this.conventionalUnits = conventionalUnits;
	}
	
	@NotNull
	public String getConventionalTextResult() {
		return conventionalTextResult;
	}
	
	public void setConventionalTextResult(String conventionalTextResult) {
		this.conventionalTextResult = conventionalTextResult;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((conventionalTextResult == null) ? 0
						: conventionalTextResult.hashCode());
		result = prime
				* result
				+ ((conventionalUnits == null) ? 0 : conventionalUnits
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SingleValueLaboratoryObject))
			return false;
		SingleValueLaboratoryObject other = (SingleValueLaboratoryObject) obj;
		if (conventionalTextResult == null) {
			if (other.conventionalTextResult != null)
				return false;
		} else if (!conventionalTextResult.equals(other.conventionalTextResult))
			return false;
		if (conventionalUnits == null) {
			if (other.conventionalUnits != null)
				return false;
		} else if (!conventionalUnits.equals(other.conventionalUnits))
			return false;
		return true;
	}
	
}
