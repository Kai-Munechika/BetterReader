package com.kaim808.betterreader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KaiM on 7/19/17.
 */

// TODO: 7/19/17 Learn dependency injection to replace using this singleton

public class RetrofitSingleton {
    public static String MANGA_EDEN_BASE_URL = "https://www.mangaeden.com/api/";

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MANGA_EDEN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static MangaEdenApiInterface mangaEdenApiInterface = retrofit.create(MangaEdenApiInterface.class);
}
