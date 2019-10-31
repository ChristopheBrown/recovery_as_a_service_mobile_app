package cjmb.com.raasrelease01;

/*The global variable class is meant to hold an accounts personal information while the user
* is logged in. This information is requested from the server upon successful login.
* It is then held throughout runtime. One instance of this class is made and can be accessed
* from anywhere by instantiating like so:
*
* final GlobalValue globalVariable = (GlobalValue) getApplication().getApplicationContext();
*
* Calling any of the get methods in the class will allow retrieval of the information.*/

import android.app.Application;
import android.content.Context;

public class GlobalValue extends Application {
    private String userName;
    private String firstName;
    private String lastName;
    private String address;
    private double height;
    private double weight;
    private double phoneNumber;

    private static Context context;

    private String userNameGetRequest;

    public GlobalValue() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String aName) {
        userName = aName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserNameGetRequest() {
        return userNameGetRequest;
    }

    public void setUserNameGetRequest(String userNameGetRequest) {
        this.userNameGetRequest = userNameGetRequest;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalValue.context = getApplicationContext();
    }

    public static Context getContext() {
        return  GlobalValue.context;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
