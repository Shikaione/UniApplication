package com.mpetroiu.uniapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SharedPreferences prefs = null;

    private FirebaseAuth mAuth;

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
         //Sa nu vina bara de status peste navigation
         menu.setPadding(0, getStatusBarHeight(), 0, 0);

         //instantiaza autentificarea si firaestore(fara asta nu ar merge)
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
