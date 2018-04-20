package net.sourceforge.simcpux.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.WelcomeAdapter;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.manager.PrefManager;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PrefManager prefManager;
    private LinearLayout ll_dots;
    private static final String TAG = "WelcomeActivity";
    private int lastPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            enterHomeActivity();
        }

        setContentView(R.layout.activity_welcome);
        initView();
        initData();
    }

    private void enterHomeActivity() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    private void initData() {
        int[] layouts = {R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4};
        WelcomeAdapter welcomeAdapter = new WelcomeAdapter(layouts);
        viewPager.setAdapter(welcomeAdapter);

        ll_dots.removeAllViews();
        TextView[] dots = new TextView[layouts.length];
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setPadding(5, 0, 5, 0);
            if (i == 0) {
                dots[i].setTextColor(Color.GRAY);
            } else {
                dots[i].setTextColor(Color.WHITE);
            }
            ll_dots.addView(dots[i]);
        }


    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TextView tv_position_last = (TextView) ll_dots.getChildAt(lastPostion);
                tv_position_last.setTextColor((int) argbEvaluator.evaluate(positionOffset, Color.WHITE, Color.BLACK));

                TextView tv_position = (TextView) ll_dots.getChildAt(position);
                tv_position.setTextColor((int) argbEvaluator.evaluate(positionOffset, Color.BLACK, Color.WHITE));

                L.i(TAG, "position: " + position + "     offSet: " + positionOffset + "      lastPostion: " + lastPostion);
            }

            @Override
            public void onPageSelected(int position) {
                lastPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ll_dots = findViewById(R.id.ll_dots);

        findViewById(R.id.btn_enter_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(true);
                enterHomeActivity();
            }
        });
    }
}
