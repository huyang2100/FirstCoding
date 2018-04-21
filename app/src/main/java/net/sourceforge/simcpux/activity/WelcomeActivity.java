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
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.WelcomeAdapter;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.manager.PrefManager;

import ezy.ui.view.RoundButton;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PrefManager prefManager;
    private LinearLayout ll_dots;
    private static final String TAG = "WelcomeActivity";
    private View btn_enter_app;
    private TextView tv_desc;
    private int[] descs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);

        setContentView(R.layout.activity_welcome);
        initView();
        initData();
    }

    private void enterHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
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


        descs = new int[]{R.string.slide_1_title, R.string.slide_2_title, R.string.slide_3_title, R.string.slide_4_title};
        tv_desc.setText(descs[0]);
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
            private int lastPostion;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset != 0.0f) {
                    if (lastPostion == position) {
                        tv_desc.setTextColor((int) argbEvaluator.evaluate(1 - positionOffset, Color.TRANSPARENT, Color.BLACK));
                    } else {
                        tv_desc.setTextColor((int) argbEvaluator.evaluate(positionOffset, Color.TRANSPARENT, Color.BLACK));
                    }

                    //first visible position
                    TextView tv_position = (TextView) ll_dots.getChildAt(position);
                    tv_position.setTextColor((int) argbEvaluator.evaluate(1 - positionOffset, Color.WHITE, Color.BLACK));

                    //second visible position
                    int secondPostion = Math.min(position + 1, ll_dots.getChildCount());
                    TextView tv_position_last = (TextView) ll_dots.getChildAt(secondPostion);
                    int evaluate = (int) argbEvaluator.evaluate(positionOffset, Color.WHITE, Color.BLACK);
                    tv_position_last.setTextColor(evaluate);

                    if (lastPostion == ll_dots.getChildCount() - 1) {
                        btn_enter_app.setAlpha(positionOffset);
                    }
                    L.i(TAG, "position: " + position + "     offSet: " + positionOffset + "      lastPostion: " + lastPostion + "   positionOffsetPixels:" + positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position != ll_dots.getChildCount() - 1) {
                    btn_enter_app.setVisibility(View.GONE);
                } else {
                    btn_enter_app.setVisibility(View.VISIBLE);
                }
                tv_desc.setText(descs[position]);
                tv_desc.setAlpha(0f);
                tv_desc.animate().alpha(1f).setInterpolator(new AccelerateInterpolator()).setDuration(1000).start();

                lastPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ll_dots = findViewById(R.id.ll_dots);
        tv_desc = findViewById(R.id.tv_desc);

        btn_enter_app = findViewById(R.id.btn_enter_app);
        btn_enter_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(false);
                enterHomeActivity();
            }
        });
    }
}
