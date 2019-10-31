package cjmb.com.raasrelease01;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.security.PublicKey;

import cjmb.com.raasrelease01.GlobalValue;

public class PopTart {

    // POPTART
    // Creates an android "Toast" or "Snackbar" message, useful for debugging or giving instructions to the user
    // during runtime
    public PopTart (String message) {
        Toast toast = Toast.makeText(new GlobalValue().getContext(),message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public PopTart (View v, String message) {
        Snackbar snackbar = Snackbar.make(v,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
