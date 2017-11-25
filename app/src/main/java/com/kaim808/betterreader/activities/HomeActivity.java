package com.kaim808.betterreader.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.kaim808.betterreader.GridSpacingItemDecoration;
import com.kaim808.betterreader.R;
import com.kaim808.betterreader.etc.HomeAdapter;
import com.kaim808.betterreader.etc.ItemClickSupport;
import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.utils.ViewMeasurementUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kaim808.betterreader.pojos.Manga.FAVORITED;
import static com.kaim808.betterreader.pojos.Manga.HITS;
import static com.kaim808.betterreader.pojos.Manga.LAST_CHAPTER_DATE;
import static com.kaim808.betterreader.pojos.Manga.TITLE;

// Note: it takes around 4 - 4.5 seconds to load all ~17k manga

/* After mvp stage */
// TODO: 8/25/17 for favorited, display last chapter date rather than number of views, and "completed" if finished.
// TODO: 8/21/17 add the drop down for categories -- look at expandable list view; do this after the app is pass mvp stage
// TODO: 8/21/17 have toolbar collapse on scroll down, back on scroll up https://guides.codepath.com/android/handling-scrolls-with-coordinatorlayout
// TODO: 8/17/17 make persistManga a method that works on a background service rather than thread; the manga saving is disrupted onDestroy()
// TODO: 8/19/17 use an auto resizing textview so that it fits within 2 lines for the manga title, or use ellipses
// TODO: 8/21/17 add the header view for the nav drawer
// TODO: 8/23/17 learn how to build a backend using either a cloud or a physical server to host all this data and create an api for it
// TODO: 8/21/17 credit categories icons;
// Icons made by Picol from www.flaticon.com is licensed by CC 3.0 BY
// https://www.flaticon.com/authors/picol
// https://www.flaticon.com
// http://creativecommons.org/licenses/by/3.0/

/* Primary todos */
// TODO: 8/19/17 add placeholder image for loading manga cover arts
// TODO: 8/19/17 add a splash image https://www.bignerdranch.com/blog/splash-screens-the-right-way/
// TODO: 8/19/17 add a draggable scrollbar indicator
// TODO: 8/21/17 use endless scrolling instead of loading everything at once https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView

public class HomeActivity extends AppCompatActivity implements ItemClickSupport.OnItemClickListener{

    String TAG = HomeActivity.class.getSimpleName();
    public static final String SELECTED_MANGA_IMAGE_URL = "selected_manga_image_url";
    public static final String SELECTED_MANGA_NAME = "selected_manga_name";
    public static final String SELECTED_MANGA_ID = "selected_manga_id";
    public static final String SELECTED_MANGA_CATEGORIES = "selected_manga_categories";
    public static final String SELECTED_MANGA_STATUS = "selected_manga_status";
    public static final String SELECTED_MANGA_VIEWS = "selected_manga_views";
    public static final String TITLES_LIST_KEY = "titles_list_key";
    public static final String ORM_ID = "ORM_ID";

    private final String testChapterId = "5970b931719a168178337a81";
    private final String testImageUrl = "d6/d644db3bb8647192e4bd66b9bfa5dd73e1fbb6bcd83d39d7b5882438.jpg";
    private final String testName = "Boku no Hero Academia";
    private final String testMangaId = "541aabc045b9ef49009d69b6";
    private final List<String> testCategories = Arrays.asList("Action", "Science Fiction", "Adventure");
    private final int testStatus = 1;
    private final int testViews = 3957293;

