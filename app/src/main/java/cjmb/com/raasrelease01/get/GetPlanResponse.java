package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the plan of an account.
 * JSON format:
 * {
 *  "doctor": "String",
 *  "endDate": "String",
 *  "progress": object,
 *  "startDate": "String",
 *  "tasks": [
 *    {
 *      "amount": double,
 *      "completed": boolean,
 *      "date": "String",
 *      "requirementType": "String",
 *      "task": "String"
 *    }
 *  ],
 *  "username": "String"
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetPlanResponse {
	private Object tasks;
	private Object progress;
	private int statusCode;
	private String doctor;
	private String endDate;
	private String startDate;
	private String username;
	
	public Object getTasks() {
		return tasks;
	}
	public void setTasks(Object tasks) {
		this.tasks = tasks;
	}
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
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}