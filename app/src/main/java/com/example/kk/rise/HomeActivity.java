package com.example.kk.rise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by KK on 3/29/2019.
 */

public class HomeActivity extends AppCompatActivity {

    public final static String EXTRA_ALARM_OBJECT = "alarmObject";
    public final static String EXTRA_BROADCAST_ID = "broadcastId";
    public final static String EXTRA_EDIT_HOUR = "editHour";
    public final static String EXTRA_EDIT_MINUTE = "editMinute";
    public final static String EXTRA_EDIT_AMPM = "editAmpm";
    public final static String EXTRA_EDIT_KEY = "editKey";



    private int broadcastId = 1;
    private LinearLayout alarmList;
    private LayoutInflater inflator;
    private ArrayList<AlarmObject> alarmObjects;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        Bundle data = getIntent().getExtras();
        if (data != null){
            if(data.getParcelableArrayList(EXTRA_ALARM_OBJECT) != null) {
                alarmObjects = data.getParcelableArrayList(EXTRA_ALARM_OBJECT);
            }
            if(getIntent().hasExtra(EXTRA_BROADCAST_ID)){
                broadcastId = data.getInt(EXTRA_BROADCAST_ID);
            }
        }else{
            this.alarmObjects = new ArrayList<>();
            this.broadcastId = 1;
        }

        setAlarmList();



        //go to set alarm activity (main activity)
        Button set_button = findViewById(R.id.set_alarm_button);
        set_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent alarmIntent = new Intent(HomeActivity.this, MainActivity.class);
                alarmIntent.putParcelableArrayListExtra(EXTRA_ALARM_OBJECT, alarmObjects);
                alarmIntent.putExtra(EXTRA_BROADCAST_ID, broadcastId);
                startActivity(alarmIntent);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    private void setAlarmList(){

        TextView timeText;

        alarmList = findViewById(R.id.alarm_list);
        inflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Comparator<AlarmObject> byAmpm = new Comparator<AlarmObject>() {
            @Override
            public int compare(AlarmObject a1, AlarmObject a2) {
                return (a1.getAmpm().compareTo(a2.getAmpm()));
            }
        };
        Comparator<AlarmObject> byHour = new Comparator<AlarmObject>() {
            @Override
            public int compare(AlarmObject a1, AlarmObject a2) {
                return (a1.getHour().compareTo(a2.getHour()));
            }
        };
        Comparator<AlarmObject> byMinute = new Comparator<AlarmObject>() {
            @Override
            public int compare(AlarmObject a1, AlarmObject a2) {
                return (a1.getMinutes().compareTo(a2.getMinutes()));
            }
        };

        Collections.sort(alarmObjects, byAmpm.thenComparing(byHour).thenComparing(byMinute));

        for(int i =0; i <alarmObjects.size() ; i++){
            View view = inflator.inflate(R.layout.alarm_details, alarmList, false);
            timeText = view.findViewById(R.id.time_text);
            String time = alarmObjects.get(i).toString();
            timeText.setText(time);
            final int hour = Integer.valueOf(alarmObjects.get(i).getHour());
            final int minute = Integer.valueOf(alarmObjects.get(i).getMinutes());
            final String ampm = alarmObjects.get(i).getAmpm();
            final int key = i;

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent editIntent = new Intent(HomeActivity.this, MainActivity.class);
                    editIntent.putParcelableArrayListExtra(EXTRA_ALARM_OBJECT, alarmObjects);
                    editIntent.putExtra(EXTRA_BROADCAST_ID, broadcastId);
                    editIntent.putExtra(EXTRA_EDIT_HOUR, hour);
                    editIntent.putExtra(EXTRA_EDIT_MINUTE, minute);
                    editIntent.putExtra(EXTRA_EDIT_AMPM, ampm);
                    editIntent.putExtra(EXTRA_EDIT_KEY, key);


                    startActivity(editIntent);
                    //Toast.makeText(HomeActivity.this,    "is pressed", Toast.LENGTH_SHORT).show();
                }
            });
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            alarmList.addView(view);
        }
    }

    /*protected void onSaveInstanceState(Bundle state){
        Log.e("I AM SAVING","");
        super.onSaveInstanceState(state);
        state.putInt("broadcastId", broadcastId);
        state.putParcelableArrayList("alarmObjects", alarmObjects);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.e("I AM RESTORING","");
        super.onRestoreInstanceState(savedInstanceState);
        broadcastId = savedInstanceState.getInt("broadcastId");
        alarmObjects = savedInstanceState.getParcelableArrayList("alarmObjects");
    }*/

}
