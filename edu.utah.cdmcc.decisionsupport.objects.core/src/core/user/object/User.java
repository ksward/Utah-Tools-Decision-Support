package core.user.object;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import core.model.object.BaseEntity;

@Entity
@org.hibernate.annotations.GenericGenerator(name="hibernate-uuid", strategy="uuid")
@NamedQuery(name = "getUserByAccountName", query = "from User where accountName = :accountName")
public class User extends BaseEntity {
	public static final String GETUSERBYACCOUNTNAME = "getUserByAccountName";
	private String lastName;
	private String firstName;
	private Boolean administrativeRights = false;
	private String accountName;
	private String passwordDigest;

	public User(String lastName, String firstName, String accountName,
			String passwordDigest) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.accountName = accountName;
		this.passwordDigest = passwordDigest;
	}

	public User() {
	}

	@NotNull
	@Length(min = 2, max = 20)
	public String getLastName() {
		return lastName;
	}

	@NotNull
	@Length(min = 2, max = 20)
	public String getFirstName() {
		return firstName;
	}

	@Transient
	public String getDisplayName() {
		return getLastName().trim() + ", " + getFirstName().trim();
	}

	@NotNull
	@Length(min = 2, max = 20)
	public String getAccountName() {
		return accountName;
	}

	@NotNull
	public String getPasswordDigest() {
		return passwordDigest;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setPasswordDigest(String passwordDigest) {
		this.passwordDigest = passwordDigest;
	}

	public Boolean getAdministrativeRights() {
		return administrativeRights;
	}

	public void setAdministrativeRights(Boolean administrativeRights) {
		this.administrativeRights = administrativeRights;
	}

	@Override
	@Transient
	public String toString() {
		return "User [getDisplayName()=" + getDisplayName() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((passwordDigest == null) ? 0 : passwordDigest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (passwordDigest == null) {
			if (other.passwordDigest != null)
				return false;
		} else if (!passwordDigest.equals(other.passwordDigest))
			return false;
		return true;
	}
}
