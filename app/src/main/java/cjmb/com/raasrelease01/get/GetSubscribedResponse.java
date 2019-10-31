package cjmb.com.raasrelease01.get;

/**
 * <pre>
 * POJO that contains the subscribers of an account.
 * JSON format:
 * {
 * 	"list":[
    	"String"
  	],
 * 	"statusCode":number
 * }
 * </pre>
 */
public class GetSubscribedResponse {
	private Object list;
	private int statusCode;
	public Object getList() {
		return list;
	}
	public void setList(Object list) {
		this.list = list;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
