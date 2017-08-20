package com.kaim808.betterreader.etc;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.utils.ImageLoadingUtilities;

import java.util.List;

/**
 * Created by KaiM on 8/17/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mangaImageView;
        ContentLoadingProgressBar mangaProgressBar;
        TextView mangaTitle;
        TextView mangaViews;

        public ViewHolder(View itemView) {
            super(itemView);

            mangaImageView = (ImageView) itemView.findViewById(R.id.manga_image);
            mangaProgressBar = (ContentLoadingProgressBar) itemView.findViewById(R.id.manga_progress_bar);
            mangaTitle = (TextView) itemView.findViewById(R.id.manga_title);
            mangaViews = (TextView) itemView.findViewById(R.id.manga_views);
        }
    }

    private List<Manga> mMangaList;
    private Context mContext;

    public HomeAdapter(List<Manga> mangaList, Context context) {
        mMangaList = mangaList;
        mContext = context;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View mangaView = inflater.inflate(R.layout.home_grid_tile, parent, false);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) mangaView.getLayoutParams();
        layoutParams.height = parent.getMeasuredHeight() / 2;
        mangaView.setLayoutParams(layoutParams);

        return new ViewHolder(mangaView);

    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        Manga manga = mMangaList.get(position);
        holder.mangaProgressBar.setVisibility(View.VISIBLE);
        holder.mangaTitle.setText(manga.getTitle());
        holder.mangaViews.setText(manga.getFormattedNumViews());
        ImageLoadingUtilities.loadUrlIntoImageViewAndSetProgressbarVisibility(manga.getImageUrl(), holder.mangaImageView, mContext, holder.mangaProgressBar);

    }

    @Override
    public int getItemCount() {
        return mMangaList.size();
    }
}
