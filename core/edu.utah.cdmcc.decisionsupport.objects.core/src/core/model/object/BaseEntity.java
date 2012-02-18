package core.model.object;

//import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;

@MappedSuperclass
@org.hibernate.annotations.GenericGenerator(name="hibernate-uuid", strategy = "uuid")
public class BaseEntity implements Identifiable {

	private String id;
	private int version;
	protected Boolean valid = true;
	
	public BaseEntity() {
		this.valid = true;
	}

	@Id
	@GeneratedValue(generator="hibernate-uuid")
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Version
	@Column(nullable = true)
	public int getVersion() {
		return version;
	}

	@SuppressWarnings("unused")
	private void setVersion(final int version) {
		this.version = version;
	}
	
	@NotNull
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}
}
