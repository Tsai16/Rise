package com.example.kk.rise;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
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
import android.widget.Toast;

import java.text.ParseException;

/**
 * Created by KK on 3/27/2019.
 */

public class RingtonePlayingService extends Service {


    MediaPlayer media_song;
    int start_id;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}


    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("LocalService","Received start id " + startId + ": " + intent);
        //fetch the extra string values
        String state = intent.getExtras().getString("extra");
        Log.e("Ringtone state extra is", state);

        //this converts the extra strings from the intent
        //to start IDs, values 0 tor 1
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        //if / else statements

        //if there is no music playing and the user pressed "alarm on"
        //music should start playing
        if(!this.isRunning && startId == 1){
            Log.e("there is no music, ", "and you want on");
            //create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.wrecker);
            //start the ringtone
            media_song.start();



            this.isRunning = true;
            this.start_id = 0;
        }
        //if there is music playing, and the user pressed "alarm off"
        else if(this.isRunning && startId == 0){
            Log.e("there is music, ", "and you want off");
            //stop the ringtone
            media_song.stop();
            media_song.reset();
            this.isRunning = false;
            this.start_id = 0;

        }
        //these are if the user presses random buttons
        //just to bug-proof the app
        //if there is no music playing, and the user pressed "alarm off"
        //do nothing
        else if(!this.isRunning && startId == 0){
            Log.e("there is no music, ", "and you want off");
            this.isRunning = false;
            this.start_id = 0;
        }
        //if there is music playing, and the user pressed "alarm on"
        else if(this.isRunning && startId == 1){
            Log.e("there is music, ", "and you want on");
            this.isRunning = false;
            this.start_id = 1;
        }
        //can't think of anything else, just catch the odd events
        else{
            Log.e("Somehow, ", "you reached this");
        }

        return START_NOT_STICKY;
    }

    public void onDestroy(){
        //Tell the user that we stopped
        Log.e("On Destroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;

    }




}
