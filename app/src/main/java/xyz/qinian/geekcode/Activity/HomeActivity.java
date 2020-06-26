package xyz.qinian.geekcode.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import xyz.qinian.geekcode.Adapter.HomeFragPagerAdapter;
import xyz.qinian.geekcode.R;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private RadioGroup mRgTabBar;
    private RadioButton mRbExamination, mRbStatus, mRbRank, mRbPersonal;
    RadioButton[] rbs;
    private HomeFragPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences("account", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");
        if (userId.isEmpty()) {
            enterLoginActivity();
        }
        pagerAdapter = new HomeFragPagerAdapter(getSupportFragmentManager(), userId);
        mRgTabBar = findViewById(R.id.rg_tab_bar);
        mRbExamination = findViewById(R.id.rb_examination);
        mRbStatus = findViewById(R.id.rb_status);
        mRbRank = findViewById(R.id.rb_rank);
        mRbPersonal = findViewById(R.id.rb_personal);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);

        rbs = new RadioButton[]{mRbExamination, mRbStatus, mRbRank, mRbPersonal};
        for (RadioButton rb : rbs) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drs = rb.getCompoundDrawables();
            //获取drawables
            Rect r = new Rect(0, 0, drs[1].getMinimumWidth() / 8, drs[1].getMinimumHeight() / 8);
            //定义一个Rect边界
            drs[1].setBounds(r);
            rb.setCompoundDrawables(null, drs[1], null, null);
        }
        mRgTabBar.setOnCheckedChangeListener(this);
        mRbExamination.setChecked(true);
    }

    public void enterLoginActivity() {
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_examination:
                setRbChecked(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_status:
                setRbChecked(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_rank:
                setRbChecked(2);
                viewPager.setCurrentItem(2);
                break;
            case R.id.rb_personal:
                setRbChecked(3);
                viewPager.setCurrentItem(3);
                break;
        }
    }

    private void setRbChecked(int n) {
        for (int i = 0; i < rbs.length; i++) {
            rbs[i].setTextColor(getResources().getColor(i == n ? R.color.colorPrimary:R.color.colorBase));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) { // 滑动完成后
            rbs[viewPager.getCurrentItem()].setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}