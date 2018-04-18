package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.NewsContentFragment;

public class ColorStatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_status_bar);

        getSupportActionBar().hide();

//        View decorView = getWindow().getDecorView();
//        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        decorView.setSystemUiVisibility(option);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        if (savedInstanceState == null) {
            String title = getIntent().getStringExtra(NewsContentFragment.EXTRA_TITLE);
            String content = getIntent().getStringExtra(NewsContentFragment.EXTRA_CONTENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, NewsContentFragment.getInstance(title, content)).commit();
        }
    }

    public static void actionStart(Context context, String title, String content) {
        Intent intent = new Intent(context, ColorStatusBarActivity.class);
        intent.putExtra(NewsContentFragment.EXTRA_TITLE, title);
        intent.putExtra(NewsContentFragment.EXTRA_CONTENT, content);
        context.startActivity(intent);
    }
}
