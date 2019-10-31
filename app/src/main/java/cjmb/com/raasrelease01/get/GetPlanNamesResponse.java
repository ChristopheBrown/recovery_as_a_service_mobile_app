package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the name of the plans in an account.
 * JSON format:
 * {
 * 	"planNames":[
    	"String"
  	],
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetPlanNamesResponse {
	private Object planNames;
	private int statusCode;

	public Object getPlanNames() {
		return planNames;
	}
	public void setPlanNames(Object planNames) {
		this.planNames = planNames;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}