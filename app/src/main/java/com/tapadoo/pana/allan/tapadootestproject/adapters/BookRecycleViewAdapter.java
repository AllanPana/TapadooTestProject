package com.tapadoo.pana.allan.tapadootestproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.activities.BookDetailsActivity;
import com.tapadoo.pana.allan.tapadootestproject.extras.TagNToast;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentBookDetail;
import com.tapadoo.pana.allan.tapadootestproject.pojos.Books;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allan on 15/06/15.
 */
public class BookRecycleViewAdapter extends RecyclerView.Adapter<BookRecycleViewAdapter.ViewHolderBook>{

    private Context context;
    private LayoutInflater layoutInflater;
    private Books book;
    private List<Books>booksList = new ArrayList<>();

    private MyRecyclerViewOnClickListener myRecyclerViewOnClickListener;



    /**
     *
     * @param context
     */
    public BookRecycleViewAdapter(Context context){
        this.context=context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolderBook onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.custom_book_list, parent, false);
        ViewHolderBook viewHolderBook = new ViewHolderBook(itemView);
        return viewHolderBook;
    }

    @Override
    public void onBindViewHolder(ViewHolderBook holder, int position) {
        book = booksList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewPrice.setText("$ "+book.getPrice());
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }


    /**
     *
     * @param booksList to store all books
     */
    public void setBooksList(List<Books> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder use by Adapter
     */
    class ViewHolderBook extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewAuthor;
        private TextView textViewPrice;

        public ViewHolderBook(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewAuthor= (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            myRecyclerViewOnClickListener.myREcyclerViewOnClick(v, getAdapterPosition());
        }
    }



    public void setMyRecyclerViewOnClickListener(MyRecyclerViewOnClickListener myRecyclerViewOnClickListener) {
        this.myRecyclerViewOnClickListener = myRecyclerViewOnClickListener;
    }

    /**
     * Call its method in the onClick() of ViewHolder class
     * implement this interface to communicate with the Fragment
     *
     */
    public interface MyRecyclerViewOnClickListener{
        public void myREcyclerViewOnClick(View view,int position);
    }
}
