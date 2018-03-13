package com.skoczo.motogpcalendarimporter.entities;

/**
 * Created by skoczo on 23.06.17.
 */

public class CalendarEntry {
    private final long calendarID;
    private String accountName;
    private String displayName;
    private String ownerAccount;

    public CalendarEntry(long calendarID, String accountName, String displayName, String ownerAccount) {
        this.calendarID = calendarID;
        this.accountName = accountName;
        this.displayName = displayName;
        this.ownerAccount = ownerAccount;
    }

    public boolean isDefault() {
        return accountName.equals(ownerAccount) && displayName.equals(accountName);
    }

    public String getAccountName() {
        return accountName;
    }

    public long getCalendarID() {
        return calendarID;
    }

    public String getDisplayName() {
        return displayName;
    }


    public String getOwnerAccount() {
        return ownerAccount;
    }

}
