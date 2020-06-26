package xyz.qinian.geekcode.Fragment;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.Banner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.qinian.geekcode.Adapter.ExamAdapter;
import xyz.qinian.geekcode.R;
import xyz.qinian.geekcode.Utils.ActivityUtil;
import xyz.qinian.geekcode.Utils.Constant;
import xyz.qinian.geekcode.Utils.GlideImageLoader;
import xyz.qinian.geekcode.Utils.HttpUtil;

public class ExaminationFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout mRlSearch;
    private SearchView mSvExam;
    private TextView mTvExam;
    private RecyclerView mRvExamList;
    private String userId;
    private Banner banner;

    public ExaminationFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_examination, container, false);
        mRlSearch = view.findViewById(R.id.rl_search);
        mSvExam = view.findViewById(R.id.sv_search_exam);
        mTvExam = view.findViewById(R.id.tv_search_exam);
        mRvExamList = view.findViewById(R.id.rv_exam_list);
        banner = view.findViewById(R.id.banner);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvExamList.setLayoutManager(manager);
        mSvExam.setOnClickListener(this);
        mSvExam.setIconified(true);
        TextView textView = mSvExam.findViewById(mSvExam.getContext().getResources().getIdentifier("android:id/search_src_text",null,null));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        // 速度
        banner.setDelayTime(3000);
        // 设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        // 设置图片集合
        List<Uri> images = new ArrayList<>();
        images.add(getUriFromDrawableRes(R.drawable.post_image1));
        images.add(getUriFromDrawableRes(R.drawable.post_image2));
        banner.setImages(images);
        banner.start();
        mSvExam.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTvExam.setVisibility(newText.equals("") ? View.VISIBLE : View.GONE);
                return false;
            }
        });
        HttpUtil.sendOkHttpRequest(Constant.ADDRESS + "/api/problemList", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    final JSONArray problems = new JSONArray(responseData);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRvExamList.setAdapter(new ExamAdapter(problems));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sv_search_exam:
                mSvExam.setIconified(false);
                break;
        }
    }

    public Uri getUriFromDrawableRes(int id) {
        Resources resources = getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }
}
