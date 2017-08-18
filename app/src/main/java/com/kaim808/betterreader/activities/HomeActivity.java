package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.etc.HomeAdapter;
import com.kaim808.betterreader.etc.ItemClickSupport;
import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.pojos.MangaList;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: Note: it takes around 4 - 4.5 seconds to load
// TODO: 8/17/17 make persistManga a method that works on a background service rather than thread; the manga saving is disrupted onDestroy()
public class HomeActivity extends AppCompatActivity implements ItemClickSupport.OnItemClickListener{

    public static String SELECTED_MANGA_IMAGE_URL = "selected_manga_image_url";
    public static String SELECTED_MANGA_NAME = "selected_manga_name";
    public static String SELECTED_MANGA_ID = "selected_manga_id";
    public static String SELECTED_MANGA_CATEGORIES = "selected_manga_categories";
    public static String SELECTED_MANGA_STATUS = "selected_manga_status";
    public static String SELECTED_MANGA_VIEWS = "selected_manga_views";

    private final String testChapterId = "5970b931719a168178337a81";
    private final String testImageUrl = "d6/d644db3bb8647192e4bd66b9bfa5dd73e1fbb6bcd83d39d7b5882438.jpg";
    private final String testName = "Boku no Hero Academia";
    private final String testMangaId = "541aabc045b9ef49009d69b6";
    private final List<String> testCategories = Arrays.asList("Action", "Science Fiction", "Adventure");
    private final int testStatus = 1;
    private final int testViews = 3957293;

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;

    List<Manga> mMangas;
    HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initializeUi();
        load_mMangas();

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
    }

    private void mMangasUpdated() {
        if (mAdapter == null) {
            HomeAdapter mAdapter = new HomeAdapter(mMangas, this);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void load_mMangas() {
        try {
            mMangas = Manga.listAll(Manga.class);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (mMangas == null || mMangas.size() == 0) {
                makeMangaListCall(RetrofitSingleton.mangaEdenApiInterface);
            } else {
                mMangasUpdated();
            }
        }
    }

    private void makeMangaListCall(final MangaEdenApiInterface apiInterface) {
        Log.e("kaikai", "Start time: " + System.currentTimeMillis());
        Call<MangaList> call = apiInterface.getMangaList();
        call.enqueue(new Callback<MangaList>() {
            @Override
            public void onResponse(Call<MangaList> call, Response<MangaList> response) {
                Log.e("kaikai", "end time: " + System.currentTimeMillis());
                MangaList mangaListRoot = response.body();
                mMangas = mangaListRoot.getMangas();
                mMangasUpdated();
                persistMangas();

            }

            @Override
            public void onFailure(Call<MangaList> call, Throwable t) {

            }
        });
    }

    private void persistMangas() {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mMangas.size(); i++) {
                    Manga manga = mMangas.get(i);
                    manga.setCategoriesAsString(manga.categoriesToString());
                    manga.save();
                }
            }
        });
        backgroundThread.start();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Manga manga = mMangas.get(position);
        onMangaSelected(manga);
    }
    private void onMangaSelected(Manga manga) {
        // pass the id, title, and image url, categories, status, views(hits),
        final Intent intent = new Intent(this, MangaAndItsChaptersInfoActivity.class);
        intent.putExtra(SELECTED_MANGA_IMAGE_URL, manga.getImageUrl());
        intent.putExtra(SELECTED_MANGA_NAME, manga.getTitle());
        intent.putExtra(SELECTED_MANGA_ID, manga.getMangaId());

        intent.putExtra(SELECTED_MANGA_CATEGORIES, manga.categoriesToString());
        intent.putExtra(SELECTED_MANGA_STATUS, manga.getFormattedStatus(this));
        intent.putExtra(SELECTED_MANGA_VIEWS, manga.getFormattedNumViews());

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
                if (mMangas != null && mMangas.size() != 0) {
                    int i = (int) (Math.random() * mMangas.size());
                    onMangaSelected(mMangas.get(i));
                } else {
                    Log.e("kaikai", "mMangas is null or size == 0");
                }
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
