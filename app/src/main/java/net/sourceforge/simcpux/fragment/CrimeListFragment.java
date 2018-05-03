package net.sourceforge.simcpux.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.activity.CrimePagerActivity;
import net.sourceforge.simcpux.activity.CrimeLab;
import net.sourceforge.simcpux.bean.Crime;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CrimeListFragment extends Fragment {

    private static final String KEY_IS_SHOW_SUBTITLE = "is_show_subtitle";
    private RecyclerView recyclerView;
    private CriminalAdapter criminalAdapter;
    private boolean isShowSubTitle = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isShowSubTitle = savedInstanceState.getBoolean(KEY_IS_SHOW_SUBTITLE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SHOW_SUBTITLE, isShowSubTitle);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_crime, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_crime_title_show);
        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        ActionBar actionBar = compatActivity.getSupportActionBar();
        if (isShowSubTitle) {
            menuItem.setTitle(R.string.menu_crime_size_hide);
            actionBar.setSubtitle(getString(R.string.format_crime_size, CrimeLab.get().getCrimeList().size()));
        } else {
            menuItem.setTitle(R.string.menu_crime_size_show);
            actionBar.setSubtitle(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crime_add:
                Crime crime = new Crime();
                CrimeLab.get().add(crime);
                startActivity(CrimePagerActivity.newIntent(getActivity(), crime.getId()));
                return true;
            case R.id.menu_crime_title_show:
                isShowSubTitle = !isShowSubTitle;
                getActivity().invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_criminal_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        updateUI();
        return v;
    }

    private void updateUI() {
        if (criminalAdapter == null) {
            criminalAdapter = new CriminalAdapter(CrimeLab.get().getCrimeList());
            recyclerView.setAdapter(criminalAdapter);
        } else {
            criminalAdapter.notifyDataSetChanged();
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CriminalAdapter extends RecyclerView.Adapter<CriminalAdapter.CriminalHolder> {

        private final List<Crime> criminalList;

        public CriminalAdapter(List<Crime> criminalList) {
            this.criminalList = criminalList;
        }

        @NonNull
        @Override
        public CriminalAdapter.CriminalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CriminalHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CriminalAdapter.CriminalHolder holder, final int position) {
            final Crime criminal = criminalList.get(position);
            holder.bind(criminal);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(CriminalActivity.newIntent(getActivity(), criminal.getId()));
                    startActivity(CrimePagerActivity.newIntent(getActivity(), criminal.getId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return criminalList.size();
        }

        public class CriminalHolder extends RecyclerView.ViewHolder {

            private final TextView tv_title;
            private final TextView tv_date;
            private final ImageView iv_solved;
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm (E)");


            public CriminalHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_criminal, parent, false));
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_date = itemView.findViewById(R.id.tv_date);
                iv_solved = itemView.findViewById(R.id.iv_solved);
            }

            public void bind(final Crime criminal) {
                tv_title.setText(criminal.getTitle());
                iv_solved.setVisibility(criminal.isSolved() ? View.VISIBLE : View.INVISIBLE);
                tv_date.setText(sdf.format(criminal.getDate()));
            }
        }
    }
}
