package com.kaim808.betterreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/* splash screen from following post: https://www.bignerdranch.com/blog/splash-screens-the-right-way/ */

// TODO: 9/5/17 move the initial api call here; show a progress bar while saving the mangas to sqlite
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
