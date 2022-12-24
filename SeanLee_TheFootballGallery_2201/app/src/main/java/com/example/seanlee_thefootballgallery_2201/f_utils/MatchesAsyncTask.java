package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MatchesAsyncTask extends AsyncTask<String, Integer, ArrayList<String>> {

    public static final String TAG = "MatchesAsyncTask.TAG";

    public interface OnFinishFixturesAsyncTask{
        void onPostFixturesAsync(ArrayList<String> _result);
    }

    private OnFinishFixturesAsyncTask mFinishListener;

    public MatchesAsyncTask(OnFinishFixturesAsyncTask _finished) {
        mFinishListener = _finished;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {

        String result = "Err: result not set..";

        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> leagueIds = new ArrayList<>(Arrays.asList("39", "140", "78", "135", "61"));

        // TODO: API-Football API

        for (String leagueId: leagueIds) {
            try {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + leagueId + "&season=2021")
                        .get()
                        .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "87092e5811msh686251dc948c110p12d3cejsn68a77a1b3734")
                        .build();

                Response response = client.newCall(request).execute();

                result = response.body().string();

                results.add(result);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<String> _results) {
        mFinishListener.onPostFixturesAsync(_results);
    }
}
