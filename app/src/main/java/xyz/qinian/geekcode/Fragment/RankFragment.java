package xyz.qinian.geekcode.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.qinian.geekcode.R;

public class RankFragment extends Fragment {

    private WebView webView;
    private String userId;

    public RankFragment(String userId) {
        this.userId = userId;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        webView = view.findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        webView.loadUrl("http://47.114.6.104:3000/rankList?userId=" + userId);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        return view;
    }

}
