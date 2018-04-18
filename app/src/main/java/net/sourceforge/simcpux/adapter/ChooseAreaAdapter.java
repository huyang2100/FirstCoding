package net.sourceforge.simcpux.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sourceforge.simcpux.bean.AreaItem;

import java.util.ArrayList;

/**
 * Created by yanghu on 2018/4/12.
 * 省市县adapter
 */

public class ChooseAreaAdapter extends RecyclerView.Adapter<ChooseAreaAdapter.ViewHolder> {
    private ArrayList<AreaItem> areaList;
    private OnItemClickLisenter onItemClickLisenter;

    public ChooseAreaAdapter(ArrayList<AreaItem> areaList) {
        this.areaList = areaList;
    }

    @Override
    public ChooseAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseAreaAdapter.ViewHolder holder, int position) {
        final AreaItem areaItem = areaList.get(position);
        holder.tv_name.setText(areaItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLisenter != null) {
                    onItemClickLisenter.itemClick(areaItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public void update(ArrayList<AreaItem> areaList) {
        this.areaList = areaList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(android.R.id.text1);
        }
    }

    public void setOnItemClickLisenter(OnItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }

    public interface OnItemClickLisenter {
        void itemClick(AreaItem areaItem);
    }
}
