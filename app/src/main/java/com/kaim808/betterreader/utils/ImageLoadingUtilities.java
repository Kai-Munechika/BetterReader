package com.kaim808.betterreader.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by KaiM on 7/19/17.
 */

public class ImageLoadingUtilities {

    public static String IMAGE_BASE_URL = "https://cdn.mangaeden.com/mangasimg/";

    // this is a utility method; should be used in many activities
    public static void loadUrlIntoImageView(String suffix, ImageView view, Context context){
        String url = IMAGE_BASE_URL + suffix;
        Glide.with(context)
                .load(url)
//                .placeholder(android.R.drawable.sym_def_app_icon)
                .into(view);
    }

    public static void loadUrlIntoImageView(String suffix, ImageView view, Context context, int height, int width){
        String url = IMAGE_BASE_URL + suffix;
        Glide.with(context)
                .load(url)
                .override(width, height)
                .into(view);
    }
}
