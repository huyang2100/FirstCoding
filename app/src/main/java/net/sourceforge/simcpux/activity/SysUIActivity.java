package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import net.sourceforge.simcpux.R;

public class SysUIActivity extends AppCompatActivity {

    private View mDecorView;
    private Handler mHandler = new Handler();
    private TextView mTvTitle;
    private SeekBar mSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_ui);

        initView();
        initLisenter();
    }

    private void initLisenter() {
        findViewById(R.id.rootview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int systemUiVisibility = mDecorView.getSystemUiVisibility();
                boolean isImmersiveMode = (systemUiVisibility & View.SYSTEM_UI_FLAG_IMMERSIVE) == View.SYSTEM_UI_FLAG_IMMERSIVE;
                if (isImmersiveMode) {
                    showSystemUI();
                } else {
                    hideSystemUI();
                }
            }
        });

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                boolean isFullScreenMode = (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (isFullScreenMode) {
                    mTvTitle.setVisibility(View.GONE);
                    mSb.setVisibility(View.GONE);
                } else {
                    mTvTitle.setVisibility(View.VISIBLE);
                    mSb.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            showSystemUI();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSystemUI();
                }
            }, 2000);
        }
    }

    private void initView() {
        mDecorView = getWindow().getDecorView();
        mTvTitle = findViewById(R.id.tv_title);
        mSb = findViewById(R.id.sb);
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (Build.VERSION.SDK_INT >= 19) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SysUIActivity.class);
        context.startActivity(intent);
    }
}
