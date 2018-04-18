package net.sourceforge.simcpux.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.activity.DownloadBestActivity;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.manager.DownloadManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    private DownloadBinder mBinder = new DownloadBinder();
    private OkHttpClient client = new OkHttpClient();
    private DownloadThread downloadThread;
    private Handler mHandler = new Handler();
    private DownloadManager manager = DownloadManager.getInstance();
    private DownloadLisenter downladLisenter = new DownloadLisenter() {
        @Override
        public void onSuccess() {
            downloadThread = null;
            getNotificationMananger().notify(1, getNotification("download success!", -1));
            stopForeground(true);
            showMessage("download success");
        }

        @Override
        public void onFailed(String info) {
            downloadThread = null;
            getNotificationMananger().notify(1, getNotification("download failed:" + info, -1));
            stopForeground(true);
            showMessage("download failed：" + info);
            L.e(TAG, info);
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onProgress(String fileName, int progress) {
            getNotificationMananger().notify(1, getNotification("正在下载：" + fileName, progress));
        }
    };

    private void showMessage(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DownloadService.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url, DownloadLisenter lisenter) {
            if (downloadThread == null) {
                downloadThread = new DownloadThread(url);
                downloadThread.start();
            }
        }

        public void startDownload(String url) {
            if (!manager.isExist(url)) {
                manager.addDownload(url);
                downloadThread = new DownloadThread(url);
                downloadThread.start();
                showMessage("download...");
            }
        }

        public void pauseDownload() {

        }

        public void cancelDownload() {

        }
    }

    private Notification getNotification(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(DownloadService.this, "1");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, DownloadBestActivity.class), 0);
        builder.setSmallIcon(R.drawable.img);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            getNotificationMananger().createNotificationChannel(new NotificationChannel("1", "one", NotificationManager.IMPORTANCE_DEFAULT));
        }
        return builder.build();
    }

    private NotificationManager getNotificationMananger() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public interface DownloadLisenter {
        /**
         * 成功
         */
        void onSuccess();

        /**
         * 失败
         *
         * @param s 失败原因
         */
        void onFailed(String s);

        /**
         * 暂停
         */
        void onPause();

        /**
         * 取消
         */
        void onCancel();

        /**
         * 进度
         *
         * @param fileName
         * @param progress 下载进度,最大100
         */
        void onProgress(String fileName, int progress);
    }

    class DownloadThread extends Thread {
        private final String url;

        public DownloadThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            super.run();
            String fileName = Uri.parse(url).getLastPathSegment();
            L.i(TAG, "fileName: " + fileName);
            File dir = new File(Environment.getExternalStorageDirectory(), getPackageName() + "/download");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            L.i(TAG, "file path: " + file.getAbsolutePath());

            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    long contentLength = response.body().contentLength();
                    L.i(TAG, "downedLength: " + file.length());
                    L.i(TAG, "contentLength: " + contentLength);
                    if (contentLength == 0) {
//                        lisenter.onFailed("contentLength: 0");
                        manager.notifyFailed(url, "contentLength: 0");
                        return;
                    } else if (contentLength == file.length()) {
//                        lisenter.onSuccess();
                        manager.notifySuccess(url);
                        manager.delDownload(url);
                        return;
                    }

                    request = new Request.Builder()
                            .url(url)
                            .header("RANGE", "bytes=" + file.length() + "-").build();
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
                        int len = -1;
                        byte[] buffer = new byte[1024 * 1024 * 2];
                        long total = file.length();
                        while ((len = is.read(buffer)) != -1) {
                            raf.write(buffer, 0, len);
                            total += len;
                            int progress = (int) (total * 100 / contentLength);
//                            lisenter.onProgress(fileName, progress);
                            manager.notifyProgress(url, fileName, progress);
                        }
                        raf.close();
                        is.close();
//                        lisenter.onSuccess();
                        manager.notifySuccess(url);
                        manager.delDownload(url);
                        L.i(TAG, "download success!");
                    } else {
//                        lisenter.onFailed("code: " + response.code());
                        manager.notifyFailed(url, "code: " + response.code());
                        return;
                    }
                } else {
//                    lisenter.onFailed("code: " + response.code());
                    manager.notifyFailed(url, "code: " + response.code());
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
//                lisenter.onFailed(e.toString());
                manager.notifyFailed(url, e.toString());
                return;
            }
        }
    }
}
