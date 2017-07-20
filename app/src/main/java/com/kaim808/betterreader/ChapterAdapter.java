package com.kaim808.betterreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaim808.betterreader.utils.ImageLoadingUtilities;

/**
 * Created by KaiM on 7/20/17.
 */


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private String[] mImageUrls;
    private Context mContext;

    public ChapterAdapter(String[] imageUrls, Context context) {
        mImageUrls = imageUrls;
        mContext = context;
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {
        ImageView pageHolder;

        ChapterViewHolder(View itemView) {
            super(itemView);
            pageHolder = (ImageView) itemView.findViewById(R.id.page_container);
        }
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.chapter_list_page_layout;
        View v = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ChapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        ImageLoadingUtilities.loadUrlIntoImageView(mImageUrls[position], holder.pageHolder, mContext);
    }

    @Override
    public int getItemCount() {
        return mImageUrls == null ?  0 : mImageUrls.length;
    }

    public void setImageUrls(String[] imageUrls) {
        mImageUrls = imageUrls;
        notifyDataSetChanged();
    }
}
