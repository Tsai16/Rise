package com.example.kk.rise;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

/**
 * Created by KK on 3/27/2019.
 */

public class Alarm_Receiver extends BroadcastReceiver {

    MediaPlayer media_song;
    int start_id;
    boolean isRunning;

    @Override
    public void onReceive(Context context, Intent intent) {

        //this is how you fetch the extra strings from the intent
        String get_your_string =  intent.getExtras().getString("extra");
        int broadcastId = intent.getExtras().getInt(HomeActivity.EXTRA_BROADCAST_ID);
        Intent notification_intent = new Intent(context, NotificationService.class);
        Log.e("What is the key", get_your_string);
        notification_intent.putExtra("extra", get_your_string);
        notification_intent.putExtra(HomeActivity.EXTRA_BROADCAST_ID, broadcastId);


        //start notification service

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1
                && get_your_string.equals("alarm on")) {
            context.startForegroundService(notification_intent);
        }else {
            context.startService(notification_intent);
        }


    }
}


