package com.kaim808.betterreader;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.pojos.MangaList;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KaiM on 11/24/17.
 */

public class UpdateMangaService extends JobService {

    private static final String TAG = UpdateMangaService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters params) {
        // return false if the task did not take long and is done by the time the method returns
        // return true if the task will sake some time
                // if we return true, we need to tell the system when the task is complete
                    // by calling jobFinished(JobParameters params, boolean needsRescheduled)
//        new JobTask(this).execute(params);
        Log.e(TAG, "manga list call started in job");
        MangaEdenApiInterface apiInterface = RetrofitSingleton.mangaEdenApiInterface;
        Call<MangaList> call = apiInterface.getMangaList();
        call.enqueue(new Callback<MangaList>() {
            @Override
            public void onResponse(Call<MangaList> call, Response<MangaList> response) {
                Log.e(TAG, "end time: " + System.currentTimeMillis());
                List<Manga> mangas = response.body().getMangas();

                // update vals in sql
                for (Manga manga : mangas) {
                    try {
                        Manga mangaToBeUpdated = Manga.find(Manga.class,
                                "i = ? LIMIT 1", manga.getI())
                                .get(0);

                        // update the image url(im), status(s), hits(h), and last update(ld)
                        mangaToBeUpdated.setIm(manga.getIm());
                        mangaToBeUpdated.setS(manga.getS());
                        mangaToBeUpdated.setH(manga.getH());
                        mangaToBeUpdated.setLd(manga.getLd());
                        mangaToBeUpdated.save();
                        Log.e(TAG,  manga.getTitle() + " updated");
                    } catch (Exception e) {
                        // manga not in db
                        manga.setCategoriesAsString(manga.categoriesToString());
                        manga.save();
                        Log.e(TAG,  manga.getTitle() + " added and saved");
                    }

                }
                Log.e(TAG, "finished updating mangas db");
                UpdateMangaService.this.jobFinished(params, false);
            }

            @Override
            public void onFailure(Call<MangaList> call, Throwable t) {
                String errorMessage = getResources().getString(R.string.networkError);
                UpdateMangaService.this.jobFinished(params, true);
            }
        });

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return true; // reschedule the job if it gets cancelled,
                    // happens when user loses wifi connection, or they unplug their phone

    }
}
