package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import net.sourceforge.simcpux.R;

import java.util.ArrayList;

public class AlphabetActivity extends BaseActivity {

    private ListView lv;

    @Override
    protected int getResId() {
        return R.layout.activity_alphabet;
    }

    @NonNull
    @Override
    protected String[] requestPermissions() {
        return new String[]{Manifest.permission.READ_CONTACTS};
    }

    @Override
    protected void initData() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String[] from = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(simpleCursorAdapter);
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.lv);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AlphabetActivity.class);
    }
}
