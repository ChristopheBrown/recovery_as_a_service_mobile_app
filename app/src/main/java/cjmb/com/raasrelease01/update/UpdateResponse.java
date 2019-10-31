package cjmb.com.raasrelease01.update;

/**
 * <pre>
 * POJO that contains the status of a general update function.
 * JSON format:
 * {
 * 	"statusCode":number
 * }
 * </pre>
 */
public class UpdateResponse {
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
