package com.example.android.booklisting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class BookAdapter extends ArrayAdapter<Book> {

    BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_listitem, parent, false);
        }

        final Book currentBook = getItem(position);
        ImageView thumbnail = listItemView.findViewById(R.id.item_image);
        TextView title = listItemView.findViewById(R.id.item_title);
        TextView author = listItemView.findViewById(R.id.item_author);
        TextView price = listItemView.findViewById(R.id.item_price);
        TextView rating = listItemView.findViewById(R.id.item_rating);
        ImageView rating_star = listItemView.findViewById(R.id.rating_star);

        assert currentBook != null;
        if (Objects.equals(currentBook.getPrice(), "€---")) {
            price.setVisibility(View.GONE);
        } else {
            price.setText(currentBook.getPrice() + "");
        }
        if (Objects.equals(currentBook.getRating(), "-")) {
            rating.setVisibility(View.GONE);
            rating_star.setVisibility(View.GONE);
        } else {
            rating.setText(currentBook.getRating());
        }

        StringBuilder Author = new StringBuilder(currentBook.getAuthor());
        Author.deleteCharAt(currentBook.getAuthor().length() -1);
        Author.deleteCharAt(currentBook.getAuthor().length() -2);
        Author.delete(0,2);
        if (Author.indexOf(",") != -1) {
            String sep = Author.toString();
            String[] separated = sep.split(",");
            StringBuilder seper = new StringBuilder(separated[0]);
            seper.deleteCharAt(separated[0].length() -1);
            StringBuilder seper2 = new StringBuilder(separated[1]);
            seper2.deleteCharAt(0);
            String result = seper + " und " + seper2;
            if (result.length() > 20) {
                StringBuilder real_result = new StringBuilder(result);
                real_result.delete(20, result.length());
                real_result.append("...");
                author.setText(real_result);
            } else {
                author.setText(result);
            }
        } else {
            author.setText(Author);
        }

        StringBuilder bookTitle = new StringBuilder(currentBook.getBookTitle());
        if (currentBook.getBookTitle().length() > 20) {
            bookTitle.delete(20, currentBook.getBookTitle().length());
            bookTitle.append("...");
            Log.e("BookTitle", bookTitle.toString());
        }

        String thumbnail_url = currentBook.getThumbnailUrl();
        if (thumbnail_url == null) {
            thumbnail.setImageResource(R.drawable.no_image_available);
        } else {
            Picasso.with(getContext()).load(thumbnail_url).into(thumbnail);
        }
        title.setText(bookTitle.toString());

        return listItemView;
    }
}
