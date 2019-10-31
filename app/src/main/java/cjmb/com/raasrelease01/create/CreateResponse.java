package cjmb.com.raasrelease01.create;

/**
 * <pre>
 * POJO that contains the general response of creating something.
 * JSON format:
 * {
 * 	"statusCode":number
 * }
 * </pre>
 */
public class CreateResponse {
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
