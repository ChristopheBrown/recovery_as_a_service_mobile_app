package cjmb.com.raasrelease01;

/*This Activity loads when selecting a plan on the PlanFragment page. You'll see more information
* about a plan and its tasks*/

import android.content.Context;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cjmb.com.raasrelease01.get.GetPatientPlanRequest;
import cjmb.com.raasrelease01.get.GetPlanResponse;
import cjmb.com.raasrelease01.process.ProcessPlanRequest;
import cjmb.com.raasrelease01.process.ProcessPlanResponse;
import cjmb.com.raasrelease01.process.ProcessResponse;
import cjmb.com.raasrelease01.process.ProcessTasksRequest;

public class PlanSelectedActivity extends AppCompatActivity implements StatusCode{

    private String username;
    private String planName;

    /*Custom Task class that mirrors the implementation in the AWS server*/
    public class Task {
        private String task;
        private double amount;
        private boolean completed;
        private String requirementType;

        public Task(String task, double amount, String requirementType, boolean completed){
            this.task = task;
            this.amount = amount;
            this.requirementType = requirementType;
            this.completed = completed;
        }
    }

    /*Holds a collection of tasks with a String key representing the task's date*/
    HashMap<String,Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_selected);

        final Context mContext = getApplicationContext();

        final TextView PlanTextView = findViewById(R.id.PlanNameTextView);
        final TextView PlanDetailsTextView = findViewById(R.id.PlanDetailsTextView);
        final TextView DoctorTextView = findViewById(R.id.DoctorTextView);
        final TextView taskDetailsTextView = findViewById(R.id.taskDetailsTextView);

        final ListView TasksList = (ListView) findViewById(R.id.Tasks);

        if(getIntent().hasExtra("USERNAME_STRING"))
            username = getIntent().getExtras().getString("USERNAME_STRING");
        if(getIntent().hasExtra("PLAN_NAME_STRING"))
            planName = getIntent().getExtras().getString("PLAN_NAME_STRING");

        processTasks();

        GetPatientPlanRequest getPatientPlanRequest = new GetPatientPlanRequest();
        {
            getPatientPlanRequest.setUsername(username);
            getPatientPlanRequest.setPatientUsername(username);
            getPatientPlanRequest.setPlanName(planName);
        }

        String getPatientPlanRequestJSONObject = new Gson().toJson(getPatientPlanRequest);

        RecoveryAsAServiceNetworkTransaction getPlan =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/patientplan",getPatientPlanRequestJSONObject);

        getPlan.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPlanResponse getPlanResponse =
                        new Gson().fromJson(result,GetPlanResponse.class);


                PlanTextView.setText(planName);
                PlanDetailsTextView.setText("Until " + getPlanResponse.getEndDate());
                DoctorTextView.setText("Dr. " + getPlanResponse.getDoctor());

                tasks = new HashMap<String,Task>();

                /*dateSet is purely the date of a task. dateSetVisible is the date of a task
                * plus some addition context (can be seen in its usage). dateSet is for the algorithm
                * processing, dateSetVisible is what the user will see.*/
                final ArrayList<String> dateSet = new ArrayList<String>();
                final ArrayList<String> dateSetVisible = new ArrayList<String>();

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(result);
                JsonArray jsonArray = jsonObject.getAsJsonArray("tasks");

                if (jsonArray.size()<1) {
                    new PopTart(findViewById(android.R.id.content),"No Tasks!");
                    return;
                }

                /*Keep a numbered order for the tasks when displaying to the user.
                * Task 1 <...>
                * Task 2: <...>*/
                int i=1;

               /*Each JSON element corresponds to a task in a JSONArray. There is one JSONArray
               * per plan. So this loops over the tasks in a plan*/

               /*"For each task in a plan, convert it's JSON attributes to ones that are useable
               * in the local Task class. Then instantiate a Task() class. Store that class in
               * the Tasks HashMap alongside its dates*/
                for (JsonElement e : jsonArray)
                {
                    JsonObject object = e.getAsJsonObject();
                    String taskString = object.get("task").getAsString();
                    double amount = object.get("amount").getAsDouble();
                    String requirementType = object.get("requirementType").getAsString();
                    boolean completed = object.get("completed").getAsBoolean();
                    Task task = new Task(taskString,amount,requirementType,completed);

                    dateSet.add(object.get("date").getAsString());
                    dateSetVisible.add("Task " + i++ + ": " + object.get("date").getAsString());
                    tasks.put(object.get("date").getAsString(),task);

                }


                /*Sort dates ascending so the today is shown first and the user can scroll to older
                * dates*/
                Collections.reverse(dateSet);
                Collections.reverse(dateSetVisible);


                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(mContext,R.layout.activity_simple_array_list_adapter, dateSetVisible);

                TasksList.setAdapter(arrayAdapter);
                TasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        /*When we click on a task, we should see its description and whether or not
                        * it has been completed.*/

                        Task toDo = tasks.get(dateSet.get(i));
                        String taskName = toDo.task;
                        String trueTaskName = taskName.substring(0,taskName.indexOf(' '));

                        String completed = toDo.completed ? "COMPLETED: " : "NOT COMPLETED: ";
                        taskDetailsTextView.setText(completed + "\n" + trueTaskName + " approximately " + toDo.amount + " " + toDo.requirementType);
                    }
                });

                ProcessPlanRequest processPlanRequest
                        = new ProcessPlanRequest();
                {
                    processPlanRequest.setPlanName(planName);
                    processPlanRequest.setUsername(username);
                }

                String processPlanRequestJSONObject = new Gson().toJson(processPlanRequest);

                RecoveryAsAServiceNetworkTransaction processPlan
                        = new RecoveryAsAServiceNetworkTransaction(mContext,"/process/plan",processPlanRequestJSONObject);

                processPlan.run(new ServerCallback() {
                    @Override
                    public void onSuccess(String result) {
                        /*see description for processTasks() method. Essentially same here.
                        This also sets the percentage completion for the task when the page loads*/
                        ProcessPlanResponse processPlanResponse = new Gson().fromJson(result,ProcessPlanResponse.class);

                        double percentage = (double) ( processPlanResponse.getProgress());
                        String percentCompletion = String.valueOf((int)(percentage*100));

                        final TextView PlanTextView2 = findViewById(R.id.PlanNameTextView);
                        PlanTextView2.append( " (" +percentCompletion + "%)");
                    }
                });
            }
        });
    }

    void processTasks () {
        /*This method pushes a processTasks to the server. It's sole purpose is to simply
        * keep the server updated on a user's tasks*/
        ProcessTasksRequest processTasksRequest
                = new ProcessTasksRequest();
        {
            processTasksRequest.setPlanName(planName);
            processTasksRequest.setUsername(username);
        }

        String processTasksRequestJSONObject = new Gson().toJson(processTasksRequest);

        RecoveryAsAServiceNetworkTransaction processTasks
                = new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/process/tasks",processTasksRequestJSONObject);

        processTasks.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                ProcessResponse processResponse = new Gson().fromJson(result,ProcessResponse.class);

                switch (processResponse.getStatusCode()) {
                    case SUCCESS:
                        new PopTart(findViewById(android.R.id.content),"Tasks have been synced.");
                        break;
                    case FORBIDDEN:
                    case NOTFOUND:
                    case UNAUTHORIZED:
                        new PopTart(findViewById(android.R.id.content),"Unable to sync tasks.");
                        break;
                }
            }
        });
    }


}



