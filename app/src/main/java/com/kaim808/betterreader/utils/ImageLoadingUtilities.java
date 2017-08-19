package com.kaim808.betterreader.utils;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by KaiM on 7/19/17.
 */

public class ImageLoadingUtilities {

    public static String IMAGE_BASE_URL = "https://cdn.mangaeden.com/mangasimg/";

    // this is a utility method; should be used in many activities
    public static void loadUrlIntoImageView(String suffix, ImageView view, Context context){
        String url = IMAGE_BASE_URL + suffix;
        loadExactUrlIntoImageView(url, view, context);
    }
    public static void loadExactUrlIntoImageView(String url, ImageView view, Context context) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .into(view);
    }

    public static void loadUrlIntoImageViewAndSetProgressbarVisibility(String suffix, ImageView view, final Context context, final ContentLoadingProgressBar progressBar) {
        String url = IMAGE_BASE_URL + suffix;
        progressBar.show();
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // TODO: 8/19/17 populate the imageView with a "image unavailable" kind of placeholder
                        progressBar.hide();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.hide();
                        return false;
                    }
                })
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
