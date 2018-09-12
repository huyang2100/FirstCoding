package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.sourceforge.simcpux.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();
        initData();
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com/");
//        webView.loadUrl("http://ewmfk.bjrenrentong.com/commit.html");
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    private void initView() {
        webView = findViewById(R.id.webview);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        context.startActivity(intent);
    }
}
