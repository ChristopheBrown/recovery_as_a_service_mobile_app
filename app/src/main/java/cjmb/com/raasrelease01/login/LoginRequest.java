package cjmb.com.raasrelease01.login;

/**
 * <pre>
 * POJO that contains the login information of an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"password":"String"
 * }
 * </pre>
 */
public class LoginRequest {
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
