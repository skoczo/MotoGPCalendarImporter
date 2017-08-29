package com.skoczo.motogpcalendarimporter.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.skoczo.motogpcalendarimporter.R;

/**
 * Created by skoczo on 09.07.17.
 */

public class Utility {
    private static boolean isCommercial = true;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static AdView addCommercials(AdView adView) {
        if(!isCommercial)
            return null;

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("4B4E791193D432168AB081047D7262E1").addTestDevice("AE39FB318BB3859940C843793601F26E").build();
        adView.loadAd(adRequest);

        return adView;
    }
}
