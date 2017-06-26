package com.skoczo.motogpcalendarimporter.async;

import android.os.AsyncTask;

import com.skoczo.motogpcalendarimporter.ErrorSupport;
import com.skoczo.motogpcalendarimporter.entities.MotoEvent;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by skoczo on 22.06.17.
 */

public class GetEventsTask extends AsyncTask {
    private final String year;
    private Date currentDate;

    public GetEventsTask(String year) {
        this.year = year;
        currentDate = Calendar.getInstance().getTime();
    }

    @Override
    protected List<MotoEvent> doInBackground(Object[] params) {
        try {
            Document document = (Document) params[0];

            Elements events = document.getElementsByClass("event_container");

            List<MotoEvent> eventsList = new ArrayList<>();

            for(int i=0; i<events.size(); i++) {
                Element e = events.get(i);
                if(!e.hasClass("hidden")) {
                    eventsList.add(buildEvent(e));
                }
            }

            return eventsList;
        } catch (Exception e) {
            ErrorSupport.error("Error during event build: " + e.getMessage(), e);
        }


        return null;
    }

    private MotoEvent buildEvent(Element e) throws ParseException {
        MotoEvent event = new MotoEvent();
        event.setName(getValue(e,"event_title"));

        String day = getValue(e, "event_day");
        String month = getValue(e, "event_month");

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMMdd", Locale.ENGLISH);
        Date date = df.parse(year + month + day);
        event.setDate(date);

        event.setEventUrl(e.getElementsByClass("event_name").get(0).attr("href"));

        event.setLocation(getValue(e,"location"));

        if(event.getDate().before(currentDate)) {
            event.setSelected(false);
        } else {
            event.setSelected(true);
        }

        return event;
    }

    private String getValue(Element e, String event_day) {
        Elements elements = e.getElementsByClass(event_day);

        if(elements.size() == 0 || elements.size() > 1) {
            ErrorSupport.error("Error during event parse");
        }

        return elements.text();
    }
}
