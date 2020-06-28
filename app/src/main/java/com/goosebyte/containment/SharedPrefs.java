package com.goosebyte.containment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    public static final String SHARED_PREFS_NAME = "GBContainmentPrefs";
    public static final String SHARED_PREF_USERNAME = "UserName";
    public static final String SHARED_PREF_MAXYEARREACHED = "MaxYearReached";
    public static final String SHARED_PREF_PEAKPOPULATION = "PeakPopulation";
    public static final String SHARED_PREFS_GAMEATTEMPT = "GameAttempt";
    public static final String SHARED_PREFS_USERTUTORIALSHOWN = "UserTutorialShown";
    public static final String SHARED_PREFS_CONFIGCOMPLETED ="ConfigCompleted";
    public static final String SHARED_PREFS_SOUNDONOFF ="SoundEnabled";

    public static boolean sharedPrefsExist(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains(SHARED_PREFS_CONFIGCOMPLETED))
        {
            return true;
        }
        return false;

    }

    private static boolean createSharedPrefs(Context context, String userName) {
        boolean result = false;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(SHARED_PREF_USERNAME, userName);
            editor.putLong(SHARED_PREF_MAXYEARREACHED,0);
            editor.putLong(SHARED_PREF_PEAKPOPULATION,0);
            editor.putInt(SHARED_PREFS_GAMEATTEMPT,0);
            editor.putBoolean(SHARED_PREFS_USERTUTORIALSHOWN,false);
            editor.putBoolean(SHARED_PREFS_CONFIGCOMPLETED,true);
            editor.putBoolean(SHARED_PREFS_SOUNDONOFF,true);
            editor.apply();
            editor.commit();
            result  = true;

        } catch (Exception x) {
            Log.e("SHAREDPREFS",x.getMessage());
        }
        return result;
    }



}
