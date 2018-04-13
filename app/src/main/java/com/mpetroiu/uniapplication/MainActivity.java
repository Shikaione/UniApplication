package com.mpetroiu.uniapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean logged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating the toolbar( menu ) when creating the activity
         Toolbar menu = (Toolbar) findViewById(R.id.menu);
         setSupportActionBar(menu);

         //No title
         getSupportActionBar().setTitle(null);
         //Activating the back icon on the menu
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Menu Item
     * Search Option
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        /*Daca Userul este loggat si fiseaza un Menu
         *Daca nu este logat afiseaza un Guest Menu
         */
        if(logged == false) {
            getMenuInflater().inflate(R.menu.guest_menu, menu);
        }else{
            getMenuInflater().inflate(R.menu.user_menu, menu);
        }
        //Search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Iteme din Menu si actiunile acestora
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        else if (id == R.id.action_login) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
