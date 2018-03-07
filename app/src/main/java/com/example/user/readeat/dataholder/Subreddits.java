package com.example.user.readeat.dataholder;

public class Subreddits {
    private String author;
    private int score;
    private String title;
    private String permalink;
    private int comment;
    private String subredditName;
    private int time;
    private String thumbnail;
    private String url;
    private boolean isSelf;
    private String postHint;

    public Subreddits(){}

    public Subreddits(String author, int score, String title, String permalink, int comment,
                      String subredditName, int time, String thumbnail, String url, boolean isSelf, String postHint) {
        this.author = author;
        this.score = score;
        this.title = title;
        this.permalink = permalink;
        this.comment = comment;
        this.subredditName = subredditName;
        this.time = time;
        this.thumbnail = thumbnail;
        this.url = url;
        this.isSelf = isSelf;
        this.postHint = postHint;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public void setPostHint(String postHint) {
        this.postHint = postHint;
    }

    public String getPostHint() {
        return postHint;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getScore() {
        return score;
    }

    public String getPermalink() {
        return permalink;
    }

    public int getComment() {
        return comment;
    }

    public String getSubredditName() {
        return subredditName;
    }
}
