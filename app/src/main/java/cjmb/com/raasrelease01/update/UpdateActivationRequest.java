package cjmb.com.raasrelease01.update;

/**
 * <pre>
 * POJO that contains the activation of an account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"activated":boolean
 * }
 * </pre>
 */
public class UpdateActivationRequest {
	private String username;
	private Boolean activated;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
}
