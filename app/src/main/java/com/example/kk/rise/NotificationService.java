package com.example.kk.rise;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.SerializablePermission;

/**
 * Created by KK on 3/29/2019.
 */

public class NotificationService extends Service {

    final String CHANNEL_ID = "225";
    NotificationChannel channel;
    MediaPlayer media_song;
    boolean isRunning;
    int broadcastId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId){

        String state = intent.getExtras().getString("extra");
        broadcastId = intent.getExtras().getInt(HomeActivity.EXTRA_BROADCAST_ID);

        if(this.isRunning && state.equals("alarm off")){
            stopAlarm();
        }else if(!this.isRunning && state.equals("alarm on")) {
            createNotificationChannel();
            runNotification(broadcastId);
            media_song = MediaPlayer.create(this, R.raw.wrecker);
            media_song.start();                                                             //start the ringtone
            this.isRunning = true;
        }
        //if there is no music playing, and the user pressed "alarm off"
        //do nothing
        else if(!this.isRunning && state.equals("alarm off")){
            Log.e("there is no music, ", "and you want off");
            this.isRunning = false;
        }
        //if there is music playing, and the user pressed "alarm on"
        else if(this.isRunning && state.equals("alarm on")){
            Log.e("there is music, ", "and you want on");
            this.isRunning = false;
        }
        //can't think of anything else, just catch the odd events
        else{
            Log.e("Somehow, ", "you reached this");
        }
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID,
                    "my channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("my notification channel");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void stopAlarm(){
        media_song.stop();
        media_song.reset();
        this.isRunning = false;
        stopSelf();
    }

    private void runNotification(int broadcastId){
        //notification
        //setup the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notify_manager.createNotificationChannel(channel);
        }

        //setup the intent that goes to the main activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        //set up pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        Intent snoozeIntent = new Intent(this, SnoozeActivity.class);
        snoozeIntent.setAction("snooze action");
        snoozeIntent.putExtra("extra", "snooze");
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        Intent dismissIntent = new Intent(this, Alarm_Receiver.class);
        dismissIntent.setAction("dismiss action");
        dismissIntent.putExtra("extra", "alarm off");
        PendingIntent dismissPendingIntent =
                PendingIntent.getBroadcast(this, 0, dismissIntent, 0);

        //make notification parameter
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("An alarm is going off")
                .setContentText("Click me")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.add_alarm, "snooze",
                        snoozePendingIntent)
                .addAction(R.drawable.add_alarm, "dismiss",
                        dismissPendingIntent);

        //setup notification start command
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        startForeground(broadcastId, builder.build());
        broadcastId++;

    }
}
