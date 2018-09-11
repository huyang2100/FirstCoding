package net.sourceforge.simcpux.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import net.sourceforge.simcpux.app.FCApplication;


public class SPUtil {

    public static void putString(String key, String value) {
        getSP().edit().putString(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getSP().edit().putBoolean(key, value).commit();
    }

    public static void putInt(String key, int value) {
        getSP().edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public static String getString(String key) {
        return getSP().getString(key, "");
    }

    private static SharedPreferences getSP() {
        return PreferenceManager.getDefaultSharedPreferences(FCApplication.getContext());
    }
}
