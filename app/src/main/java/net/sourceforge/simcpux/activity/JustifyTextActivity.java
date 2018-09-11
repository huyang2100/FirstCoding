package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import net.sourceforge.simcpux.R;


public class JustifyTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justify_text);
        TextView tv_nomal = findViewById(R.id.tv_nomal);
        tv_nomal.setText(R.string.shanxi_info_normal);
        TextView tv_html = findViewById(R.id.tv_html);
        tv_html.setText(Html.fromHtml(getString(R.string.shanxi_info_html)));
    }

    public static void actionStart(Context context){
        context.startActivity(new Intent(context,JustifyTextActivity.class));
    }
}
