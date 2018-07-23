package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import net.sourceforge.simcpux.R;

public class TouchEventActivity extends AppCompatActivity {
    private static final String TAG = "TouchEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);

        findViewById(R.id.tv_touch).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG,"event: "+event.getAction());

                if (PermissionChecker.checkSelfPermission(TouchEventActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TouchEventActivity.this,
                            new String[] { Manifest.permission.RECORD_AUDIO }, 1);
                    return true;
                }


                Log.i(TAG,"event: --------------------------------");
                return false;
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,TouchEventActivity.class);
    }
}
