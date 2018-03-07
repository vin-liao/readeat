package com.example.user.readeat.responses;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostListResponse {
    @SerializedName("data")
    Data data;

    public Data getData() {
        return data;
    }


    public class Data {
        @SerializedName("children")
        private List<Children> childrenList = null;
        private String after;
        private String before;

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }

        public List<Children> getChildrenList() {
            if (childrenList == null) Log.i("readeat", "null childrenlist");
            return childrenList;
        }


        public class Children {
            @SerializedName("data")
            ChildrenData childrenData;

            public ChildrenData getChildrenData() {

                return childrenData;
            }


            public class ChildrenData {
                private int score;
                private String author;
                private String title;
                private String permalink;
                private int num_comments;
                private String subreddit;
                private int created_utc;
                private String thumbnail;
                private String url;
                private boolean is_self;
                private String post_hint;

                public String getPostHint(){
                    return post_hint;
                }

                public boolean isSelf() {
                    return is_self;
                }

                public String getUrl() {
                    return url;
                }

                public String getThumbnail(){
                    return thumbnail;
                }

                public int getTime(){
                    return created_utc;
                }

                public String getSubredditName() {
                    return subreddit;
                }

                public int getNum_comments() {
                    return num_comments;
                }

                public String getPermalink() {
                    return permalink;
                }

                public int getScore() {
                    return score;
                }

                public String getAuthor() {
                    return author;
                }

                public String getTitle() {
                    return title;
                }
            }
        }
    }
}
