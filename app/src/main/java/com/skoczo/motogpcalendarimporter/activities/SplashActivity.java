package com.skoczo.motogpcalendarimporter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skoczo.motogpcalendarimporter.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                // Start MainActivity.class
                Intent myIntent = new Intent(SplashActivity.this,
                        ListOfRacesActivity.class);
                startActivity(myIntent);

                // Close SplashScreenActivity.class
                finish();
            }
        };

        RunSplash.schedule(ShowSplash, 1000);
    }
}
