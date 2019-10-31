package cjmb.com.raasrelease01.process;

/**
 * <pre>
 * POJO that contains the general response to processing information for an account.
 * JSON format:
 * {
 * 	"statusCode":number
 * }
 * </pre>
 */
public class ProcessResponse {
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
