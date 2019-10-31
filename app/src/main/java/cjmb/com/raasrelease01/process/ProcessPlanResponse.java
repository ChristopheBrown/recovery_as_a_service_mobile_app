package cjmb.com.raasrelease01.process;

/**
 * <pre>
 * POJO that contains the plans of an account.
 * JSON format:
 * {
 * 	"statusCode":number,
 * 	"progress":double
 * }
 * </pre>
 */
public class ProcessPlanResponse {
	private int statusCode;
	private Object progress;

	public Object getProgress() {
		return progress;
	}
	public void setProgress(Object progress) {
		this.progress = progress;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
