package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kaim808.betterreader.R;

public class HomeActivity extends AppCompatActivity {

    public static String SELECTED_CHAPTER = "selected_chapter";

    private static final String testMangaId = "541aabc045b9ef49009d69b6";
    private static final String testChapterId = "5970b931719a168178337a81";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Intent intent = new Intent(this, ChapterViewingActivity.class);
        intent.putExtra(SELECTED_CHAPTER, testChapterId);

        TextView testTextView = (TextView) findViewById(R.id.testTextView);
        testTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


    }


}
