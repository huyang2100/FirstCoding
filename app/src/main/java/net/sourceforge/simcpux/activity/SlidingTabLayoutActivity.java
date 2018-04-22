package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.NewsContentFragment;

import java.util.ArrayList;

public class SlidingTabLayoutActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String[] items = {"Android", "Ios", "卡巴斯基", "WPhone8", "微软", "赛门铁克", "阿里巴巴"};
    private ArrayList<NewsContentFragment> contentFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab_layout);

        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < items.length; i++) {
            contentFragments.add(NewsContentFragment.getInstance(items[i], items[i]));
        }
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager(), contentFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    private void initView() {
        slidingTabLayout = findViewById(R.id.slidingtablayout);
        viewPager = findViewById(R.id.viewpager);
    }

    class ContentAdapter extends FragmentPagerAdapter {

        private final ArrayList<NewsContentFragment> contentFragments;

        public ContentAdapter(FragmentManager fm, ArrayList<NewsContentFragment> contentFragments) {
            super(fm);
            this.contentFragments = contentFragments;
        }

        @Override
        public int getCount() {
            return contentFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return contentFragments.get(position).getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return contentFragments.get(position);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SlidingTabLayoutActivity.class);
        context.startActivity(intent);
    }
}
