package com.tapadoo.pana.allan.tapadootestproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by allan on 15/06/15.
 */
public class MyApplication extends Application {

    private static MyApplication myApplicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //oncreate method getting called before everything inside the app
        //initialize the myApplicationInstance
        myApplicationInstance = this;
    }

    /**
     * a public static getter to access this MyApplication class
     * @return myApplicationInstance
     */
    public static MyApplication getMyApplicationInstance(){
        return myApplicationInstance;
    }


    /**
     *
     * @return application context
     */
    public static Context getAppContext(){
        Context context = myApplicationInstance.getApplicationContext();
        return context;
    }

}
