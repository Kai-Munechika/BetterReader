package com.kaim808.betterreader.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.fragments.ChapterPageFragment;
import com.kaim808.betterreader.pojos.ChapterPages;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;
import com.kaim808.betterreader.ui.ViewPagerFixed;
import com.kaim808.betterreader.utils.ViewMeasurementUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// TODO: 8/8/17 next/previous chapter if they try to go to next page at either end; via swipe or button touch
// TODO: 8/12/17 add settings; 1 setting: show dialog on next/previous at either end of chapter

// TODO: 8/9/17 figure out how to load images faster
// TODO: 8/9/17 read and save progress for each chapter
// TODO: 8/12/17 enable up navigation
// TODO: 8/12/17 fix the colors; make it a dark background with some trasparancy and white text

public class ChapterViewingActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPagerFixed mPager;
    @BindView(R.id.chapter_viewing_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.page_seekbar)
    SeekBar mPageSeekbar;
    @BindView(R.id.progress_text_view)
    TextView mProgressLabel;
    @BindView(R.id.bottom_ui_container)
    CardView mBottomNavigationView;

    private PagerAdapter mPagerAdapter;
    private String[] mImageUrls;

    private View mDecorView;
    private final int SYSTEM_SHOW = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private final int SYSTEM_HIDE = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hideActivityUi nav bar
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hideActivityUi status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_viewing);
        ButterKnife.bind(this);

        initializeActionBar();
        initializeMainContent();
        initializeBottomUI();
        initializeSystemUi();

    }

    /* Methods for initializing the system ui */
    private void initializeSystemUi() {
        // any view here works, as long as it's in the activity
        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // The system bars are visible
                            showActivityUi();
                        } else {
                            // The system bars are NOT visible
                            hideActivityUi();
                        }
                    }
                });

    }

    public void toggleUI() {
        if (mDecorView.getSystemUiVisibility() == SYSTEM_HIDE) {
            showSystemUi();
        } else {
            hideSystemUi();
        }    }

    private void hideSystemUi() {
        mDecorView.setSystemUiVisibility(SYSTEM_HIDE);
    }

    private void showSystemUi() {
        mDecorView.setSystemUiVisibility(SYSTEM_SHOW);
    }

    /* Methods for initializing the Ui */
    private void initializeActionBar() {
        mToolbar.setTitle(getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_NAME));
        mToolbar.setSubtitle(getIntent().getStringExtra(MangaAndItsChaptersInfoActivity.SELECTED_CHAPTER_SUBTITLE));
        setSupportActionBar(mToolbar);

        // if I decide to use the up action, gotta declare the parent activity in the manifest
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeMainContent() {
        String chapterId = getIntent().getStringExtra(MangaAndItsChaptersInfoActivity.SELECTED_CHAPTER_ID);
        makeChapterCall(RetrofitSingleton.mangaEdenApiInterface, chapterId);

        // insert space between pages
        int margin = 32;
        mPager.setPageMargin(margin);
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

    private void showActivityUi() {

        ObjectAnimator showToolbarAnimator = ObjectAnimator.ofFloat(mToolbar, "translationY", ViewMeasurementUtils.getStatusBarHeight(this));
        ObjectAnimator showBottomAnimator = ObjectAnimator.ofFloat(mBottomNavigationView, "translationY", -ViewMeasurementUtils.getNavigationBarHeight(this));

        showToolbarAnimator.start();
        showBottomAnimator.start();


    }

    private void hideActivityUi() {
        ObjectAnimator hideToolbarAnimator = ObjectAnimator.ofFloat(mToolbar, "y", -(mToolbar.getY() + mToolbar.getHeight()));
        ObjectAnimator hideBottomAnimator = ObjectAnimator.ofFloat(mBottomNavigationView, "translationY", ViewMeasurementUtils.getNavigationBarHeight(this) + mBottomNavigationView.getHeight());

        hideToolbarAnimator.start();
        hideBottomAnimator.start();

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
    private void makeChapterCall(MangaEdenApiInterface apiInterface, String chapterId) {
        Call<ChapterPages> chapterPagesCall = apiInterface.getChapter(chapterId);
        chapterPagesCall.enqueue(new Callback<ChapterPages>() {
            @Override
            public void onResponse(Call<ChapterPages> call, Response<ChapterPages> response) {
                ChapterPages chapterPages = response.body();

                mImageUrls = getImageUrls(chapterPages);
                mPagerAdapter = new ChapterPageAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
                mPager.setOffscreenPageLimit(5);

                mPageSeekbar.setMax(mImageUrls.length - 1);
                mPageSeekbar.setEnabled(true);

                hideSystemUi();
            }

            @Override
            public void onFailure(Call<ChapterPages> call, Throwable t) {
                Toast.makeText(ChapterViewingActivity.this, "Failure in makeChapterCall\n" + getString(R.string.networkError), Toast.LENGTH_SHORT).show();
                ((ContentLoadingProgressBar) ChapterViewingActivity.this.findViewById(R.id.progress_bar)).hide();
            }
        });
    }

    private String[] getImageUrls(ChapterPages chapter) {
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
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP && mImageUrls != null) {
            return true;
        }
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



