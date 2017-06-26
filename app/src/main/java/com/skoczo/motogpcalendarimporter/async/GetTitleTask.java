package com.skoczo.motogpcalendarimporter.async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by skoczo on 22.06.17.
 */

public class GetTitleTask extends AsyncTask {

    private final String url;

    public GetTitleTask(String url) {
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Document doc = (Document) params[0];
        Elements titleClasses = doc.getElementsByClass("title");

        for (int i = 0; i < titleClasses.size(); i++) {
            if (titleClasses.get(i).text().contains("Calendar")) {
                return "MotoGP " + titleClasses.get(i).text();
            }
        }

        Log.e(this.getClass().getName(), "Can't parse page");

        return null;
    }
}
