package xyz.qinian.geekcode.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.qinian.geekcode.Activity.AboutActivity;
import xyz.qinian.geekcode.Activity.HomeActivity;
import xyz.qinian.geekcode.Activity.MsgCenterActivity;
import xyz.qinian.geekcode.Activity.MyDetailsActivity;
import xyz.qinian.geekcode.Activity.MySubmitActivity;
import xyz.qinian.geekcode.R;
import xyz.qinian.geekcode.Utils.ActivityUtil;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {

    private RelativeLayout relativeLayout;
    private WebView webView;

    private Handler mHandler = new Handler();

    private String userId;

    public PersonalFragment(String userId) {
        this.userId = userId;
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        webView = view.findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new javaScriptInterface(), "android");
        webView.setWebViewClient(new WebViewClient());
        if (webView != null) {
            webView.loadUrl("http://47.114.6.104:3000/personalDetails?userId=" + userId);
        }
        return view;
    }

    final class javaScriptInterface {

        public javaScriptInterface() {
        }

        @JavascriptInterface
        public void about() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), AboutActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void msgCenter() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), MsgCenterActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void myDetails() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), MyDetailsActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void mySubmit() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), MySubmitActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void logout() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"已注销，请重新登录",Toast.LENGTH_SHORT).show();
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    SharedPreferences.Editor editor = homeActivity.getSharedPreferences("account", MODE_PRIVATE).edit();
                    editor.remove("user_id");
                    editor.apply();
                    homeActivity.enterLoginActivity();
                }
            });
        }

    }

}
