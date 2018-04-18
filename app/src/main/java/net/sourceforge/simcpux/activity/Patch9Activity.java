package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Message;

import java.util.ArrayList;

public class Patch9Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText et;
    private ArrayList<Message> msgList = new ArrayList<>();
    private static final String TAG = "Patch9Activity";
    private MsgAdapter msgAdapter;
    private InputMethodManager imm;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch9);

        initView();
        initData();
        initLisenter();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "onWindowFocusChanged: "+hasFocus);

        moveLast();
        et.clearFocus();
        Log.i(TAG, "imm is show: "+et.isInEditMode());
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            Message msg1 = new Message("你好，我叫小明", Message.TYPE_RECEIVE);
            Message msg2 = new Message("你呢？", Message.TYPE_SEND);
            Message msg3 = new Message("你是中国人吗？", Message.TYPE_RECEIVE);
            Message msg4 = new Message("你的哪个大学毕业的呢？", Message.TYPE_SEND);
            Message msg5 = new Message("你学习的专业是什么？", Message.TYPE_RECEIVE);
            msgList.add(msg1);
            msgList.add(msg2);
            msgList.add(msg3);
            msgList.add(msg4);
            msgList.add(msg5);
        }


        msgAdapter = new MsgAdapter(msgList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);

    }

    class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

        private final ArrayList<Message> msgList;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patch9, parent, false);
            return new ViewHolder(view);
        }

        public MsgAdapter(ArrayList<Message> msgList) {
            this.msgList = msgList;
            Log.i(TAG, "MsgAdapter msgSize: " + msgList.size());
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Message msg = msgList.get(position);
            if (msg.getType() == Message.TYPE_RECEIVE) {
                holder.tvSend.setVisibility(View.GONE);
                holder.tvReceive.setVisibility(View.VISIBLE);
                holder.tvReceive.setText(msg.getContent());
            } else {
                holder.tvReceive.setVisibility(View.GONE);
                holder.tvSend.setVisibility(View.VISIBLE);
                holder.tvSend.setText(msg.getContent());
            }
        }

        @Override
        public int getItemCount() {
            return msgList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvReceive;
            private TextView tvSend;

            public ViewHolder(View itemView) {
                super(itemView);
                tvReceive = itemView.findViewById(R.id.tv_receive);
                tvSend = itemView.findViewById(R.id.tv_send);
            }
        }
    }

    private void initLisenter() {
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int keyHeight = Patch9Activity.this.getWindowManager().getDefaultDisplay().getHeight()/3;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//                    Toast.makeText(Patch9Activity.this,"键盘弹起",Toast.LENGTH_SHORT).show();
                    moveLast();
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                    Toast.makeText(Patch9Activity.this,"键盘落下",Toast.LENGTH_SHORT).show();

                }
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    msgList.add(new Message(content, Message.TYPE_SEND));
                    msgAdapter.notifyDataSetChanged();
                    et.setText("");
                    moveLast();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+newState);

                if(et.hasFocus()){
                    et.clearFocus();
                }
            }
        });


        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    imm.hideSoftInputFromWindow(et.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    private void moveLast() {
        recyclerView.scrollToPosition(msgList.size() - 1);
    }

    private void initView() {
        rootView = findViewById(R.id.rootview);
        recyclerView = findViewById(R.id.recyclerview);
        et = findViewById(R.id.et);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, Patch9Activity.class);
        context.startActivity(intent);
    }
}
