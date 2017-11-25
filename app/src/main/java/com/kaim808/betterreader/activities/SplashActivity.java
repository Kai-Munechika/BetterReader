package com.kaim808.betterreader.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaim808.betterreader.R;
import com.kaim808.betterreader.UpdateMangaService;
import com.kaim808.betterreader.pojos.Manga;
import com.kaim808.betterreader.pojos.MangaList;
import com.kaim808.betterreader.retrofit.MangaEdenApiInterface;
import com.kaim808.betterreader.retrofit.RetrofitSingleton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* splash screen from following post: https://www.bignerdranch.com/blog/splash-screens-the-right-way/ */

// TODO: 9/5/17 move the initial api call here; show a progress bar while saving the mangas to sqlite
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    private static final String MANGAS_SAVED = "mangas_saved";

    @BindView(R.id.text_progress_indicator)
    TextView progressTextView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private List<Manga> mMangas;
    private int numManga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mangasSaved()) {
            if (!isJobServiceOn(this, JOB_ID)) {
                Log.e(TAG, "scheduler starting");
                startMangaUpdateScheduler();
            } else {
                Log.e(TAG, "job service was already on");
            }
            startHomeActivity();
        } else {
            Manga.deleteAll(Manga.class);   // in case of previously disturbed save
            setContentView(R.layout.activity_splash);
            ButterKnife.bind(this);
            makeMangaListCall(RetrofitSingleton.mangaEdenApiInterface);
        }
    }


    private void makeMangaListCall(final MangaEdenApiInterface apiInterface) {
        Log.e(TAG, "Start time: " + System.currentTimeMillis());
        Call<MangaList> call = apiInterface.getMangaList();
        call.enqueue(new Callback<MangaList>() {
            @Override
            public void onResponse(Call<MangaList> call, Response<MangaList> response) {
                Log.e(TAG, "end time: " + System.currentTimeMillis());
                mMangas = response.body().getMangas();
                numManga = mMangas.size();
                persistMangasInSqliteAndUpdateProgressIndicators();
            }

            @Override
            public void onFailure(Call<MangaList> call, Throwable t) {
                String errorMessage = getResources().getString(R.string.networkError);
                progressTextView.setText(errorMessage);
            }
        });
    }

    private void persistMangasInSqliteAndUpdateProgressIndicators() {
        progressBar.setMax(numManga);

        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < numManga; i++) {
                    Manga manga = mMangas.get(i);
                    manga.setCategoriesAsString(manga.categoriesToString());
                    manga.save();
                    publishProgress(i);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                updateProgressIndicators(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onMangasSaved();
                startHomeActivity();
            }

        }.execute();
    }

    private static final int JOB_ID = 7;
    private void startMangaUpdateScheduler() {
        JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(this, UpdateMangaService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(true)
                .setPeriodic(86_400_000)    // one day in ms
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        Log.e(TAG, "Job started");
        jobScheduler.schedule(job);
    }

    private boolean isJobServiceOn(Context context, int JOB_ID) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE );
        if (scheduler == null) {
            return false;
        }

        boolean hasBeenScheduled = false;
        List<JobInfo> jobs = scheduler.getAllPendingJobs();
        for (JobInfo jobInfo : jobs) {
            Log.e(TAG, "JobInfo: " + jobInfo.getId() + " " + jobInfo.toString());
            if (jobInfo.getId() == JOB_ID) {
                hasBeenScheduled = true;
                break;
            }
        }

        return hasBeenScheduled;
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateProgressIndicators(Integer value) {
        progressBar.setProgress(value);
        String s = String.valueOf((value + 1) * 100 / numManga) + "%";
        progressTextView.setText(s);
    }

    private void onMangasSaved() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(MANGAS_SAVED, true);
        editor.apply();
    }

    private boolean mangasSaved() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(MANGAS_SAVED, false);
    }

}
