package com.example.kk.rise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int editKey = -1;
    private int broadcastId;
    private AlarmManager alarm_manager;
    private TimePicker alarm_timepicker;
    private TextView update_text;
    private Context context;
    private Calendar calendar;
    private PendingIntent pending_intent;
    private Intent my_intent;
    private Intent home_intent;
    private ArrayList<AlarmObject> alarmObjects;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        this.context = this;
        home_intent = new Intent(this.context, HomeActivity.class);
        my_intent = new Intent(this.context, Alarm_Receiver.class);                         //create an intent to the Alarm_Receiver class
        alarm_timepicker = findViewById(R.id.timePicker);                                   //initialize our timepicker
        calendar = Calendar.getInstance();                                                  //create an instance of a calendar
        Bundle data = getIntent().getExtras();
        if (data != null){
            if(data.getParcelableArrayList(HomeActivity.EXTRA_ALARM_OBJECT) != null) {
                alarmObjects = data.getParcelableArrayList(HomeActivity.EXTRA_ALARM_OBJECT);
            }
            if(getIntent().hasExtra(HomeActivity.EXTRA_BROADCAST_ID)){
                broadcastId = data.getInt(HomeActivity.EXTRA_BROADCAST_ID);
            }
            if(getIntent().hasExtra(HomeActivity.EXTRA_EDIT_AMPM)){
                if (data.getString(HomeActivity.EXTRA_EDIT_AMPM).equals("pm")) {
                    alarm_timepicker.setHour(data.getInt(HomeActivity.EXTRA_EDIT_HOUR) + 12);
                } else {
                    alarm_timepicker.setHour(data.getInt(HomeActivity.EXTRA_EDIT_HOUR));
                }
            }
            if(getIntent().hasExtra(HomeActivity.EXTRA_EDIT_MINUTE)) {
                alarm_timepicker.setMinute(data.getInt(HomeActivity.EXTRA_EDIT_MINUTE));
            }
            if(getIntent().hasExtra(HomeActivity.EXTRA_EDIT_KEY)) {
                editKey = data.getInt(HomeActivity.EXTRA_EDIT_KEY);
            }

        }
        Button start_alarm = findViewById(R.id.alarm_on);                                   //initialize buttons
        start_alarm.setOnClickListener(new View.OnClickListener() {                         //create an onClick listener to start the alarm
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                long _alarm = get_time();
                print_alarm_time(alarmObjects, editKey);
                activate_alarm(_alarm, alarmObjects, broadcastId, editKey);
                home_intent.putParcelableArrayListExtra(HomeActivity.EXTRA_ALARM_OBJECT, alarmObjects);
                home_intent.putExtra(HomeActivity.EXTRA_BROADCAST_ID,broadcastId);
                if(alarmObjects.size()>0)
                    Log.e("ampm is", alarmObjects.get(alarmObjects.size()-1).getAmpm());
                startActivity(home_intent);
            }
        });
    }

    //gets the time on the time picker and returns the
    //alarm time
    @RequiresApi(api = Build.VERSION_CODES.M)
    private long get_time(){
        long _alarm = 0;
        Calendar now = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());                     //setting calendar instance with the hour and minute
        calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());                        //that we picked on the timepicker

        if(calendar.getTimeInMillis() <= now.getTimeInMillis()){
            _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
        }else{
            _alarm = calendar.getTimeInMillis();
        }
        return _alarm;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void print_alarm_time(ArrayList<AlarmObject> alarmObjects, int editKey){
        int hour = alarm_timepicker.getHour();                                              //get the int of the hour and minute
        int minute = alarm_timepicker.getMinute();
        String ampm = "am";
        AlarmObject object;
        String hour_string = String.valueOf(hour);                                          //convert the int values to string
        String minute_string = String.valueOf(minute);
        if(hour > 12){
            hour_string = String.valueOf(hour-12);
            ampm = "pm";
        }
        if(minute < 10){
            minute_string = "0" + String.valueOf(minute);
        }

        if(editKey < 0) {
            object = new AlarmObject(hour_string, minute_string, ampm);
            alarmObjects.add(object);
        }else{
            alarmObjects.get(editKey).setHour(hour_string);
            alarmObjects.get(editKey).setMinutes(minute_string);
            alarmObjects.get(editKey).setAmpm(ampm);
        }
    }

    //activate the alarm receiver when the time is ready
    //using the alarm manager and pending intent
    private void activate_alarm(long time, ArrayList<AlarmObject> alarmObjects, int broadcastId, int editKey){

        my_intent.putExtra("extra","alarm on");                                //put in extra string that delays the intent
                                                                                            //tells the clock that you have pressed the alarm on button
        my_intent.putExtra(HomeActivity.EXTRA_BROADCAST_ID, broadcastId);
        pending_intent = PendingIntent.getBroadcast(MainActivity.this,              //create a pendingIntent that will delay the intent
                broadcastId, my_intent,                                                     //until the specified calendar time
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);             //initialize our alarm manager
        broadcastId++;                                                                      //increment the broadcastId
        if(editKey < 0) {
            alarmObjects.get(alarmObjects.size() - 1).setPendingIntent(pending_intent);
        }else{
            alarm_manager.cancel(alarmObjects.get(editKey).getPendingIntent());
            alarmObjects.get(editKey).setPendingIntent(pending_intent);
        }
                                                                                            //add pending intent to the alarm object
        //alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,                               //set the alarm manager
        //_alarm, AlarmManager.INTERVAL_DAY, pending_intent);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, time,
                pending_intent);

    }


}
