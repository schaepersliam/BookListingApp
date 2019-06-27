package com.example.android.booklisting;

import android.text.TextUtils;
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
import java.util.List;

class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpsRequest(URL url) throws IOException {
        String jsonResponse = "";
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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFromJSONResponse(String JSONResponse) {
        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        String ratings;
        String price;
        String author = null;
        String thumbnailUrl;
        String title;
        String url;
        int bookCount;
        JSONArray jsonArray;

        try {
            JSONObject response = new JSONObject(JSONResponse);
            bookCount = response.getInt("totalItems");
            if (bookCount > 0) {
                jsonArray = response.getJSONArray("items");
            } else {
                return null;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentBook = (JSONObject) jsonArray.get(i);
                JSONObject volumeInfo = (JSONObject) currentBook.get("volumeInfo");
                JSONObject saleInfo = (JSONObject) currentBook.get("saleInfo");
                if (volumeInfo.has("authors")) {
                    author = volumeInfo.getString("authors");
                }
                if (volumeInfo.has("averageRating")) {
                    ratings = volumeInfo.getString("averageRating");
                } else { ratings = "-"; }
                if (saleInfo.has("retailPrice")) {
                    JSONObject retailPrice = (JSONObject)saleInfo.get("retailPrice");
                    price = retailPrice.getString("amount");
                    price = "€" + price;
                } else {
                    price = "€---";
                }
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = (JSONObject) volumeInfo.get("imageLinks");
                    thumbnailUrl = imageLinks.getString("smallThumbnail");
                } else { thumbnailUrl = null; }
                if (volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                } else { title = "No title found"; }
                if (volumeInfo.has("infoLink")) {
                    url = volumeInfo.getString("infoLink");
                } else { url = ""; }

                Book book = new Book(title, thumbnailUrl, author, ratings, price,url);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    static List<Book> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpsRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFromJSONResponse(jsonResponse);
        Log.e("fetchEarthquakeData", "Fetching the earthquake data successfully completed!");
        return books;
    }
}
