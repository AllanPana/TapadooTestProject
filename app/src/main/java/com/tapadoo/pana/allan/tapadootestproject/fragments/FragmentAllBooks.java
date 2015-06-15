package com.tapadoo.pana.allan.tapadootestproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.extras.TagNToast;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleySingleton;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAllBooks extends Fragment {

    private static final String END_POINT_URL = "http://tpbookserver.herokuapp.com/books ";

    private Communicator communicator;
    private List<Books> mBooksList;
    private TextView textViewVolleyError;

    public FragmentAllBooks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_books, container, false);
        mBooksList = new ArrayList<>();
        textViewVolleyError = (TextView) view.findViewById(R.id.textViewVolleyError);

        sendJSonRequest();
        return view;
    }


    //process networking request
    private void sendJSonRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(END_POINT_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        textViewVolleyError.setVisibility(View.GONE);
                        mBooksList = parseJSonResponse(jsonArray);
                        TagNToast.setToast(getActivity(), mBooksList.toString());
                        TagNToast.setLog(mBooksList.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        handleVolleyError(volleyError);
                    }
                });

        VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    /**
     *
     * @param error = VolleyError
     */
    private void handleVolleyError(VolleyError error){
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

    /**
     * @param jsonArray = jsonResponse from Volley
     */
    private List<Books> parseJSonResponse(JSONArray jsonArray) {
        List<Books> list = new ArrayList<>();
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

                    //create books object with title, author, price
                    Books books = new Books(title, author, price);
                    // only add the data into books if  title has a right value
                    if (!title.equals(NA)) {
                        list.add(books);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * @param communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }


    /**
     * use to communicate with the MainActivity
     */
    public interface Communicator {
        public void setBookDetail(int position);
    }
}
