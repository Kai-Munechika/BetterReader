package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.etc.ChapterMetaDataAdapter;
import com.kaim808.betterreader.etc.ItemClickSupport;
import com.kaim808.betterreader.pojos.ChapterMetaData;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;
import com.kaim808.betterreader.pojos.MangaDetails;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;
import com.kaim808.betterreader.utils.ImageLoadingUtilities;
import com.kaim808.betterreader.utils.ViewMeasurementUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 8/3/17 blur banner image or pull first google image result to replace it
// TODO: 8/3/17 animate expansion/collapse of description
// TODO: 8/6/17 add animation to recyclerview item touches
// TODO: 8/9/17 and scrolling scrubber
// TODO: 8/12/17 animate the fab press, and initialize animation from right-off screen to left

public class MangaAndItsChaptersInfoActivity extends AppCompatActivity {

    public static String SELECTED_CHAPTER_ID = "selected_chapter_id";
    public static String SELECTED_CHAPTER_SUBTITLE = "selected_chapter_subtitle";

    @BindView(R.id.image_banner)
    ImageView mImageBanner;
    @BindView(R.id.manga_info_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.chapters_recycler_view)
    RecyclerView mChaptersRecyclerView;

    private ChapterMetaData mChapterMetaData;
    private MangaDetails mMangaDetails;
    private ChapterMetaDataAdapter mDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_and_its_chapters_info);
        ButterKnife.bind(this);

        mMangaDetails = getMangaDetails();

        initializeUi();
        makeMangaAndChaptersCall(RetrofitSingleton.mangaEdenApiInterface, getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_ID));
    }

    /* UI related */
    private void initializeUi() {
        ImageLoadingUtilities.loadUrlIntoImageView(mMangaDetails.getImageUrl(), mImageBanner, this);
        setupToolbarTitle();
        moveLayoutBelowStatusBar(mToolbar, this);
        setStatusBarTranslucent(true, getWindow());
        enableUpNavigation(mToolbar, this);
        initializeRecyclerView();
    }

    private void setupToolbarTitle() {
        mToolbar.setTitle(getMangaDetails().getMangaName());
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primaryLargeWhiteText));
        // hide title when expanded
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
    }

    protected static void moveLayoutBelowStatusBar(Toolbar toolbar, AppCompatActivity appCompatActivity) {
        if (appCompatActivity.findViewById(R.id.root_view_group) instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(0, ViewMeasurementUtils.getStatusBarHeight(appCompatActivity), 0, 0);
            toolbar.setLayoutParams(layoutParams);
        }
        else if (appCompatActivity.findViewById(R.id.root_view_group) instanceof CoordinatorLayout) {
            CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(0, ViewMeasurementUtils.getStatusBarHeight(appCompatActivity), 0, 0);
            toolbar.setLayoutParams(layoutParams);
        }

    }

    protected static void setStatusBarTranslucent(boolean makeTranslucent, Window window) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected static void enableUpNavigation(Toolbar toolbar, AppCompatActivity appCompatActivity) {
        appCompatActivity.setSupportActionBar(toolbar);
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable drawable = toolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.primaryLargeWhiteText), PorterDuff.Mode.SRC_ATOP);
        }
    }

    // use the api call to get the chapters, description
    private void makeMangaAndChaptersCall(final MangaEdenApiInterface apiInterface, String mangaId) {

        final Call<MangaAndItsChapters> getMangaAndChaptersCall = apiInterface.getMangaAndChapters(mangaId);

        getMangaAndChaptersCall.enqueue(new Callback<MangaAndItsChapters>() {
            @Override
            public void onResponse(Call<MangaAndItsChapters> call, Response<MangaAndItsChapters> response) {
                MangaAndItsChapters mangaAndItsChapters = response.body();

                mMangaDetails.setDescription(getDescription(mangaAndItsChapters));
                mDataAdapter.updateUiFlags();
                updateRecyclerView(mangaAndItsChapters);

            }

            @Override
            public void onFailure(Call<MangaAndItsChapters> call, Throwable t) {
                Toast.makeText(MangaAndItsChaptersInfoActivity.this, "makeMangaAndChaptersCall onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* data model related */
    private String getDescription(MangaAndItsChapters mangaAndItsChapters) {
        String description = mangaAndItsChapters.getDescription();
        if (description == null || description.length() == 0) {
            description = "No description available\n";
        }
        return description;
    }

    private void initializeRecyclerView() {
        mDataAdapter = new ChapterMetaDataAdapter(mMangaDetails, this);
        mChaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChaptersRecyclerView.setAdapter(mDataAdapter);
        mChaptersRecyclerView.setNestedScrollingEnabled(true);
    }

    private void updateRecyclerView(MangaAndItsChapters mangaAndItsChapters) {
        mChapterMetaData = new ChapterMetaData(mangaAndItsChapters.getChapters());
        mDataAdapter.setChapterData(mChapterMetaData);
        mDataAdapter.notifyDataSetChanged();

        ItemClickSupport.addTo(mChaptersRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (position != ChapterMetaDataAdapter.HEADER_POSITION) {
                            position--;
                            String chapterId = mChapterMetaData.getChapterId(position);
                            Intent intent = new Intent(MangaAndItsChaptersInfoActivity.this, ChapterViewingActivity.class);
                            intent.putExtra(SELECTED_CHAPTER_ID, chapterId);
                            intent.putExtra(HomeActivity.SELECTED_MANGA_NAME, mMangaDetails.getMangaName());
                            intent.putExtra(SELECTED_CHAPTER_SUBTITLE, mChapterMetaData.getChapterTitle(position));
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    private MangaDetails getMangaDetails() {
        String mangaImageUrl = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_IMAGE_URL);
        String title = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_NAME);
        String categories = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_CATEGORIES);
        String status = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_STATUS);
        String views = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_VIEWS);

        return new MangaDetails(mangaImageUrl, title, categories, status, views, "");
    }
}
