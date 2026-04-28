package com.bookstore.model;

public class WishlistItem {
    private String bookId;
    private String title;
    private String author;
    private double price;
    private String imageUrl;
    private String category;

    public WishlistItem() {
    }

    public WishlistItem(String bookId, String title, String author, double price, String imageUrl, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
