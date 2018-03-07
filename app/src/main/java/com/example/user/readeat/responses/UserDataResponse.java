package com.example.user.readeat.responses;


import com.google.gson.annotations.SerializedName;

public class UserDataResponse {
    @SerializedName("data")
    Data data;

    public Data getData(){
        return data;
    }

    public class Data{
        String name;
        long created_utc;
        int link_karma;
        int comment_karma;

        public String getUsername() {
            return name;
        }

        public long getCreatedUtc() {
            return created_utc;
        }

        public int getLinkKarma() {
            return link_karma;
        }

        public int getCommentKarma() {
            return comment_karma;
        }
    }
}
