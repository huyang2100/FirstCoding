package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;

import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class JZVideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jzvideo_player);

        initView();
    }

    private void initView() {
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子闭眼睛");
//        jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, JZVideoPlayerActivity.class);
        context.startActivity(intent);
    }
}
