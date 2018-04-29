package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.fragment.CriminalListFragment;

public class CriminalListActivity extends BaseSingleFragmentActivity {


    @Override
    protected Fragment getFragment() {
        return new CriminalListFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,CriminalListActivity.class);
    }
}
