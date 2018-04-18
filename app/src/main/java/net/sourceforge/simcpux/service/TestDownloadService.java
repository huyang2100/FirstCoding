package net.sourceforge.simcpux.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.activity.MainActivity;
import net.sourceforge.simcpux.activity.RecyclerActivity;
import net.sourceforge.simcpux.activity.ServiceActivity;
import net.sourceforge.simcpux.log.L;

public class TestDownloadService extends Service {
    private static final String TAG = "TestDownloadService";
    private DownloadBinder downloadBinder = new DownloadBinder();

    public TestDownloadService() {
    }

    public class DownloadBinder extends Binder {
        public void startDownload() {
            L.i(TAG, "startDownload execute!");
        }

        public long getProgress() {
            return System.currentTimeMillis();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i(TAG, "onCreate");

        Intent intent = new Intent(this, ServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "1")
                .setContentTitle("First Coding")
                .setContentText("Yang's MacBook Air 13")
                .setSmallIcon(android.R.drawable.star_on)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(new NotificationChannel("1", "channel", NotificationManager.IMPORTANCE_HIGH));
        }
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        L.i(TAG, "onBind");
        return downloadBinder;
    }
}
