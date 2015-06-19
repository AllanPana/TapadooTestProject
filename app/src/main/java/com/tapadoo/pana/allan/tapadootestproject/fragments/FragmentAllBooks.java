package com.tapadoo.pana.allan.tapadootestproject.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
        Util.showProgressBar(progressDialog);
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


    /**
     *
     * @param view
     * @param position
     */
    @Override
    public void myREcyclerViewOnClick(View view, int position) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentBookDetail fragmentBookDetail =
                (FragmentBookDetail) fragmentManager.findFragmentById(R.id.fragmentBookDetails);
        Books book = mBooksList.get(position);

        //check if your layout in a landscape orientation
        if(fragmentBookDetail != null && fragmentBookDetail.isVisible()){
            Util.setLog("landscape mode................" + book.getId());
            //fragmentBookDetail.setBookDetails(fragmentBookDetail.mBook);
            fragmentBookDetail.sendJsonRequest(book.getId());

        }else {
            Util.setLog("portrait mode................" + book.getId());
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("id", book.getId());
            startActivity(intent);
        }

    }


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




    //SEARCHVIEW IMPLEMENTATION


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
            Util.setToast(getActivity(), "id..............." + b.getId());
            startActivity(intent);
        }

        return true;
    }


    public boolean onQueryTextChange(String newText) {
        final List<Books> filteredModelList = filter(mBooksList, newText);
        if(newText.equals("")){
            bookRecycleViewAdapter.setBooksList(mBooksList);
        }else {
            bookRecycleViewAdapter.setBooksList(filteredModelList);
        }

        bookRecycleViewAdapter.notifyDataSetChanged();
        return true;

    }

   /* public Books removeItem(int position) {
        final Books book = mBooksList.remove(position);
        bookRecycleViewAdapter.notifyItemRemoved(position);
        return book;
    }

    public void addItem(int position, Books book) {
        mBooksList.add(position, book);
        bookRecycleViewAdapter.notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Books books = mBooksList.remove(fromPosition);
        mBooksList.add(toPosition, books);
        bookRecycleViewAdapter.notifyItemMoved(fromPosition, toPosition);
    }



    public void animateTo(List<Books> list) {
        applyAndAnimateRemovals(list);
        applyAndAnimateAdditions(list);
        applyAndAnimateMovedItems(list);
    }



    private void applyAndAnimateRemovals(List<Books> list) {
        for (int i = mBooksList.size() - 1; i >= 0; i--) {
            final Books book = mBooksList.get(i);
            if (!list.contains(book)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Books> list) {
        for (int i = 0, count = list.size(); i < count; i++) {
            final Books book = list.get(i);
            if (!list.contains(book)) {
                addItem(i, book);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Books> list) {
        for (int toPosition = list.size() - 1; toPosition >= 0; toPosition--) {
            final Books book = list.get(toPosition);
            final int fromPosition = mBooksList.indexOf(book);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


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

        Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
        intent.putExtra("id", filteredModelList.get(0));
        startActivity(intent);
        Util.setLog(query);
        return true;
    }


    public boolean onQueryTextChange(String newText) {
        final List<Books> filteredModelList = filter(mBooksList, newText);
        animateTo(filteredModelList);
        recyclerViewBook.scrollToPosition(0);
        return true;

    }
*/



}
