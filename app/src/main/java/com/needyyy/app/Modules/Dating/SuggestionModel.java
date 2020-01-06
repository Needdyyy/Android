package com.needyyy.app.Modules.Dating;

public class SuggestionModel {
    private String imageUrl;
    private String ratingBar;
    private String name;

    public SuggestionModel(String imageUrl, String name, String ratingBar) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.ratingBar = ratingBar;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageView) {
        this.imageUrl = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }

}
