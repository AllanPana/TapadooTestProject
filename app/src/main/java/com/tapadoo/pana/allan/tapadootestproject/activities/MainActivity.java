package com.tapadoo.pana.allan.tapadootestproject.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.extras.TagNToast;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentAllBooks;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentBookDetail;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String SORT_AUTHOR = "sortAuthor";
    private static final String SORT_TITLE = "sortTitle";
    private static final String SORT_PRICE = "sortPrice";
    private Toolbar toolbar;
    private FragmentAllBooks fragmentAllBook;
    private FragmentBookDetail fragmentBookDetail;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentAllBook = (FragmentAllBooks) fragmentManager.findFragmentById(R.id.fragmentAllBook);

        setFAB();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * All about Floating Action Buttonn
     */
    public void setFAB(){
        ImageView fabMainIcon = new ImageView(this);
        fabMainIcon.setImageResource(R.drawable.ic_add_white_36dp);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(fabMainIcon)
                .setBackgroundDrawable(R.drawable.red_touch)
                .build();


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
}
