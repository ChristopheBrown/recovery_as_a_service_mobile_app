package cjmb.com.raasrelease01;

/*This class is used but not needed. When you tap on a username in the User's tab, you can
* see their most updated plan and the percent completion.
*
* This is not used because the Users tab is for subscribed users, not users the logged-in account
* is subscribed to. It can be used if you implement the code to show the user's subscriptions*/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import cjmb.com.raasrelease01.get.GetPersonalInformationResponse;
import cjmb.com.raasrelease01.get.GetPlanRequest;
import cjmb.com.raasrelease01.get.GetPlanResponse;
import cjmb.com.raasrelease01.get.GetRequest;

public class SubscribedUserActivity extends AppCompatActivity {

    private String usernameGetRequest;
    private String subscribedUserUsername;
    private String subscribedUserUsernameGetRequestJSONObject;

    private ListView SubscribedUserPlansListView;
    private TextView SubscribedUserFirstNameTextView;
    private TextView SubscribedPlanNameTextView;
    private TextView SubscribedUserPercentageTextView;
    private TextView SubscribedUserTodayTextView;
    private ListView SubscribedUserTodaysTasksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_user);

        SubscribedUserPlansListView = (ListView) findViewById(R.id.SubscribedUserPlansListView);
        SubscribedUserFirstNameTextView = (TextView) findViewById(R.id.SubscribedUserFirstNameTextView);
        SubscribedPlanNameTextView = (TextView) findViewById(R.id.SubscribedPlanNameTextView);
        SubscribedUserPercentageTextView = (TextView) findViewById(R.id.SubscribedUserPercentageTextView);
        SubscribedUserTodayTextView = (TextView) findViewById(R.id.SubscribedUserTodayTextView);
        SubscribedUserTodaysTasksListView = (ListView) findViewById(R.id.SubscribedUserTodaysTasksListView);

        if(getIntent().hasExtra("USERNAME_GET_REQUEST_STRING"))
            usernameGetRequest = getIntent().getExtras().getString("USERNAME_GET_REQUEST_STRING");
        if(getIntent().hasExtra("SUBSCRIBED_USER_USERNAME"))
            subscribedUserUsername = getIntent().getExtras().getString("SUBSCRIBED_USER_USERNAME");

        GetRequest getSubscribedUserGetRequest = new GetRequest();
        {
            getSubscribedUserGetRequest.setUsername(subscribedUserUsername);

        }

        subscribedUserUsernameGetRequestJSONObject = new Gson().toJson(getSubscribedUserGetRequest);

        getSubscribedUserFirstName(getSubscribedUserGetRequest);
        getSubscribedUserPlansList();
    }

    private void getSubscribedUserPlansList() {
        RecoveryAsAServiceNetworkTransaction getSubScribedUserPlans =
                new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/get/plannames",subscribedUserUsernameGetRequestJSONObject);

        getSubScribedUserPlans.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("planNames");

                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> list = new Gson().fromJson(jsonArray, listType);

                if (list == null || list.isEmpty())
                {
                    new PopTart("User has no active plans!");
                    SubscribedPlanNameTextView.setText("No Active Plan");
                    SubscribedUserPercentageTextView.setText("0%");
                    SubscribedUserTodayTextView.setText("");
                    return;
                }


                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_simple_array_list_adapter, list);

                final List<String> list2 = list;

                if (SubscribedUserPlansListView == null) new PopTart("Null Object!");

                SubscribedUserPlansListView.setAdapter(arrayAdapter);
                SubscribedUserPlansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SubscribedPlanNameTextView.setText(list2.get(i));

                        GetPlanRequest getPlanRequest = new GetPlanRequest();
                        {
                            getPlanRequest.setUsername(subscribedUserUsername);
                            getPlanRequest.setPlanName(list2.get(i));
                        }

                        getSubscribedUserPlanPercentage(getPlanRequest);

                    }
                });
            }
        });
    }

    private void getSubscribedUserFirstName(GetRequest getSubscribedUserGetRequest) {
        RecoveryAsAServiceNetworkTransaction getSubscribedUserPersonalInformation =
                new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/get/personalinformation", subscribedUserUsernameGetRequestJSONObject);

        getSubscribedUserPersonalInformation.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPersonalInformationResponse getPersonalInformationResponse =
                        new Gson().fromJson(result,GetPersonalInformationResponse.class);

                String firstName = getPersonalInformationResponse.getFirstName();
                SubscribedUserFirstNameTextView.setText(firstName);
            }
        });
    }

    private void getSubscribedUserPlanPercentage(GetPlanRequest getPlanRequest) {
        String getPlanRequestJSONObject = new Gson().toJson(getPlanRequest);
        RecoveryAsAServiceNetworkTransaction getSubscribedUserPlan =
                new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/get/plan",getPlanRequestJSONObject);

        getSubscribedUserPlan.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPlanResponse getPlanResponse = new Gson().fromJson(result,GetPlanResponse.class);

                SubscribedUserPercentageTextView.setText(Double.toString((double)getPlanResponse.getProgress()*100.0) + "%");

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("tasks");

                List<String> taskNames = new Vector<String>();

                for (JsonElement e : jsonArray) // Get List of task names
                {
                    JsonObject item = e.getAsJsonObject();
                    taskNames.add(item.get("task").getAsString());
                }

                String daysArray[] = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                SubscribedUserTodayTextView.setText(daysArray[day] + ":");

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_simple_array_list_adapter, taskNames);

                SubscribedUserTodaysTasksListView.setAdapter(arrayAdapter);
                SubscribedUserTodaysTasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
            }
        });
    }
}
