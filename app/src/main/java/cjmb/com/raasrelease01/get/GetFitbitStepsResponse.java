package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the plans of an account.
 * JSON format:
 * {
 * 	"steps":{
 *  	"date":"value"
 *  }
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetFitbitStepsResponse {
	private Object steps;
	private int statusCode;

	public Object getSteps() {
		return steps;
	}
	public void setSteps(Object steps) {
		this.steps = steps;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
