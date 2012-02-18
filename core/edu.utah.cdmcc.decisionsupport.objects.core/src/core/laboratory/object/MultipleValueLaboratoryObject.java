package core.laboratory.object;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;

@Entity
@DiscriminatorValue("multipleTest")
public class MultipleValueLaboratoryObject extends
		BasicLaboratoryObject {
	
	
	private Set<LaboratoryPanelComponent> components = new HashSet<LaboratoryPanelComponent>();

	public MultipleValueLaboratoryObject(){		
	}
	
	@org.hibernate.annotations.CollectionOfElements(targetElement = LaboratoryPanelComponent.class,
			fetch=FetchType.EAGER)
	public Set<LaboratoryPanelComponent> getComponents() {
		return components;
	}
	
    /* The following setter is currently not called by my code but is required for Hibernate.*/ 
	public void setComponents(Set<LaboratoryPanelComponent> components) {
		this.components = components;
	}
	
	public void addLaboratoryComponent(LaboratoryPanelComponent component){
		component.setParentPanel(this);
		getComponents().add(component);
	}
}
