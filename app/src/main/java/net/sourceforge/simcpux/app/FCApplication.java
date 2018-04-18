package net.sourceforge.simcpux.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by yanghu on 2018/4/10.
 */

public class FCApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
