package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.NewsContentFragment;
import net.sourceforge.simcpux.fragment.NewsTitleListFragment;

import java.util.ArrayList;
import java.util.Random;

public class FragmentBestPracticeActivity extends AppCompatActivity implements NewsTitleListFragment.OnNewsTitleClickListener {

    private Random random = new Random();
    private StringBuilder sb = new StringBuilder();
    private NewsContentFragment fragment;
    private ArrayList<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_best_practice);

        initData();

        if (savedInstanceState == null) {
            NewsTitleListFragment titleListFragment = NewsTitleListFragment.getInstance(titleList);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_news_title, titleListFragment).commit();
        }


        if (isTableMode()) {
            onNewsTitleClick(titleList.get(0));
        }
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            titleList.add("title: " + i);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FragmentBestPracticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onNewsTitleClick(String title) {
        sb.setLength(0);
        for (int i = 0; i < random.nextInt(50) + 200; i++) {
            sb.append(title);
        }


        if (isTableMode()) {
            if (fragment == null) {
                fragment = NewsContentFragment.getInstance(title, sb.toString());
            } else {
                fragment.refresh(title, sb.toString());
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, fragment).commit();
        } else {
            ColorStatusBarActivity.actionStart(this, title, sb.toString());
        }
    }

    private boolean isTableMode() {
        return findViewById(R.id.frag_content) != null;
    }
}
