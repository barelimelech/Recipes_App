package com.example.recipes_app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

public class LoginActivity extends AppCompatActivity {

    NavController navCtl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navCtl = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navCtl);


    }


        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            if (!super.onOptionsItemSelected(item)) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        navCtl.navigateUp();
                        return true;
                    default:
                        NavigationUI.onNavDestinationSelected(item, navCtl);
                }
            } else {
                return true;
            }
            return false;
        }

    }
