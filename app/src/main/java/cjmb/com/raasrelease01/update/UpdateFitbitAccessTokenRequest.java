/**
 * 
 */
package cjmb.com.raasrelease01.update;

/**
 * <pre>
 * POJO that contains the new accessToken of a Fitbit account.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"accessToken":"String",
 * 	"refreshToken":"String"
 * }
 * </pre>
 */
public class UpdateFitbitAccessTokenRequest {
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
