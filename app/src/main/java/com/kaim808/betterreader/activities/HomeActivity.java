package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.pojos.MangaList;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 8/10/17 see how long it takes to make the mangaListCall via progressbar/logging 
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

    ProgressBar testProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // pass the id, title, and image url, categories, status, views(hits),
        final Intent intent = new Intent(this, MangaAndItsChaptersInfoActivity.class);
        intent.putExtra(SELECTED_MANGA_IMAGE_URL, testImageUrl);
        intent.putExtra(SELECTED_MANGA_NAME, testName);
        intent.putExtra(SELECTED_MANGA_ID, testMangaId);

        intent.putExtra(SELECTED_MANGA_CATEGORIES, StringUtils.join(testCategories, ", "));
        intent.putExtra(SELECTED_MANGA_STATUS, statusToString(testStatus));
        intent.putExtra(SELECTED_MANGA_VIEWS, numViewsToString(testViews));

        TextView testTextView = (TextView) findViewById(R.id.testTextView);
        testTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        testProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    private void makeMangaListCall(final MangaEdenApiInterface apiInterface){
        Call<MangaList> call = apiInterface.getMangaList();
        call.enqueue(new Callback<MangaList>() {
            @Override
            public void onResponse(Call<MangaList> call, Response<MangaList> response) {

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


}
