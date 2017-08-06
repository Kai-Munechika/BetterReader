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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaim808.betterreader.ChapterMetaDataAdapter;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.ChapterMetaData;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;
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
// TODO: 8/6/17 try to somehow optimize performance

public class MangaAndItsChaptersInfoActivity extends AppCompatActivity {

    @BindView(R.id.image_banner)
    ImageView mImageBanner;
    @BindView(R.id.full_poster_image)
    ImageView mPosterImage;

    @BindView(R.id.manga_info_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.title_label)
    TextView mTitle;
    @BindView(R.id.categories_label)
    TextView mCategories;
    @BindView(R.id.status_label)
    TextView mStatus;
    @BindView(R.id.view_count_label)
    TextView mViewCount;
    @BindView(R.id.top_divider)
    View mDivider;

    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.description_toggle)
    Button mDescriptionToggleButton;


    @BindView(R.id.chapters_recycler_view)
    RecyclerView mChaptersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_and_its_chapters_info);
        ButterKnife.bind(this);

        initializeUi();
        makeMangaAndChaptersCall(RetrofitSingleton.mangaEdenApiInterface, getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_ID));
    }

    private void initializeUi() {
        // manga image
        String mangaImageUrl = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_IMAGE_URL);
        ImageLoadingUtilities.loadUrlIntoImageView(mangaImageUrl, mImageBanner, this);
        ImageLoadingUtilities.loadUrlIntoImageView(mangaImageUrl, mPosterImage, this);

        // manga title
        String title = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_NAME);
        mToolbar.setTitle(title);
        mTitle.setText(title);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        // categories
        mCategories.setText(getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_CATEGORIES));

        // status
        mStatus.setText(getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_STATUS));

        // views(hits)
        mViewCount.setText(getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_VIEWS));


        // adjusting to status bar height, so we don't have the toolbar behind the status bar
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0, ViewMeasurementUtils.getStatusBarHeight(this), 0, 0);
        mToolbar.setLayoutParams(layoutParams);
        setStatusBarTranslucent(true);

        // enabling up navigation
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable drawable = mToolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        }

    }
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    // use the api call to get the chapters, description
    private void makeMangaAndChaptersCall(final MangaEdenApiInterface apiInterface, String mangaId) {

        final Call<MangaAndItsChapters> getMangaAndChaptersCall = apiInterface.getMangaAndChapters(mangaId);

        getMangaAndChaptersCall.enqueue(new Callback<MangaAndItsChapters>() {
            @Override
            public void onResponse(Call<MangaAndItsChapters> call, Response<MangaAndItsChapters> response) {
                MangaAndItsChapters mangaAndItsChapters = response.body();

                updateDescription(mangaAndItsChapters);
                toggleVisibilities();
                updateRecyclerView(mangaAndItsChapters);

            }

            @Override
            public void onFailure(Call<MangaAndItsChapters> call, Throwable t) {
                Toast.makeText(MangaAndItsChaptersInfoActivity.this, "makeMangaAndChaptersCall onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDescription(MangaAndItsChapters mangaAndItsChapters) {
        String description = mangaAndItsChapters.getDescription();
        if (description == null || description.length() == 0) {
            description = "No description available\n";
        }
        mDescription.setText(description);
        mDescriptionToggleButton.setVisibility(View.INVISIBLE);
    }

    private void toggleVisibilities() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mDescriptionToggleButton.setVisibility(View.VISIBLE);
        mDivider.setVisibility(View.VISIBLE);
    }

    private void updateRecyclerView(MangaAndItsChapters mangaAndItsChapters) {
        ChapterMetaData chapterMetaData = new ChapterMetaData(mangaAndItsChapters.getChapters());
        ChapterMetaDataAdapter adapter = new ChapterMetaDataAdapter(chapterMetaData);
        mChaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChaptersRecyclerView.setAdapter(adapter);
        mChaptersRecyclerView.setNestedScrollingEnabled(false);
        mChaptersRecyclerView.setMinimumHeight(this.getResources().getDimensionPixelSize(R.dimen.rowHeight)*chapterMetaData.size());
    }


    public void toggleDescription(View view) {
        int maxLines = mDescription.getMaxLines();
        if (maxLines == 3) {
            mDescription.setMaxLines(mDescription.getLineCount());
            ((Button) view).setText(R.string.collapse_label);
        } else {
            mDescription.setMaxLines(3);
            ((Button) view).setText(R.string.expand_label);
        }
    }
}
