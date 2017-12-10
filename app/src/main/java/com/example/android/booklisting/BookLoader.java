package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e("onStartLoading", "Force load successfully!");
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Book> books = null;
        try {
            books = QueryUtils.fetchEarthquakeData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
