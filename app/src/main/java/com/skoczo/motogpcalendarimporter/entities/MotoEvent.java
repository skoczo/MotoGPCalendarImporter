package com.skoczo.motogpcalendarimporter.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by skoczo on 22.06.17.
 */

public class MotoEvent implements Serializable {
    private Date date;
    private String name;
    private String location;
    private String eventUrl;
    private Boolean selected;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl+"#schedule";
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name + " " + location;
    }
}
