package com.kaim808.betterreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kaim808.betterreader.ChapterAdapter;
import com.kaim808.betterreader.MangaEdenApiInterface;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.RetrofitSingleton;
import com.kaim808.betterreader.pojos.Chapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewingActivity extends AppCompatActivity {

    @BindView(R.id.chapter_viewing_recyclerview) public RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_viewing);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mLayoutManager = linearLayoutManager;


        mRecyclerView.setLayoutManager(mLayoutManager);


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


//                ImageLoadingUtilities.loadUrlIntoImageView(getImageUrlSuffix(chapter, 0), null, null);
                String[] imageUrls = getImageUrls(chapter);

                mAdapter = new ChapterAdapter(imageUrls, ChapterViewingActivity.this);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                Toast.makeText(ChapterViewingActivity.this, "chapterCall onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String[] getImageUrls(Chapter chapter) {
        List<List<String>> images = chapter.getImages();
        String[] imageUrls = new String[images.size()];

        for(int i = 0; i < images.size(); i++) {
            imageUrls[i] = (getImageUrlSuffix(images, i));
        }

        return imageUrls;
    }

    private String getImageUrlSuffix(List<List<String>> imageObjects, int pageNumber) {
        final int PAGE_IMAGE_URL_SUFFIX_INDEX = 1;
        return imageObjects.get(pageNumber).get(PAGE_IMAGE_URL_SUFFIX_INDEX);
    }

//    // used since we receive images in reverse order
//    private String[] reverseStringArray(String[] strArray) {
//        List<String> strList = Arrays.asList(strArray);
//        Collections.reverse(strList);
//        return (String[]) strList.toArray();
//    }
}


