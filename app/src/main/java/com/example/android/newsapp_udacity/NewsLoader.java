package com.example.android.newsapp_udacity;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikem on 6/6/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        Log.i("batman",mUrl);
        ArrayList<News> newsStories = QueryUtils.fetchNewsData(mUrl);
        return newsStories;
    }

}
