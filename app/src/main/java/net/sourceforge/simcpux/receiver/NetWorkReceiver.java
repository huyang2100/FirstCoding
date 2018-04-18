package net.sourceforge.simcpux.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by yanghu on 2018/3/13.
 */

public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        if(activeNetworkInfo!=null && activeNetworkInfo.isAvailable()){
            Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "network is not available", Toast.LENGTH_SHORT).show();
        }
    }
}
