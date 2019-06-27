package com.example.android.booklisting;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String url = "https://www.googleapis.com/books/v1/volumes?q=stve%20jobs&maxResults=10";
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View loading_screen = findViewById(R.id.loading_spinner);
        ImageButton open_menu = findViewById(R.id.main_open_menu);
        ImageButton start_search = findViewById(R.id.main_start_search);
        final EditText main_edit = findViewById(R.id.main_search_edit);
        loading_screen.setVisibility(View.GONE);
        ListView listView = findViewById(R.id.list_view);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);
        Intent intent = new Intent(this, MainFrontScreen.class);
        startActivityForResult(intent, 1);

        start_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!main_edit.getText().toString().equals("")) {
                    doQuery(main_edit.getText().toString());
                    main_edit.getText().clear();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Your search is empty, try again!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menu = new Intent(getApplicationContext(),about_menu.class);
                startActivity(menu);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item_url = Objects.requireNonNull(mAdapter.getItem(i)).getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item_url));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String newText = data.getStringExtra("input_text");
                Log.i("Result from Input: ", newText);
                doQuery(newText);
            }
        }
    }

    private void doQuery(String query) {
        query = query.replaceAll("\\s", "%20");
        Log.i("Query", query);
        String url1 = "https://www.googleapis.com/books/v1/volumes?q=";
        String url2 = "&maxResults=10";
        url = url1 + query.toLowerCase() + url2;
        Loader();
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
            ArrayList<Book> final_list = new ArrayList<>();
            for (int i = 0; i < books.size(); i++) {
                Book currentbook = books.get(i);
                if (!currentbook.getPrice().equals("€---")) {
                    if (!currentbook.getRating().equals("-")) {
                        final_list.add(currentbook);
                        books.remove(i);
                        break;
                    }
                }
            }
            for (int i = 0;i<books.size();i++) {
                Book currentbook = books.get(i);
                if (!currentbook.getRating().equals("-") && currentbook.getPrice().equals("€---")) {
                    final_list.add(currentbook);
                }
                else if (!currentbook.getPrice().equals("€---") && currentbook.getRating().equals("-")) {
                    final_list.add(currentbook);
                } else if (!currentbook.getRating().equals("-") && !currentbook.getPrice().equals("€---")) {
                    final_list.add(currentbook);
                }
            }
            mAdapter.addAll(final_list);
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
