package cjmb.com.raasrelease01.create;

/**
 * <pre>
 * POJO that contains information to create a new subscriber.
 * JSON format:
 * {
 * 	"username":"String",
 * 	"subscriber":"String"
 * }
 * </pre>
 */
public class CreateSubscriberRequest {
	private String username;
	private String subscriber;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}
}
