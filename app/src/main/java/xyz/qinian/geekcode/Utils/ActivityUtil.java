package xyz.qinian.geekcode.Utils;

import android.content.Context;
import android.view.ViewGroup;

public class ActivityUtil {

    /**
     * 为状态栏留出空间
     * @author 张震
     * @param layout 传入最外层layout，为其设置上内边距，空出状态栏
     */
    public static void setPaddingForStatusBar(Context context,ViewGroup layout) {
        int statusBarHeight = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelOffset(resourceId);
        }
        layout.setPadding(layout.getPaddingStart(), statusBarHeight, layout.getPaddingEnd(), layout.getPaddingBottom());
    }

}
