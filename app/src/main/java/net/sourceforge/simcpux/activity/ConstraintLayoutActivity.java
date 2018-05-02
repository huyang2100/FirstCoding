package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.sourceforge.simcpux.R;

/**
 * Created by yanghu on 2018/4/30.
 */

public class ConstraintLayoutActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraintlayout);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context,ConstraintLayoutActivity.class);
    }
}
