package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        if (requestPermissions(requestPermissions())) {
            return;
        }
        initView();
        initData();
    }

    @NonNull
    protected String[] requestPermissions() {
        return null;
    }

    protected abstract int getResId();

    private boolean requestPermissions(String... permissions) {
        if (permissions == null) {
            return false;
        }

        ArrayList<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int permission : grantResults) {
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        initView();
                        initData();
                    }
                } else {
                    finish();
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected abstract void initData();

    protected abstract void initView();
}
