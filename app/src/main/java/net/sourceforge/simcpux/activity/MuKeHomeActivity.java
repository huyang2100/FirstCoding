package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;

public class MuKeHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mu_ke_home);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MuKeHomeActivity.class);
        context.startActivity(intent);
    }
}
