package com.kaim808.betterreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaim808.betterreader.pojos.ChapterMetaData;
import com.kaim808.betterreader.pojos.MangaDetails;
import com.kaim808.betterreader.utils.ImageLoadingUtilities;

/**
 * Created by KaiM on 8/6/17.
 */

public class ChapterMetaDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int HEADER_POSITION = 0;

    private MangaDetails mMangaDetails;

    private ChapterMetaData mChapterData;
    private Context mContext;

    private int mProgressBarVisibility = View.VISIBLE;
    private int mToggleButtonVisibility = View.INVISIBLE;

    public ChapterMetaDataAdapter(MangaDetails mangaDetails, Context context) {
        mChapterData = null;
        mMangaDetails = mangaDetails;
        mContext = context;
    }

    public void setChapterData(ChapterMetaData chapterData) {
        this.mChapterData = chapterData;
    }

    public void updateUiFlags() {
        mProgressBarVisibility = View.GONE;
        mToggleButtonVisibility = View.VISIBLE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_ITEM) {
            View chapterRow = inflater.inflate(R.layout.chapter_row, parent, false);
            return new VHItem(chapterRow);
        }
        else if (viewType == TYPE_HEADER) {
            View chapterHeader = inflater.inflate(R.layout.chapter_recycler_header, parent, false);
            return new VHHeader(chapterHeader);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            position -= 1;
            ((VHItem) holder).titleLabel.setText(mChapterData.getChapterTitle(position));
            ((VHItem) holder).chapterNumberLabel.setText(mChapterData.getChapterNumber(position));
//        int maxProgress = 100;
//        int currentProgress = 40;
//        holder.progressBar.setMax(maxProgress);
//        holder.progressBar.setProgress(currentProgress);
        }
        else if (holder instanceof  VHHeader) {
            ImageLoadingUtilities.loadUrlIntoImageView(mMangaDetails.getImageUrl(), ((VHHeader) holder).fullPosterImage, mContext);
            ((VHHeader) holder).titleLabel.setText(mMangaDetails.getTitle());
            ((VHHeader) holder).categories.setText(mMangaDetails.getCategories());
            ((VHHeader) holder).status.setText(mMangaDetails.getStatus());
            ((VHHeader) holder).views.setText(mMangaDetails.getViews());

            ((VHHeader) holder).progressBar.setVisibility(mProgressBarVisibility);
            ((VHHeader) holder).descriptionButton.setVisibility(mToggleButtonVisibility);

            final TextView description = ((VHHeader) holder).description;
            description.setText(mMangaDetails.getDescription());

            ((VHHeader) holder).descriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int maxLines = description.getMaxLines();
                    if (maxLines == 3) {
                        description.setMaxLines(description.getLineCount());
                        ((Button) view).setText(R.string.collapse_label);
                    } else {
                        description.setMaxLines(3);
                        ((Button) view).setText(R.string.expand_label);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mChapterData == null ? 1 : mChapterData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == HEADER_POSITION ? TYPE_HEADER : TYPE_ITEM;
    }

    private class VHItem extends RecyclerView.ViewHolder {
        TextView titleLabel;
        TextView chapterNumberLabel;
//        public ProgressBar progressBar;

        VHItem(View itemView) {
            super(itemView);

            titleLabel = (TextView) itemView.findViewById(R.id.title);
            chapterNumberLabel = (TextView) itemView.findViewById(R.id.chapter_number_label);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.horizontal_progress_bar);
//            progressBar.setProgress(0);
        }
    }

    private class VHHeader extends RecyclerView.ViewHolder {
        ImageView fullPosterImage;
        TextView titleLabel;
        TextView categories;
        TextView status;
        TextView views;
        TextView description;
        Button descriptionButton;
        ProgressBar progressBar;

        VHHeader(View itemView) {
            super(itemView);

            fullPosterImage = (ImageView) itemView.findViewById(R.id.full_poster_image);
            titleLabel = (TextView) itemView.findViewById(R.id.title_label);
            categories = (TextView) itemView.findViewById(R.id.categories_label);
            status = (TextView) itemView.findViewById(R.id.status_label);
            views = (TextView) itemView.findViewById(R.id.view_count_label);
            description = (TextView) itemView.findViewById(R.id.description);
            descriptionButton = (Button) itemView.findViewById(R.id.description_toggle);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }
}