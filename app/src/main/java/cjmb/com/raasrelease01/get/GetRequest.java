package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the username of an account.
 * JSON format:
 * {
 * 	"username":"String"
 * }
 * </pre>
 */
public class GetRequest {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
