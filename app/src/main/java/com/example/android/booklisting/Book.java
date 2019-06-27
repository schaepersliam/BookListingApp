package com.example.android.booklisting;

class Book {
    private String mTitle;
    private String mThumbnailUrl;
    private String mAuthor;
    private String mRating;
    private String mPrice;
    private String mUrl;

    Book(String title, String thumbnailUrl, String author, String Rating, String price, String url) {
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mAuthor = author;
        mRating = Rating;
        mPrice = price;
        mUrl = url;
    }

    String getBookTitle() {
        return mTitle;
    }

    String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getRating() { return mRating; }

    String getPrice() { return mPrice; }

    String getUrl() {return mUrl; }
}