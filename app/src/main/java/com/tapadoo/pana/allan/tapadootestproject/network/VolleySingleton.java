package com.tapadoo.pana.allan.tapadootestproject.network;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tapadoo.pana.allan.tapadootestproject.MyApplication;

/**
 * Created by allan on 15/06/15.
 */
public class VolleySingleton {

    private static  VolleySingleton instance;
    private RequestQueue requestQueue;

    /**
     * private constructor to avoid other classes to construct/initialize this class
     * initialize here the requestQueue and imageLoader
     */
    private  VolleySingleton(){
        requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

    }


    /**
     *
     * @return VolleySingleton instance
     */
    public synchronized  static VolleySingleton getInstance(){
        if(instance == null){
            instance = new VolleySingleton();
        }
        return instance;
    }


    /**
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    /**
     *
     * @param request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request){

        getRequestQueue().add(request);
    }
}
