package net.sourceforge.simcpux.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.view.TouchImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {

    private TouchImageView iv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv = view.findViewById(R.id.iv);
        iv.setImageResource(R.drawable.img);
    }

    public static ImageFragment getInstance(){
        ImageFragment dialogFragment = new ImageFragment();
        return dialogFragment;
    }

}
