package com.example.energyconnectjavafx.Model;

public class NewsCategory {
    private int newsId;
    private int categoryId;

    // Constructor
    public NewsCategory(int newsId, int categoryId) {
        this.newsId = newsId;
        this.categoryId = categoryId;
    }

    // Getter
    public int getNewsId() {
        return newsId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
