package net.sourceforge.simcpux.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sourceforge.simcpux.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNewsTitleClickListener} interface
 * to handle interaction events.
 */
public class NewsTitleListFragment extends Fragment {

    private static final String EXTRA_TITLE_LIST = "title_list";
    private OnNewsTitleClickListener mListener;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_title_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> titleList = (ArrayList<String>) getArguments().getSerializable(EXTRA_TITLE_LIST);
        NewsTitleAdapter newsTitleAdapter = new NewsTitleAdapter(titleList);
        recyclerView.setAdapter(newsTitleAdapter);
    }

    public static NewsTitleListFragment getInstance(ArrayList<String> titleList){
        NewsTitleListFragment fragment = new NewsTitleListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TITLE_LIST,titleList);
        fragment.setArguments(args);
        return fragment;
    }

    class NewsTitleAdapter extends RecyclerView.Adapter<NewsTitleAdapter.ViewHolder> {

        private final ArrayList<String> titleList;

        public NewsTitleAdapter(ArrayList<String> titleList) {
            this.titleList = titleList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_tile, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            String title = titleList.get(position);
            holder.tv_title.setText(title);
            if (position % 2 == 0) {
                holder.tv_title.setBackgroundColor(Color.parseColor("#33ff0000"));
            } else {
                holder.tv_title.setBackgroundColor(Color.parseColor("#3300ff00"));
            }
            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNewsTitleClick(titleList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return titleList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_title;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_title = itemView.findViewById(R.id.tv);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsTitleClickListener) {
            mListener = (OnNewsTitleClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewsTitleClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewsTitleClickListener {
        // TODO: Update argument type and name
        void onNewsTitleClick(String title);
    }
}
