package cjmb.com.raasrelease01;

/*Logging into our application. There are no saved states in this app. If you restart the app,
* you will always need to relogin.*/

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import cjmb.com.raasrelease01.get.GetRequest;
import cjmb.com.raasrelease01.login.LoginRequest;
import cjmb.com.raasrelease01.login.LoginResponse;

public class LoginActivity extends AppCompatActivity implements AdvanceUserInformation, StatusCode {


    /*Login credentials*/
    private String username;
    private String password;

    /*For network transactions - see java docs for information on requests*/
    private LoginRequest loginRequest;
    private String loginRequestJson;
    private GetRequest usernameGetRequest;

    /*Boolean for checkbox on login page.
    * In our design, patients have accounts created for them by their doctor. The password is
    * preset to their birthdate. So when a new patient logs in for the first time, they
    * must change their password. This is how we know to make users change password.*/
    private boolean newPatient = false;

    /*Items on the login page*/
    private TextView landingPageTextView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox newPatientCheckBox;

    /*Items on login page*/
    private ConstraintLayout constraintLayout;

    /*Animation for background colors*/
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        // init constraintLayout
        constraintLayout = findViewById(R.id.constraintLayout);

        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        // setting enter fade animation duration to 1.9 seconds
        animationDrawable.setEnterFadeDuration(1900);

        // setting exit fade animation duration to 1.9 seconds
        animationDrawable.setExitFadeDuration(1900);

        final GlobalValue globalVariable = (GlobalValue) getApplication().getApplicationContext();

        landingPageTextView = (TextView) findViewById(R.id.landingPageTextView);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        newPatientCheckBox = (CheckBox) findViewById(R.id.newPatientCheckBox);

        final Context mContext = getApplicationContext();
        final Gson gson = new Gson();

        usernameGetRequest = new GetRequest();

        /*Checking the new patient box will toggle the new patient variable*/
        CheckBox newPatientCheckBox = (CheckBox) findViewById(R.id.newPatientCheckBox);
        newPatientCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPatient = !newPatient;
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopTart(findViewById(android.R.id.content),"Checking credentials...");
//                new PopTart("Checking credentials...");
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                // REGULAR CLIENT USE BEGINS HERE
                // Confirm a valid login before going to next activity
                if (checkForUsernameAndPassword()) {
                    loginRequest = new LoginRequest();
                    {
                        loginRequest.setUsername(username);
                        loginRequest.setPassword(password);
                    }
                    loginRequestJson = gson.toJson(loginRequest);

                    RecoveryAsAServiceNetworkTransaction raastransactionLogin =
                            new RecoveryAsAServiceNetworkTransaction(mContext, "/login", loginRequestJson);

                    raastransactionLogin.run(new ServerCallback() {
                        @Override
                        public void onSuccess(String result) {
                            LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);
                            int StatusCode = loginResponse.getStatusCode();
                            if (StatusCode == SUCCESS) {
                                new PopTart(findViewById(android.R.id.content),"Success");
                                initGlobalVariable();
                                advancePage(globalVariable.getUserNameGetRequest());

                            } else if (newPatient && StatusCode == FORBIDDEN) {
                                initGlobalVariable();
                                changePasswordForNewUser();
                            }
                            else new PopTart(findViewById(android.R.id.content),"Invalid Credentials");
                        }
                    });
                } else {
                    new PopTart(findViewById(android.R.id.content),"Please provide a username and password");
                }
            }
        });

        Button newAccountButton = (Button) findViewById(R.id.newAccountButton);
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Create a new account - used by 3rd parties not enlisted in a recovery program.*/
                Intent createAccountActivity = new Intent(getApplicationContext(), cjmb.com.raasrelease01.CreateAccountActivity.class);
                startActivity(createAccountActivity);
            }
        });
    }

    @Override
    public void advancePage(String userInformation) {
        Intent SelectPlanActivity = new Intent(getApplicationContext(), cjmb.com.raasrelease01.HomeActivity.class);
        SelectPlanActivity.putExtra("cjmb.com.raas0.USER_INFORMATION", userInformation);
        startActivity(SelectPlanActivity);
    }

    private boolean checkForUsernameAndPassword() {
        if (this.username.length() > 0 && this.password.length() > 0) return true;
        return false;
    }

    private void changePasswordForNewUser() {
        /*New patients do not have their *active* value set to true. This is
         * verified on login, and a FORBIDDEN will be returned. If the user
         * checks the newPatient box, then the user and server are in
         * agreement that the user is a first timer, and their password
         * must be changed. */
        Intent ChangePasswordActivity = new Intent(getApplicationContext(), cjmb.com.raasrelease01.ChangePasswordActivity.class);
//        ChangePasswordActivity.putExtra("USERNAME_GET_REQUEST_STRING", new Gson().toJson(usernameGetRequest));
        ChangePasswordActivity.putExtra("NEW_PATIENT_BOOLEAN", true);
        startActivity(ChangePasswordActivity);
    }

    private void initGlobalVariable() {
        /*See globalValue class for info regarding what this does.*/
        final GlobalValue globalVariable = (GlobalValue) getApplication().getApplicationContext();

        usernameGetRequest.setUsername(username);
        String usernameGetRequestJSONString = new Gson().toJson(usernameGetRequest);

        globalVariable.setUserName(username);
        globalVariable.setUserNameGetRequest(usernameGetRequestJSONString);
    }

    @Override
    protected void onResume() {
        /*Tells the background colors on the page to animate */
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        /*Tells the background colors on the page to not animate */
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }

}