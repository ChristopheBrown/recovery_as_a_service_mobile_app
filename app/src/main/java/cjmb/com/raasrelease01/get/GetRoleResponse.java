package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the role of an account.
 * JSON format:
 * {
 * 	"role":"String",
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetRoleResponse {
	private String role;
	private int statusCode;
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
