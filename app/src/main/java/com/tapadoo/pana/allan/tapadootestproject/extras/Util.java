package com.tapadoo.pana.allan.tapadootestproject.extras;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tapadoo.pana.allan.tapadootestproject.MyApplication;

import java.text.NumberFormat;
import java.util.Currency;

/**
 * Created by allan on 15/06/15.
 */
public class Util {

    /**
     * Toast
     * @param context
     * @param text
     */
    public static void setToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
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
     * @param progressDialog
     */
    public static void showProgressBar(ProgressDialog progressDialog){
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    public static void dismissProgressBar(ProgressDialog progressDialog){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


    /**
     * Format the currency pricing
     * @param isoCurrencyCode
     * @param amount
     * @return
     */
    public static String getFormattedCurrency(String isoCurrencyCode, double amount){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        numberFormat.setCurrency(Currency.getInstance(isoCurrencyCode));
        String currencyAmount = numberFormat.format(amount);

        return currencyAmount;
    }
}
