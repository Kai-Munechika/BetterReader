package com.kaim808.betterreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kaim808.betterreader.MangaEdenApiInterface;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.RetrofitSingleton;
import com.kaim808.betterreader.pojos.Chapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_viewing);

        String chapterId = getIntent().getStringExtra(HomeActivity.SELECTED_CHAPTER);
        chapterCall(RetrofitSingleton.mangaEdenApiInterface, chapterId);

    }

    // this belongs in chapter viewing activity
    private void chapterCall(MangaEdenApiInterface apiInterface, String chapterId) {
        Call<Chapter> chapterPagesCall = apiInterface.getChapter(chapterId);
        chapterPagesCall.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                Chapter chapter = response.body();

                // iterate over all pages and populate the recyclerview with them

//                ImageLoadingUtilities.loadUrlIntoImageView(getImageUrlSuffix(chapter, 0), null, null); 

            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                Toast.makeText(ChapterViewingActivity.this, "chapterCall onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getImageUrlSuffix(Chapter chapter, int pageNumber) {
        final int PAGE_IMAGE_URL_SUFFIX_INDEX = 1;
        return chapter.getImages().get(pageNumber).get(PAGE_IMAGE_URL_SUFFIX_INDEX);
    }
}
