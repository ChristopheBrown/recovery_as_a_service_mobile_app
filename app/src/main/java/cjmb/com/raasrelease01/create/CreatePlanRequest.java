package cjmb.com.raasrelease01.create;

import java.util.ArrayList;

/**
 * <pre>
 * POJO that contains information to create a new plan.
 * JSON format:
 * {
 * 	"planName":"String",
 * 	"doctor":"String",
 * 	"endDate":"String",
 * 	"startDate":"String",
 * 	"username":"String",
 * 	"doctorUsername":"String",
 * 	"tasks": [
 * 		{
 *        "amount":double,
 *        "completed":boolean,
 *        "date":"String",
 *        "requirementType":"String",
 *        "task":"String"
 *      }
 * 	]
 * }
 * </pre>
 */
public class CreatePlanRequest {
	private String planName;
	private String doctor;
	private String endDate;
	private String startDate;
	private String username;
	private String doctorUsername;
	private ArrayList tasks;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
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
	public String getDoctorUsername() {
		return doctorUsername;
	}
	public void setDoctorUsername(String doctorUsername) {
		this.doctorUsername = doctorUsername;
	}
	public ArrayList getTasks() {
		return tasks;
	}
	public void setTasks(ArrayList tasks) {
		this.tasks = tasks;
	}
	
}
