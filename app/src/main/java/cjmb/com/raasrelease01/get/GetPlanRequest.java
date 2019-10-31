package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the username and plan of an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"planName":"String"
 * }
 * </pre>
 */
public class GetPlanRequest {
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
