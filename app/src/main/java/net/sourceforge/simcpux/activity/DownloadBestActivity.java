package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.manager.DownloadManager;
import net.sourceforge.simcpux.service.DownloadService;

import java.util.ArrayList;

public class DownloadBestActivity extends AppCompatActivity {

    private ServiceConnection conn;
    private DownloadService.DownloadBinder downloadBinder;
    private static final String TAG = "DownloadBestActivity";
    private DownloadManager manager = DownloadManager.getInstance();
    private java.lang.String url = "http://m.bjcourt.gov.cn:8071/schedule/apk/schedule.apk";
    private DownloadService.DownloadLisenter downloadLisenter;
    private SeekBar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_best);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.register(url, downloadLisenter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (downloadLisenter != null) {
            manager.unRegister(url);
        }
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(1024);
        for (int i = 0; i < runningService.size(); i++) {
//            L.i(TAG, runningService.get(i).service.getClassName().toString());
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        initLisenter();
        sb = findViewById(R.id.sb);
        sb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        downloadLisenter = new DownloadService.DownloadLisenter() {
            @Override
            public void onSuccess() {
                Toast.makeText(DownloadBestActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
                L.i(TAG, "下载成功！");
            }

            @Override
            public void onFailed(String s) {
                L.i(TAG, "下载失败：" + s);
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(String fileName, int progress) {
                L.i(TAG, "下载: " + fileName + " " + progress + "%");
                sb.setProgress(progress);
            }
        };

        boolean serviceRunning = isServiceRunning(this, DownloadService.class.getName());
        L.i(TAG, "serviceRunning: " + serviceRunning);
        Intent intent = new Intent(this, DownloadService.class);
        if (!serviceRunning) {
            startService(intent);
        }
        conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadBinder = (DownloadService.DownloadBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
//            stopService(new Intent(this, DownloadService.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    finish();
                    Toast.makeText(this, "拒绝权限无法使用该功能", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initLisenter() {
        findViewById(R.id.btn_start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder != null) {
                    downloadBinder.startDownload(url);
                }
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DownloadBestActivity.class);
        context.startActivity(intent);
    }
}
