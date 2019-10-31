package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the plans of an account.
 * JSON format:
 * {
 * 	"plans":{
 *  "plan name": {
 *    "Doctor": "String",
 *    "EndDate": "String",
 *    "Progress": double,
 *    "StartDate": "String",
 *    "Tasks": [
 *      {
 *        "amount": double,
 *        "completed": boolean,
 *        "date": "String",
 *        "requirementType": "String",
 *        "task": "String"
 *      }
 *    ],
 *    "Username": "String"
 *  },
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetAllPlansResponse {
	private Object plans;
	private int statusCode;
	public Object getPlans() {
		return plans;
	}
	public void setPlans(Object plans) {
		this.plans = plans;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
