package net.sourceforge.simcpux.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantReceiver;

public class HomeActivity extends AppCompatActivity {
    private LocalBroadcastManager broadcastManager;
    private OfflineReceiver offlineReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        broadcastManager = LocalBroadcastManager.getInstance(this);
        findViewById(R.id.btn_offline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastManager.sendBroadcast(new Intent(getPackageName() + ConstantReceiver.OFFLINE));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        offlineReceiver = new OfflineReceiver();
        broadcastManager.registerReceiver(offlineReceiver, new IntentFilter(getPackageName() + ConstantReceiver.OFFLINE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (offlineReceiver != null) {
            broadcastManager.unregisterReceiver(offlineReceiver);
            offlineReceiver = null;
        }
    }

    class OfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Wraning")
                    .setMessage("You are forced offline.Please login again!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    }).show();
        }

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
