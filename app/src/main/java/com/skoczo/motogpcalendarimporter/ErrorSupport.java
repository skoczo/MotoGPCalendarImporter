package com.skoczo.motogpcalendarimporter;

import android.util.Log;

/**
 * Created by skoczo on 22.06.17.
 */

public class ErrorSupport {

    public static void error(String msg, Exception e) {
        Log.e(ErrorSupport.class.getName(), msg,e);
    }

    public static void error(String msg) {
        Log.e(ErrorSupport.class.getName(), msg);
    }
}
