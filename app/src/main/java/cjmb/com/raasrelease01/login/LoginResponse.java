package cjmb.com.raasrelease01.login;

/**
 * <pre>
 * POJO that contains the login response of an account.
 * JSON format:
 * {
 * 	"statusCode":number
 * }
 * </pre>
 */
public class LoginResponse {
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
