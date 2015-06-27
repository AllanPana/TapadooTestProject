package com.tapadoo.pana.allan.tapadootestproject.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentAllBooks;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentBookDetail;
import com.tapadoo.pana.allan.tapadootestproject.services.MyService;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private static final String SORT_AUTHOR = "sortAuthor";
    private static final String SORT_TITLE = "sortTitle";
    private static final String SORT_PRICE = "sortPrice";
    private static final int JOB_ID = 100;
    private Toolbar toolbar;
    private FragmentAllBooks fragmentAllBook;
    private FragmentBookDetail fragmentBookDetail;
    private FragmentManager fragmentManager;

    private JobScheduler jobScheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobScheduler = JobScheduler.getInstance(this);

        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentAllBook = (FragmentAllBooks) fragmentManager.findFragmentById(R.id.fragmentAllBook);

        constructJob();
        setFAB();

    }


    /**
     *
     * @param menu = SearchView
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(searchView.getImeOptions()
                | EditorInfo.IME_ACTION_SEARCH
                | EditorInfo.IME_FLAG_NO_EXTRACT_UI
                | EditorInfo.IME_FLAG_NO_FULLSCREEN);

        searchView.setOnQueryTextListener(this);
        return true;
    }



     //========All about Floating Action Buttonn
    public void setFAB(){
        ImageView fabMainIcon = new ImageView(this);
        fabMainIcon.setImageResource(R.drawable.ic_add_white_36dp);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(fabMainIcon)
                .setBackgroundDrawable(R.drawable.red_touch)
                .build();


        //Set up Floating action menu
        ImageView sortByTitle = new ImageView(this);
        ImageView sortByAuthor = new ImageView(this);
        ImageView sortByPrice = new ImageView(this);
        sortByTitle.setImageResource(R.drawable.ic_format_color_text_white_24dp);
        sortByAuthor.setImageResource(R.drawable.ic_person_white_24dp);
        sortByPrice.setImageResource(R.drawable.ic_attach_money_white_24dp);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_touch));
        SubActionButton buttonSortByTitle = itemBuilder.setContentView(sortByTitle).build();
        SubActionButton buttonSortByAuthor = itemBuilder.setContentView(sortByAuthor).build();
        SubActionButton buttonSortByPrice = itemBuilder.setContentView(sortByPrice).build();

        buttonSortByTitle.setTag(SORT_TITLE);
        buttonSortByAuthor.setTag(SORT_AUTHOR);
        buttonSortByPrice.setTag(SORT_PRICE);
        buttonSortByTitle.setOnClickListener(this);
        buttonSortByAuthor.setOnClickListener(this);
        buttonSortByPrice.setOnClickListener(this);


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortByTitle)
                .addSubActionView(buttonSortByAuthor)
                .addSubActionView(buttonSortByPrice)
                .attachTo(actionButton)
                .build();
    }


    /**
     *
     * @param v = action menu button
     */
    @Override
    public void onClick(View v) {
        if(v.getTag().equals(SORT_TITLE)){
            fragmentAllBook.sortBooksByTitle();

        }
        if(v.getTag().equals(SORT_AUTHOR)){
            fragmentAllBook.sortBooksByAuthor();
        }
        if(v.getTag().equals(SORT_PRICE)){
           fragmentAllBook.sortBooksByPrice();
        }
    }
    //=========end FAB setup




    //=======================SEARCHVIEW=========================
    @Override
    public boolean onQueryTextSubmit(String query) {
        fragmentAllBook.onQueryTextSubmit(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        fragmentAllBook.onQueryTextChange(newText);
        return true;
    }
    //SearchView setup end


    /**
     * 1.Build a jobservice
     * 2.Schedule the job by pasing the jobinfo into the jobscheduler
     */
    public void constructJob(){
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,new ComponentName(this, MyService.class));

        //polling/interval every 30minutes
        builder.setPeriodic(1800000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(true)
                .setPersisted(true);



        jobScheduler.schedule(builder.build());
    }

}//class end
