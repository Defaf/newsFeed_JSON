package com.dhaffaf.quicknews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    //News JSON url
    private static final String myUrl = "http://content.guardianapis.com/search?show-tags=contributor&api-key=test";

    //Adapter for the list of News
    private NewsAdapter adapter;
    //TextView that is displayed when the list is empty
    private TextView emptyTextView;
    private static int NewsID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the newsList in the layout
        ListView newsList = (ListView) findViewById(R.id.list);

        emptyTextView = (TextView) findViewById(R.id.empty_view);
        newsList.setEmptyView(emptyTextView);

        // Create a new adapter that takes an empty list of News as input
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the newsList
        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current News that was clicked on
                News myLink = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsClickUri = Uri.parse(myLink.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsClickUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        Log.i(LOG_TAG, "Test: calling initLoader()..");

        //check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader.
            loaderManager.initLoader(NewsID, null, this);
        } else {
            //display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }


    //Loader part
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "Test: onCreate() called");

        // Create a new loader for the given URL
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String listpref = sharedPrefs.getString(getString(R.string.list_key), getString(R.string.list_default_value));
        String page = sharedPrefs.getString(getString(R.string.Edit_page_key), getString(R.string.Edit_page_default_value));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(myUrl);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("page", page);
        uriBuilder.appendQueryParameter("order-by", listpref);
        Log.v(LOG_TAG,"the url is: " + uriBuilder.toString());

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        Log.i(LOG_TAG, "Test: onLoadFinish() called...");

        // Hide loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        emptyTextView.setText(R.string.no_News);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "Test: onLoaderReset() called...");
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, settingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


