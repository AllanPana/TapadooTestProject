package com.tapadoo.pana.allan.tapadootestproject;

import android.app.Application;
import android.content.Context;

import com.tapadoo.pana.allan.tapadootestproject.database.BookDatabase;

/**
 * Created by allan on 15/06/15.
 */
public class MyApplication extends Application {

    private static MyApplication myApplicationInstance;
    private static BookDatabase bookDatabase;

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


    public synchronized static BookDatabase getWritableBookDatabase(){
        if(bookDatabase == null){
            bookDatabase = new BookDatabase(getAppContext());
        }
        return bookDatabase;
    }

}
