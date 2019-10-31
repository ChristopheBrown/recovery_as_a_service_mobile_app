package cjmb.com.raasrelease01.record;

/**
 * <pre>
 * POJO that contains information to record an excercise.
 * JSON format:
 * {
 * 	"username":"String",
 *  "requirementType":"String",
 *  "amount":double
 * }
 * </pre>
 */
public class RecordExerciseRequest {
	private String username;
	private String requirementType;
	private double amount;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRequirementType() {
		return requirementType;
	}
	public void setRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
