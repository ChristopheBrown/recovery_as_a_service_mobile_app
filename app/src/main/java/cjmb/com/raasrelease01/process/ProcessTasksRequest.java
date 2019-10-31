package cjmb.com.raasrelease01.process;

/**
 * <pre>
 * POJO that contains the planName of an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"planName":"String"
 * }
 * </pre>
 */
public class ProcessTasksRequest {
	private String username;
	private String planName;
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
}
