package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Fruit;

public class FruitDetialActivity extends AppCompatActivity {
    private static String EXTRA_FRUIT = "fruit";
    private TextView tv_content;
    private StringBuilder sb = new StringBuilder();
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_detial);

        initView();
        initData();
    }

    private void initData() {
        Fruit fruit = (Fruit) getIntent().getSerializableExtra(EXTRA_FRUIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(fruit.getName());
        }

        iv.setImageResource(fruit.getResId());
        for (int i = 0; i < 500; i++) {
            sb.append(fruit.getName());
        }
        tv_content.setText(sb.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        iv = findViewById(R.id.iv);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "That's done!", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public static void actionStart(Context context, Fruit fruit) {
        Intent intent = new Intent(context, FruitDetialActivity.class);
        intent.putExtra(EXTRA_FRUIT, fruit);
        context.startActivity(intent);
    }
}
