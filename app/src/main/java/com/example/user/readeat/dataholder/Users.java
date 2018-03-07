package com.example.user.readeat.dataholder;

public class Users {
    private String username;
    private int linkKarma;
    private int commentKarma;
    private int createdUtc;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLinkKarma() {
        return linkKarma;
    }

    public void setLinkKarma(int linkKarma) {
        this.linkKarma = linkKarma;
    }

    public int getCommentKarma() {
        return commentKarma;
    }

    public void setCommentKarma(int commentKarma) {
        this.commentKarma = commentKarma;
    }

    public int getCreatedUtc() {
        return createdUtc;
    }

    public void setCreatedUtc(int createdUtc) {
        this.createdUtc = createdUtc;
    }
}
