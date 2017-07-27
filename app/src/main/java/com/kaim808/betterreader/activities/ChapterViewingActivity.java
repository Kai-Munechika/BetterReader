package com.kaim808.betterreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
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

// TODO: 7/23/17 add space between pages in our ViewPager


public class ChapterViewingActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPagerFixed mPager;
    @BindView(R.id.chapter_viewing_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.page_seekbar)
    SeekBar mPageSeekbar;
    @BindView(R.id.progress_text_view)
    TextView mProgressLabel;

    private PagerAdapter mPagerAdapter;
    private String[] mImageUrls;

    private static String testTitle = "Boku no Hero Academia 117";
    private static String testSubtitle = "C’mon, Rappa, Let’s Have Ourselves a Match!!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_viewing);
        ButterKnife.bind(this);

        initializeActionBar();
        initializeMainContent();
        initializeBottomUI();

    }


    /* Methods for initializing the UI */
    private void initializeActionBar() {
        mToolbar.setTitle(testTitle);
        mToolbar.setSubtitle(testSubtitle);
        setSupportActionBar(mToolbar);

        // if I decide to use the up action, gotta declare the parent activity in the manifest
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeMainContent() {
        String chapterId = getIntent().getStringExtra(HomeActivity.SELECTED_CHAPTER);
        chapterCall(RetrofitSingleton.mangaEdenApiInterface, chapterId);
    }

    private void initializeBottomUI() {
        mPageSeekbar.setEnabled(false);

        // have the viewPager update when the seekbar's value changes
        mPageSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            final String label = "Page %s of %s ⋅ %s%%";

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                /* implementation where we use seekbar.setMax(mImageUrls.length - 1 ) */
                int numPages = mPagerAdapter.getCount();
                int percentageRead = (int) ((((float) i + 1) / numPages) * 100);

                String newLabel = String.format(label, i + 1, numPages, String.valueOf(percentageRead));
                mProgressLabel.setText(newLabel);
                mPager.setCurrentItem(i);


                // TODO: 7/26/17 figure this out ; implementation when we use seekbar.setMax(999) for the illusion of continuous thumb dragging
//                // i is the current value the seekbar holds; [0, 999] - this is 1000 values
//                // max = 999
//                // numPages = 22
//                /* implementation where we use seekbar.setMax(1000), to make the seekbar feel continuous */
//                int seekbarMax = seekBar.getMax();
//                int numPages = mPagerAdapter.getCount();
//
//                int percentageRead = (int) ((float)(i + 1) / seekbarMax * 100);
//                int currentPageNum = (int) (((float) ((i+1)*numPages))/(seekbarMax + 1));
//                String newLabel = String.format(label, currentPageNum + 1, numPages, percentageRead);
//
//                mProgressLabel.setText(newLabel);
//                mPager.setCurrentItem(currentPageNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // have the seekbar update when the viewPager turns to a different page
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageSeekbar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chapter_viewing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lorem:
                Toast.makeText(this, "lorem", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_ipsum:
                Toast.makeText(this, "ipsum", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* Methods for our http call */
    private void chapterCall(MangaEdenApiInterface apiInterface, String chapterId) {
        Call<Chapter> chapterPagesCall = apiInterface.getChapter(chapterId);
        chapterPagesCall.enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                Chapter chapter = response.body();

                mImageUrls = getImageUrls(chapter);
                mPagerAdapter = new ChapterPageAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);

                mPageSeekbar.setMax(mImageUrls.length - 1);
//                mPageSeekbar.setMax(999);
                mPageSeekbar.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
                Toast.makeText(ChapterViewingActivity.this, "Failure in chapterCall\n" + getString(R.string.networkError), Toast.LENGTH_SHORT).show();
                ((ContentLoadingProgressBar) ChapterViewingActivity.this.findViewById(R.id.progress_bar)).hide();
            }
        });
    }

    private String[] getImageUrls(Chapter chapter) {
        List<List<String>> images = chapter.getImages();
        String[] imageUrls = new String[images.size()];

        for (int i = 0; i < images.size(); i++) {
            imageUrls[i] = (getImageUrlSuffix(images, i));
        }

        return reverseStringArray(imageUrls);
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


    /* Methods for handling volume rocker presses */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int currentPageNum = mPager.getCurrentItem();

        if (event.getAction() == KeyEvent.ACTION_DOWN && mImageUrls != null) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    gotoNextPage(currentPageNum);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    gotoPreviousPage(currentPageNum);
                    return true;
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP && mImageUrls != null) { return true; }
        return super.dispatchKeyEvent(event);
    }

    private void gotoNextPage(int currentPageNum) {
        if (currentPageNum < mImageUrls.length - 1) {
            mPager.setCurrentItem(currentPageNum + 1);
        }
    }

    private void gotoPreviousPage(int currentPageNum) {
        if (currentPageNum > 0) {
            mPager.setCurrentItem(currentPageNum - 1);
        }
    }


    /* Adapter for our view pager */
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



