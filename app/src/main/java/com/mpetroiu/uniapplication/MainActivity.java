package com.mpetroiu.uniapplication;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
         Toolbar menu = (Toolbar) findViewById(R.id.main_menu);
         setSupportActionBar(menu);
         getSupportActionBar().setTitle(null);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

}
