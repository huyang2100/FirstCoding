package net.sourceforge.simcpux.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.manager.PrefManager;
import net.sourceforge.simcpux.utils.SPUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SplashActivity extends AppCompatActivity implements Runnable {
    private Handler handler = new Handler();
    private PrefManager prefManager;
    private static final String TAG = "SplashActivity";


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
            tv_version.setText("版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ImageView iv = findViewById(R.id.iv);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        boolean isCache = dayOfMonth == SPUtil.getInt(SPUtil.KEY_DAY_OF_MONTH);
        SPUtil.putInt(SPUtil.KEY_DAY_OF_MONTH, dayOfMonth);
        L.i(TAG,"isCache: "+isCache);
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load("https://open.saintic.com/api/bingPic/").apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                handler.postDelayed(SplashActivity.this, 1000);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).into(iv);
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
