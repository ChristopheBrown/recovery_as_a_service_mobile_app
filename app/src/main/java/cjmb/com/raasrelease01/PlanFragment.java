package cjmb.com.raasrelease01;

/*Plan fragment shows a list of the user's plans. Tapping on a plan will take you a
* more comprehensive page of their plan*/

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;

    private ListView plansList;
    private TextView PlanSelectionHeaderTextView;

//    Fade-in effect on text on the screen
    final Animation in = new AlphaAnimation(0.0f,1.0f);

    private OnFragmentInteractionListener mListener;

    public PlanFragment() {
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
    public static PlanFragment newInstance(String param1, String param2) {
        PlanFragment fragment = new PlanFragment();
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
            in.setDuration(800);
            in.setRepeatCount(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
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
        mContext = getActivity().getApplicationContext();

        plansList = (ListView) view.findViewById(R.id.Plans);
        PlanSelectionHeaderTextView = (TextView) view.findViewById(R.id.PlanSelectionHeaderTextView);

        final GlobalValue globalVariable = (GlobalValue) mContext;
        final String usernameGetRequestJSONString  = globalVariable.getUserNameGetRequest();

        RecoveryAsAServiceNetworkTransaction getPlans =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/plannames",usernameGetRequestJSONString);

        getPlans.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                PlanSelectionHeaderTextView.startAnimation(in);
                PlanSelectionHeaderTextView.setText("Select a plan below:");

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("planNames");

                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> list = new Gson().fromJson(jsonArray, listType);

                PlanSelectionHeaderTextView.startAnimation(in);
                plansList.startAnimation(in);

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(mContext,R.layout.activity_simple_array_list_adapter, list);

                /*This list2 is made because to use items in the RecoveryAsAServiceNetworkTransaction
                * run method, they must be declared final. The original list cant be made final because
                * the items in the list arent known at declaration time. */
                final List<String> list2 = list;

                if (plansList == null) new PopTart("Null Object!");

                plansList.setAdapter(arrayAdapter);
                plansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        GlobalValue globalVariable = (GlobalValue) mContext;
                        String username  = globalVariable.getUserName();
                        String planName = list2.get(i);

                        Intent viewPlanIntent = new Intent(mContext,cjmb.com.raasrelease01.PlanSelectedActivity.class);
                        viewPlanIntent.putExtra("USERNAME_STRING",username);
                        viewPlanIntent.putExtra("PLAN_NAME_STRING",planName);
                        startActivity(viewPlanIntent);
                    }
                });
            }
        });
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
