package cjmb.com.raasrelease01.record;

/**
 * <pre>
 * POJO that contains the general response of recording something.
 * JSON format:
 * {
 * 	"statusCode":number
 * }
 * </pre>
 */
public class RecordResponse {
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}