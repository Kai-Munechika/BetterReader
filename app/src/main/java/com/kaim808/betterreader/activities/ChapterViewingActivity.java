package com.kaim808.betterreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.kaim808.betterreader.MangaEdenApiInterface;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.RetrofitSingleton;
import com.kaim808.betterreader.ViewPagerFixed;
import com.kaim808.betterreader.fragments.ChapterPageFragment;
import com.kaim808.betterreader.pojos.Chapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterViewingActivity extends AppCompatActivity {

    @BindView(R.id.view_pager) ViewPagerFixed mPager;
    private PagerAdapter mPagerAdapter;
    private String[] mImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_viewing);
        ButterKnife.bind(this);

        String chapterId = getIntent().getStringExtra(HomeActivity.SELECTED_CHAPTER);
        Log.e("kaikai", chapterId);

        chapterCall(RetrofitSingleton.mangaEdenApiInterface, chapterId);


    }

    // this belongs in chapter viewing activity
    private void chapterCall(MangaEdenApiInterface apiInterface, String chapterId) {
        Call<Chapter> chapterPagesCall = apiInterface.getChapter(chapterId);
        chapterPagesCall.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                Chapter chapter = response.body();

                mImageUrls = getImageUrls(chapter);
                mImageUrls = reverseStringArray(mImageUrls);
                mPagerAdapter = new ChapterPageAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
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

    private String getImageUrlSuffix(List<List<String>> listList, int pageNumber) {
        final int PAGE_IMAGE_URL_SUFFIX_INDEX = 1;
        return listList.get(pageNumber).get(PAGE_IMAGE_URL_SUFFIX_INDEX);
    }

    // used since we receive images in reverse order
    private String[] reverseStringArray(String[] strArray) {
        List<String> strList = Arrays.asList(strArray);
        Collections.reverse(strList);
        return (String[]) strList.toArray();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int currentPageNum = mPager.getCurrentItem();

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (currentPageNum < mImageUrls.length - 1) {
                        mPager.setCurrentItem(currentPageNum + 1);
                    }
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (currentPageNum > 0) {
                        mPager.setCurrentItem(currentPageNum - 1);
                    }
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private class ChapterPageAdapter extends FragmentStatePagerAdapter {

        public ChapterPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ChapterPageFragment.newInstance(mImageUrls[position]);
        }

        @Override
        public int getCount() {
            return mImageUrls.length;
        }
    }
}



