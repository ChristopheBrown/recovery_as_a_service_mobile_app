package cjmb.com.raasrelease01;

/*Class is self-explanatory, reference the corresponding XML activity preview if you are
* having troubles*/

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import cjmb.com.raasrelease01.get.GetRequest;
import cjmb.com.raasrelease01.update.UpdateActivationRequest;
import cjmb.com.raasrelease01.update.UpdatePasswordRequest;
import cjmb.com.raasrelease01.update.UpdateResponse;

public class ChangePasswordActivity extends AppCompatActivity implements StatusCode {

    private String username;
    private String oldPassword;
    private String confrimOldPassword;
    private String newPassword;
    private String confrimNewPassword;

    private String usernameGetRequestJSONObject;
    private UpdatePasswordRequest updatePasswordRequest;
    private boolean newPatientIsTrue;

    private TextView changePasswordHeaderTextView;
    private EditText changePasswordEmailEditText;
    private EditText changePasswordOldPasswordEditText;
    private EditText changePasswordConfirmOldPasswordEditText;
    private EditText changePasswordNewPasswordEditText;
    private EditText changePasswordConfirmNewPasswordEditText;
    private Button changePasswordFinishButton;

    private Context mContext;

    final private String onPasswordChangeSuccess = "New password set successfully. ";
    final private String onPasswordChangeFailure = "Password change failed. ";
    final private String onNewPatientSuccess = "Activating account...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changePasswordHeaderTextView = (TextView) findViewById(R.id.changePasswordHeaderTextView);
        changePasswordEmailEditText = (EditText) findViewById(R.id.changePasswordEmailEditText);
        changePasswordOldPasswordEditText = (EditText) findViewById(R.id.changePasswordOldPasswordEditText);
        changePasswordConfirmOldPasswordEditText = (EditText) findViewById(R.id.changePasswordConfirmOldPasswordEditText);
        changePasswordNewPasswordEditText = (EditText) findViewById(R.id.changePasswordNewPasswordEditText);
        changePasswordConfirmNewPasswordEditText = (EditText) findViewById(R.id.changePasswordConfirmNewPasswordEditText);
        changePasswordFinishButton = (Button) findViewById(R.id.changePasswordFinishButton);

        mContext = getApplicationContext();
        GlobalValue globalValue = (GlobalValue) mContext;

        updatePasswordRequest = new UpdatePasswordRequest();

        usernameGetRequestJSONObject = globalValue.getUserNameGetRequest();
        if (getIntent().hasExtra("NEW_PATIENT_BOOLEAN"))
            newPatientIsTrue = getIntent().getExtras().getBoolean("NEW_PATIENT_BOOLEAN",false);

        GetRequest getRequest = new Gson().fromJson(usernameGetRequestJSONObject,GetRequest.class);
        changePasswordEmailEditText.setText(getRequest.getUsername());
        changePasswordEmailEditText.setEnabled(false);

        changePasswordFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = changePasswordEmailEditText.getText().toString();
                oldPassword = changePasswordOldPasswordEditText.getText().toString();
                confrimOldPassword = changePasswordConfirmOldPasswordEditText.getText().toString();
                newPassword = changePasswordNewPasswordEditText.getText().toString();
                confrimNewPassword = changePasswordConfirmNewPasswordEditText.getText().toString();

                if (    username.length()==0 ||
                        oldPassword.length()==0 ||
                        confrimOldPassword.length()==0 ||
                        newPassword.length()==0 ||
                        confrimNewPassword.length()==0) {
                    new PopTart("Please fill all fields.");
                    return;
                }

                if (!newPassword.equals(confrimNewPassword))
                {
                    new PopTart("New Passwords do not match.");
                    return;
                }

                if (!oldPassword.equals(confrimOldPassword))
                {
                    new PopTart("Old Passwords do not match.");
                    return;
                }
                if (oldPassword.equals(newPassword)) {
                    new PopTart("Old and new passwords cannot be the same.");
                    return;
                }

                updatePasswordRequest.setUsername(username);
                updatePasswordRequest.setOldPassword(oldPassword);
                updatePasswordRequest.setNewPassword(newPassword);

                String updatePasswordRequestJson = new Gson().toJson(updatePasswordRequest);

                RecoveryAsAServiceNetworkTransaction changePasswordTransaction =
                        new RecoveryAsAServiceNetworkTransaction(mContext,"/update/password",updatePasswordRequestJson);

                changePasswordTransaction.run(new ServerCallback() {

                    @Override
                    public void onSuccess(String result) {
                        UpdateResponse updateResponse = new Gson().fromJson(result,UpdateResponse.class);
                        int statusCode = updateResponse.getStatusCode();
                        switch (statusCode) {
                            case SUCCESS:
                                if (newPatientIsTrue)
                                {
                                    new PopTart(onNewPatientSuccess);
                                    setActive();
                                }
                                else new PopTart(onPasswordChangeSuccess);
                                break;
                            case UNAUTHORIZED:
                            case FORBIDDEN:
                            case NOTFOUND:
                                new PopTart(onPasswordChangeFailure);
                                break;
                        }
                    }
                });
            }
        });
    }


    void setActive() {
        /*For new users, their accounts will be set to active when they change their password from
        * their birthdate*/
        final UpdateActivationRequest updateActivationRequest = new UpdateActivationRequest();
        updateActivationRequest.setUsername(username);
        updateActivationRequest.setActivated(true);

        String updateActivationRequestJSON = new Gson().toJson(updateActivationRequest);
        RecoveryAsAServiceNetworkTransaction recoveryAsAServiceNetworkTransaction =
                new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/update/activation",updateActivationRequestJSON);

        recoveryAsAServiceNetworkTransaction.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                UpdateResponse updateResponse = new Gson().fromJson(result,UpdateResponse.class);
                if (updateResponse.getStatusCode() == SUCCESS)
                {
                    new PopTart("Account is activated!");
                    // Password changed successfully - send user back to landing page, package a getRequest Json object with them
                    Intent landingPageActivity = new Intent(getApplicationContext(), cjmb.com.raasrelease01.HomeActivity.class);
                    startActivity(landingPageActivity);
                }
            }
        });
    }
}
