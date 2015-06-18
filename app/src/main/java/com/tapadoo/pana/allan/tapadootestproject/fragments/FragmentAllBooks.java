package com.tapadoo.pana.allan.tapadootestproject.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.activities.BookDetailsActivity;
import com.tapadoo.pana.allan.tapadootestproject.adapters.BookRecycleViewAdapter;
import com.tapadoo.pana.allan.tapadootestproject.extras.TagNToast;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleyErrorHandler;
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
public class FragmentAllBooks extends Fragment implements BookRecycleViewAdapter.MyRecyclerViewOnClickListener{

    private static final String END_POINT_URL = "http://tpbookserver.herokuapp.com/books ";
    private static final String BOOKS_PARCEL = "booksParcel";




    private ProgressDialog progressDialog;
    private RecyclerView recyclerViewBook;
    private BookRecycleViewAdapter bookRecycleViewAdapter;
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
        recyclerViewBook = (RecyclerView) view.findViewById(R.id.recycleViewBook);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookRecycleViewAdapter = new BookRecycleViewAdapter(getActivity());
        recyclerViewBook.setAdapter(bookRecycleViewAdapter);
        bookRecycleViewAdapter.setMyRecyclerViewOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());

        if(savedInstanceState != null){
            mBooksList = savedInstanceState.getParcelableArrayList(BOOKS_PARCEL);
            bookRecycleViewAdapter.setBooksList(mBooksList);
        }else {
            sendJSonRequest();
        }


        return view;
    }


    /**
     *
     * @param outState = mBookList
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BOOKS_PARCEL, (ArrayList<? extends Parcelable>) mBooksList);
    }

    /**
     * Process networking request
     */
    private void sendJSonRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(END_POINT_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        textViewVolleyError.setVisibility(View.GONE);
                        mBooksList = parseJSonResponse(jsonArray);
                        bookRecycleViewAdapter.setBooksList(mBooksList);
                        if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyErrorHandler.handleVolleyError(volleyError, textViewVolleyError);
                        if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
        TagNToast.showProgressBar(progressDialog);
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
                    Books books = new Books(id,title, author, price,currencyCode);
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

    @Override
    public void myREcyclerViewOnClick(View view, int position) {

        /*recyclerViewBook.setBackgroundColor(Color.BLUE);
        view.setBackgroundColor(Color.RED);*/

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentBookDetail fragmentBookDetail =
                (FragmentBookDetail) fragmentManager.findFragmentById(R.id.fragmentBookDetails);
        Books book = mBooksList.get(position);

        //check if your layout in a landscape orientation
        if(fragmentBookDetail != null && fragmentBookDetail.isVisible()){
            TagNToast.setLog("landscape mode................" + book.getId());
            //fragmentBookDetail.setBookDetails(fragmentBookDetail.mBook);
            fragmentBookDetail.sendJsonRequest(book.getId());

        }else {
            TagNToast.setLog("portrait mode................"+book.getId());
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("id", book.getId());
            startActivity(intent);
        }

    }
}
