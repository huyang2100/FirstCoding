package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.CriminalFragment;

public class CriminalActivity extends BaseSingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context,CriminalActivity.class);
    }

    @Override
    protected Fragment getFragment() {
        return new CriminalFragment();
    }
}
