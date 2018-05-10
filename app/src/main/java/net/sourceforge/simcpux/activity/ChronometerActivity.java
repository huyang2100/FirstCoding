package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
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
import net.sourceforge.simcpux.log.L;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private ArrayList<String> permissionList = new ArrayList<>();
    private ArrayList<File> fileList = new ArrayList<>();

    private static final String TAG = "ChronometerActivity";
    private File recordFile;
    private File dir;
    private boolean isStopRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 0);
            return;
        }

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0) {
                    for (int permission : grantResults) {
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        init();
                    }
                } else {
                    finish();
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (File f : fileList) {
            if (f.exists()) {
                f.delete();
            }
        }
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void init() {
        dir = new File(Environment.getExternalStorageDirectory(), "/yidongzhushou/record");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer ch) {
                updateTime = getRecordTime(SystemClock.elapsedRealtime() - chronometer.getBase());
                if (!TextUtils.isEmpty(recordTime) && recordTime.equals(updateTime)) {
                    initPlayBtn();
                    stopChronometer();
                }
                chronometer.setText(updateTime);
            }
        });
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chageStatus(STATUS_START);
                startChronometer();
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
                pauseChronometer();
            }
        });
        btn_over = findViewById(R.id.btn_over);
        btn_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTime = updateTime;
                if (recordTime == null || recordTime.equals("00:00:00")) {
                    return;
                }
                chageStatus(STATUS_OVER);
                stopChronometer();
            }
        });
        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_play.getText().equals("播放")) {
                    btn_play.setText("暂停");
                    startChronometer();
                    startPlaying();
                } else {
                    initPlayBtn();
                    pauseChronometer();
                    mPlayer.pause();
                }
            }
        });

        chageStatus(STATUS_INIT);
    }

    private void stopChronometer() {
        chronometer.stop();
        lastTime = 0L;
    }

    private void pauseChronometer() {
        chronometer.stop();
        lastTime = SystemClock.elapsedRealtime();
    }

    private void startChronometer() {
        if (lastTime == 0L) {
            initChronometer();
        } else {
            chronometer.setBase(chronometer.getBase() + (SystemClock.elapsedRealtime() - lastTime));
        }
        chronometer.start();
    }


    private void startPlaying() {
        if (!recordFile.exists()) {
            Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(recordFile.getAbsolutePath());
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Toast.makeText(this, "播放失败：" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        mPlayer.start();
    }

    private void startRecord() {
        isStopRecord = false;
        File file = new File(dir, System.currentTimeMillis() + ".amr");
        fileList.add(file);
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(file.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "录音失败：" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        mRecorder.start();
    }

    private void stopRecord() {
        if (!isStopRecord) {
            isStopRecord = true;
            mRecorder.stop();
        }
    }

    private void initPlayBtn() {
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
                startRecord();
                btn_start.setEnabled(false);
                btn_stop.setEnabled(true);
                btn_play.setEnabled(false);
                btn_over.setEnabled(true);
                break;
            case STATUS_STOP:
                stopRecord();
                btn_start.setEnabled(true);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(false);
                btn_over.setEnabled(true);
                break;
            case STATUS_OVER:
                stopRecord();
                mergeRecord();
                btn_start.setEnabled(false);
                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);
                btn_over.setEnabled(false);
                break;
        }
    }

    private void mergeRecord() {
        if (fileList.size() == 1) {
            recordFile = fileList.get(0);
        } else {
            try {
                recordFile = new File(dir, "audio_record.amr");
                if (recordFile.exists()) {
                    recordFile.delete();
                }
                RandomAccessFile raf = new RandomAccessFile(recordFile, "rw");
                byte[] buffer = new byte[1024];
                int len = -1;
                for (int i = 0; i < fileList.size(); i++) {
                    RandomAccessFile temp = new RandomAccessFile(fileList.get(i), "rw");
                    if (i > 0) {
                        temp.seek(6);
                    }
                    while ((len = temp.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                    }
                    temp.close();
                }
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ChronometerActivity.class);
    }
}
