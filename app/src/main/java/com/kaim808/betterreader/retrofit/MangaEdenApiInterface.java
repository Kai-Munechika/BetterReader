package com.kaim808.betterreader.retrofit;

import com.kaim808.betterreader.pojos.ChapterPages;
import com.kaim808.betterreader.pojos.MangaAndItsChapters;
import com.kaim808.betterreader.pojos.MangaList;

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
    @GET("list/0/")     // append ?p=0 to load first 500 manga
    Call<MangaList> getMangaList();

    @GET("manga/{mangaId}")
    Call<MangaAndItsChapters> getMangaAndChapters(@Path("mangaId") String mangaId);

    @GET("chapter/{chapterId}")
    Call<ChapterPages> getChapter(@Path("chapterId") String chapterId);

}
