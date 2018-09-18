package net.sourceforge.simcpux.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.manager.PrefManager;

public class SplashActivity extends AppCompatActivity implements Runnable {
    private Handler handler = new Handler();
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        TextView tv_version = findViewById(R.id.tv_version);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tv_version.setText("版本："+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iv = findViewById(R.id.iv);
        Glide.with(this).load("https://open.saintic.com/api/bingPic/").transition(DrawableTransitionOptions.withCrossFade()).into(iv);
        handler.postDelayed(this, 2000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }
}
