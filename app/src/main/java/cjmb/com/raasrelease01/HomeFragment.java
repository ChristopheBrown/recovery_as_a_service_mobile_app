package cjmb.com.raasrelease01;

/*The HomeFragment serves to show the user a quick summary of their most progressed plan and show
* the tasks they need to complete for the day.*/

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import cjmb.com.raasrelease01.get.GetPersonalInformationResponse;
import cjmb.com.raasrelease01.get.GetPlanRequest;
import cjmb.com.raasrelease01.get.GetPlanResponse;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView HomePlanNameTextView;
    private TextView HomePlanPercentageTextView;
    private ListView TodaysTasksListVIew;
    private TextView TodayTextView;

    GlobalValue globalValue;

    Context mContext;

    /*Used for the plan name displayed on the page*/
    String displayPlanName;

    /*This is an over-the-top approach used to display the user's plan progression percentage */
    private class pct {
        String percentage;
        public void setPercentage(String percent) {
            this.percentage=percent;
        }
        public String getPercentage() {
            return this.percentage;
        }
    }

    pct p;

    /*Fade-in affect on items on the display*/
    final Animation in = new AlphaAnimation(0.0f,1.0f);
//    in.setDuration(800);

    /*NOTICE: many classes have excessive information like the next couple lines. These came
    * preloaded with the class when creating a new android fragment. I did not implement them,
    * but that can be used for other purposes should another user come accross them. Please learn
    * Android some before modifying this code.*/
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        new PopTart(getActivity().findViewById(android.R.id.content),"Retrieving plan data");
        in.setDuration(800);
        in.setRepeatCount(0);

        p = new pct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        HomePlanNameTextView = view.findViewById(R.id.HomePlanNameTextView);
        HomePlanPercentageTextView = view.findViewById(R.id.HomePlanPercentageTextView);
        TodaysTasksListVIew = view.findViewById(R.id.TodaysTasksListView);
        TodayTextView = view.findViewById(R.id.TodayTextView);

        mContext = getActivity().getApplicationContext();

        globalValue = (GlobalValue) mContext;

        /*Show what today's day is, according to the device's settings*/
        String daysArray[] = {"Saturday","Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday"};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        TodayTextView.setText(daysArray[day] + ":");
        getPlanName();
    }

    private void getPlanName() {
        /*This method will take the first listed plan on a users account and show its name and
        * plan progress at the top of the page.*/
        String planName = null;

        String usernameGetRequestJSONString  = globalValue.getUserNameGetRequest();

        RecoveryAsAServiceNetworkTransaction getPlans =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/plannames",usernameGetRequestJSONString);

        getPlans.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("planNames");

                /*If there are no plans, stop here.*/
                if (jsonArray.size() == 0) return;

                /*Extracted from the JSON string*/
                displayPlanName = new Gson().fromJson(jsonArray.get(0),String.class);

                getPlanPercentage();
            }
        });

        planName = displayPlanName;

        return;
    }



    private String getPlanPercentage() {
        String percentage = null;

        if (displayPlanName == null) return null; // getPlanName() didnt work

        GetPlanRequest getPlanRequest = new GetPlanRequest();
        {
            getPlanRequest.setUsername(globalValue.getUserName());
            getPlanRequest.setPlanName(displayPlanName);
        }

        String getPlanRequestJSONObeject = new Gson().toJson(getPlanRequest);

        final RecoveryAsAServiceNetworkTransaction getPlan =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/plan",getPlanRequestJSONObeject);

        getPlan.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetPlanResponse getPlanResponse = new Gson().fromJson(result,GetPlanResponse.class);

                p.setPercentage(Double.toString((int)((double)getPlanResponse.getProgress()*100.0)) + "%");

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("tasks");

                List<String> taskNames = new Vector<String>();

                for (JsonElement e : jsonArray) // Get List of task names
                {
                    JsonObject item = e.getAsJsonObject();
                    taskNames.add(item.get("task").getAsString());
                }

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(mContext,R.layout.activity_simple_array_list_adapter, taskNames);

                HomePlanNameTextView.setText(displayPlanName);
                HomePlanPercentageTextView.setText(p.getPercentage());
                TodaysTasksListVIew.setAdapter(arrayAdapter);

                HomePlanPercentageTextView.startAnimation(in);
                HomePlanNameTextView.startAnimation(in);
                TodaysTasksListVIew.startAnimation(in);

                TodaysTasksListVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        /*Can be used to change pages to look at more details for a task*/
                    }
                });
            }
        });
        return percentage;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
