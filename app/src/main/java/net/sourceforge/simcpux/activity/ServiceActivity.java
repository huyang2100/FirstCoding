package net.sourceforge.simcpux.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.service.TestDownloadService;
import net.sourceforge.simcpux.service.MyIntentService;

public class ServiceActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection;
    private TestDownloadService.DownloadBinder downloadBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        initLisenter();
        initData();
    }

    private void initData() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadBinder = (TestDownloadService.DownloadBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    private void initLisenter() {
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TestDownloadService.class);
                startService(intent);
            }
        });

        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TestDownloadService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder != null) {
                    downloadBinder.startDownload();
                }else{
                    Toast.makeText(ServiceActivity.this, "请先绑定服务", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_get_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder != null) {
                    long progress = downloadBinder.getProgress();
                    Toast.makeText(ServiceActivity.this, "" + progress, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ServiceActivity.this, "请先绑定服务", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
                downloadBinder = null;
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TestDownloadService.class);
                stopService(intent);
            }
        });

        findViewById(R.id.btn_intent_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyIntentService.class);
                startService(intent);
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ServiceActivity.class);
        context.startActivity(intent);
    }
}
