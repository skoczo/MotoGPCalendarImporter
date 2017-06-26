package com.skoczo.motogpcalendarimporter.async;

import android.os.AsyncTask;

import com.skoczo.motogpcalendarimporter.ErrorSupport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by skoczo on 23.06.17.
 */

public class GetMotoGPCalendar extends AsyncTask {
    @Override
    protected Document doInBackground(Object[] params) {
        try {
            return Jsoup.connect("http://www.motogp.com/en/calendar/").get();
        } catch (IOException e) {
            // TODO
            ErrorSupport.error("Error during calendar getting " + e.getMessage(), e);
        }

        return null;
    }
}
