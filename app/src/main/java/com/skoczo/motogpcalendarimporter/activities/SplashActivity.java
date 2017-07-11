package com.skoczo.motogpcalendarimporter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.skoczo.motogpcalendarimporter.R;
import com.skoczo.motogpcalendarimporter.utility.Utility;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, ListOfRacesActivity.APP_ID);

        mAdView = Utility.addCommercials((AdView) findViewById(R.id.adView));

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

        RunSplash.schedule(ShowSplash, 5000);
    }

    @Override
    protected void onResume() {
        if(mAdView!=null)mAdView.resume();

        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mAdView!=null)mAdView.pause();

        super.onPause();
    }
}
