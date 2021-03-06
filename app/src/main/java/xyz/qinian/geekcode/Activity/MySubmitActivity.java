package xyz.qinian.geekcode.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import xyz.qinian.geekcode.R;

public class MySubmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_submit);

        SharedPreferences prefs = getSharedPreferences("account", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://47.114.6.104:3000/myrecord?userId=" + userId);
    }
}