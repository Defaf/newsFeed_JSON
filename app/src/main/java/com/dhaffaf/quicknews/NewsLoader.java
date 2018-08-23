package com.dhaffaf.quicknews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN8 on 12/01/18.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private String nUrl;//Query URL

    public NewsLoader(Context context, String url) {
        super(context);
        this.nUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"Test:onStartLoading() called...");
        forceLoad();
    }
    //This is on a background thread.
    @Override
    public List<News> loadInBackground() {

        Log.i(LOG_TAG,"Test: loadInBackground() called...");
        if (nUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        return  extractFeatureFromJson(this.nUrl);
    }
    //start extracting the features from JSON bu first start create HTTP request
    public  List<News> extractFeatureFromJson(String newJSON) {
        //create an object from Connection class
        Connection connect = new Connection();
        //Create an URL
        URL url = connect.createUrl(nUrl);
        try {
            //Make the HTTP connection
            newJSON = connect.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newJSON)) {
            return null;
        }
        //Create a new list from News to handle the data from JSON
        List<News> createNews = new ArrayList<>();
        // Try to parse the JSON response string.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newJSON);
            JSONObject newsRespnce = baseJsonResponse.getJSONObject("response");
            JSONArray articleList = newsRespnce.getJSONArray("results");
            //create a loop to fetch the data from array
            for (int i = 0; i < articleList.length(); i++) {
                JSONObject currentArt = articleList.getJSONObject(i);
                String type = currentArt.getString("type");
                String sectionName = currentArt.getString("sectionName");
                String title = currentArt.getString("webTitle");
                String date = currentArt.getString("webPublicationDate");
                String nUrl = currentArt.getString("webUrl");
                JSONArray rName = currentArt.getJSONArray("tags");
                for (int j = 0; j < rName.length(); j++) {
                    JSONObject node = rName.getJSONObject(j);
                    String name = node.getString("lastName");
                    News myNews = new News(type, title, sectionName, name, date, nUrl);
                    createNews.add(myNews);//add the constructor varible to the list
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try"
            // block display this message in the Log
            Log.e("Connection", "Problem parsing in JSON results", e);
        }
        // Return the list of News
        return createNews;
    }

}
