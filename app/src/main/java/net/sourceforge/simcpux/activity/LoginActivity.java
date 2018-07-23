package net.sourceforge.simcpux.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.TestLooperManager;
import android.preference.PreferenceManager;
import android.support.transition.Slide;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.constant.ConstantSP;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etPwd;
    private CheckBox cbPwd;
    private SharedPreferences sp;
    private View root;
    private View flLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        initLisenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_bottom);
    }

    private void initData() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = sp.getBoolean(ConstantSP.REMEMBER_PWD, false);
        String username = sp.getString(ConstantSP.USERNAME, "");
        etUserName.setText(username);
        cbPwd.setChecked(isRemember);
        if (isRemember) {
            etPwd.setText(sp.getString(ConstantSP.PWD, ""));
        } else {
            etPwd.setText("");
        }

        //处理光标
        if (TextUtils.isEmpty(username)) {
            etUserName.setFocusable(true);
            etUserName.setFocusableInTouchMode(true);
            etUserName.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else if (!TextUtils.isEmpty(username) && TextUtils.isEmpty(etPwd.getText().toString())) {
            etPwd.setFocusable(true);
            etPwd.setFocusableInTouchMode(true);
            etPwd.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(etPwd.getText().toString())) {
            root.setFocusable(true);
            root.setFocusableInTouchMode(true);
            root.requestFocus();
        }
    }

    private void closeInputMethod(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initLisenter() {
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = etUserName.getText().toString();
                final String pwd = etPwd.getText().toString();


                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                flLoading.setVisibility(View.VISIBLE);
                closeInputMethod();
                etUserName.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoading.setVisibility(View.GONE);
                        sp.edit().putBoolean(ConstantSP.REMEMBER_PWD, cbPwd.isChecked()).commit();

                        if (userName.equals("admin") && pwd.equals("123456")) {
                            sp.edit().putString(ConstantSP.USERNAME, "admin").commit();
                            sp.edit().putString(ConstantSP.PWD, "123456").commit();
                            HomeActivity.actionStart(LoginActivity.this);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                },2000);

            }
        });

    }

    private void initView() {
        etUserName = findViewById(R.id.et_username);
        etPwd = findViewById(R.id.et_pwd);
        cbPwd = findViewById(R.id.cb_pwd);
        root = findViewById(R.id.root);
        flLoading = findViewById(R.id.fl_loading);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context).toBundle());
    }
}
