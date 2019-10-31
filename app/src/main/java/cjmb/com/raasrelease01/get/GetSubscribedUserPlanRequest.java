package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the username of an account and one that subscribed to.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"subscribedUser":"String"
 * }
 * </pre>
 */
public class GetSubscribedUserPlanRequest {
	private String username;
	private String subscribedUser;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubscribedUser() {
		return subscribedUser;
	}
	public void setSubscribedUser(String subscribedUser) {
		this.subscribedUser = subscribedUser;
	}
}
