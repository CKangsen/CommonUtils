package com.threetree.baseproject.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by rst on 2018/5/24.
 */

public class GlideUtil {
    /**
     * 从URI加载图片
     */
    public static void loadPicture(Context context, String url, ImageView imageView){
        try {
            Picasso.with(context).load(url).placeholder(R.drawable.bg_error).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
