/**
 * This class is a component of a multi-valued laboratory panel
 * and does not contain information that would link it back to
 * the panel.  It is strictly being used as a component mapping
 * in the Hibernate model of panel laboratory objects.
 */

package core.laboratory.object;


import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

@Embeddable
public class LaboratoryPanelComponent {

	private String loincCode;
	private String labelName;
	private String conventionalUnits;
	private String conventionalTextResult;
	private MultipleValueLaboratoryObject parentPanel;
	
	public LaboratoryPanelComponent(){}
	
	@NotNull
	public String getLoincCode() {
		return loincCode;
	}
	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}
	
	@NotNull
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
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

	@org.hibernate.annotations.Parent
	public MultipleValueLaboratoryObject getParentPanel(){
		return parentPanel;
	}
	
	public void setParentPanel(MultipleValueLaboratoryObject parentPanel) {
		this.parentPanel = parentPanel;
	}

	@Transient
	public Integer getIntegerResult(){
		return null;
		}
	
	@Transient
	public Double getDoubleResult(){
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((conventionalUnits == null) ? 0 : conventionalUnits
						.hashCode());
		result = prime * result
				+ ((labelName == null) ? 0 : labelName.hashCode());
		result = prime * result
				+ ((loincCode == null) ? 0 : loincCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LaboratoryPanelComponent))
			return false;
		LaboratoryPanelComponent other = (LaboratoryPanelComponent) obj;
		if (conventionalUnits == null) {
			if (other.conventionalUnits != null)
				return false;
		} else if (!conventionalUnits.equals(other.conventionalUnits))
			return false;
		if (labelName == null) {
			if (other.labelName != null)
				return false;
		} else if (!labelName.equals(other.labelName))
			return false;
		if (loincCode == null) {
			if (other.loincCode != null)
				return false;
		} else if (!loincCode.equals(other.loincCode))
			return false;
		return true;
	}
	
	
}
