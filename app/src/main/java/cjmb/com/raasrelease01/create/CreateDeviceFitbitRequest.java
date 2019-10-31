package cjmb.com.raasrelease01.create;

/**
 * <pre>
 * POJO that contains information to create a new fitbit.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"accessToken":"String",
 * 	"refreshToken":"String"
 * }
 * </pre>
 */
public class CreateDeviceFitbitRequest {
	private String username;
	private String accessToken;
	private String refreshToken;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
