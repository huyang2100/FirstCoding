package net.sourceforge.simcpux.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;

public class SplashActivity extends AppCompatActivity implements Runnable {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(this,2000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }
}
