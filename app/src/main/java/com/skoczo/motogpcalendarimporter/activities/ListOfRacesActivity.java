package com.skoczo.motogpcalendarimporter.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.skoczo.motogpcalendarimporter.ErrorSupport;
import com.skoczo.motogpcalendarimporter.adapters.EventAdapter;
import com.skoczo.motogpcalendarimporter.async.GetMotoGPCalendar;
import com.skoczo.motogpcalendarimporter.async.GetEventsTask;
import com.skoczo.motogpcalendarimporter.async.GetTitleTask;
import com.skoczo.motogpcalendarimporter.entities.MotoEvent;
import com.skoczo.motogpcalendarimporter.R;
import com.skoczo.motogpcalendarimporter.utility.Utility;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class ListOfRacesActivity extends AppCompatActivity {

    public final static String APP_ID = "ca-app-pub-0659010952198256~3902225502";
    public final static String COMMERCIAL_ID = "ca-app-pub-0659010952198256/5378958706";
    public final static String FULL_COMMERCIAL_ID = "ca-app-pub-0659010952198256/9669557501";

    private ArrayList<MotoEvent> events;
    private ArrayAdapter<MotoEvent> adapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_races);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, ListOfRacesActivity.APP_ID);

        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
            finishAffinity();
        } else {

            TextView title = (TextView) findViewById(R.id.title);

            ((FloatingActionButton) findViewById(R.id.go_to_category_selection)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ListOfRacesActivity.this, CategorySelectionActivity.class);

                    myIntent.putExtra("events", events);
                    startActivity(myIntent);
                }
            });

            GetTitleTask titlePage = new GetTitleTask("http://www.motogp.com/en/calendar/");
            try {
                Document calDoc = (Document) (new GetMotoGPCalendar()).execute().get();
                if (calDoc == null) {
                    Toast.makeText(getApplicationContext(), R.string.cant_connect_to_the_page, Toast.LENGTH_SHORT).show();
                    Log.e(getClass().getName(), "Can't connect to the page");
                    finish();
                }

                Object titleStr = titlePage.execute(calDoc).get();

                if (titleStr == null) {
                    Toast.makeText(getApplicationContext(), R.string.cant_connect_to_the_page, Toast.LENGTH_SHORT).show();
                    Log.e(getClass().getName(), "Can't connect to the page");
                    finish();
                }

//            title.setText(titleStr.toString());
                title.setText((getString(R.string.main_title)));


                GetEventsTask eventsTask = new GetEventsTask(titleStr.toString().split(" ")[1]);
                events = (ArrayList<MotoEvent>) eventsTask.execute(calDoc).get();

                if (events == null) {
                    Toast.makeText(getApplicationContext(), R.string.event_build_error, Toast.LENGTH_LONG).show();
                    ErrorSupport.error("Can't build events");
                    finish();
                }

                ListView eventList = (ListView) findViewById(R.id.events_list);

                adapter = new EventAdapter(this, events);
                eventList.setAdapter(adapter);

            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                Toast.makeText(getApplicationContext(), R.string.cant_connect_to_the_page, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        mAdView.resume();

        super.onResume();
    }

    @Override
    protected void onPause() {
        mAdView.pause();

        super.onPause();
    }
}
