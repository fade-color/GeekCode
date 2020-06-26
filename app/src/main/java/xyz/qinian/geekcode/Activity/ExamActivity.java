package xyz.qinian.geekcode.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import xyz.qinian.geekcode.R;
import xyz.qinian.geekcode.Utils.Constant;
import xyz.qinian.geekcode.Utils.HttpUtil;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvExamDetails, mTvResult, mTvTabTitle, mTvTitle, mTvCorrectNum, mTvSubmitNum;
    private EditText mEtCode;
    private Button mBtnSubmitCode;
    private ImageView mIvBack;
    private RelativeLayout mRlResult;

    private int examId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        initView();

        Intent intent = getIntent();
        examId = intent.getIntExtra("exam_id", 1);
        String content = intent.getStringExtra("content");
        String submitNum = intent.getStringExtra("submitNum");
        String correctNum = intent.getStringExtra("correctNum");
        SharedPreferences prefs = getSharedPreferences("account", MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        mTvExamDetails.setText("编码区：        GeekCode™");
        mTvTitle.setText(examId + ". " + content);
        mTvSubmitNum.setText(submitNum);
        mTvCorrectNum.setText(correctNum);
        mEtCode.setText("public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello World!\");\n" +
                "    }\n" +
                "    \n" +
                "}");
    }

    private void initView() {
        mTvExamDetails = findViewById(R.id.tv_exam_details);
        mEtCode = findViewById(R.id.et_code);
        mBtnSubmitCode = findViewById(R.id.btn_submit_code);
        mIvBack = findViewById(R.id.iv_back);
        mRlResult = findViewById(R.id.rl_result);
        mTvResult = findViewById(R.id.tv_result);
        mTvTabTitle = findViewById(R.id.tv_tab_title);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSubmitNum = findViewById(R.id.tv_submit_count);
        mTvCorrectNum = findViewById(R.id.tv_correct_count);

        mBtnSubmitCode.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_code:
                final SVProgressHUD progress1 = new SVProgressHUD(this);
                progress1.showWithStatus("正在提交...");
                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", userId);
                map.put("code", mEtCode.getText().toString());
                map.put("problemId", examId);
                String data = gson.toJson(map);
                System.out.println(data);
                HttpUtil.sendPostRequest(
                        Constant.ADDRESS + "/api/submitCode",
                        data,
                        new Callback() {
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
                                        try {
                                            JSONObject rs = new JSONObject(data);
                                            int code = rs.getInt("code");
                                            mRlResult.setVisibility(View.VISIBLE);
                                            if (code == 200) {
                                                mRlResult.setBackgroundColor(getResources().getColor(R.color.colorAnswerWrong));
                                                mTvResult.setText("编译错误");
                                            } else if (code == 100) {
                                                String runStatus = rs.getJSONObject("extend").getString("runStatus");
                                                if (runStatus.contains("正确")) {
                                                    mRlResult.setBackgroundColor(getResources().getColor(R.color.colorAnswerCorrect));
                                                    mTvResult.setText("答案正确");
                                                } else {
                                                    mRlResult.setBackgroundColor(getResources().getColor(R.color.colorAnswerWrong));
                                                    mTvResult.setText("答案错误");
                                                }
                                            } else {
                                                mRlResult.setBackgroundColor(getResources().getColor(R.color.colorAnswerWrong));
                                                mTvResult.setText("发生异常");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            System.out.println(data);
                                        }
                                        progress1.dismissImmediately();
                                    }
                                });
                            }
                        }
                );
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}