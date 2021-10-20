package entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Customer {
	
	@BsonId
	private String customerId;
	@BsonProperty("first_name")
	private String firstName;
	@BsonProperty("last_name")
	private String lastName;
	private String email;
	@BsonProperty("registration_date")
	private Date registrationDate;
	
	private Address address;
	private List<Phone> phones;
	
	/**
	 * @param customerId
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param registrationDate
	 */
	public Customer(String customerId, String firstName, String lastName, String email, Date registrationDate) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registrationDate = registrationDate;
	}
	
	public Customer(String customerId, String firstName, String lastName, String email, List<Phone> phones) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phones = phones;
	}

	public Customer(String customerId) {
		super();
		this.customerId = customerId;
	}

	/**
	 * 
	 */
	public Customer() {
	}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", registrationDate="
				+ df.format(registrationDate) + ", phone=" + phones + "]";
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return customerId.equalsIgnoreCase(other.customerId);
	}
	
}
