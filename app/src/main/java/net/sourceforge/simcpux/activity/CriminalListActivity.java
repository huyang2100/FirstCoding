package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import net.sourceforge.simcpux.fragment.CrimeListFragment;

public class CriminalListActivity extends BaseSingleFragmentActivity {


    @Override
    protected Fragment getFragment() {
        return new CrimeListFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,CriminalListActivity.class);
    }
}
