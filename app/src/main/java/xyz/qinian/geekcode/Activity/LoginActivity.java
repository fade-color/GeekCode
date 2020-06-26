package xyz.qinian.geekcode.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.qinian.geekcode.R;
import xyz.qinian.geekcode.Utils.Constant;
import xyz.qinian.geekcode.Utils.HttpUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvRegisterLink;
    private EditText mEtAccount, mEtPwd;
    private Button mBtnLogin, mBtnRegister;
    private EditText mEtAccountRegister, mEtUsername, mEtPwdRegister, mEtRePwdRegister;
    private LinearLayout mLlLogin, mLlRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences("account", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");
        if (!userId.isEmpty()) {
            Intent intent =  new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        mTvRegisterLink = findViewById(R.id.tv_register_link);
        mEtAccount = findViewById(R.id.et_account);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnLogin = findViewById(R.id.btn_login_pwd);
        mEtAccountRegister = findViewById(R.id.et_account_register);
        mEtPwdRegister = findViewById(R.id.et_pwd_register);
        mEtRePwdRegister = findViewById(R.id.et_repwd_register);
        mEtUsername = findViewById(R.id.et_username_register);
        mBtnRegister = findViewById(R.id.btn_register);
        mLlLogin = findViewById(R.id.layout_pwd);
        mLlRegister = findViewById(R.id.layout_register);

        mTvRegisterLink.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_link:
                mLlLogin.setVisibility(View.GONE);
                mLlRegister.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login_pwd:
                final String account = mEtAccount.getText().toString();
                String pwd = mEtPwd.getText().toString();
                HttpUtil.sendOkHttpRequest(Constant.ADDRESS + "/login?username=" + account + "&password=" + pwd, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String data = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
                                int code = 0;
                                try {
                                    code = new JSONObject(data).getInt("code");
                                    if (code == 100) {
                                        // 登录成功
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        SharedPreferences prefs = getSharedPreferences("account", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("user_id", account);
                                        editor.apply();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        new SVProgressHUD(LoginActivity.this).showErrorWithStatus("用户名或密码错误！");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    new SVProgressHUD(LoginActivity.this).showErrorWithStatus("发生异常！");
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.btn_register:
                final String accountRegister = mEtAccountRegister.getText().toString();
                final String username = mEtUsername.getText().toString();
                final String pwdRegister = mEtPwdRegister.getText().toString();
                String rePwd = mEtRePwdRegister.getText().toString();
                if (accountRegister.isEmpty() || username.isEmpty() || pwdRegister.isEmpty() || rePwd.isEmpty()) {
                    new SVProgressHUD(LoginActivity.this).showErrorWithStatus("请输入完整信息！");
                    return;
                }
                if (!pwdRegister.equals(rePwd)) {
                    new SVProgressHUD(LoginActivity.this).showErrorWithStatus("两次密码输入不一致！");
                    return;
                }
                HttpUtil.sendOkHttpRequest(Constant.ADDRESS + "/register/verify?userId=" + accountRegister, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String data = response.body().string();
                        try {
                            JSONObject jsonData = new JSONObject(data);
                            if (jsonData.getInt("code") == 100) {
                                HttpUtil.sendOkHttpRequest(Constant.ADDRESS + "/register/api/user?userId=" + accountRegister + "&userName=" + username + "&password=" + pwdRegister,
                                        new Callback() {
                                            @Override
                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                String data = response.body().string();
                                                System.out.println(data);
                                                try {
                                                    JSONObject jsonData = new JSONObject(data);
                                                    if (jsonData.getInt("code") == 100) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                new SVProgressHUD(LoginActivity.this).showSuccessWithStatus("注册成功！请进行登录");
                                                                mLlLogin.setVisibility(View.VISIBLE);
                                                                mLlRegister.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    }
                                                } catch (JSONException e) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(LoginActivity.this, "返回数据异常", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            } else if (jsonData.getInt("code") == 200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new SVProgressHUD(LoginActivity.this).showErrorWithStatus("此账号已被注册！");
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "发生异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }
}