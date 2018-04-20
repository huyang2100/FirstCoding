package net.sourceforge.simcpux.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.sourceforge.simcpux.activity.WeatherActivity;
import net.sourceforge.simcpux.bean.WeatherInfo;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.constant.ConstantURL;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.net.OkHttpMananger;

public class UpdateWeatherService extends Service {

    private static final String TAG = "UpdateWeatherService";
    private SharedPreferences preferences;
    private Gson gson = new Gson();

    public UpdateWeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i(TAG, "service start!!!");
        updateWeatherJson();
        updateBingUrl();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 60 * 60 * 1000;//1h
//        long triggerTime = SystemClock.elapsedRealtime() + 10 * 1000;//10s
        PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, UpdateWeatherService.class), 0);
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingUrl() {
        OkHttpMananger.get(ConstantURL.BASE_BING_PIC, new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, "bing_pic_url: " + str);
                preferences.edit().putString(ConstantSP.BING_PIC_URL, str).commit();
            }

            @Override
            public void onErr(Exception e) {

            }
        });
    }

    private void updateWeatherJson() {
        final String weatherid = preferences.getString(ConstantSP.WEATHER_ID, "");
        if (!TextUtils.isEmpty(weatherid)) {
            String url = Uri.parse(ConstantURL.BASE_WEATHER).buildUpon()
                    .appendQueryParameter("cityid", weatherid)
                    .appendQueryParameter("key", "8802d926b6274cd5a4ba0053d484846e")
                    .build()
                    .toString();
            L.i(TAG, url);

            OkHttpMananger.get(url, new OkHttpMananger.CallBack() {
                @Override
                public void onSuccess(String str) {
                    L.i(TAG, str);
                    L.i(TAG, str);
                    preferences.edit().putString(weatherid, str).commit();
                }

                @Override
                public void onErr(Exception e) {
                    L.i(TAG, e.toString());
                }
            });
        }
    }
}
