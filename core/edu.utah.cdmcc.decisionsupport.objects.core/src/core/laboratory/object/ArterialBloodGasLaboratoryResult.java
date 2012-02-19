package core.laboratory.object;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("abg")
public class ArterialBloodGasLaboratoryResult extends MultipleValueLaboratoryObject {

	public static final String LOINC_CODE = "24336-0";
	public static final String CONVENTIONAL_UNITS = "";
	
	public ArterialBloodGasLaboratoryResult(){
		this.setLabelName("abg");
		this.setLoincCode(LOINC_CODE);
	}
	
	public ArterialBloodGasLaboratoryResult(GregorianCalendar timeOfSpecimenCollection) {
		this.setLabelName("abg");
		this.setLoincCode(LOINC_CODE);
		this.setTimeOfSpecimenCollection(timeOfSpecimenCollection);
	}
	
	@Transient
	public Double getPhValue() {
		for (LaboratoryPanelComponent component : getComponents()) {
			if (component.getLoincCode().equals(ArterialPhLaboratoryResult.LOINC_CODE)) {
				return Double.parseDouble(component.getConventionalTextResult());
			}
		}
		return null;
	}
	
	@Transient
	public String getPhString(){
		return getPhValue().toString();
	}
	
	@Transient
	public Integer getPo2Value(){
		for (LaboratoryPanelComponent component : getComponents()) {
			if (component.getLoincCode().equals(ArterialOxygenLaboratoryResult.LOINC_CODE)) {
				return Integer.parseInt(component.getConventionalTextResult());
			}
		}
		return null;
	}
	
	@Transient
	public String getPo2String(){
		return getPo2Value().toString();
	}
	
	@Transient
	public Integer getPco2Value(){
		for (LaboratoryPanelComponent component : getComponents()) {
			if (component.getLoincCode().equals(ArterialCarbonDioxideLaboratoryResult.LOINC_CODE)) {
				return Integer.parseInt(component.getConventionalTextResult());
			}
		}
		return null;
	}
	
	@Transient
	public String getPco2String(){
		return getPco2Value().toString();
	}
	
	@Transient
	public String getTimeOfABG(){
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
		return df.format(getTimeOfSpecimenCollection().getTime());
	}
	
	@Override
	public String toString() {
		String s = this.getTimeOfSpecimenCollection().getTime().toString() + "\n" + getLabelName();
		return s;
	}	
}
