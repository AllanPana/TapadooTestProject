package com.tapadoo.pana.allan.tapadootestproject.extras;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tapadoo.pana.allan.tapadootestproject.MyApplication;

/**
 * Created by allan on 15/06/15.
 */
public class TagNToast {

    /**
     * Toast
     * @param context
     * @param text
     */
    public static void setToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    /**
     * Log
     * @param log
     */
    public static void setLog(String log){
        Log.d("tapadoo", log);
    }

    /**
     * Progressbar
     * @param progressBar
     */
    public static void showProgressBar(ProgressDialog progressBar){
        progressBar.setMessage("Connecting to server....");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
}
