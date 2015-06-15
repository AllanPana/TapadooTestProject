package com.tapadoo.pana.allan.tapadootestproject.extras;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tapadoo.pana.allan.tapadootestproject.MyApplication;

/**
 * Created by allan on 15/06/15.
 */
public class TagNToast {

    public static void setToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    public static void setLog(String log){
        Log.d("tapadoo",log);
    }
}
