package com.tapadoo.pana.allan.tapadootestproject.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.extras.Util;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleyErrorHandler;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleySingleton;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBookDetail extends Fragment {

    private static final String END_POINT_URL = "http://tpbookserver.herokuapp.com/book/";
    public Books mBook;

    private ProgressDialog progressDialog;
    private TextView textViewDescription;
    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewIsbn;
    private TextView textViewPrice;
    private int mID;

    public FragmentBookDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        progressDialog = new ProgressDialog(getActivity());
        Intent intent = getActivity().getIntent();
        mID = intent.getIntExtra("id", 100);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        textViewDescription.setMovementMethod(new ScrollingMovementMethod());
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewAuthor = (TextView) view.findViewById(R.id.textViewAuthor);
        textViewIsbn = (TextView) view.findViewById(R.id.textViewIsbn);
        textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);

        if(savedInstanceState != null){
            mBook = savedInstanceState.getParcelable(BOOKS_PARCEL);
            setBookDetails(mBook);
        }else {
            sendJsonRequest(mID);
        }


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        outState.putParcelable(BOOKS_PARCEL, mBook);
    }

    /**
     * Process networking request using Volley
     */
    public void sendJsonRequest(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                END_POINT_URL+id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        textViewDescription.setTextColor(Color.BLACK);
                        mBook = parseJsonResponse(jsonObject);
                        setBookDetails(mBook);
                        Util.dismissProgressBar(progressDialog);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textViewDescription.setTextColor(Color.RED);
                        VolleyErrorHandler.handleVolleyError(volleyError, textViewDescription);
                        Util.dismissProgressBar(progressDialog);
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance().addToRequestQueue(jsonObjectRequest);
        Util.showProgressBar(progressDialog);

    }


    public void setBookDetails(Books book) {
        String currencySmbol=NA;
        if(book.getCurrencyCode().equalsIgnoreCase(EUR)){
            currencySmbol="€";
        }
        if(book.getCurrencyCode().equalsIgnoreCase(GBP)){
            currencySmbol="£";
        }
        if(book.getCurrencyCode().equalsIgnoreCase(USD)){
            currencySmbol="$";
        }

        double p = book.getPrice();
        double price = p/100;

        textViewDescription.setText(book.getDescription());
        textViewTitle.setText(book.getTitle());
        textViewAuthor.setText(book.getAuthor());
        textViewIsbn.setText(book.getIsbn());
        textViewPrice.setText(currencySmbol+price);
    }

    private Books parseJsonResponse(JSONObject jsonObject) {
        Books books = new Books();
        int id = 0;
        String title = NA;
        String isbn = NA;
        int price = 0;
        String currencyCode = NA;
        String author = NA;
        String description = NA;

        try {
            if (jsonObject.has(KEY_TITLE) && !jsonObject.isNull(KEY_TITLE)) {
                title = jsonObject.getString(KEY_TITLE);
            }
            if (jsonObject.has(KEY_ISBN) && !jsonObject.isNull(KEY_ISBN)) {
                isbn = jsonObject.getString(KEY_ISBN);
            }
            if (jsonObject.has(KEY_DESCRIPTION) && !jsonObject.isNull(KEY_DESCRIPTION)) {
                description = jsonObject.getString(KEY_DESCRIPTION);
            }
            if (jsonObject.has(KEY_PRICE) && !jsonObject.isNull(KEY_PRICE)) {
                price = jsonObject.getInt(KEY_PRICE);
            }
            if (jsonObject.has(KEY_CURRENCY_CODE) && !jsonObject.isNull(KEY_CURRENCY_CODE)) {
                currencyCode = jsonObject.getString(KEY_CURRENCY_CODE);
            }
            if (jsonObject.has(KEY_AUTHOR) && !jsonObject.isNull(KEY_AUTHOR)) {
                author = jsonObject.getString(KEY_AUTHOR);
            }

            books = new Books(id, title, isbn, description, price, currencyCode, author);

        } catch (JSONException je) {

        }
        return books;

    }



}
