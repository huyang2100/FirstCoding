package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;

public class ViewConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_connect);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,ViewConnectActivity.class);
    }
}
