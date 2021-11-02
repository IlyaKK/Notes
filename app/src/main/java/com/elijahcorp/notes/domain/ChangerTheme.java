package com.elijahcorp.notes.domain;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ChangerTheme {
    private final static String PREFS_NAME = "theme_prefs";
    private final static String KEY_THEME = "prefs.theme";

    public final static int THEME_DARK = 0;
    public final static int THEME_LIGHT = 1;

    private static SharedPreferences sharedPreferences;

    public static boolean initialiseTheme(Context cnt) {
        sharedPreferences = cnt.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        switch (getSavedTheme()) {
            case THEME_DARK:
                setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK);
                return true;
            case THEME_LIGHT:
                setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT);
                return false;
            default:
                return true;
        }
    }

    public static void setTheme(int themeMode, int prefsMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);
        saveTheme(prefsMode);
    }

    private static void saveTheme(int theme) {
        sharedPreferences.edit().putInt(KEY_THEME, theme).apply();
    }

    private static int getSavedTheme() {
        return sharedPreferences.getInt(KEY_THEME, THEME_LIGHT);
    }
}