    private final int POPULAR_INDEX = 0;
    private final int HOT_INDEX = 1;
    private final int RECENT_INDEX = 2;
    private final int FAVORITED_INDEX = 3;
    private final int ABOUT_INDEX = 4;
    private final int SETTINGS_INDEX = 5;
    private final int initialNumManga = 50;
    private String SQLite_TRUE = "1";

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.root_view_group)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_drawer_view)
    NavigationView mNavigationView;

    List<Manga> mMangas;
    List<Manga> mPopularMangas;
    List<Manga> mRecentlyUpdatedMangas;
    List<Manga> mHotMangas;
    List<Manga> mFavoritedMangas;


    HomeAdapter mHomeAdapter;
    MenuItem mSearchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initializeUi();
        showFavoritesIfAvailable();
    }
    // TODO: 8/27/17 rather than always reloading favorites, check if the size changed, reload if it did - don't otherwise
    @Override
    protected void onResume() {
        super.onResume();
//        update favorites in case they favorited/unfavorited something and navigated back to the same list of manga
        if (mNavigationView.getMenu().findItem(R.id.nav_favorited).isChecked()) {
            loadFavoritedManga();
            closeSearchView();
        }
    }

    private void initializeUi() {
        initializeSystemUi();
        initializeToolbar();
        initializeNavigationDrawer();
        initializeRecyclerView();
    }
    private void initializeSystemUi() {
        MangaAndItsChaptersInfoActivity.setStatusBarTranslucent(true, getWindow());
        MangaAndItsChaptersInfoActivity.moveLayoutBelowStatusBar(mToolbar, this);
    }
    private void initializeToolbar() {
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primaryLargeWhiteText));
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(mToolbar);
    }

    /* nav drawer related */
    private void initializeNavigationDrawer() {
        int topPaddingInPixels = ViewMeasurementUtils.getStatusBarHeight(this);
        mNavigationView.setPadding(0, topPaddingInPixels, 0, 0);
        initializeDrawerContent(mNavigationView);
    }
    private void initializeDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    private void selectDrawerItem(MenuItem menuItem) {
        closeSearchView();
        try {
            load_mMangas(menuItem);
        } catch (SQLiteException e) {
            // Manga table doesn't exist
            e.printStackTrace();
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }
    // TODO: 8/27/17 figure out how to save the recycler's position as well and scroll back to it on restore. use recycler.computeVerticalOffset and scrollBy
    private void load_mMangas(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_popular:
                loadPopularMangas();
                break;
            case R.id.nav_hot:
                loadHotMangas();
                break;
            case R.id.nav_most_recent:
                loadMostRecentMangas();
                break;
            case R.id.nav_favorited:
                loadFavoritedManga();
                break;
            case R.id.nav_about:
                update_mMangas(new ArrayList<Manga>()); // clear it out for now
                break;
            case R.id.nav_settings:
                update_mMangas(new ArrayList<Manga>()); // clear it out for now
            default:
        }
    }
    private MenuItem getSelectedNavDrawerMenuItem() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                return menuItem;
            }
        }
        return null;    // should never reach this line, since 1 item will always be checked at all times
    }
    private void showFavoritesIfAvailable() {
        selectDrawerItem(mNavigationView.getMenu().getItem(FAVORITED_INDEX));
        if (mMangas.size() == 0) {
            selectDrawerItem(mNavigationView.getMenu().getItem(POPULAR_INDEX));
        }
    }

    private void on_mMangasUpdated() {
        if (mHomeAdapter == null) {
            HomeAdapter mAdapter = new HomeAdapter(mMangas, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mHomeAdapter.notifyDataSetChanged();
        }
    }

    private void loadPopularMangas() {
        if (mPopularMangas == null) {
            mPopularMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga ORDER BY " + HITS + " DESC LIMIT ?", String.valueOf(initialNumManga));
//            mPopularMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga WHERE t NOT LIKE 'Pokemon%' ORDER BY " + HITS + " DESC LIMIT ?", String.valueOf(initialNumManga));
        }
        update_mMangas(mPopularMangas);
    }
    private void loadHotMangas() {
        if (mHotMangas == null) {
            Double oneMonthInEpoch = 2629743.0; // 1 month (30.44 days) = 2629743 seconds
            Double oneWeekInEpoch = 604800.0;   // 1 week = 604800 seconds
            Double pastWeekInEpoch = System.currentTimeMillis()/1000 - oneWeekInEpoch;

            // here, I'm defining "hot" as most popular and updated within the past week
            mHotMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga WHERE " + LAST_CHAPTER_DATE + " > ? ORDER BY " + HITS + " DESC LIMIT ?", String.valueOf(pastWeekInEpoch), String.valueOf(initialNumManga));
//            mHotMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga WHERE " + LAST_CHAPTER_DATE + " > ? AND t NOT LIKE 'Noblesse' ORDER BY " + HITS + " DESC LIMIT ?", String.valueOf(pastWeekInEpoch), String.valueOf(initialNumManga));
        }
        update_mMangas(mHotMangas);
    }
    private void loadMostRecentMangas() {
        if (mRecentlyUpdatedMangas == null) {
            mRecentlyUpdatedMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga ORDER BY " + LAST_CHAPTER_DATE + " DESC LIMIT ?", String.valueOf(initialNumManga));
        }
        update_mMangas(mRecentlyUpdatedMangas);
    }
    private void loadFavoritedManga() {
        // don't cache since it won't be consistent if the user favorites/unfavorites over at other nav categories(?)
        mFavoritedMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga WHERE " + FAVORITED + " = ? LIMIT ?", SQLite_TRUE, String.valueOf(initialNumManga));
        update_mMangas(mFavoritedMangas);
    }
    private void loadSearchQueriesManga(String searchQuery) {
        if (searchQuery == null || searchQuery.length() == 0) {
            load_mMangas(getSelectedNavDrawerMenuItem());
        }
        else {
            String searchQueryWithWildCards = "%" + searchQuery + "%";
            String minHits = "1";
            List<Manga> searchedMangas = Manga.findWithQuery(Manga.class, "SELECT * FROM Manga WHERE " + TITLE + " LIKE ? AND " + HITS + " >= ? LIMIT ?", searchQueryWithWildCards, minHits, String.valueOf(initialNumManga));
            update_mMangas(searchedMangas);
        }
    }

    private void update_mMangas(List<Manga> mangas) {
        if (mMangas != null) {
            mMangas.clear();
            mMangas.addAll(mangas);
        } else {
            mMangas = new ArrayList<>(mangas);  // copy
        }
        on_mMangasUpdated();
    }

    /* recycler related */
    private void initializeRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        initializeGridSpacing();
        initializeFastScroller();
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
    }

    private void initializeGridSpacing() {
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        int span = layoutManager.getSpanCount();
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.homeGridLayoutSpacing);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(span, spacingInPixels, true));
    }

    private void initializeFastScroller() {
        FastScroller fastScroller = (FastScroller) findViewById(R.id.fast_scroll);
        fastScroller.setRecyclerView(mRecyclerView);
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
        intent.putExtra(ORM_ID, manga.getId());

        startActivity(intent);
    }

    /* searchView related */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_menu, menu);

        mSearchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                toggleKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mMangas == null) {
                    return false;
                }
                else {
                    loadSearchQueriesManga(newText);
                    return true;
                }
            }
        });
        return true;
    }
    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    private void closeSearchView() {
        if (mSearchItem != null) {
            mSearchItem.collapseActionView();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                Log.e(TAG, "menu button pressed");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case (R.id.action_search):
                Log.e(TAG, "search button pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
