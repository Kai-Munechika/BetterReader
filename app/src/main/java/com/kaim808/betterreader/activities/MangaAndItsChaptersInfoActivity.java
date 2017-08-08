package com.kaim808.betterreader.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaim808.betterreader.ChapterMetaDataAdapter;
import com.kaim808.betterreader.R;
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

// TODO: 8/3/17 blur banner image
// TODO: 8/3/17 animate expansion/collapse of description
// TODO: 8/6/17 wire up recyclerview touches to viewing that actual chapter
// TODO: 8/6/17 add animation to recyclerview item touches

public class MangaAndItsChaptersInfoActivity extends AppCompatActivity {

    @BindView(R.id.image_banner)
    ImageView mImageBanner;
    @BindView(R.id.manga_info_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.chapters_recycler_view)
    RecyclerView mChaptersRecyclerView;

    MangaDetails mMangaDetails;
    ChapterMetaDataAdapter mDataAdapter;

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
        moveLayoutBelowStatusBar();
        setStatusBarTranslucent(true);
        enableUpNavigation();
        initializeRecyclerView();
    }

    private void setupToolbarTitle() {
        mToolbar.setTitle(getMangaDetails().getTitle());
        // hide title when expanded
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
    }

    private void moveLayoutBelowStatusBar() {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0, ViewMeasurementUtils.getStatusBarHeight(this), 0, 0);
        mToolbar.setLayoutParams(layoutParams);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void enableUpNavigation() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable drawable = mToolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
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
        ChapterMetaData chapterMetaData = new ChapterMetaData(mangaAndItsChapters.getChapters());
        mDataAdapter.setChapterData(chapterMetaData);
        mDataAdapter.notifyDataSetChanged();
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
