package cjmb.com.raasrelease01;

/*This page displays all of the account's associated devices (like fitbits) and the quantified
* data from those devices is displayed (like stepcount) */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import cjmb.com.raasrelease01.get.GetFitbitStepsResponse;

public class DeviceMonitoringActivity extends AppCompatActivity implements StatusCode{

    TextView DeviceStepsTextView;

    String usernameGetRequest;

    private ArrayList<String> deviceList;

    private HashMap<String,String> deviceRecords;
    private ArrayList<String> dates;
    private ArrayList<String> formattedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_monitoring);

        new PopTart("Retrieving device data..");

        final GlobalValue globalVariable = (GlobalValue) getApplicationContext();

        final ListView DeviceSelectionListView =(ListView) findViewById(R.id.DeviceSelectionListView);
        final ListView DeviceDateListView =(ListView) findViewById(R.id.DeviceDateListView);
        final TextView DeviceStepsTextView = (TextView) findViewById(R.id.DeviceStepsTextView);

        dates = new ArrayList<String>();
        formattedDates = new ArrayList<String>();

        /*This is hardcoded because the patient for our demo only had one device associated. There
        is no algorithm to pull these devices from the server. You may need one. */
        deviceList = new ArrayList<String>();
        deviceList.add("Fitbit Charge 2");

        ArrayAdapter<String> deviceAdapter =
                new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_simple_array_list_adapter, deviceList);

        DeviceSelectionListView.setAdapter(deviceAdapter);

        usernameGetRequest = globalVariable.getUserNameGetRequest();

        RecoveryAsAServiceNetworkTransaction getFitbitSteps =
                new RecoveryAsAServiceNetworkTransaction(getApplicationContext(),"/get/fitbitsteps",usernameGetRequest);

        getFitbitSteps.run(new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                GetFitbitStepsResponse getFitbitStepsResponse =
                        new Gson().fromJson(result,GetFitbitStepsResponse.class);

                if (getFitbitStepsResponse.getStatusCode() != SUCCESS) {
                    new PopTart("Failed to retrieve device data.");
                    return;
                }

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);


                Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                deviceRecords = new Gson().fromJson(json.getAsJsonObject("steps"), type);

                Map<String,String> sortedDates = new TreeMap<>(deviceRecords);

                NavigableMap<String, String> nmap = ((TreeMap<String, String>) sortedDates).descendingMap();

                for (String date : nmap.navigableKeySet()) {
                    dates.add(date);
                    try {
                        formattedDates.add(parseDate(date));
                    } catch (ParseException e) {
                        new PopTart("Failed to convert date format.");
                    }
                }

                ArrayAdapter<String> dateAdapter =
                        new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_simple_array_list_adapter, formattedDates);

                DeviceDateListView.setAdapter(dateAdapter);
                DeviceDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DeviceStepsTextView.setText(deviceRecords.get(dates.get(i)) + " steps");
                    }
                });
            }
        });
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        /*Unused method.*/
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    private String parseDate(String date) throws ParseException
    {
        /*Dates from the fitbits are formatted differently from how our group's Task dates
        * are formatted, so they need a different DateFormat. This is how fitbit does it. If you
        * implement support for a different device, this format can again change.*/
        SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyyMMdd" );
        SimpleDateFormat outgoingFormat = new SimpleDateFormat("EEE, MMM d, ''yy" );
        Date incomingDate = incomingFormat.parse(date);
        String outgoingDate = outgoingFormat.format(incomingDate);
        return outgoingDate;
    }
}
