package com.sp.mad_studypal;

import android.content.Context;
import android.content.SharedPreferences;

public class holder {

    private static final String APP_PREFS = "AppPreferences";
    private static final String KEY_EMAIL = " ";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public holder(Context context) {
        preferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveVariable(String variable) {
        editor.putString(KEY_EMAIL, variable);
        editor.apply();
    }

    public String getVariable() {
        return preferences.getString(KEY_EMAIL, "default_email");
    }
}