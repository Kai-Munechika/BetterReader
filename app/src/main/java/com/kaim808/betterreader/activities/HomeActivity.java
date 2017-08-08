package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kaim808.betterreader.R;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {


    public static String SELECTED_CHAPTER_ID = "selected_chapter_id";
    public static String SELECTED_MANGA_IMAGE_URL = "selected_manga_image_url";
    public static String SELECTED_MANGA_NAME = "selected_manga_name";
    public static String SELECTED_MANGA_ID = "selected_manga_id";
    public static String SELECTED_MANGA_CATEGORIES = "selected_manga_categories";
    public static String SELECTED_MANGA_STATUS = "selected_manga_status";
    public static String SELECTED_MANGA_VIEWS = "selected_manga_views";

    private final String testChapterId = "5970b931719a168178337a81";
    private final String testImageUrl = "83/83ec7cce8eb4142ce01ca3d28ab791e1452021a9981b433bf0929f6c.jpg";
    private final String testName= "Boku no Hero Academia";
    private final String testMangaId = "541aabc045b9ef49009d69b6";
    private final List<String> testCategories = Arrays.asList("Action", "Science Fiction", "Adventure");
    private final int testStatus = 1;
    private final int testViews = 3957293;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        final Intent intent = new Intent(this, ChapterViewingActivity.class);
//        intent.putExtra(SELECTED_CHAPTER_ID, testChapterId);

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


    }

    private String statusToString(int status) {
        return status == 1 ? getString(R.string.ongoing) : getString(R.string.completed);
    }

    private String numViewsToString(int numViews) {
        return NumberFormat.getNumberInstance(Locale.US).format(numViews) + " views";
    }


}
