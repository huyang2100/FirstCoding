package net.sourceforge.simcpux.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.activity.CrimeLab;
import net.sourceforge.simcpux.bean.Crime;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {


    private static final String KEY_CRIME_ID = "crime_id";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONSTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private Crime crime;
    private Button btn_detail;
    private Button btn_suspect;
    private File photoFile;
    private static final String TAG = "CrimeFragment";
    private ImageView iv_photo;
    private ArrayList<String> permissionList = new ArrayList<>();

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
        photoFile = CrimeLab.get().getPhotoFile(crime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_DATE) {
                    Date date = (Date) data.getSerializableExtra(CrimeDateFragment.KEY_DATE);
                    crime.setDate(date);
                    updateCrime();
                    updateDate();
                } else if (requestCode == REQUEST_CONSTACT) {
                    Cursor cursor = getContext().getContentResolver().query(data.getData(), new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null, null);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String name = cursor.getString(0);
                        btn_suspect.setText(name);
                        crime.setSuspect(name);
                        updateCrime();
                    }
                }
            }
            if (requestCode == REQUEST_PHOTO) {
                updatePhoto();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int permission : grantResults) {
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "权限被拒绝", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        permissionList.clear();
                        takePicture();
                    }
                } else {
                    Toast.makeText(getActivity(), "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_criminal, container, false);
        iv_photo = v.findViewById(R.id.iv_photo);
        updatePhoto();
        ImageButton ib_photo = v.findViewById(R.id.ib_photo);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean isCanPhoto = photoFile != null && intent.resolveActivity(getActivity().getPackageManager()) != null;
        ib_photo.setEnabled(isCanPhoto);
        ib_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        btn_suspect = v.findViewById(R.id.btn_suspect);
        if (!TextUtils.isEmpty(crime.getSuspect())) {
            btn_suspect.setText(crime.getSuspect());
        }
        btn_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CONSTACT);
                } else {
                    Toast.makeText(getActivity(), "请先安装联系人应用！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        EditText et_title = v.findViewById(R.id.et_title);
        String title = crime.getTitle();
        if (!TextUtils.isEmpty(title)) {
            et_title.setText(title);
            et_title.setSelection(title.length());
        }
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
                crimeDateFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
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

    private void updatePhoto() {
        if (photoFile != null && photoFile.exists()) {
            RequestOptions options = new RequestOptions();
            options.signature(new MediaStoreSignature("image/*", photoFile.lastModified(), 0));

            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", photoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Glide.with(this).setDefaultRequestOptions(options).load(photoFile).into(iv_photo);
        }
    }

    private void takePicture() {
        if (checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //授权
        List<ResolveInfo> resolveInfos = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            for (ResolveInfo resolveInfo : resolveInfos) {
                getActivity().grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(intent, REQUEST_PHOTO);
        }
    }

    private void updateCrime() {
        CrimeLab.get().update(crime);
    }

    private void updateDate() {
        btn_detail.setText(crime.getDate().toString());
    }

}
