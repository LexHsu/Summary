package com.andorid.lex.article;

public class Article {
    private int mId;
    private String mTitle;
    private String mAbstract;
    private String mUrl;

    public Article(int id, String title, String abs, String url) {
        this.mId = id;
        this.mTitle = title;
        this.mAbstract = abs;
        this.mUrl = url;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getId() {
        return this.mId;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setAbstract(String abs) {
        this.mAbstract = abs;
    }

    public String getAbstract() {
        return this.mAbstract;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return this.mUrl;
    }
}