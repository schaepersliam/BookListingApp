package com.example.android.booklisting;

class Book {
    private String mTitle;
    private String mThumbnailUrl;
    private String mAuthor;
    private String mRating;
    private String mPrice;

    Book(String title, String thumbnailUrl, String author, String Rating, String price) {
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mAuthor = author;
        mRating = Rating;
        mPrice = price;
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
}