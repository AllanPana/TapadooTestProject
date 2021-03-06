package com.tapadoo.pana.allan.tapadootestproject.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tapadoo.pana.allan.tapadootestproject.MyApplication;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.activities.BookDetailsActivity;
import com.tapadoo.pana.allan.tapadootestproject.adapters.BookRecycleViewAdapter;
import com.tapadoo.pana.allan.tapadootestproject.extras.Util;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleyErrorHandler;
import com.tapadoo.pana.allan.tapadootestproject.network.VolleySingleton;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.tapadoo.pana.allan.tapadootestproject.extras.MyConstant.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAllBooks extends Fragment implements BookRecycleViewAdapter.MyRecyclerViewOnClickListener{

    private static final String END_POINT_URL = "http://tpbookserver.herokuapp.com/books ";

    private ProgressDialog progressDialog;
    private RecyclerView recyclerViewBook;
    private BookRecycleViewAdapter bookRecycleViewAdapter;
    private List<Books> mBooksList;
    private ArrayList<Books> listFromDB;
    private TextView textViewVolleyError;
    private FragmentManager fragmentManager;

    public FragmentAllBooks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_books, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        mBooksList = new ArrayList<>();
        textViewVolleyError = (TextView) view.findViewById(R.id.textViewVolleyError);
        recyclerViewBook = (RecyclerView) view.findViewById(R.id.recycleViewBook);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookRecycleViewAdapter = new BookRecycleViewAdapter(getActivity());
        recyclerViewBook.setAdapter(bookRecycleViewAdapter);
        bookRecycleViewAdapter.setMyRecyclerViewOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());

        listFromDB = MyApplication.getWritableBookDatabase().getAllBooks();

        if(savedInstanceState != null) {
            mBooksList = savedInstanceState.getParcelableArrayList(BOOKS_PARCEL);

        }else if(savedInstanceState == null && listFromDB.isEmpty()){
            sendJSonRequest();

        }else {

            mBooksList = listFromDB;
        }


        bookRecycleViewAdapter.setBooksList(mBooksList);

        //sendJSonRequest();
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



    private void sendJSonRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(END_POINT_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        textViewVolleyError.setVisibility(View.GONE);
                        mBooksList = parseJSonResponse(jsonArray);
                        bookRecycleViewAdapter.setBooksList(mBooksList);
                        Util.dismissProgressBar(progressDialog);
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyErrorHandler.handleVolleyError(volleyError, textViewVolleyError);
                        Util.dismissProgressBar(progressDialog);
                    }
                });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance().addToRequestQueue(jsonArrayRequest);
        Util.showProgressBar(progressDialog);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }





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

    /**
     *
     * @param view
     * @param position
     */
    @Override
    public void myREcyclerViewOnClick(View view, int position) {

        FragmentBookDetail fragmentBookDetail =
                (FragmentBookDetail) fragmentManager.findFragmentById(R.id.fragmentBookDetails);
        Books book = mBooksList.get(position);

        //check if your layout in a landscape orientation
        if(fragmentBookDetail != null && fragmentBookDetail.isVisible()){
            Util.setLog("landscape mode................" + book.getId());
            fragmentBookDetail.sendJsonRequest(book.getId());

        }else {
            Util.setLog("portrait mode................" + book.getId());
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("id", book.getId());
            startActivity(intent);
        }

    }


    //SORTING=======================================================================================
    /**
     * Sort books by Title
     */
    public void sortBooksByTitle(){
        Collections.sort(mBooksList, new Comparator<Books>() {
            @Override
            public int compare(Books lhs, Books rhs) {
                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
            }
        });
        bookRecycleViewAdapter.notifyDataSetChanged();
    }

    /**
     * Sort Books by Author
     */
    public void sortBooksByAuthor(){
        Collections.sort(mBooksList, new Comparator<Books>() {
            @Override
            public int compare(Books lhs, Books rhs) {
                return lhs.getAuthor().compareToIgnoreCase(rhs.getAuthor());
            }
        });
        bookRecycleViewAdapter.notifyDataSetChanged();
    }

    /**
     * Sort by Price
     */
    public void sortBooksByPrice(){
        Collections.sort(mBooksList, new Comparator<Books>() {
            @Override
            public int compare(Books lhs, Books rhs) {
                int lhsPrice = lhs.getPrice();
                int rhsPrice = rhs.getPrice();
                if (lhsPrice > rhsPrice) {
                    return 1;
                } else if (lhsPrice < rhsPrice) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        bookRecycleViewAdapter.notifyDataSetChanged();
    }




    //SEARCHVIEW IMPLEMENTATION=====================================================================

    /**
     *
     * @param list = list of books to be filtered
     * @param query = book title
     * @return
     */
    private List<Books> filter(List<Books> list, String query) {
        query = query.toLowerCase();
        final List<Books> filteredbookList = new ArrayList<>();
        for (Books book : list) {
            final String title = book.getTitle().toLowerCase();
            if (title.contains(query)) {
                filteredbookList.add(book);
            }
        }
        return filteredbookList;
    }


    public boolean onQueryTextSubmit(String query) {
        List<Books> filteredModelList = filter(mBooksList, query);

        for(Books b : filteredModelList){
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("id", b.getId());
            startActivity(intent);
        }
        return true;
    }


    public boolean onQueryTextChange(String newText) {
        List<Books> filteredModelList = filter(mBooksList, newText);
        if(newText.equals("")){
            mBooksList = listFromDB;
            bookRecycleViewAdapter.setBooksList(mBooksList);
        }else {
            mBooksList  = filteredModelList;
        bookRecycleViewAdapter.setBooksList(mBooksList);
        }

        bookRecycleViewAdapter.notifyDataSetChanged();
        return true;

    }


}
