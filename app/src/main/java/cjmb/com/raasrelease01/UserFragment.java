package cjmb.com.raasrelease01;

/* Show subscribed users*/

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cjmb.com.raasrelease01.create.CreateResponse;
import cjmb.com.raasrelease01.create.CreateSubscriberRequest;
import cjmb.com.raasrelease01.get.GetRequest;
import cjmb.com.raasrelease01.get.GetRoleResponse;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements StatusCode{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView UserFragmentDoctorTextView;
    private Button ChatWithDoctorButton;
    private ListView SubscribedUsersListView;
    private Button newSubscriberButton;
    private EditText newSubscriberEditText;
    private boolean newSubscriberEditTextMode = false;

    private String accountToSubscribeTo;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        }new PopTart(getActivity().findViewById(android.R.id.content),"Fetching subsribers...");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
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
        final Context mContext = getActivity().getApplicationContext();
        final GlobalValue globalValue = (GlobalValue) mContext;

        UserFragmentDoctorTextView = (TextView) view.findViewById(R.id.UserFragmentDoctorTextView);
        SubscribedUsersListView = (ListView) view.findViewById(R.id.SubscribedUsersListView);
        newSubscriberButton = (Button) view.findViewById(R.id.newSubscriberButton);
        newSubscriberEditText = (EditText) view.findViewById(R.id.newSubscriberEditText);

        UserFragmentDoctorTextView.append("Doctor");

        final String usernameGetRequestJSONString  = globalValue.getUserNameGetRequest();

        RecoveryAsAServiceNetworkTransaction getSubscribedUsers =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/get/subscribedusers",usernameGetRequestJSONString);

        getSubscribedUsers.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                /*If an account has any subscribers, we'll retrieve them and display them*/

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonArray jsonArray = json.getAsJsonArray("list");

                if (jsonArray.size() == 0)
                {
                    new PopTart("No Subscribed Users.");
                    return;
                }

                Type listType = new TypeToken<List<Object>>() {}.getType();
                final List<Object> list = new Gson().fromJson(jsonArray, listType);

                ArrayAdapter<Object> arrayAdapter =
                        new ArrayAdapter<Object>(mContext,R.layout.activity_simple_array_list_adapter, list);

                SubscribedUsersListView.setAdapter(arrayAdapter);
                SubscribedUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String usernameGetRequest = globalValue.getUserNameGetRequest();
                        String subscribedUserUsernameGetRequest = (String) list.get(i);

                        Intent SubscribedUserActivity =
                                new Intent(mContext,cjmb.com.raasrelease01.SubscribedUserActivity.class);
                        SubscribedUserActivity.putExtra("USERNAME_GET_REQUEST_STRING",usernameGetRequest);
                        SubscribedUserActivity.putExtra("SUBSCRIBED_USER_USERNAME",subscribedUserUsernameGetRequest);
                        startActivity(SubscribedUserActivity);
                    }
                });
            }
        });
        newSubscriberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toggle the visibility of the EditText box to enter a user's account to subscribe to*/
                if (!newSubscriberEditTextMode)
                {
                    newSubscriberEditText.setVisibility(View.VISIBLE);
                    newSubscriberEditText.setEnabled(true);
                } else {
                    accountToSubscribeTo = newSubscriberEditText.getText().toString();

                    if (accountToSubscribeTo.equals(""))
                    {
                        /*No username entered? User changed their mind. Toggle visiblity */
                        newSubscriberEditText.setEnabled(false);
                        newSubscriberEditText.setVisibility(View.INVISIBLE);
                        newSubscriberEditTextMode = !newSubscriberEditTextMode;
                        return;
                    }
                    GetRequest getRequest = new GetRequest();
                    getRequest.setUsername(accountToSubscribeTo);

                    String getRequestJson = new Gson().toJson(getRequest);

                    // Get the role of the entered user. If they are an active patient, subscribe to them.
                    /*You will ONLY subscribe to accounts that have their role as "PATIENT" non case-sensitive*/
                    RecoveryAsAServiceNetworkTransaction getRoleOfPatient =
                            new RecoveryAsAServiceNetworkTransaction(mContext,"/get/role",getRequestJson);

                    getRoleOfPatient.run(new ServerCallback() {
                        int statusCode = 0;
                        String role = null;
                        @Override
                        public void onSuccess(String result) {
                            GetRoleResponse getRoleResponse = new Gson().fromJson(result,GetRoleResponse.class);
                            statusCode = getRoleResponse.getStatusCode();
                            role = getRoleResponse.getRole();
                            if (statusCode==SUCCESS && role.toUpperCase().equals("PATIENT")) {
                                CreateSubscriberRequest createSubscriberRequest = new CreateSubscriberRequest();
                                {
                                    createSubscriberRequest.setSubscriber(accountToSubscribeTo);
                                    createSubscriberRequest.setUsername(globalValue.getUserName());
                                }

                                String subscriptionRequest = new Gson().toJson(createSubscriberRequest);

                                RecoveryAsAServiceNetworkTransaction makeSubscriptionParty =
                                        new RecoveryAsAServiceNetworkTransaction(mContext,"/create/subscribeduser",subscriptionRequest);

                                makeSubscriptionParty.run(new ServerCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        CreateResponse createResponse = new Gson().fromJson(result,CreateResponse.class);
                                        if (createResponse.getStatusCode() == SUCCESS)
                                            new PopTart("Subscriber successfully added. New changes will be active on next sign-in.");
                                        else new PopTart("Adding Subscription failed.");
                                    }
                                });
                            } else new PopTart("The username entered does not correspond to an existing or active patient.");

                        }
                    });

                    newSubscriberEditText.setEnabled(false);
                    newSubscriberEditText.setVisibility(View.INVISIBLE);
                }

                /*Toggle the username-entering mode on button press*/
                newSubscriberEditTextMode = !newSubscriberEditTextMode;
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
