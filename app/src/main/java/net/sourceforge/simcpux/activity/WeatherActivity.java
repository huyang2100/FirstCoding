package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.AreaItem;
import net.sourceforge.simcpux.bean.WeatherInfo;
import net.sourceforge.simcpux.constant.ConstantSP;
import net.sourceforge.simcpux.constant.ConstantURL;
import net.sourceforge.simcpux.fragment.ChooseAreaFragment;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.net.OkHttpMananger;

public class WeatherActivity extends AppCompatActivity implements ChooseAreaFragment.OnAreaLisenter, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "WeatherActivity";
    private static final String KEY_WEATHER_ID = "weatherid";
    private Gson gson = new Gson();
    private TextView tv_city_title;
    private TextView tv_update_time;
    private TextView tv_now_degree;
    private TextView tv_now_info;
    private LinearLayout forecast_layout;
    private TextView tv_aqi;
    private TextView tv_pm25;
    private TextView tv_suggestion_sport;
    private TextView tv_suggestion_car;
    private TextView tv_suggestion_comfor;
    private SharedPreferences preferences;
    private String weatherid;
    private ImageView iv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
        initData();
    }

    private void initData() {
        weatherid = getIntent().getStringExtra(KEY_WEATHER_ID);
        preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        preferences.edit().putString(ConstantSP.WEATHER_ID, weatherid).commit();
        String weather_json = preferences.getString(weatherid, "");
        if (TextUtils.isEmpty(weather_json)) {
            getWeatherFromServer();
        } else {
            try {
                handleJson(weather_json);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(WeatherActivity.this, "json解析异常：" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        String bing_pic_url = preferences.getString(ConstantSP.BING_PIC_URL, "");
        if (TextUtils.isEmpty(bing_pic_url)) {
            loadBingPic();
        } else {
            Glide.with(this).load(bing_pic_url).into(iv);
        }

    }

    private void getWeatherFromServer() {
        swipeRefreshLayout.setRefreshing(true);
        String url = Uri.parse(ConstantURL.BASE_WEATHER).buildUpon()
                .appendQueryParameter("cityid", weatherid)
                .appendQueryParameter("key", "8802d926b6274cd5a4ba0053d484846e")
                .build()
                .toString();
        L.i(TAG, url);
        OkHttpMananger.get(url, new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, str);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    handleJson(str);
                    preferences.edit().putString(WeatherActivity.this.weatherid, str).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WeatherActivity.this, "json解析异常：" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErr(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WeatherActivity.this, "获取天气信息失败: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Drawable drawable = iv.getDrawable();
        L.i(TAG, "iv server drawable: " + drawable);
        loadBingPic();
    }

    private void loadBingPic() {
        OkHttpMananger.get(ConstantURL.BASE_BING_PIC, new OkHttpMananger.CallBack() {
            @Override
            public void onSuccess(String str) {
                L.i(TAG, "bing_pic_url: " + str);
                preferences.edit().putString(ConstantSP.BING_PIC_URL, str).commit();
                Glide.with(WeatherActivity.this).load(str).into(iv);
            }

            @Override
            public void onErr(Exception e) {

            }
        });
    }

    private void handleJson(String str) throws Exception {
        WeatherInfo weatherInfo = gson.fromJson(str, WeatherInfo.class);
        WeatherInfo.HeWeather heWeather = weatherInfo.HeWeather.get(0);
        if (heWeather != null && heWeather.status.equals(ConstantURL.STATUS_OK)) {
            findViewById(R.id.root_weather).setVisibility(View.VISIBLE);
            tv_city_title.setText(heWeather.basic.city);
            tv_update_time.setText(heWeather.basic.update.loc);
            tv_now_degree.setText(heWeather.now.tmp.concat("°C"));
            tv_now_info.setText(heWeather.now.cond.txt);

            forecast_layout.removeAllViews();
            for (WeatherInfo.HeWeather.ForeCast foreCast : heWeather.daily_forecast) {
                View forecast_item = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_forecast_item, forecast_layout, false);
                TextView tv_date = forecast_item.findViewById(R.id.tv_date);
                tv_date.setText(foreCast.date);
                TextView tv_info = forecast_item.findViewById(R.id.tv_info);
                tv_info.setText(foreCast.cond.txt_d);
                TextView tv_max = forecast_item.findViewById(R.id.tv_max);
                tv_max.setText(foreCast.tmp.max);
                TextView tv_min = forecast_item.findViewById(R.id.tv_min);
                tv_min.setText(foreCast.tmp.min);
                forecast_layout.addView(forecast_item);
            }

            tv_aqi.setText(heWeather.aqi.city.aqi);
            tv_pm25.setText(heWeather.aqi.city.pm25);

            tv_suggestion_comfor.setText("舒适指数：" + heWeather.suggestion.comf.txt);
            tv_suggestion_sport.setText("运动指数：" + heWeather.suggestion.sport.txt);
            tv_suggestion_car.setText("洗车指数：" + heWeather.suggestion.cw.txt);
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, ChooseAreaFragment.newInstance(null)).commit();
        iv = findViewById(R.id.iv);
        findViewById(R.id.iv_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        drawerLayout = findViewById(R.id.drawerlayout);
        L.i(TAG, "iv init drawable: " + iv.getDrawable());
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        tv_city_title = findViewById(R.id.tv_city_title);
        tv_update_time = findViewById(R.id.tv_update_time);
        tv_now_degree = findViewById(R.id.tv_now_degree);
        tv_now_info = findViewById(R.id.tv_now_info);
        forecast_layout = findViewById(R.id.forecast_layout);
        tv_aqi = findViewById(R.id.tv_aqi);
        tv_pm25 = findViewById(R.id.tv_pm25);
        tv_suggestion_comfor = findViewById(R.id.tv_suggestion_comfor);
        tv_suggestion_sport = findViewById(R.id.tv_suggestion_sport);
        tv_suggestion_car = findViewById(R.id.tv_suggestion_car);
    }

    public static void actionStart(Context context, String weatherid) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(KEY_WEATHER_ID, weatherid);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AreaItem areaItem) {
        if (AreaItem.LEVEL_COUNTRY == areaItem.getCurLevel()) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            weatherid = areaItem.getWeather_id();
            preferences.edit().putString(ConstantSP.WEATHER_ID, weatherid).commit();
            getWeatherFromServer();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, ChooseAreaFragment.newInstance(areaItem)).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackClick() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getWeatherFromServer();
    }
}
