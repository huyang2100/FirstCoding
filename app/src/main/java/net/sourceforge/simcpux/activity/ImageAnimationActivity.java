package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.sourceforge.simcpux.fragment.ImageFragment;
import net.sourceforge.simcpux.R;

public class ImageAnimationActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_animation);

        initView();
        initLisenter();
    }

    private void initLisenter() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFragment.getInstance().show(getSupportFragmentManager(),null);
            }
        });
    }

    private void initView() {
        iv = findViewById(R.id.iv);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ImageAnimationActivity.class);
        context.startActivity(intent);
    }
}
