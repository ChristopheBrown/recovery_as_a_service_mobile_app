package cjmb.com.raasrelease01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import cjmb.com.raasrelease01.create.CreateAccountRequest;
import cjmb.com.raasrelease01.create.CreateResponse;
import cjmb.com.raasrelease01.get.GetRequest;

public class CreateAccountActivity extends AppCompatActivity {

    String username, emailAddress, role, phone,height,weight;
    String firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final Context mContext = getApplicationContext();
        final Gson gson = new Gson();

        final ScrollView activityCreateAccountScrollView = (ScrollView) findViewById(R.id.activityCreateAccountScrollView);
        final TextView activityCreateAccountHeaderTextView = (TextView) findViewById(R.id.activityCreateAccountHeaderTextView);
        final EditText activityCreateAccountFirstNameEditText = (EditText) findViewById(R.id.activityCreateAccountFirstNameEditText);
        final EditText activityCreateAccountLastNameEditText = (EditText) findViewById(R.id.activityCreateAccountLastNameEditText);
        final EditText activityCreateAccountUsernameEditText = (EditText) findViewById(R.id.activityCreateAccountUsernameEditText);
        final EditText activityCreateAccountRoleEditText = (EditText) findViewById(R.id.activityCreateAccountRoleEditText);
        activityCreateAccountUsernameEditText.setEnabled(false);
        activityCreateAccountUsernameEditText.setText("Non-patient");
        final EditText activityCreateAccountPhoneEditText = (EditText) findViewById(R.id.activityCreateAccountPhoneEditText);
        final EditText activityCreateAccountHeightEditText = (EditText) findViewById(R.id.activityCreateAccountHeightEditText);
        final EditText activityCreateAccountWeightEditText = (EditText) findViewById(R.id.activityCreateAccountWeightEditText);

        final Button activityCreateAccountFinishButton = (Button) findViewById(R.id.activityCreateAccountFinishButton);
        activityCreateAccountFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = activityCreateAccountFirstNameEditText.getText().toString().trim();
                lastName = activityCreateAccountLastNameEditText.getText().toString().trim();
                username = activityCreateAccountUsernameEditText.getText().toString().trim();
                role = activityCreateAccountRoleEditText.getText().toString().trim();
                phone = activityCreateAccountPhoneEditText.getText().toString().trim();
                height = activityCreateAccountHeightEditText.getText().toString().trim();
                weight = activityCreateAccountWeightEditText.getText().toString().trim();


                new PopTart("Creating account...");
                CreateAccountRequest createAccountRequest = new CreateAccountRequest();
                {

                    createAccountRequest.setPhone(Double.parseDouble(phone)); // FIX THIS FOR 10-DIGIT NUMBERS
                    createAccountRequest.setHeight(Double.parseDouble(height));
                    createAccountRequest.setWeight(Double.parseDouble(weight));
//                        createAccountRequest.setPassword(passwordA);
                    createAccountRequest.setRole(role);
                    createAccountRequest.setUsername(username);
                    createAccountRequest.setAddress(emailAddress);
                    createAccountRequest.setFirstName(firstName);
                    createAccountRequest.setLastName(lastName);
                    if (createAccountRequest.getRole().toUpperCase().equals("PATIENT") ||
                            createAccountRequest.getRole().toUpperCase().equals("DOCTOR") ) {
//                            createAccountRequest.setActivated(true);
                    }
                }

                String json = gson.toJson(createAccountRequest); Log.i("MSG",json);

                RecoveryAsAServiceNetworkTransaction recoveryAsAServiceNetworkTransaction =
                        new RecoveryAsAServiceNetworkTransaction(mContext,"/create/account",json);

                recoveryAsAServiceNetworkTransaction.run(new ServerCallback() {
                    boolean accountCreated = false;
                    @Override
                    public void onSuccess(String result) {
                        CreateResponse createResponse = gson.fromJson(result,CreateResponse.class);
//                            accountCreated = createResponse.getStatus();
                        if (accountCreated)
                        {
                            new PopTart("Success!");
                            activityCreateAccountScrollView.smoothScrollTo(0,0);
                            GetRequest getRequest = new GetRequest();
                            getRequest.setUsername(username);
                            String usernameGetRequest = gson.toJson(getRequest);
                            advancePage();
                        }
                        else
                        {
                            activityCreateAccountScrollView.smoothScrollTo(0,0);
                            new PopTart("Account Creation Failed");
                        }
                    }
                });

            }
        });
    }

    public void advancePage () {
        Intent landingPageActivity = new Intent(getApplicationContext(), cjmb.com.raasrelease01.LoginActivity.class);
//        landingPageActivity.putExtra("cjmb.com.raas0.USER_INFORMATION", userInformation);
        startActivity(landingPageActivity);
    }
}

