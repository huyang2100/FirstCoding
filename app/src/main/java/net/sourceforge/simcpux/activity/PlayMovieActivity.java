package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.view.FullScreenVideoView;

import java.io.InputStream;

public class PlayMovieActivity extends AppCompatActivity {

    private FullScreenVideoView videoView;
    private ViewGroup.LayoutParams videoViewLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setTheme(android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);

        setContentView(R.layout.activity_play_movie);
        getSupportActionBar().hide();

        initView();
        initData();
        initLisenter();
    }

    private void initData() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.abc);
        videoView.setVideoURI(uri);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(layoutParams);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            videoView.setLayoutParams(videoViewLayoutParams);
        }
    }

    private void initLisenter() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.suspend();
    }

    private void initView() {
        videoView = findViewById(R.id.videoview);
        videoViewLayoutParams = videoView.getLayoutParams();
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.start();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlayMovieActivity.class);
        context.startActivity(intent);
    }
}
