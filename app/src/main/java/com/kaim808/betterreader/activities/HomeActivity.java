package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kaim808.betterreader.R;

public class HomeActivity extends AppCompatActivity {


    public static String SELECTED_CHAPTER = "selected_chapter";
    public static String SELECTED_MANGA_IMAGE_URL = "selected_manga_image_url";
    public static final String SELECTED_MANGA_NAME = "selected_manga_name";

    private static final String testMangaId = "541aabc045b9ef49009d69b6";
    private static final String testChapterId = "5970b931719a168178337a81";
    private static final String testName= "Boku no Hero Academia";


    private static final String testImageUrl = "9c/9cbe24afd66972b1a067dcba0d42b75779221bd0a127a9e0b2f2e05e.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        final Intent intent = new Intent(this, ChapterViewingActivity.class);
//        intent.putExtra(SELECTED_CHAPTER, testChapterId);

        final Intent intent = new Intent(this, MangaAndItsChaptersInfoActivity.class);
        intent.putExtra(SELECTED_MANGA_IMAGE_URL, testImageUrl);
        intent.putExtra(SELECTED_MANGA_NAME, testName);


        TextView testTextView = (TextView) findViewById(R.id.testTextView);
        testTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


    }


}
