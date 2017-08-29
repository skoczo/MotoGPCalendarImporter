package com.skoczo.motogpcalendarimporter.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.skoczo.motogpcalendarimporter.ErrorSupport;
import com.skoczo.motogpcalendarimporter.R;
import com.skoczo.motogpcalendarimporter.entities.MotoEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by skoczo on 23.06.17.
 */

public class EventToCalendarLoader extends AsyncTask {
    private final List<MotoEvent> events;
    private Activity activity;

    public EventToCalendarLoader(List<MotoEvent> events, Activity activity) {
        this.events = events;
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        final ProgressDialog progDial = (ProgressDialog) params[0];
        final Activity activity = ((Activity) params[1]);
        final Set<String> selectedCategories = (Set<String>) params[2];

        int i = 0;
        try {
            for (MotoEvent e : events) {
                Document doc = Jsoup.connect(e.getEventUrl()).get();
                Elements raceDays = doc.getElementsByClass("c-schedule__table");
                Elements offsetElement = doc.getElementsByClass("c-schedule__time radio active");
                String offset = null;
                if(offsetElement.size() > 0) {
                    offset = offsetElement.get(i).text();
                    offset = offset.substring(offset.indexOf("(")+1, offset.length()-1);
                    offset= offset.replace(" ", "");
                }

                for (int dayCount = 0; dayCount < raceDays.size(); dayCount++) {
                    Element raceDay = raceDays.get(dayCount);

                    // Date currentDay = (Date) e.getDate().clone();
                    Calendar currentDayCal = Calendar.getInstance();
                    currentDayCal.setTime(e.getDate());
                    currentDayCal.add(Calendar.DAY_OF_MONTH, (dayCount + 1) - raceDays.size());

                    Elements raceDayEvents = raceDay.getElementsByClass("c-schedule__table-row");
                    for (int eventCount = 0; eventCount < raceDayEvents.size(); eventCount++) {
                        Element raceDayEvent = raceDayEvents.get(eventCount);

                        Elements data = raceDayEvent.getElementsByClass("c-schedule__table-cell");
                        String hours = data.get(0).text();
                        String category = data.get(1).text();
                        String title = data.get(2).text();

                        if (selectedCategories.contains(category)) {
                            // create event
                            String[] startEnd = hours.split(" - ");
                            String[] startHours = startEnd[0].split(":");
                            String[] endHours = null;

                            if(startEnd.length > 1) {
                                endHours = startEnd[1].split(":");
                            }

                            currentDayCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHours[0]));
                            currentDayCal.set(Calendar.MINUTE, Integer.parseInt(startHours[1]));
                            currentDayCal.setTimeZone(TimeZone.getTimeZone(offset));
                            Date startDate = currentDayCal.getTime();

                            if (endHours == null) {
                                currentDayCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHours[0]) + 2);
                                currentDayCal.set(Calendar.MINUTE, Integer.parseInt(startHours[1]));
                            } else {
                                currentDayCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHours[0]));
                                currentDayCal.set(Calendar.MINUTE, Integer.parseInt(endHours[1]));
                            }

                            Date endDate = currentDayCal.getTime();

                            pushAppointmentsToCalender(activity,
                                    category + ": " + title,
                                    e.getName(),
                                    e.getLocation(),
                                    startDate.getTime(),
                                    endDate.getTime(),
                                    15,
                                    offset);
                        }
                    }

                }

                progDial.setProgress(++i);
            }
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), R.string.event_add_error_msg, Toast.LENGTH_LONG).show();
                }
            });

            ErrorSupport.error("Error during appintment add", e,activity.getApplicationContext());
            return false;
        } finally {
            progDial.dismiss();
        }

        ((Activity) params[1]).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), R.string.event_add_success_msg, Toast.LENGTH_SHORT).show();
            }
        });


        return true;
    }

    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place,
                                                  long startDate, long endDate, int reminder, String tz) {
        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);
        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);
        eventValues.put("eventStatus", 1);
        eventValues.put("eventTimezone", Calendar.getInstance().getTimeZone().getID());

        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        /***************** Event: Reminder(with alert) Adding reminder to event *******************/

        String reminderUriString = "content://com.android.calendar/reminders";

        ContentValues reminderValues = new ContentValues();

        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", reminder);
        reminderValues.put("method", 1);

        Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);

        return eventID;

    }
}
