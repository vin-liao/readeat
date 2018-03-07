package com.example.user.readeat.dataholder;

import com.google.gson.JsonElement;
import java.util.List;

public class Threads {
    private String author;
    private String title;
    private String selftext;
    private int score;
    private String body;
    private JsonElement replies;
    private int depth;
    private String id;
    private List<String> moreChildren;
    private String url;
    private int time;
    private String parentId;
    private String kind;

    public Threads(){}

    public Threads(List<String> moreChildren, String moreParent, String kind, int depth, String id) {
        //FOR MORE COMMENT
        this.moreChildren = moreChildren;
        this.parentId = moreParent;
        this.kind = kind;
        this.depth = depth;
        this.id = id;
    }

    public Threads(String author, String title, String selftext, int score, String url, int time) {
        //FOR POST
        this.author = author;
        this.title = title;
        this.selftext = selftext;
        this.score = score;
        this.url = url;
        this.time = time;
    }

    public Threads(String author, int score, String body, JsonElement replies, int depth, String id, String kind, int time) {
        //FOR COMMENT
        this.author = author;
        this.score = score;
        this.body = body;
        this.replies = replies;
        this.depth = depth;
        this.id = id;
        this.kind = kind;
        this.time = time;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setReplies(JsonElement replies) {
        this.replies = replies;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoreChildren(List<String> moreChildren) {
        this.moreChildren = moreChildren;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getParentId() {
        return parentId;
    }

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public List<String> getMoreChildren() {
        return moreChildren;
    }

    public int getDepth() {
        return depth;
    }

    public JsonElement getReplies() {
        return replies;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getSelftext() {
        return selftext;
    }

    public int getScore() {
        return score;
    }

    public String getBody() {
        return body;
    }
}
