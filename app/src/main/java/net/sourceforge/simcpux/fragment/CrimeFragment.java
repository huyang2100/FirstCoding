package net.sourceforge.simcpux.fragment;


import android.app.Activity;
import android.content.Intent;
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
import net.sourceforge.simcpux.activity.CrimeLab;
import net.sourceforge.simcpux.bean.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {


    private static final String KEY_CRIME_ID = "crime_id";
    private Crime crime;
    private Button btn_detail;

    public static CrimeFragment newInstance(UUID crimeId) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CRIME_ID, crimeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(KEY_CRIME_ID);
        crime = CrimeLab.get().getCrime(crimeId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data!=null){
            Date date = (Date) data.getSerializableExtra(CrimeDateFragment.KEY_DATE);
            crime.setDate(date);
            updateCrime();
            updateDate();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_criminal, container, false);
        EditText et_title = v.findViewById(R.id.et_title);
        et_title.setText(crime.getTitle());
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_detail = v.findViewById(R.id.btn_detail);
        updateDate();
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeDateFragment crimeDateFragment = CrimeDateFragment.newInstance(crime.getDate());
                crimeDateFragment.setTargetFragment(CrimeFragment.this,0);
                crimeDateFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        CheckBox cb_sloved = v.findViewById(R.id.cb_sloved);
        cb_sloved.setChecked(crime.isSolved());
        cb_sloved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                updateCrime();
            }
        });
        return v;
    }

    private void updateCrime() {
        CrimeLab.get().update(crime);
    }

    private void updateDate() {
        btn_detail.setText(crime.getDate().toString());
    }

}
