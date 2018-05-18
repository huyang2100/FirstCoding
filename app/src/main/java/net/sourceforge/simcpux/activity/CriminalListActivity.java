package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Crime;
import net.sourceforge.simcpux.fragment.CrimeFragment;
import net.sourceforge.simcpux.fragment.CrimeListFragment;

import java.util.List;

public class CriminalListActivity extends BaseSingleFragmentActivity implements CrimeListFragment.CallBacks, CrimeFragment.CallBacks {


    @Override
    protected Fragment getFragment() {
        return new CrimeListFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CriminalListActivity.class);
    }

    @Override
    protected int getReslayoutId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Crime> crimeList = CrimeLab.get().getCrimeList();
        if (!crimeList.isEmpty() && findViewById(R.id.fragment_container_detail) != null) {
            onCrimeSelected(crimeList.get(0));
        }
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.fragment_container_detail) == null) {
            startActivity(CrimePagerActivity.newIntent(this, crime.getId()));
        } else {
            CrimeFragment crimeFragment = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail, crimeFragment).commit();
        }
    }

    @Override
    public void onCrimeUpdate(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
