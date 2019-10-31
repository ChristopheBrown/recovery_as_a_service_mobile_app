package cjmb.com.raasrelease01;

/*Arguable the most important class in this project. This is how all network transactions are
* completed.
*
* Communication with the RaaS AWS server is done via HTTP requests and Android Volley
*
* Most of this code was acquired from StackOverflow. What you should know is that to make a request:
*
* Instantiate a RecoveryAsAServiceNetworkTransaction(), passing a Context, an extension (see Java
* docs), and a JSON parameter used by that extension (again see any java doc example)
*
* After instantiating, call the run(new ServerCallback) on the object instance and handle whatever
* actions need to be done on the returned result string.*/

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class RecoveryAsAServiceNetworkTransaction {

   private RequestQueue requestQueue;
   private String JSONRequestString;
   private String JSONResponseString;

   /*URL for our server. Can change based on how you do it.*/
   final private String baseURL = "https://yfzy5kga1b.execute-api.us-west-2.amazonaws.com/beta";
   private String URLExtension;
   private String reqeustURL = "";

    /*Concats the user's input extension with the base URL*/
    public RecoveryAsAServiceNetworkTransaction(Context context, String extension) {
        this.requestQueue = Volley.newRequestQueue(context);
        URLExtension = extension;
        reqeustURL = baseURL + URLExtension;
    }

    public RecoveryAsAServiceNetworkTransaction(Context context, String extension, String JSONObjectBody) {
        this.requestQueue = Volley.newRequestQueue(context);
        URLExtension = extension;
        reqeustURL = baseURL + URLExtension;

        JSONRequestString = JSONObjectBody;
    }

    String getJSONResponseString() {
        return this.JSONResponseString;
    }

    void run (final ServerCallback callback) {
        try {
            String URL = reqeustURL;
            JSONObject jsonBody = new JSONObject(JSONRequestString);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { //Successful transaction sends a JSONResponseString to where it was called
                    Log.i("VOLLEY1", response);
                    JSONResponseString = response;
                    callback.onSuccess(JSONResponseString);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) { // Make sure your device is connected to the Internet.
                    /*Check the JSONResponseString if you're having issues here.*/
                    Log.e("VOLLEY2", error.toString());
                    JSONResponseString = error.toString();
                    new PopTart(JSONRequestString);
                    callback.onSuccess(JSONResponseString);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
