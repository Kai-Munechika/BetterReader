package com.kaim808.betterreader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaim808.betterreader.pojos.ChapterMetaData;

/**
 * Created by KaiM on 8/6/17.
 */

public class ChapterMetaDataAdapter extends RecyclerView.Adapter<ChapterMetaDataAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleLabel;
        public TextView chapterNumberLabel;
//        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            titleLabel = (TextView) itemView.findViewById(R.id.title);
            chapterNumberLabel = (TextView) itemView.findViewById(R.id.chapter_number_label);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.horizontal_progress_bar);
//            progressBar.setProgress(0);
        }
    }

    private ChapterMetaData chapterData;

    public ChapterMetaDataAdapter(ChapterMetaData chapterData) {
        this.chapterData = chapterData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View chapterRow = inflater.inflate(R.layout.chapter_row, parent, false);
        return new ViewHolder(chapterRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        int maxProgress = 100;
//        int currentProgress = 40;

        holder.titleLabel.setText(chapterData.getChapterTitle(position));
        holder.chapterNumberLabel.setText(chapterData.getChapterNumber(position));
//        holder.progressBar.setMax(maxProgress);
//        holder.progressBar.setProgress(currentProgress);

    }

    @Override
    public int getItemCount() {
        return chapterData.size();
    }

}