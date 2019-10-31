package cjmb.com.raasrelease01.create;

/**
 * <pre>
 * POJO that contains information to create a new plan.
 * JSON format:
 * {
 * 	"username":"String",
 *  "planName":"String",
 *  "date":"String",
 *  "requirementType":"String",
 *  "task":"String",
 *  "amount":double
 * }
 * </pre>
 */
public class CreateTaskRequest {
	private String username;
	private String planName;
	private String date;
	private String requirementType;
	private String task;
	private double amount;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRequirementType() {
		return requirementType;
	}
	public void setRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
