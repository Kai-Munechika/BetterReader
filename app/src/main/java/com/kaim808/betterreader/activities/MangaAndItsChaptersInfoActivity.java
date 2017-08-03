package com.kaim808.betterreader.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaim808.betterreader.MangaEdenApiInterface;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;
import com.kaim808.betterreader.utils.ImageLoadingUtilities;
import com.kaim808.betterreader.utils.ViewMeasurementUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaAndItsChaptersInfoActivity extends AppCompatActivity {

    @BindView(R.id.image_banner)
    ImageView mImageBanner;
    @BindView(R.id.manga_info_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_and_its_chapters_info);
        ButterKnife.bind(this);

        String mangaImageUrl = getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_IMAGE_URL);
        ImageLoadingUtilities.loadUrlIntoImageView(mangaImageUrl, mImageBanner, this);

        initializeUi();


    }

    private void initializeUi() {
        mToolbar.setTitle(getIntent().getStringExtra(HomeActivity.SELECTED_MANGA_NAME));
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.setMargins(0, ViewMeasurementUtils.getStatusBarHeight(this), 0, 0);
        mToolbar.setLayoutParams(layoutParams);

        mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);


        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable drawable = mToolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        }
        setStatusBarTranslucent(true);
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

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
