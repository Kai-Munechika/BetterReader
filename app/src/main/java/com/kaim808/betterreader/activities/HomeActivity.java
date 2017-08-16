package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.pojos.MangaList;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: Note: it takes around 4 - 4.5 seconds to load
public class HomeActivity extends AppCompatActivity {

    public static String SELECTED_MANGA_IMAGE_URL = "selected_manga_image_url";
    public static String SELECTED_MANGA_NAME = "selected_manga_name";
    public static String SELECTED_MANGA_ID = "selected_manga_id";
    public static String SELECTED_MANGA_CATEGORIES = "selected_manga_categories";
    public static String SELECTED_MANGA_STATUS = "selected_manga_status";
    public static String SELECTED_MANGA_VIEWS = "selected_manga_views";

    private final String testChapterId = "5970b931719a168178337a81";
    private final String testImageUrl = "d6/d644db3bb8647192e4bd66b9bfa5dd73e1fbb6bcd83d39d7b5882438.jpg";
    private final String testName= "Boku no Hero Academia";
    private final String testMangaId = "541aabc045b9ef49009d69b6";
    private final List<String> testCategories = Arrays.asList("Action", "Science Fiction", "Adventure");
    private final int testStatus = 1;
    private final int testViews = 3957293;

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;

    TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initializeUi();

//        final Intent intent = new Intent(this, MangaAndItsChaptersInfoActivity.class);
//        intent.putExtra(SELECTED_MANGA_IMAGE_URL, testImageUrl);
//        intent.putExtra(SELECTED_MANGA_NAME, testName);
//        intent.putExtra(SELECTED_MANGA_ID, testMangaId);
//
//        intent.putExtra(SELECTED_MANGA_CATEGORIES, StringUtils.join(testCategories, ", "));
//        intent.putExtra(SELECTED_MANGA_STATUS, statusToString(testStatus));
//        intent.putExtra(SELECTED_MANGA_VIEWS, numViewsToString(testViews));
//
        testTextView = (TextView) findViewById(R.id.testTextView);




        makeMangaListCall(RetrofitSingleton.mangaEdenApiInterface);

    }
    private void mangaSelected(Manga manga) {
        // pass the id, title, and image url, categories, status, views(hits),
        final Intent intent = new Intent(this, MangaAndItsChaptersInfoActivity.class);
        intent.putExtra(SELECTED_MANGA_IMAGE_URL, manga.getImageUrl());
        intent.putExtra(SELECTED_MANGA_NAME, manga.getTitle());
        intent.putExtra(SELECTED_MANGA_ID, manga.getId());

        intent.putExtra(SELECTED_MANGA_CATEGORIES, StringUtils.join(manga.getCategories(), ", "));
        intent.putExtra(SELECTED_MANGA_STATUS, statusToString(manga.getStatus()));
        intent.putExtra(SELECTED_MANGA_VIEWS, numViewsToString(manga.getHits()));

        startActivity(intent);
    }

    private void initializeUi() {
        setupSystemUi();
        setupToolbar();
    }

    private void setupSystemUi() {
        MangaAndItsChaptersInfoActivity.setStatusBarTranslucent(true, getWindow());
        MangaAndItsChaptersInfoActivity.moveLayoutBelowStatusBar(mToolbar, this);
    }
    private void setupToolbar() {
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primaryLargeWhiteText));
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(mToolbar);
    }

    private void makeMangaListCall(final MangaEdenApiInterface apiInterface){
        Log.e("kaikai", "Start time: " + System.currentTimeMillis());
        Call<MangaList> call = apiInterface.getMangaList();
        call.enqueue(new Callback<MangaList>() {
            @Override
            public void onResponse(Call<MangaList> call, Response<MangaList> response) {
                Log.e("kaikai", "end time: " + System.currentTimeMillis());
                MangaList mangaListRoot = response.body();
                final List<Manga> mangas = mangaListRoot.getMangas();

                testTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mangaSelected(mangas.get(770));
                    }
                });

            }

            @Override
            public void onFailure(Call<MangaList> call, Throwable t) {

            }
        });
    }

    private String statusToString(int status) {
        return status == 1 ? getString(R.string.ongoing) : getString(R.string.completed);
    }

    private String numViewsToString(int numViews) {
        return NumberFormat.getNumberInstance(Locale.US).format(numViews) + " views";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                Log.e("kaikai", "menu button pressed");
                return true;
            case (R.id.action_search):
//                launch a searchable spinner dialog
                Log.e("kaikai", "search button pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
