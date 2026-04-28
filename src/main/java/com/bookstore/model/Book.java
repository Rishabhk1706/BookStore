package com.bookstore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private double price;
    private String category;
    private int stock;
    private String description;
    private String imageUrl;
    private double rating;
    private int reviewCount;

    public Book() {
        this.rating = 4.5;
        this.reviewCount = 0;
    }

    public Book(String title, String author, double price, String category, int stock, String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.description = description;
        this.rating = 4.5;
        this.reviewCount = 0;
    }

    public Book(String title, String author, double price, String category, int stock, String description,
            String imageUrl, double rating, int reviewCount) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
