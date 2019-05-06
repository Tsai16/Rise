package com.example.kk.rise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by KK on 3/29/2019.
 */

public class SnoozeActivity extends AppCompatActivity{

    AlarmManager alarm_manager;
    TextView update_text;
    PendingIntent pending_intent;
    Intent my_intent;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getExtras().getString("snooze");
        if (!message.equals(null) && message.equals("yes")) {
            activate_alarm(System.currentTimeMillis() + 30000);
            set_alarm_text("Alarm snoozed for 5 minutes");                                  //method that changes the update text box
            Log.e("Alarm snoozed", "for 5 minutes");
        }
    }

    //activate the alarm receiver when the time is ready
    //using the alarm manager and pending intent
    private void activate_alarm(long time){
        my_intent = new Intent(this, Alarm_Receiver.class);                  //create an intent to the Alarm_Receiver class
        my_intent.putExtra("extra","alarm on");                                //put in extra string that delays the intent
        //tells the clock that you have pressed the alarm on button
        pending_intent = PendingIntent.getBroadcast(SnoozeActivity.this,            //create a pendingIntent that will delay the intent
                0, my_intent,                                                   //until the specified calendar time
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);                     //initialize our alarm manager

        //alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,                               //set the alarm manager
        //_alarm, AlarmManager.INTERVAL_DAY, pending_intent);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, time,
                pending_intent);
    }
    private void set_alarm_text(String s) {
        update_text.setText(s);
    }


}
