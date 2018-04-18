package net.sourceforge.simcpux.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sourceforge.simcpux.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContentFragment extends Fragment {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    private TextView tv_title;
    private TextView tv_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);

        Bundle args = getArguments();

        refresh(args.getString(EXTRA_TITLE), args.getString(EXTRA_CONTENT));
    }

    public static NewsContentFragment getInstance(String title, String content) {
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    public void refresh(String title, String content) {
        tv_title.setText(title);
        tv_content.setText(content);
    }
}
