package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.CriminalFragment;

public class CriminalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new CriminalFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,CriminalActivity.class);
    }
}
