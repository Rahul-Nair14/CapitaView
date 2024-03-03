package com.example.capitaview_page_1;

public class Headline {
    private String title;
    private String imageResource;

    public Headline(String title, String imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getImageResource() {
        return imageResource;
    }
}

