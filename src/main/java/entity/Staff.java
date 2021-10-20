package entity;

import java.util.Objects;

public class Staff {
	
	private long staffId;
	private String firstName;
	private String lastName;
	private Phone phone;
	private Staff manager;
	private String email;
	
	/**
	 * 
	 * @param staffId
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param manager
	 * @param email
	 */
	public Staff(long staffId, String firstName, String lastName, Phone phone, String email) {
		super();
		this.staffId = staffId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}
	
	public Staff(long staffId, String firstName, String lastName, Phone phone) {
		super();
		this.staffId = staffId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	/**
	 * 
	 * @param staffId
	 */
	public Staff(long staffId) {
		super();
		this.staffId = staffId;
	}

	public Staff() {
		super();
	}
	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
	public Staff getManager() {
		return manager;
	}
	public void setManager(Staff manager) {
		this.manager = manager;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Staff [staffId=" + staffId + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone
				+ ", manager=" + manager + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(staffId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Staff other = (Staff) obj;
		return staffId == other.staffId;
	}
}
