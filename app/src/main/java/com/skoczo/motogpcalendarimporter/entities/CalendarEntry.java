package com.skoczo.motogpcalendarimporter.entities;

/**
 * Created by skoczo on 23.06.17.
 */

public class CalendarEntry {
    private String accountName;
    private String displayName;
    private String ownerAccount;

    public CalendarEntry(String accountName, String displayName, String ownerAccount) {
        this.accountName = accountName;
        this.displayName = displayName;
        this.ownerAccount = ownerAccount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }
}
