package cjmb.com.raasrelease01.update;

/**
 * <pre>
 * POJO that contains the new password of an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"oldPassword":"String",
 * 	"newPassword":"String"
 * }
 * </pre>
 */
public class UpdatePasswordRequest {
	private String username;
	private String oldPassword;
	private String newPassword;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
