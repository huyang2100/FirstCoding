package net.sourceforge.simcpux.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.activity.CriminalActivity;
import net.sourceforge.simcpux.activity.CriminalLab;
import net.sourceforge.simcpux.bean.Criminal;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yanghu on 2018/4/29.
 */

public class CriminalListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CriminalAdapter criminalAdapter;
    private int mSelPostion;

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
            criminalAdapter = new CriminalAdapter(CriminalLab.get().getCriminalList());
            recyclerView.setAdapter(criminalAdapter);
        } else {
            criminalAdapter.notifyItemChanged(mSelPostion);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CriminalAdapter extends RecyclerView.Adapter<CriminalAdapter.CriminalHolder> {

        private final List<Criminal> criminalList;

        public CriminalAdapter(List<Criminal> criminalList) {
            this.criminalList = criminalList;
        }

        @NonNull
        @Override
        public CriminalAdapter.CriminalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CriminalHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CriminalAdapter.CriminalHolder holder, final int position) {
            final Criminal criminal = criminalList.get(position);
            holder.bind(criminal);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelPostion = position;
                    startActivity(CriminalActivity.newIntent(getActivity(), criminal.getId()));
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

            public void bind(final Criminal criminal) {
                tv_title.setText(criminal.getTitle());
                iv_solved.setVisibility(criminal.isSolved() ? View.VISIBLE : View.INVISIBLE);
                tv_date.setText(sdf.format(criminal.getDate()));
            }
        }
    }
}
