package cjmb.com.raasrelease01;

/*This interface is used on the ServerCallback for network transactions. By implementing this function on
* RecoveryAsAServiceNetWorkTransaction.run(new ServerCallback), you can take action based on what happend
* or what returned from the server side.*/

public interface ServerCallback {
//    void onSuccess(JSONObject result);
    void onSuccess(String result);

}
