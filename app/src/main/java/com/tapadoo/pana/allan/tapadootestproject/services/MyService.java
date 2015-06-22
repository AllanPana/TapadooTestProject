package com.tapadoo.pana.allan.tapadootestproject.services;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.tapadoo.pana.allan.tapadootestproject.MyApplication;
import com.tapadoo.pana.allan.tapadootestproject.extras.Util;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleyErrorHandler;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleySingleton;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_AUTHOR;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_CURRENCY_CODE;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_ID;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_ISBN;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_PRICE;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.KEY_TITLE;
import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.NA;

/**
 * Created by allan on 21/06/15.
 */
public class MyService extends JobService {

    //Return true if you want to run the job in a separate thread
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Util.setToast(this,"onStart Job called");
       new MyTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    /**
     *
     */
    public class MyTask extends AsyncTask<JobParameters,Void,JobParameters>{
        private static final String END_POINT_URL = "http://tpbookserver.herokuapp.com/books ";
        MyService myService;

        MyTask(MyService myService){
            this.myService = myService;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONArray jsonArray = sendJSonArrayRequest();
            ArrayList<Books> booksList = parseJSonResponse(jsonArray);
            MyApplication.getWritableBookDatabase().insertBooks(booksList,true);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
            //Util.setToast(MyApplication.getAppContext(),"new list of books inserted into database");
            super.onPostExecute(jobParameters);
        }

        /**
         * Process networking request
         * Volley has its own thread but this time I tell Volley use doInBackground thread by using RequestFuture
         */
        private JSONArray sendJSonArrayRequest() {
            JSONArray jsonArrayResponse = null;

            RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(END_POINT_URL,
                   requestFuture,requestFuture);

            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);

            try {
                //request future will activate @ 30seconds after the app first run
                jsonArrayResponse = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Util.setLog(e+"");
            } catch (ExecutionException e) {
                Util.setLog(e + "");
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


            return jsonArrayResponse;
        }




        /**
         * @param jsonArray = jsonResponse from Volley
         */
        private ArrayList<Books> parseJSonResponse(JSONArray jsonArray) {
            ArrayList<Books> list = new ArrayList<>();
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        //initialize the data with default value in each iteration inside this loop
                        int id = 0;
                        String title = NA;
                        String isbn = NA;
                        int price = 0;
                        String currencyCode = NA;
                        String author = NA;

                        /**
                         * Check the jsonObject if has a key and not null
                         */
                        if (jsonObject.has(KEY_ID) && !jsonObject.isNull(KEY_ID)) {
                            id = jsonObject.getInt(KEY_ID);
                        }
                        if (jsonObject.has(KEY_TITLE) && !jsonObject.isNull(KEY_TITLE)) {
                            title = jsonObject.getString(KEY_TITLE);
                        }
                        if (jsonObject.has(KEY_ISBN) && !jsonObject.isNull(KEY_ISBN)) {
                            isbn = jsonObject.getString(KEY_ISBN);
                        }
                    /*if(jsonObject.has(KEY_DESCRIPTION) && !jsonObject.isNull(KEY_DESCRIPTION)){
                        description = jsonObject.getString(KEY_DESCRIPTION);
                    }*/
                        if (jsonObject.has(KEY_PRICE) && !jsonObject.isNull(KEY_PRICE)) {
                            price = jsonObject.getInt(KEY_PRICE);
                        }
                        if (jsonObject.has(KEY_CURRENCY_CODE) && !jsonObject.isNull(KEY_CURRENCY_CODE)) {
                            currencyCode = jsonObject.getString(KEY_CURRENCY_CODE);
                        }
                        if (jsonObject.has(KEY_AUTHOR) && !jsonObject.isNull(KEY_AUTHOR)) {
                            author = jsonObject.getString(KEY_AUTHOR);
                        }

                        //create books object
                        Books books = new Books(id,title,isbn, price,currencyCode, author);
                        // only add the data into books if  title has a right value and id not =0
                        if ((!title.equals(NA)) && id != 0) {
                            list.add(books);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return list;
        }

    }
}
