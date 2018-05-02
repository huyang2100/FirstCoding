package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.CriminalFragment;

import java.util.UUID;

public class CriminalActivity extends BaseSingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "crime_id";

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CriminalActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CriminalFragment.newInstance(crimeId);
    }
}
