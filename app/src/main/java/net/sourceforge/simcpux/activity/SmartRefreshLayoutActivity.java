package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.ChooseAreaAdapter;
import net.sourceforge.simcpux.bean.AreaItem;

import java.util.ArrayList;
import java.util.Random;

import ezy.ui.layout.LoadingLayout;

public class SmartRefreshLayoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AreaItem> areaList = new ArrayList<>();
    private String[] countries = {"中国", "美国", "英国", "德国", "日本", "菲律宾", "印度", "法国", "加拿大", "冰岛"};
    private Random random = new Random();
    private Handler handler = new Handler();
    private ChooseAreaAdapter adapter;
    private SmartRefreshLayout smartRefreshLayout;
    private LoadingLayout loadingLayout;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twinking_refresh);

        initView();
        initData();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingLayout.showContent();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.setEnableLoadMore(true);
                        areaList.clear();
                        for (int i = 0; i < 20; i++) {
                            AreaItem item = new AreaItem();
                            item.setName(countries[random.nextInt(countries.length)]);
                            areaList.add(item);
                        }
                        if (adapter == null) {
                            adapter = new ChooseAreaAdapter(areaList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickLisenter(new ChooseAreaAdapter.OnItemClickLisenter() {
                                @Override
                                public void itemClick(AreaItem areaItem) {
                                    tv_name.setText("已选择："+areaItem.getName());
//                                    Toast.makeText(SmartRefreshLayoutActivity.this, areaItem.getName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 2000);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.finishLoadMore();
                        for (int i = 0; i < 20; i++) {
                            AreaItem item = new AreaItem();
                            item.setName(countries[random.nextInt(countries.length)]);
                            areaList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prue_scroll_mode_open:
                smartRefreshLayout.setEnablePureScrollMode(true);
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.prue_scroll_mode_close:
                smartRefreshLayout.setEnablePureScrollMode(false);
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.smart_refresh_style, menu);
        return true;
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        loadingLayout = findViewById(R.id.loadinglayout);
        loadingLayout.showEmpty();
        smartRefreshLayout = findViewById(R.id.smartrefresh);
        smartRefreshLayout.setEnableLoadMore(false);
        recyclerView = findViewById(R.id.recyclerview);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmartRefreshLayoutActivity.class);
        context.startActivity(intent);
    }
}
