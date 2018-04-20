package net.sourceforge.simcpux.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yanghu on 2018/4/20.
 */

public class PrefManager {
    private static final String PREF_NAME = "firstcoding";
    private static final String KEY_IS_FIRST_LAUNCH = "is_first_launch";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor edit;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirst) {
        edit.putBoolean(KEY_IS_FIRST_LAUNCH, isFirst).commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_IS_FIRST_LAUNCH, true);
    }
}
