package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Chronometer;

import net.sourceforge.simcpux.R;

public class ChronometerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long t = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.setText(DateFormat.format("kk:mm:ss", t));
            }
        });
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTime == 0L) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                } else {
                    chronometer.setBase(chronometer.getBase() + (SystemClock.elapsedRealtime() - lastTime));
                }
                chronometer.start();
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                lastTime = SystemClock.elapsedRealtime();
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ChronometerActivity.class);
    }
}
