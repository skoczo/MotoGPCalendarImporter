package com.skoczo.motogpcalendarimporter.async;

import android.content.Context;
import android.os.AsyncTask;

import com.skoczo.motogpcalendarimporter.ErrorSupport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by skoczo on 23.06.17.
 */

public class GetMotoGPCalendar extends AsyncTask {

    private final Context ctx;

    public GetMotoGPCalendar(Context ctx)
    {this.ctx = ctx;}

    @Override
    protected Document doInBackground(Object[] params) {
        try {
            return Jsoup.connect("http://www.motogp.com/en/calendar/").get();
        } catch (IOException e) {
            // TODO
            ErrorSupport.error("Error during calendar getting " + e.getMessage(), e, ctx);
        }

        return null;
    }
}
