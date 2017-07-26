package com.kaim808.betterreader.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.utils.ImageLoadingUtilities;

public class ChapterPageFragment extends Fragment {

    public static final String PAGE_IMAGE_URL = "PAGE_IMAGE_URL";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);


        ImageView pageContainer = (ImageView) rootView.findViewById(R.id.chapter_page_view);
        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) getActivity().findViewById(R.id.progress_bar);

        String url = getArguments().getString(PAGE_IMAGE_URL);

        ImageLoadingUtilities.loadUrlIntoImageViewAndSetProgressbarVisibility(url, pageContainer, getActivity(), progressBar);
        return rootView;
    }

    public static ChapterPageFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(PAGE_IMAGE_URL, url);

        ChapterPageFragment fragment = new ChapterPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
