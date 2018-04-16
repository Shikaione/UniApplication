package com.mpetroiu.uniapplication;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Toolbar menu = findViewById(R.id.menu);
         mDrawerLayout = findViewById(R.id.drawer_layout);
         setSupportActionBar(menu);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionbar.setTitle(null);
            menu.setPadding(0, getStatusBarHeight(), 0, 0);
        }




        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

         mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Status Bar Height
     * Am setat bara de "status"  transparenta in Style.xml
     * Avem nevoie de getStatusBarHeight() pentru a lua inaltimea
     * si a o folosi sa nu vina peste navigationBar
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Menu Item
     * Search Option
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        /*Daca Userul este loggat si fiseaza un Menu
         *Daca nu este logat afiseaza un Guest Menu
         */

        if(mAuth.getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.guest_menu, menu);
            Log.e(TAG, "Este User logat : " + mAuth.getCurrentUser());
        }else {
            getMenuInflater().inflate(R.menu.user_menu, menu);
            Log.e(TAG, "Este User logat : " + mAuth.getCurrentUser());
        }
        //Search

       // MenuItem searchItem = menu.findItem(R.id.action_search);
      //  SearchView searchView = (SearchView) searchItem.getActionView();

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

        if (id == R.id.home && mAuth.getCurrentUser() == null) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        else if(id == R.id.home && mAuth.getCurrentUser() != null ){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        else if (id == R.id.action_login) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            return true;
        }
        else if (id == R.id.action_logout){
            if(mAuth.getCurrentUser() != null){
                mAuth.signOut();
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
