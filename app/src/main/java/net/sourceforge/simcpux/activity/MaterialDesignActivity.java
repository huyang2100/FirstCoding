package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Fruit;

import java.util.ArrayList;
import java.util.Random;

public class MaterialDesignActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private Fruit[] fruits = new Fruit[]{
            new Fruit("JantarJaipur", R.drawable.jantarjaipur),
            new Fruit("KingPhoto", R.drawable.kingphoto),
            new Fruit("LagazuoiRefuge", R.drawable.lagazuoirefuge),
            new Fruit("LosMonegros", R.drawable.losmonegros),
            new Fruit("Mellieha", R.drawable.mellieha),
            new Fruit("Nyala", R.drawable.nyala),
            new Fruit("RainbowLorikeets", R.drawable.rainbowlorikeets),
            new Fruit("TempleStreet", R.drawable.templestreet),
            new Fruit("WaSqPk", R.drawable.wasqpk),
            new Fruit("YellowNPFirehole", R.drawable.yellownpfirehole),
    };
    private ArrayList<Fruit> fruitList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private FruitAdapter fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);

        initView();
        initData();

        fruitAdapter = new FruitAdapter(fruitList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(fruitAdapter);
    }

    private void initData() {
        fruitList.clear();
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

        private final ArrayList<Fruit> fruits;
        private Context mContext;

        @Override
        public FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_fruit, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FruitAdapter.ViewHolder holder, int position) {
            final Fruit fruit = fruits.get(position);
            Glide.with(mContext).load(fruit.getResId()).into(holder.iv);
            holder.tv.setText(fruit.getName());
            holder.rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FruitDetialActivity.actionStart(mContext, fruit);
                }
            });
        }

        public FruitAdapter(ArrayList<Fruit> fruits) {
            this.fruits = fruits;
        }

        @Override
        public int getItemCount() {
            return fruits.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;
            private View rootview;

            public ViewHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.iv);
                tv = itemView.findViewById(R.id.tv);
                rootview = itemView.findViewById(R.id.rootview);
            }
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar.setTitle("Fruits");
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.video);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        initData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fruitAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }.start();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                closeDrawer();
                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MaterialDesignActivity.class);
        context.startActivity(intent);
    }
}
