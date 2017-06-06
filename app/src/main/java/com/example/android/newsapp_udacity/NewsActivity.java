package com.example.android.newsapp_udacity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String LOG_TAG = NewsActivity.class.getName();

    private static final String GAURDIAN_URL = "http://content.guardianapis.com/search";
    private final String GAURDIAN_API_KEY = "test";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /** Adapter for the list of News stories */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** Progress bar when loading initial data*/
    private ProgressBar mProgressBarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Find reference to ProgressBar
        mProgressBarView = (ProgressBar) findViewById(R.id.loading_spinner);

        /** check for internet connection*/
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyList);

        if(isConnected) {
            // new NewsTask().execute(GAURDIAN_URL);
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            // Find a reference to the {@link ListView} in the layout
            ListView newsListView = (ListView) findViewById(R.id.list);

            // Create a new {@link ArrayAdapter} of news stories
            mAdapter = new NewsAdapter(this, new ArrayList<News>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            newsListView.setAdapter(mAdapter);
            newsListView.setEmptyView(mEmptyStateTextView);


            // set onclicklistener to send to the webpage of the news stories thats clicked on
            // Set a click listener to play the audio when the list item is clicked on
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    // Get the {@link News} object at the given position the user clicked on
                    News currentStory = mAdapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri webUrl = Uri.parse(currentStory.getPreviewLink());

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUrl);
                    startActivity(browserIntent);

                }
            });
        } else {
            mProgressBarView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.noInternet);
        }


    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(GAURDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", GAURDIAN_API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsStories) {
        //hide progress bar when loading is done
        mProgressBarView.setVisibility(View.GONE);

        // Set empty state text to display "No News Stories found."
        mEmptyStateTextView.setText(R.string.emptyList);

        if (newsStories != null && !newsStories.isEmpty()) {
            mAdapter.addAll(newsStories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
