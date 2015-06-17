package com.tapadoo.pana.allan.tapadootestproject.network;

import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by allan on 16/06/15.
 */
public class VolleyErrorHandler {

    /**
     *
     * @param error = VolleyError
     */
    public static void handleVolleyError(VolleyError error, TextView textViewVolleyError){
        textViewVolleyError.setVisibility(View.VISIBLE);
        String volleyError = "ERROR";
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            volleyError = "Network Error or No Connection";
        } else if (error instanceof AuthFailureError) {
            volleyError = "Authentication Error";
        } else if (error instanceof ServerError) {
            volleyError = "Server Error";
        } else if (error instanceof NetworkError) {
            volleyError = "Network Error";
        } else if (error instanceof ParseError) {
            volleyError = "Parser Error";
        }
        textViewVolleyError.setText(volleyError);
    }
}
