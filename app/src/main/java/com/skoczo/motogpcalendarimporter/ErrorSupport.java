package com.skoczo.motogpcalendarimporter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by skoczo on 22.06.17.
 */

public class ErrorSupport {

    public static void error(String msg, Exception e, Context ctx) {
        Log.e(ErrorSupport.class.getName(), msg, e);

        Toast.makeText(ctx, "Unknown error", Toast.LENGTH_LONG).show();
    }

    public static void error(String msg, Context ctx ) {
        Log.e(ErrorSupport.class.getName(), msg);
        Toast.makeText(ctx, "Unknown error", Toast.LENGTH_LONG).show();
    }
}
