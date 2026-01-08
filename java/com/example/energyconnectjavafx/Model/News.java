package com.example.energyconnectjavafx.Model;

import java.time.LocalDateTime;
import java.util.List;

public class News {
    private int newsId;
    private String headline;
    private LocalDateTime datePosted;
    private String author;
    private String organization;
    private String sourceUrl;
    private String imagePath;
    private String summary;
    private final int admin_id;
    private List<String> categories;

    // Constructor
    public News(int newsId, String headline, LocalDateTime datePosted, String author,
                String organization, String sourceUrl, String headlineImagePath,
                String summary, int adminId) {
        this.newsId = newsId;
        this.headline = headline;
        this.datePosted = datePosted;
        this.author = author;
        this.organization = organization;
        this.sourceUrl = sourceUrl;
        this.imagePath = headlineImagePath;
        this.summary = summary;
        this.admin_id = adminId;
    }

    // Getters and Setters
    public int getId() {
        return newsId;
    }

    public void setId( int newsId ) {
        this.newsId = newsId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setHeadlineImage(String headlineImagePath) {
        this.imagePath = headlineImagePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getAdmin_id() { return admin_id; }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
