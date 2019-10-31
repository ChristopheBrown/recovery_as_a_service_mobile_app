package cjmb.com.raasrelease01;

/*Device monitoring, self-reporting of workouts, and calendar views*/

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.squareup.timessquare.CalendarPickerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import cjmb.com.raasrelease01.get.GetPatientPlanRequest;
import cjmb.com.raasrelease01.get.GetPlanResponse;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String daysOfTheWeek[] = {"Sat", "Sun", "Mon", "Tue", "Wed",
            "Thu", "Fri"};

    String monthsOfTheYear[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    DecimalFormat mFormat= new DecimalFormat("00");

    private LinearLayout ReportWorkoutLinearLayout;
    private LinearLayout DeviceMonitoringLinearLayout;
    private CalendarPickerView datePicker;


    private OnFragmentInteractionListener mListener;

    /*Custom class used for constructing Task objects
     on the client side (mirrors their structure in the AWS server)*/
    private class Task {
        private String task;
        private double amount;
        private boolean completed;
        private String requirementType;
        private String dateString;
        private Date date;

        public Task(String task, double amount, String dateString, String requirementType, boolean completed){
            this.task = task;
            this.amount = amount;
            this.requirementType = requirementType;
            this.completed = completed;
            this.dateString = dateString;

            // covert to an actual date
            try {
                this.date = parseDate(dateString);
            } catch (ParseException e) {
                new PopTart("Error parsing constructor in WorkFrag. " + dateString);
//                new PopTart("Error at date parsing constructor in WorkFrag.");
            }
        }
    }

    // Data collections for tasks under a plan as well as the corresponding dates per task
    /*Sorting procedures are used later, hence the data types*/
    ArrayList<Task> tasks;
    Vector<Date> dateSet;

    public WorkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkFragment newInstance(String param1, String param2) {
        WorkFragment fragment = new WorkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new PopTart(getActivity().findViewById(android.R.id.content),"Syncing calendar...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        ReportWorkoutLinearLayout = view.findViewById(R.id.ReportWorkoutLinearLayout);
        DeviceMonitoringLinearLayout = view.findViewById(R.id.DeviceMonitoringLinearLayout);

        ReportWorkoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReportWorkoutActivity =
                        new Intent(getActivity().getApplicationContext(), cjmb.com.raasrelease01.ReportWorkoutActivity.class);
                startActivity(ReportWorkoutActivity);
            }
        });

        DeviceMonitoringLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DeviceMonitoringActivity =
                        new Intent(getActivity().getApplicationContext(), cjmb.com.raasrelease01.DeviceMonitoringActivity.class);
                startActivity(DeviceMonitoringActivity);
            }
        });

        dateSet = new Vector<Date>();
        tasks = new ArrayList<Task>();

        // iterate over all days looking for the earliest day, start calendar from there
        // have a function on app login that populates the calendar with tasks from EACH plan

        /*DOCUMENTATION NOTE - the calendar is hardcoded to start from Sept 1 2018. It does not
        * begin from the earlist task date. This is because on design day, we needed to show
        * tasks that were set BEFORE the current date.*/

        /*Datepicker is essentially a reference to the calendar object used on this fragment*/

        // datePicker.highlight can light up days with tasks, record-high steps, etc
        // for onDateSelected(), if the data exists in the vector, toast the task(s) for the day
        datePicker = getView().findViewById(R.id.calendarView);

        Date today = null;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        String thisMonth = "09"+"/01/"+year;
        try {
            today = parseDate(thisMonth);
        } catch (ParseException e) {
            new PopTart(e.getMessage());
        }

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        datePicker.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(today);

        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                /* This code below is designed to retrieve a date in various formats in the event
                * the serve programmer has dates formatted differently throughout. I did this rather
                 * than asking them to ensure dates are uniformly formatted */

                // Double digit day : Single digit month
                String selectedDateA = (calSelected.get(Calendar.MONTH) + 1)
                        + "/" + mFormat.format(Double.valueOf(calSelected.get(Calendar.DAY_OF_MONTH)))
                        + "/" + calSelected.get(Calendar.YEAR);

                // Single digit day : Single digit month
                String selectedDateB = (calSelected.get(Calendar.MONTH) + 1)
                        + "/" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + "/" + calSelected.get(Calendar.YEAR);

                // Double digit day : Double digit month
                String selectedDateC = mFormat.format(Double.valueOf(calSelected.get(Calendar.MONTH) + 1))
                        + "/" + mFormat.format(Double.valueOf(calSelected.get(Calendar.DAY_OF_MONTH)))
                        + "/" + calSelected.get(Calendar.YEAR);

                // Single digit day : Double digit month
                String selectedDateD = mFormat.format(Double.valueOf(calSelected.get(Calendar.MONTH) + 1))
                        + "/" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + "/" + calSelected.get(Calendar.YEAR);

                String dialogMessage="";
                boolean dateHasTasks = false;

                // "For each task in plan X, if the date for the task matches the selected date
                // (the one tapped on screen) the add the task's action (string member) to the list
                // in the dialog message that pops up." \u2022 is ascii for a list bullet point
                for (Task t : tasks)
                    if (t.dateString.equals(selectedDateA) || t.dateString.equals(selectedDateB)
                            || t.dateString.equals(selectedDateC) || t.dateString.equals(selectedDateD))
                    {
                        dateHasTasks=true;
                        dialogMessage = dialogMessage + "\u2022" + t.task +  "\n";
                    }
                    /*Now only show a dialog popup if there were tasks for that day. Non empty list*/
                if (dateHasTasks) showDialog(dialogMessage);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        getPlans();
    }

    public void getPlans() {
        /*Get the list of plans so that we can get the list of all the tasks in a plan and populate
        * the calendar with them */

        final Context mContext = getActivity().getApplicationContext();

        final GlobalValue globalVariable = (GlobalValue) mContext;
        final String usernameGetRequestJSONString  = globalVariable.getUserNameGetRequest();

        RecoveryAsAServiceNetworkTransaction getPlans =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/plannames",usernameGetRequestJSONString);


        getPlans.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("planNames");

                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> list = new Gson().fromJson(jsonArray, listType);

                for (String plan : list) {
                    /* Get the tasks for each plan*/
                    getTasks(plan);
                }
            }
        });
    }


    public void getTasks(String plan) {
        final Context mContext = getActivity().getApplicationContext();
        final GlobalValue globalVariable = (GlobalValue) mContext;

        GetPatientPlanRequest getPatientPlanRequest = new GetPatientPlanRequest();
        {
            getPatientPlanRequest.setUsername(globalVariable.getUserName());
            getPatientPlanRequest.setPatientUsername(globalVariable.getUserName());
            getPatientPlanRequest.setPlanName(plan);
        }

        String getPatientPlanRequestJSONObject = new Gson().toJson(getPatientPlanRequest);

        RecoveryAsAServiceNetworkTransaction getPlan =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/patientplan",getPatientPlanRequestJSONObject);

        getPlan.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPlanResponse getPlanResponse =
                        new Gson().fromJson(result,GetPlanResponse.class);

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(result);
                JsonArray jsonArray = jsonObject.getAsJsonArray("tasks");


                /*Vector holds an ArrayList of Task object. There is one ArrayList<Task> per plan,
                * so the vector holds all tasks for ALL plans*/
                Vector< ArrayList<Task>> taskList = new Vector< ArrayList<Task>>();

                /*Using the list of plans from before this mehtod is called,
                 we cab now iterate over the tasks in each plan
                * and make Task objects for them*/
                if (jsonArray != null) {
                    for (JsonElement e : jsonArray)
                    {
                        JsonObject object = e.getAsJsonObject();
                        String taskString = object.get("task").getAsString();
                        double amount = object.get("amount").getAsDouble();
                        String requirementType = object.get("requirementType").getAsString();
                        boolean completed = object.get("completed").getAsBoolean();
                        String date = object.get("date").getAsString();
                        Task task = new Task(taskString,amount,date,requirementType,completed);

                        tasks.add(task);
                        taskList.add(tasks);
                    }


                    /*When the above loop finishes, we have a collection of the Tasks with
                    * the dates for each task. At this point we need to highlight the calendar
                    * dates that have tasks scheduled per the patients recovery plan*/
                    initializeCalendar(taskList);
                    datePicker.highlightDates(dateSet);
                }

            }
        });
    }

    public void initializeCalendar(Vector< ArrayList<Task>> taskss){

        dateSet = new Vector<Date>();

    for (int i =0;i<taskss.size();i++)
        for (Task t : taskss.get(i))
            dateSet.add(t.date);
    }

    private Date parseDate(String date) throws ParseException {
        /*This is how dates are formatted for plans/tasks to be properly used in our calendar*/
        SimpleDateFormat incomingFormat = new SimpleDateFormat("MM/dd/yyyy" );
        Date returnDate = incomingFormat.parse(date);
        return returnDate;
    }

    public void showDialog(String message) {
        /*showDialog serves to create an on-screen popup message when a calendar date is tapped,
        * showing all of the tasks for that date*/
        final Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        TextView MessageTextView = (TextView) dialog.findViewById(R.id.MessageTextView);
        Button DismissButton = (Button) dialog.findViewById(R.id.DismissButton);

        MessageTextView.setText(message);

        dialog.show();

        DismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
