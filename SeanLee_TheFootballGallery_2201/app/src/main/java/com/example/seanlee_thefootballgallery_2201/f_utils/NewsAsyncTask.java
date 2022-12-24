package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;

public class NewsAsyncTask extends AsyncTask<String, Integer, String> {

    public static final String TAG = "NewsAsyncTask.TAG";

    public interface OnFinishNewsAsyncTask{
        void onPostNewsAsync(String _result);
    }

    private OnFinishNewsAsyncTask mFinishListener;

    public NewsAsyncTask(OnFinishNewsAsyncTask _finished) {
        mFinishListener = _finished;
    }

    @Override
    protected String doInBackground(String... strings) {

        String result = "Err: result not set..";

        // TODO: livescore.com API

        String football = "2021020913320920836";
        String d = "https://livescore6.p.rapidapi.com/news/v2/list-by-sport?category=2021020913320920836&page=1";

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://livescore6.p.rapidapi.com/news/v2/list-by-sport?category=2021020913320920836&page=1")
                    .get()
                    .addHeader("x-rapidapi-host", "livescore6.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "87092e5811msh686251dc948c110p12d3cejsn68a77a1b3734")
                    .build();

            Response response = client.newCall(request).execute();

            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "doInBackground: result: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String _result) {

        mFinishListener.onPostNewsAsync(_result);
    }
}
