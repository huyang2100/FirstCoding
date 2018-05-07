package net.sourceforge.simcpux.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import net.sourceforge.simcpux.R;

/**
 * Created by yanghu on 2018/4/29.
 */

public abstract class BaseSingleFragmentActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getReslayoutId());
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = getFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    protected int getReslayoutId() {
        return R.layout.fragment_container;
    }

    protected abstract Fragment getFragment();
}
