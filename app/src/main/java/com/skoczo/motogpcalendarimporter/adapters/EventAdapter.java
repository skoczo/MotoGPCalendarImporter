package com.skoczo.motogpcalendarimporter.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.skoczo.motogpcalendarimporter.entities.MotoEvent;
import com.skoczo.motogpcalendarimporter.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by skoczo on 23.06.17.
 */

public class EventAdapter extends ArrayAdapter<MotoEvent> {

    private final Activity context;
    private final List<MotoEvent> data;
    private final Date currentDate;

    public EventAdapter(Activity context, List<MotoEvent> data) {
        super(context, R.layout.event_layout, data);


        currentDate = Calendar.getInstance().getTime();
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MotoEvent event = data.get(position);

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.event_layout, null, true);

        CheckBox selected = (CheckBox) rowView.findViewById(R.id.checkBox);
        selected.setChecked(event.getSelected());

        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setSelected(((CheckBox) v).isChecked());
            }
        });

        TextView eventText = (TextView) rowView.findViewById(R.id.event_name);
        TextView eventLocation = (TextView) rowView.findViewById(R.id.event_location);

        eventText.setText(event.getName());
        eventLocation.setText(event.getLocation());

        return rowView;
    }
}
