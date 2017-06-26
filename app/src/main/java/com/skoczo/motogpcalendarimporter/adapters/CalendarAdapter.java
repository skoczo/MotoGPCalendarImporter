package com.skoczo.motogpcalendarimporter.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skoczo.motogpcalendarimporter.R;
import com.skoczo.motogpcalendarimporter.entities.CalendarEntry;
import com.skoczo.motogpcalendarimporter.entities.MotoEvent;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by skoczo on 23.06.17.
 */

public class CalendarAdapter extends ArrayAdapter<CalendarEntry> {
    private Activity context;
    private ArrayList<CalendarEntry> data;

    public CalendarAdapter(@NonNull Activity context, ArrayList<CalendarEntry> data) {
        super(context, R.layout.event_layout, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final CalendarEntry event = data.get(position);

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.event_layout, null, true);

        TextView eventText = (TextView) rowView.findViewById(R.id.event_name);
        TextView eventLocation = (TextView) rowView.findViewById(R.id.event_location);

        eventText.setText(event.getAccountName());
        eventLocation.setText(event.getDisplayName());

        return  rowView;
    }
}
