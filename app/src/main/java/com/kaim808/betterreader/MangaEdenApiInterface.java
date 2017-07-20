package com.kaim808.betterreader;

import com.kaim808.betterreader.pojos.Chapter;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by KaiM on 7/19/17.
 */

public interface MangaEdenApiInterface {
    /*
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
     */

    // might have a bug since this has no parameters; should probably never use this because it'll take a while to load 6.4 mb
//    @GET("list/0/")
//    Call<MangaList> getMangaList();

    @GET("manga/{mangaId}")
    Call<MangaAndItsChapters> getMangaAndChapters(@Path("mangaId") String mangaId);

    @GET("chapter/{chapterId}")
    Call<Chapter> getChapter(@Path("chapterId") String chapterId);

}