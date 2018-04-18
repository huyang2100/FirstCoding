package net.sourceforge.simcpux.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sourceforge.simcpux.app.FCApplication;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Fruit;

import java.util.ArrayList;

/**
 * Created by yanghu on 2018/4/10.
 */

public class MultiFruitAdapter extends BaseAdapter {

    private final ArrayList<Fruit> fruitList;

    public MultiFruitAdapter(ArrayList<Fruit> fruitList) {
        this.fruitList = fruitList;
    }

    @Override
    public int getCount() {
        return fruitList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public Fruit getItem(int position) {
        return fruitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitleViewHolder titleViewHolder;
        ContentViewHolder contentViewHolder;

        Fruit fruit = getItem(position);
        switch (getItemViewType(position)) {
            case Fruit.ITEM_TYPE_TITLE:
                if (convertView == null) {
                    convertView = View.inflate(FCApplication.getContext(), R.layout.item_fruit_title, null);
                    titleViewHolder = new TitleViewHolder();
                    titleViewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                    convertView.setTag(titleViewHolder);
                } else {
                    titleViewHolder = (TitleViewHolder) convertView.getTag();
                }
                titleViewHolder.tv_title.setText(fruit.getName());
                break;
            case Fruit.ITEM_TYPE_CONTENT:
                if (convertView == null) {
                    convertView = View.inflate(FCApplication.getContext(), R.layout.item_fruit_content, null);
                    contentViewHolder = new ContentViewHolder();
                    contentViewHolder.tv_content = convertView.findViewById(R.id.tv_content);
                    convertView.setTag(contentViewHolder);
                } else {
                    contentViewHolder = (ContentViewHolder) convertView.getTag();
                }
                contentViewHolder.tv_content.setText(fruit.getName());
                break;
        }
        return convertView;
    }

    class TitleViewHolder {
        TextView tv_title;
    }

    class ContentViewHolder {
        TextView tv_content;
    }
}
