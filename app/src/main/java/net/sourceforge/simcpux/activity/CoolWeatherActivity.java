package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.ChooseAreaAdapter;
import net.sourceforge.simcpux.bean.AreaItem;
import net.sourceforge.simcpux.fragment.ChooseAreaFragment;
import net.sourceforge.simcpux.log.L;

public class CoolWeatherActivity extends AppCompatActivity implements ChooseAreaFragment.OnItemClickLisenter {

    private static final String TAG = "CoolWeatherActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_weather);

        setActionBar(false, "中国");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_root, ChooseAreaFragment.newInstance(null)).commit();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                    setActionBar(false, "中国");
                }else{
                    ChooseAreaFragment chooseAreaFragment = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.fl_root);
                    Bundle args = chooseAreaFragment.getArguments();
                    if(args!=null){
                        AreaItem item = (AreaItem) args.getSerializable(ChooseAreaFragment.KEY_ITEMBEAN);
                        if (item != null) {
                            setActionBar(true,item.getName());
                        }
                    }
                }
            }
        });
    }

    private void setActionBar(boolean showBack, String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showBack);
            actionBar.setTitle(title);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CoolWeatherActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AreaItem areaItem) {
        switch (areaItem.getCurLevel()) {
            case AreaItem.LEVEL_PROVINCE:
            case AreaItem.LEVEL_CITY:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_root, ChooseAreaFragment.newInstance(areaItem)).addToBackStack(null).commit();
                break;
            case AreaItem.LEVEL_COUNTRY:
                //TODO 展示天气信息

                break;
        }
    }
}
