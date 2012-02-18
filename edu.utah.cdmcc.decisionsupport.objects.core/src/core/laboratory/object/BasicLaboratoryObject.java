
/**
 * 	This is an experimental laboratory class that will underly laboratory single test objects
 * 	as well as laboratory panels.  Thus, it has less in it than the previous version of laboratory
 *  object.
 */

package core.laboratory.object;

import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.validator.NotNull;
import core.model.object.BaseEntity;
import core.patient.object.Patient;

@Entity
@org.hibernate.annotations.GenericGenerator(name="hibernate-uuid", strategy="uuid")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="laboratoryType", discriminatorType = javax.persistence.DiscriminatorType.STRING)
@DiscriminatorValue("laboratoryTest")
@NamedQueries( {
		@NamedQuery(name = "getAllLabResultsByPatientIncludingInvalidEXP", query = "from BasicLaboratoryObject where patient = :patient order by timeOfSpecimenCollection desc"),
		@NamedQuery(name = "getAllLabResultsByPatientAndLoinc", query = "from BasicLaboratoryObject where patient = :patient and loincCode = :loincCode order by timeOfSpecimenCollection desc")})
public class BasicLaboratoryObject extends BaseEntity {

	public static final String GETALLLABRESULTSBYPATIENTINCLUDINGINVALIDEXP = "getAllLabResultsByPatientIncludingInvalidEXP";
	public static final String GETALLLABRESULTSBYPATIENTANDLOINC = "getAllLabResultsByPatientAndLoinc";
	
	protected GregorianCalendar timeOfSpecimenCollection;
	protected String loincCode;
	protected String labelName;
	private Patient patient;
	private String accountName;
	
	@NotNull
	public GregorianCalendar getTimeOfSpecimenCollection() {
		return timeOfSpecimenCollection;
	}
	public void setTimeOfSpecimenCollection(
			GregorianCalendar timeOfSpecimenCollection) {
		this.timeOfSpecimenCollection = timeOfSpecimenCollection;
	}
	
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
	
	@ManyToOne
	@NotNull
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	@Column(nullable = true)
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loincCode == null) ? 0 : loincCode.hashCode());
		result = prime
				* result
				+ ((timeOfSpecimenCollection == null) ? 0
						: timeOfSpecimenCollection.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BasicLaboratoryObject))
			return false;
		BasicLaboratoryObject other = (BasicLaboratoryObject) obj;
		if (loincCode == null) {
			if (other.loincCode != null)
				return false;
		} else if (!loincCode.equals(other.loincCode))
			return false;
		if (timeOfSpecimenCollection == null) {
			if (other.timeOfSpecimenCollection != null)
				return false;
		} else if (!timeOfSpecimenCollection
				.equals(other.timeOfSpecimenCollection))
			return false;
		return true;
	}
	
	
}
