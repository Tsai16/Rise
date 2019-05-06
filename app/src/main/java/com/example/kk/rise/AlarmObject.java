package com.example.kk.rise;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KK on 4/2/2019.
 */

public class AlarmObject implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlarmObject createFromParcel(Parcel in) {
            return new AlarmObject(in);
        }

        public AlarmObject[] newArray(int size) {
            return new AlarmObject[size];
        }
    };

    private String ampm;
    private String hour;
    private String minutes;
    private PendingIntent pendingIntent;


    public AlarmObject(String hour, String minutes, String ampm) {
        this.minutes = minutes;
        this.hour = hour;
        this.ampm = ampm;
    }

    public AlarmObject(Parcel in) {
        this.hour = in.readString();
        this.minutes = in.readString();
        this.ampm = in.readString();
        this.pendingIntent = in.readParcelable(PendingIntent.class.getClassLoader());
    }

    public String getHour() {
        return hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setHour(String s) {
        hour = s;
    }

    public void setMinutes(String s) {
        minutes = s;
    }

    public void setAmpm(String s){
        ampm = s;
    }

    public void setPendingIntent(PendingIntent pendingIntent){
        this.pendingIntent = pendingIntent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.hour);
        parcel.writeString(this.minutes);
        parcel.writeString(this.ampm);
        parcel.writeParcelable(this.pendingIntent, i);
    }

    public String toString(){
        return hour+":"+minutes + " " + ampm;
    }
}
