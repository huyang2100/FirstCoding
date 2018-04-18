package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.receiver.NetWorkReceiver;

public class ReceiverCodeNetWorkActivity extends AppCompatActivity {

    private NetWorkReceiver netWorkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_code_net_work);

        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netWorkReceiver != null) {
            unregisterReceiver(netWorkReceiver);
            netWorkReceiver = null;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ReceiverCodeNetWorkActivity.class);
        context.startActivity(intent);
    }
}
