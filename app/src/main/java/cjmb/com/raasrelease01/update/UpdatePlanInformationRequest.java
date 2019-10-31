package cjmb.com.raasrelease01.update;

/**
 * <pre>
 * POJO that contains the field to update in an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"planName":"String",
 * 	"field":"String",
 * 	"value":Object
 * }
 * </pre>
 */
public class UpdatePlanInformationRequest {
	private String username;
	private String planName;
	private String field;
	private Object value;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
