package cjmb.com.raasrelease01.create;

/**
 * <pre>
 * POJO that contains information to create a new account.
 * JSON format:
 * {
 * 	"height":double,
 * 	"phone":double,
 * 	"weight":double,
 * 	"role":"String",
 * 	"username":"String",
 * 	"address":"String",
 * 	"firstName":"String",
 * 	"lastName":"String",
 * 	"dateOfBirth":"String"
 * }
 * </pre>
 */
public class CreateAccountRequest {
	private double height;
	private double phone;
	private double weight;
	
	private String role;
	private String username;
	private String address;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getPhone() {
		return phone;
	}
	public void setPhone(double phone) {
		this.phone = phone;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
