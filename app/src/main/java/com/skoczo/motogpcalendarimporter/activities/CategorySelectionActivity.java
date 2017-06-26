package com.skoczo.motogpcalendarimporter.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skoczo.motogpcalendarimporter.adapters.CalendarAdapter;
import com.skoczo.motogpcalendarimporter.entities.CalendarEntry;
import com.skoczo.motogpcalendarimporter.async.EventToCalendarLoader;
import com.skoczo.motogpcalendarimporter.entities.MotoEvent;
import com.skoczo.motogpcalendarimporter.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class CategorySelectionActivity extends AppCompatActivity {

    private ArrayList<CalendarEntry> calendars;
    private ArrayList<MotoEvent> events;
    private ProgressDialog progressDialog;
    private EventToCalendarLoader loader;
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    private CalendarAdapter calAdapter;
    private ListView calendarsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        calendars = new ArrayList<>();

        while (cur.moveToNext()) {
            Log.i(getClass().getName(), cur.getString(1));
            Log.i(getClass().getName(), cur.getString(2));
            Log.i(getClass().getName(), cur.getString(3));
            Log.i(getClass().getName(), "----------------");

            calendars.add(new CalendarEntry(cur.getString(1), cur.getString(2), cur.getString(3)));
        }

        calAdapter = new CalendarAdapter(this, calendars);

        events = (ArrayList<MotoEvent>) getIntent().getExtras().get("events");
//        calendarsList = (ListView) findViewById(R.id.calendars);
//        calendarsList.setAdapter(calAdapter);

        events = filterEventsToLoad(events);
        FloatingActionButton generate = (FloatingActionButton) findViewById(R.id.generate_events);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView delay = (TextView) findViewById(R.id.delay_val);
                if (delay.getText().length() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.delay_validation_msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if (categorySelected()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = new ProgressDialog(CategorySelectionActivity.this);
                            progressDialog.setMax(events.size());
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setMessage(getString(R.string.loading_events));
                            progressDialog.show();
                        }
                    });


                    loader = new EventToCalendarLoader(events, CategorySelectionActivity.this);
                    loader.execute(progressDialog, CategorySelectionActivity.this, categorySelectedSet());

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.no_category_validation_msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private ArrayList<MotoEvent> filterEventsToLoad(ArrayList<MotoEvent> events) {
        ArrayList<MotoEvent> filtered = new ArrayList<>();

        for (MotoEvent e : events) {
            if (e.getSelected() != null && e.getSelected()) {
                filtered.add(e);
            }
        }

        return filtered;
    }

    private Set<String> categorySelectedSet() {
        Set<String> categories = new HashSet<>();
        if (((CheckBox) findViewById(R.id.motogp_checkbox)).isChecked())
            categories.add("MotoGP");

        if (((CheckBox) findViewById(R.id.moto2_checkbox)).isChecked())
            categories.add("Moto2");

        if (((CheckBox) findViewById(R.id.moto3_checkbox)).isChecked())
            categories.add("Moto3");

        return categories;
    }

    private boolean categorySelected() {
        return ((CheckBox) findViewById(R.id.motogp_checkbox)).isChecked() ||
                ((CheckBox) findViewById(R.id.moto2_checkbox)).isChecked() ||
                ((CheckBox) findViewById(R.id.moto3_checkbox)).isChecked();
    }


}
