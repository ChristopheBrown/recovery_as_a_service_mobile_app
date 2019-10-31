package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains personal information of an account.
 * JSON format:
 * {
 * 	"address":"String",
 * 	"firstName":"String",
 * 	"lastName":"String",
 * 	"height":dobule,
 * 	"weight":double,
 * 	"phone":double,
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetPersonalInformationResponse {
	private String address;
	private String firstName;
	private String lastName;
	private Object height;
	private Object weight;
	private Object phone;
	private int statusCode;
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
	public Object getHeight() {
		return height;
	}
	public void setHeight(Object height) {
		this.height = height;
	}
	public Object getWeight() {
		return weight;
	}
	public void setWeight(Object weight) {
		this.weight = weight;
	}
	public Object getPhone() {
		return phone;
	}
	public void setPhone(Object phone) {
		this.phone = phone;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
