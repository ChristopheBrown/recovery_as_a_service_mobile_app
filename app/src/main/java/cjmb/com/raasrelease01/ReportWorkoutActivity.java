package cjmb.com.raasrelease01;

/*Self-explanatory self-reporting of workouts. Look at the XML preview for this class if you
* have trouble following.*/

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import cjmb.com.raasrelease01.record.RecordExerciseRequest;
import cjmb.com.raasrelease01.record.RecordResponse;

public class ReportWorkoutActivity extends AppCompatActivity implements StatusCode{

    /*Spinner is a dropdown menu */
    private String username;
    private Spinner TaskTypeSpinner;
    private Button SendTaskButton;
    private EditText TaskAmountEditText;


    private static ArrayList<String> TaskTypes;

    private String selectedTaskType;
    private String taskAmountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_workout);

        final Context mContext = getApplicationContext();

        TaskTypeSpinner = findViewById(R.id.TaskTypeSpinner);
        SendTaskButton = findViewById(R.id.SendTaskButton);
        TaskAmountEditText = findViewById(R.id.TaskAmountEditText);

        GlobalValue globalValue = (GlobalValue) mContext;

        username = globalValue.getUserName();

        final String unitsArray[] = {"(miles)","(seconds)","(minutes)","(reps)"};

        TaskTypes = new ArrayList<String>();
        TaskTypes.add("Running (miles)");
        TaskTypes.add("Stretching (seconds)");
        TaskTypes.add("Icing (minutes)");
        TaskTypes.add("Lifting (repetitions)");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TaskTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TaskTypeSpinner.setAdapter(adapter);
        TaskTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedTaskType = TaskTypes.get(position);
                TaskAmountEditText.setHint("Enter Amount " + unitsArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SendTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TaskAmountEditText.getText().toString().equals("")) {
                    new PopTart("Please enter an amount.");
                    return;
                }

                taskAmountString = TaskAmountEditText.getText().toString();
                Double amount = Double.parseDouble(taskAmountString);

                RecordExerciseRequest recordExerciseRequest =
                        new RecordExerciseRequest();
                {
                    recordExerciseRequest.setUsername(username);
                    recordExerciseRequest.setRequirementType(selectedTaskType);
                    recordExerciseRequest.setAmount(amount);
                }

                String recordExerciseRequestJSONObject = new Gson().toJson(recordExerciseRequest);

                RecoveryAsAServiceNetworkTransaction recordExercise =
                        new RecoveryAsAServiceNetworkTransaction(mContext,"/record/exercise",recordExerciseRequestJSONObject);

                recordExercise.run(new ServerCallback() {
                    @Override
                    public void onSuccess(String result) {
                        RecordResponse recordResponse = new Gson().fromJson(result,RecordResponse.class);
                        switch (recordResponse.getStatusCode()) {
                            case SUCCESS:
                                new PopTart("Activity recorded!");
                                break;
                            case UNAUTHORIZED:
                                new PopTart("Error: Unauthorized");
                                break;
                            case FORBIDDEN:
                                new PopTart("Error: Forbidden");
                                break;
                            case NOTFOUND:
                                new PopTart("Error: Not found");
                                break;
                        }
                        finish();
                    }

                });
            }
        });

    }

}