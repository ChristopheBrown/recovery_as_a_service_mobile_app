package cjmb.com.raasrelease01;

/*Class is very self explanatory. This will display and/or change the attributes associated with
* a patient's personal information*/

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import cjmb.com.raasrelease01.update.UpdatePersonalInformationRequest;
import cjmb.com.raasrelease01.update.UpdateResponse;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment implements StatusCode {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView InfoFragmentChangePassword;

    EditText accountAddressValueEditText;
    EditText accountFirstNameValueEditText;
    EditText accountLastNameValueEditText;
    EditText accountHeightValueEditText;
    EditText accountWeightValueEditText;
    EditText accountPhoneValueEditText;

    Button accountAddressChangeButton;
    Button accountFirstNameChangeButton;
    Button accountLastNameChangeButton;
    Button accountHeightChangeButton;
    Button acccountWeightChangeButton;
    Button accountPhoneChangeButton;


    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
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

        InfoFragmentChangePassword = (TextView) view.findViewById(R.id.InfoFragmentChangePassword);
        InfoFragmentChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePassword = new Intent(getActivity().getApplicationContext(),cjmb.com.raasrelease01.ChangePasswordActivity.class);
                startActivity(changePassword);
            }
        });


        accountAddressValueEditText = (EditText) view.findViewById(R.id.accountAddressValueEditText);
        accountFirstNameValueEditText = (EditText) view.findViewById(R.id.accountFirstNameValueEditText);
        accountLastNameValueEditText = (EditText) view.findViewById(R.id.accountLastNameValueEditText);
        accountHeightValueEditText = (EditText) view.findViewById(R.id.accountHeightValueEditText);
        accountWeightValueEditText = (EditText) view.findViewById(R.id.accountWeightValueEditText);
        accountPhoneValueEditText = (EditText) view.findViewById(R.id.accountPhoneValueEditText);

        accountAddressValueEditText.setText(globalValue.getAddress());
        accountFirstNameValueEditText.setText(globalValue.getFirstName());
        accountLastNameValueEditText.setText(globalValue.getLastName());
        accountHeightValueEditText.setText(String.valueOf(globalValue.getHeight()));
        accountWeightValueEditText.setText(String.valueOf(globalValue.getWeight()));
        accountPhoneValueEditText.setText("9195153242");


        accountAddressChangeButton = (Button) view.findViewById(R.id.accountAddressChangeButton);
        accountAddressChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "Address" , accountAddressValueEditText.getText().toString() ); } });

        accountFirstNameChangeButton = (Button) view.findViewById(R.id.accountFirstNameChangeButton);
        accountFirstNameChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "FirstName" , accountFirstNameValueEditText.getText().toString() ); }
        });

        accountLastNameChangeButton = (Button) view.findViewById(R.id.accountLastNameChangeButton);
        accountLastNameChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "LastName" , accountLastNameValueEditText.getText().toString() ); }
        });

        accountHeightChangeButton = (Button) view.findViewById(R.id.accountHeightChangeButton);
        accountHeightChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "Height" , Double.parseDouble(accountHeightValueEditText.getText().toString()) ); }
        });

        acccountWeightChangeButton = (Button) view.findViewById(R.id.acccountWeightChangeButton);
        acccountWeightChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "Weight" , Double.parseDouble(accountWeightValueEditText.getText().toString()) ); }
        });

        accountPhoneChangeButton = (Button) view.findViewById(R.id.accountPhoneChangeButton);
        accountPhoneChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateInformation ( "Phone" , Double.parseDouble(accountPhoneValueEditText.getText().toString()) ); }
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


    public void updateInformation (final String field, Object text) {
        final Context mContext = getActivity().getApplicationContext();
        final GlobalValue globalValue = (GlobalValue) mContext;

        String username = globalValue.getUserName();
        Object value = text;

        UpdatePersonalInformationRequest updatePersonalInformationRequest =
                new UpdatePersonalInformationRequest();
        {
            updatePersonalInformationRequest.setUsername(username);
            updatePersonalInformationRequest.setField(field);
            updatePersonalInformationRequest.setValue(value);
        }

        String updatePersonalInformationRequestJSONObject =
                new Gson().toJson(updatePersonalInformationRequest);


        RecoveryAsAServiceNetworkTransaction updatePersonalInformation =
                new RecoveryAsAServiceNetworkTransaction(mContext,"/update/personalinformation",updatePersonalInformationRequestJSONObject);

        updatePersonalInformation.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                UpdateResponse updateResponse = new Gson().fromJson(result,UpdateResponse.class);

                switch (updateResponse.getStatusCode()) {
                    case SUCCESS:
                        new PopTart(getActivity().findViewById(android.R.id.content),field + " updated!");
                        break;
                    case NOTFOUND:
                        new PopTart(getActivity().findViewById(android.R.id.content),"Account does not exist!");
                        break;
                    case UNAUTHORIZED:
                        new PopTart(getActivity().findViewById(android.R.id.content),"Login timer reset. Please relogin!");
                        break;
                    case FORBIDDEN:
                        new PopTart(getActivity().findViewById(android.R.id.content),"Account is not logged in!");
                        break;
                }
            }
        });
    }

}
