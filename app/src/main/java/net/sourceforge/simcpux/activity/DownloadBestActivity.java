package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.asynctask.DownloadTask;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.service.DownloadService;

import java.util.ArrayList;

public class DownloadBestActivity extends AppCompatActivity {

    private ServiceConnection conn;
    private static final String TAG = "DownloadBestActivity";
    private java.lang.String url = "http://dldir1.qq.com/qqfile/QQforMac/QQ_V6.5.1.dmg";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_best);
        tv = findViewById(R.id.tv);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DownloadTask(new DownloadTask.DownloadLisenter() {
                    @Override
                    public void onProgress(int progress) {
                        tv.setText(progress + "%");
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DownloadBestActivity.this, "success!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(DownloadBestActivity.this, "faile!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }).execute(url);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults != null && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请先开启权限！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DownloadBestActivity.class);
        context.startActivity(intent);
    }
}
