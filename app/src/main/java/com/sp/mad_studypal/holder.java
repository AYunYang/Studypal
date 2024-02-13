package com.sp.mad_studypal;

import android.content.Context;
import android.content.SharedPreferences;

public class holder {

    private static final String APP_PREFS = "AppPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STUDYAREA = "studyArea";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIMESLOT = "timeSlot";
    private static final String KEY_ACTIVITY = "activity";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public holder(Context context) {
        preferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void saveLocation(String location) {
        editor.putString(KEY_LOCATION, location);
        editor.apply();
    }

    public void saveStudyArea(String studyArea) {
        editor.putString(KEY_STUDYAREA, studyArea);
        editor.apply();
    }

    public void saveDate(String date) {
        editor.putString(KEY_DATE, date);
        editor.apply();
    }

    public void saveTimeSlot(String timeSlot) {
        editor.putString(KEY_TIMESLOT, timeSlot);
        editor.apply();
    }

    public void saveActivity(String activity) {
        editor.putString(KEY_ACTIVITY, activity);
        editor.apply();
    }

    public String getKeyEmail() {
        return preferences.getString(KEY_EMAIL, "default_email");
    }

    public String getKeyLocation() {
        return preferences.getString(KEY_LOCATION, "");
    }

    public String getKeyStudyArea() {
        return preferences.getString(KEY_STUDYAREA, "");
    }

    public String getKeyDate() {
        return preferences.getString(KEY_DATE, "");
    }

    public String getKeyTimeSlot() {
        return preferences.getString(KEY_TIMESLOT, "");
    }

    public String getKeyActivity() {
        return preferences.getString(KEY_ACTIVITY, "");
    }
}