package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.AreaItem;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.fragment.ChooseAreaFragment;

public class CoolWeatherActivity extends AppCompatActivity implements ChooseAreaFragment.OnAreaLisenter {

    private static final String TAG = "CoolWeatherActivity";
    private TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_weather);

        String weatherid = PreferenceManager.getDefaultSharedPreferences(this).getString(ConstantSP.WEATHER_ID, "");
        if (!TextUtils.isEmpty(weatherid)) {
            WeatherActivity.actionStart(this, weatherid);
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_root, ChooseAreaFragment.newInstance(null)).commit();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CoolWeatherActivity.class);
        context.startActivity(intent);
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
                WeatherActivity.actionStart(this, areaItem.getWeather_id());
                finish();
                break;
        }
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
