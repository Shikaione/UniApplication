package com.mpetroiu.uniapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private NavigationView mNavigationView;
    private SearchView mSearchView;
    private TextView mUserName,mUserEmail;
    private String uid;
    private FrameLayout mFrameLayout;
    private Inflater mInflater;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View header = findViewById(R.id.nav_view);




        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            uid = mUser.getUid();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(null);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView  = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            setupNavigationDrawerContent(mNavigationView);
        }

        setupNavigationDrawerContent(mNavigationView);

        View headerView = mNavigationView.getHeaderView(0);

        mUserName = (TextView) headerView.findViewById(R.id.logName);
        mUserEmail = (TextView) headerView.findViewById(R.id.userEmail);

    }

    private void addContentView(int nav_header) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirestore.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot != null){
                  mUserName.setText(documentSnapshot.getString("name"));
                  mUserEmail.setText(documentSnapshot.getString("email"));
                }

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_layout, menu);

        // Getting search action from action bar and setting up search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView)searchItem.getActionView();

        // Setup searchView
        //setupSearchView(searchItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_places:
                                menuItem.setChecked(true);
                                mActionBar.setTitle(menuItem.getTitle());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_favorite:
                                menuItem.setChecked(true);
                                mActionBar.setTitle(menuItem.getTitle());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.nav_settings:
                                menuItem.setChecked(true);
                                mActionBar.setTitle(menuItem.getTitle());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                return true;
                            case R.id.action_logout:
                                menuItem.setChecked(true);
                                Toast.makeText(MainActivity.this, "See you again ! ", Toast.LENGTH_SHORT).show();
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                mAuth.signOut();
                                mGoogleSignInClient.signOut();
                                LoginManager.getInstance().logOut();
                                finish();
                                return true;
                        }
                        return true;
                    }
                });
    }
}


