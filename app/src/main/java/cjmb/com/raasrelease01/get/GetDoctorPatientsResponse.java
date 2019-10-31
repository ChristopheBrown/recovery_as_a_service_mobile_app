package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the name of the patients in a doctor account.
 * JSON format:
 * {
 * 	"patients":{
    	"Username":{
    		"Name":"String",
    		"PlanNames":[
    			"String"
    		]
    	}
  	},
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetDoctorPatientsResponse {
	private Object patients;
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Object getPatients() {
		return patients;
	}
	public void setPatients(Object patients) {
		this.patients = patients;
	}
}