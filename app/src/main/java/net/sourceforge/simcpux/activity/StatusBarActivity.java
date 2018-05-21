package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.jaeger.library.StatusBarUtil;

import net.sourceforge.simcpux.R;

public class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);

//        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorAccent));
//        ActionBar supportActionBar = getSupportActionBar();
//        if(supportActionBar!=null){
//            supportActionBar.hide();
//        }
//        StatusBarUtil.setTranslucent(this);


        SeekBar sb = findViewById(R.id.sb);
        int progress = sb.getProgress();
        changeStatusBarAlpha(progress);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeStatusBarAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void changeStatusBarAlpha(int progress) {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary),progress);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,StatusBarActivity.class);
    }
}
