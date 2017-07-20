package com.kaim808.betterreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kaim808.betterreader.MangaEdenApiInterface;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaAndItsChaptersInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_and_its_chapters_info);
    }

    private void mangaAndChapters(final MangaEdenApiInterface apiInterface, String mangaId) {

        Call<MangaAndItsChapters> mangaAndChapters = apiInterface.getMangaAndChapters(mangaId);

        mangaAndChapters.enqueue(new Callback<MangaAndItsChapters>() {
            @Override
            public void onResponse(Call<MangaAndItsChapters> call, Response<MangaAndItsChapters> response) {
                MangaAndItsChapters mangaAndItsChapters = response.body();

                // iterate over all chapters to populate recycler view showing info for each chapter

            }

            @Override
            public void onFailure(Call<MangaAndItsChapters> call, Throwable t) {
                Toast.makeText(MangaAndItsChaptersInfoActivity.this, "mangaAndChapters onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // not gonna need this since we're gonna iterate through the whole thing anyway
//    /* where n starts at 0 */
//    private String getNthChapterId(MangaAndItsChapters mangaAndItsChapters, int n) {
//        final int CHAPTER_ID_INDEX = 3;
//        int numChapters = mangaAndItsChapters.getChapters().size();
//        if (n >= numChapters) {
//            return null;
//        }
//
//        // note, the chapters are in reverse order; newest on top, oldest at the bottom
//        int nthIndex = numChapters - n - 1;
//
//        String chapterId = mangaAndItsChapters.getChapters().get(nthIndex).get(CHAPTER_ID_INDEX);
//        return chapterId;
//    }

}
