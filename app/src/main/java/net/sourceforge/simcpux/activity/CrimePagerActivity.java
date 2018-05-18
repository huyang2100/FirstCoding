package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Crime;
import net.sourceforge.simcpux.fragment.CrimeFragment;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.CallBacks{

    private static final String KEY_CRIME_ID = "crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(KEY_CRIME_ID);
        final List<Crime> crimeList = CrimeLab.get().getCrimeList();
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(crimeList.get(position).getId());
            }

            @Override
            public int getCount() {
                return crimeList.size();
            }
        });

        for(int i=0;i<crimeList.size();i++){
            Crime crime = crimeList.get(i);
            if(crime.getId().equals(crimeId)){
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(KEY_CRIME_ID,id);
        return intent;
    }

    @Override
    public void onCrimeUpdate(Crime crime) {

    }
}
