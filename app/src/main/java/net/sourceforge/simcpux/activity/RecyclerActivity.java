package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.simcpux.R;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {

    private static final String TAG = "RecyclerActivity";
    private RecyclerView recyclerView;
    private ArrayList<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        initView();
        initData();
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, RecyclerActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 60; i++) {

            datas.add("item: " + i);

//            int num = (int) (Math.random() * 10)+1;
//            sb.delete(0,sb.length());
//            for (int j = 0; j < num; j++) {
//                sb.append("item: " + i);
//            }
//            datas.add(sb.toString());
        }

        recyclerView.setAdapter(new MyAdapter(datas));
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private final ArrayList<String> datas;

        public MyAdapter(ArrayList<String> datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            String text = datas.get(position);
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            holder.iv.setBackgroundColor(Color.rgb(r, g, b));
            holder.tv.setBackgroundColor(Color.rgb(r, g, b));
            holder.tv.setText(text);
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RecyclerActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;
            private View rootView;

            public ViewHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.iv);
                tv = itemView.findViewById(R.id.tv);
                rootView = itemView;
            }
        }
    }

    private void initClick() {

    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove:
                Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
