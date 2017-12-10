package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String url1 = "https://www.googleapis.com/books/v1/volumes?q=";
    private String url2 = "&maxResults=10";
    private String url = "https://www.googleapis.com/books/v1/volumes?q=stve%20jobs&maxResults=10";
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar tools = findViewById(R.id.toolbar);
        setSupportActionBar(tools);
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        TextView emptyTextView = findViewById(R.id.empty_view);
        emptyTextView.setText(R.string.search_book);
        View loading_screen = findViewById(R.id.loading_spinner);
        loading_screen.setVisibility(View.GONE);
        ListView listView = findViewById(R.id.list_view);
        listView.setEmptyView(emptyTextView);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.replaceAll("\\s", "%20");
                Log.i("Query", query);
                url = url1 + query.toLowerCase() + url2;
                Loader();
                final ActionBar ab = getSupportActionBar();
                assert ab != null;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.e("CreatingLoader", "Successfully");
        Log.i("URL", url);
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        TextView emptyTextView = findViewById(R.id.empty_view);
        emptyTextView.setText(R.string.no_books);
        View loading_screen = findViewById(R.id.loading_spinner);
        loading_screen.setVisibility(View.GONE);
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
        Log.e("onLoadFinished", "Loading finished!");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.e("LoaderReset", "Successfully");
        mAdapter.clear();
    }

    public void Loader() {
        View loading_screen = findViewById(R.id.loading_spinner);
        TextView emptyTextView = findViewById(R.id.empty_view);
        emptyTextView.setText("");
        loading_screen.setVisibility(View.VISIBLE);
        mAdapter.clear();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            int BOOK_LOADER_ID = 1;
            loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
            Log.e("LoaderManager", "Loader init successfully");
        } else {
            loading_screen.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }
}
