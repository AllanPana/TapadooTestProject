package com.tapadoo.pana.allan.tapadootestproject.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tapadoo.pana.allan.tapadootestproject.R;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentAllBooks;
import com.tapadoo.pana.allan.tapadootestproject.fragments.FragmentBookDetail;


public class MainActivity extends AppCompatActivity implements FragmentAllBooks.Communicator{

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
        fragmentAllBook.setCommunicator(this);

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

    @Override
    public void setBookDetail(int position) {

    }
}