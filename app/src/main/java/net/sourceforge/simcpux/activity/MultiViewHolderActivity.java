package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.adapter.MultiFruitAdapter;
import net.sourceforge.simcpux.bean.Fruit;

import java.util.ArrayList;

public class MultiViewHolderActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayList<Fruit> fruitList = new ArrayList<>();
    private TextView tv_title;
    private MultiFruitAdapter multiFruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view_holder);

        initView();
        initData();
        bindData();
    }

    private void bindData() {
        multiFruitAdapter = new MultiFruitAdapter(fruitList);
        lv.setAdapter(multiFruitAdapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("苹果");
            if (i == 0) {
                fruit.setName("苹果");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("苹果：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("橘子");
            if (i == 0) {
                fruit.setName("橘子");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("橘子：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("梨");
            if (i == 0) {
                fruit.setName("梨");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("梨：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("香蕉");
            if (i == 0) {
                fruit.setName("香蕉");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("香蕉：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("葡萄");
            if (i == 0) {
                fruit.setName("葡萄");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("葡萄：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }

        for (int i = 0; i < 5; i++) {
            Fruit fruit = new Fruit();
            fruit.setCategory("石榴");
            if (i == 0) {
                fruit.setName("石榴");
                fruit.setType(Fruit.ITEM_TYPE_TITLE);
            } else {
                fruit.setName("石榴：" + i);
                fruit.setType(Fruit.ITEM_TYPE_CONTENT);
            }
            fruitList.add(fruit);
        }
    }

    private void initView() {
        lv = findViewById(R.id.lv);
        tv_title = findViewById(R.id.tv_title);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (multiFruitAdapter != null) {
                    Fruit fruit = multiFruitAdapter.getItem(firstVisibleItem);
                    if (fruit != null) {
                        tv_title.setText(fruit.getCategory());
                    }
                }
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MultiViewHolderActivity.class);
        context.startActivity(intent);
    }
}
