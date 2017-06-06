package com.example.android.newsapp_udacity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.newsapp_udacity.NewsActivity.LOG_TAG;

/**
 * Created by mikem on 6/6/2017.
 */

public class QueryUtils {

    private QueryUtils() {}

    /**
     * Query the Gaurdian dataset and return an {@link News} object to represent a NewsStory.
     */
    public static ArrayList<News> fetchNewsData(String requestUrl) {


        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link newsStory} object
        ArrayList<News> newsStories = extractNews(jsonResponse);

        // Return the {@link newsStory ArrayList}
        return newsStories;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news Story JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonResponse) {

        // Create an empty ArrayList that we can start adding News stories to
        ArrayList<News> newsStories = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.

            // Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject root = new JSONObject(jsonResponse);
            // Extract “response” JSONObject
            JSONObject response = root.getJSONObject("response");
            // Extract “results” JSONArray
            JSONArray results = response.getJSONArray("results");
//            Loop through each feature in the array
            for(int i=0;i<results.length();i++){
//                  Get News JSONObject at position i
                JSONObject newsStory = results.getJSONObject(i);
//                  Extract title
                    String title = newsStory.getString("webTitle");
//                  Extract section
                String section = newsStory.getString("sectionName");
//                  Extract webPublicationDate
                String webPublicationDate = newsStory.getString("webPublicationDate");
               // String author = newsStory.getString("author");
//                  Extract previewLink
                String previewLink = newsStory.getString("webUrl");

//                  Create News java object from title, section, and previewLink
//                  Add newsStory to list of newsStories
                newsStories.add(new News(title,section,webPublicationDate,previewLink));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }

        // Return the list of News stories
        return newsStories;
    }

}
