package xyz.qinian.geekcode.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(256, -100, RoundedCornersTransformation.CornerType.BOTTOM)))
                .into(imageView);
    }


}
