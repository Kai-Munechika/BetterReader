package com.kaim808.betterreader.pojos;

/**
 * Created by KaiM on 8/7/17.
 */

public class MangaDetails {
    private String imageUrl;
    private String title;
    private String categories;
    private String status;
    private String views;
    private String description;

    public MangaDetails(String imageUrl, String title, String categories, String status, String views, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.categories = categories;
        this.status = status;
        this.views = views;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMangaName() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
