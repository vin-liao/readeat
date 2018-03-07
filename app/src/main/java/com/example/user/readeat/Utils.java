package com.example.user.readeat;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Insert utility in here
 */

public final class Utils {

    //private constructor because this class is not created to be instantiated.
    private Utils(){}

    public static String utcToReadableTime(long created){
        long unixTime = System.currentTimeMillis() / 1000L;
        String stringTime = "";
        int createdSeconds = (int)(unixTime - created);
        if(createdSeconds > 86400){
            //days ago
            stringTime = String.valueOf(((unixTime - created) / 86400) + " days ago");
        }
        else if(createdSeconds > 3600) {
            //hours ago
            //timeView.setText(String.valueOf(((unixTime - created) / 3600) + " hours ago"));
            stringTime = String.valueOf(((unixTime - created) / 3600) + " hours ago");
        }else{
            //minutes ago
            //timeView.setText(String.valueOf(((unixTime - created) / 60) + " minutes ago"));
            stringTime = String.valueOf(((unixTime - created) / 60) + " minutes ago");
        }
        return stringTime;
    }
}
