package net.sourceforge.simcpux.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.Criminal;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriminalFragment extends Fragment {


    private Criminal criminal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        criminal = new Criminal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_criminal, container, false);
        EditText et_title = v.findViewById(R.id.et_title);
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                criminal.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button btn_detail = v.findViewById(R.id.btn_detail);
        btn_detail.setText(criminal.getDate().toString());
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO

            }
        });
        CheckBox cb_sloved = v.findViewById(R.id.cb_sloved);
        cb_sloved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                criminal.setSolved(isChecked);
            }
        });
        return v;
    }

}
