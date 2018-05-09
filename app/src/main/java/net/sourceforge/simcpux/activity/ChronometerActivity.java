package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import net.sourceforge.simcpux.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChronometerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long lastTime;
    private Button btn_stop;
    private Button btn_start;
    private Button btn_play;
    //未开始
    private static final int STATUS_INIT = 0;
    //开始
    private static final int STATUS_START = 1;
    //录制暂停
    private static final int STATUS_STOP = 2;
    //录制结束
    private static final int STATUS_OVER = 3;
    private Button btn_over;
    private String recordTime;
    private String updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            return;
        }

        init();
    }

    private void init() {
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateTime = getRecordTime(SystemClock.elapsedRealtime() - chronometer.getBase());
                chronometer.setText(updateTime);
                if (!TextUtils.isEmpty(recordTime) && recordTime.equals(updateTime)) {
                    initPlayBtn();
                }
            }
        });
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageStatus(STATUS_START);
                if (lastTime == 0L) {
                    initChronometer();
                } else {
                    chronometer.setBase(chronometer.getBase() + (SystemClock.elapsedRealtime() - lastTime));
                }
                chronometer.start();
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChronometer();
            }
        });
        btn_stop = findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageStatus(STATUS_STOP);
                chronometer.stop();
                lastTime = SystemClock.elapsedRealtime();
            }
        });
        btn_over = findViewById(R.id.btn_over);
        btn_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageStatus(STATUS_OVER);
                chronometer.stop();
                recordTime = updateTime;
            }
        });
        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_play.getText().equals("播放")) {
                    btn_play.setText("暂停");
                    initChronometer();
                    chronometer.start();
                } else {
                    initPlayBtn();
                }
            }
        });

        chageStatus(STATUS_INIT);
    }

    private void initPlayBtn() {
        chronometer.stop();
        btn_play.setText("播放");
    }

    private String getRecordTime(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sdf.format(new Date(t));
    }

    private void initChronometer() {
        lastTime = 0L;
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void chageStatus(int status) {
        switch (status) {
            case STATUS_INIT:
                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(false);
                btn_over.setEnabled(false);
                break;
            case STATUS_START:
                btn_start.setEnabled(false);
                btn_stop.setEnabled(true);
                btn_play.setEnabled(false);
                btn_over.setEnabled(true);
                break;
            case STATUS_STOP:
                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(false);
                btn_over.setEnabled(true);
                break;
            case STATUS_OVER:
                btn_start.setEnabled(false);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);
                btn_over.setEnabled(false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    finish();
                }
                break;
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ChronometerActivity.class);
    }
}
