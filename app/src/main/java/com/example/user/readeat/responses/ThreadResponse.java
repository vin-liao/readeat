package com.example.user.readeat.responses;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThreadResponse {
    public static final String TAG = ThreadResponse.class.getName();

    @SerializedName("data")
    Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("children")
        private List<Children> childrenList = null;

        public List<Children> getChildrenList() {
            if (childrenList == null) Log.i(TAG, "null childrenlist");
            return childrenList;
        }

        /*public List<String> childrenMore = null;
        public String parent_id;

        @SerializedName("children")
        public List<String> getChildrenMore() {
            if(childrenMore == null) Log.i(TAG, "null chilrenmore");
            return childrenMore;
        }

        public String getParent_id() {
            return parent_id;
        }*/

        public class Children {
            @SerializedName("data")
            ChildrenData childrenData;
            //String kind for identifier

            String kind;

            public String getKind() {
                return kind;
            }

            public ChildrenData getChildrenData() {
                return childrenData;
            }

            public class ChildrenData {
                private int score;
                private String author;
                private String title;
                private int num_comments;
                @SerializedName("selftext_html")
                private String selftext;
                @SerializedName("body_html")
                private String body;
                @SerializedName("replies")
                private JsonElement replies;
                private int depth;
                private String id;
                @SerializedName("children")
                private List<String> moreChildren;
                @SerializedName("parent_id")
                private String parentId;
                private String url;
                private int created_utc;
                private String distinguished;
                //for mod = moderator

                public int getTime(){
                    return created_utc;
                }

                public String getUrl() {
                    return url;
                }

                public List<String> getMoreChildren() {
                    return moreChildren;
                }

                public String getParentId() {
                    return parentId;
                }

                public String getId() {
                    return id;
                }

                public int getDepth() {
                    return depth;
                }

                public JsonElement getReplies() {
                    return replies;
                }

                public String getBody() {
                    return body;
                }

                public String getSelftext() {
                    return selftext;
                }

                public int getNum_comments() {
                    return num_comments;
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
