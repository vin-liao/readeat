package com.example.user.readeat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.readeat.R;
import com.example.user.readeat.dataholder.Threads;
import com.example.user.readeat.adapters.CommentListViewAdapter;
import com.example.user.readeat.responses.ThreadResponse;
//import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SubActivity extends AppCompatActivity {

    public static final String TAG = SubActivity.class.getName();
    private String points;
    private String permalink;
    private ListView listView;
    private List<Threads> threads;
    private List<Threads> addThread;
    private JSONObject postObject;
    private JSONObject commentObject;
    private CommentListViewAdapter adapter;
    private AsyncHttpClient client;
    private JSONObject moreCommentObject;
    private ProgressBar progressBarSub;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        threads = new ArrayList<Threads>();
        addThread = new ArrayList<Threads>();
        client = new AsyncHttpClient();
        listView = (ListView)findViewById(R.id.commentListView);
        adapter = new CommentListViewAdapter(SubActivity.this, threads);
        listView.setAdapter(adapter);
        progressBarSub = (ProgressBar)findViewById(R.id.progressBarSub);
        progressBarSub.setVisibility(View.VISIBLE);
        permalink = getIntent().getStringExtra("permalink");
        Log.i(TAG, "permalink got from MainActivity: " + permalink);

        toolbar = (Toolbar)findViewById(R.id.sub_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get(permalink);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back to main activity
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Threads singleThread = threads.get(position);
                threads.remove(position);
                int clickedItem = view.getId();

                if(singleThread.getKind().equals("more") && !singleThread.getId().equals("_")){
                    //more comment
                    //for(int i = 0; i < singleThread.getMoreChildren().size(); i++) {
                    for(int i = singleThread.getMoreChildren().size() - 1; i >= 0; i--){
                        //get id
                        final String commentId = singleThread.getMoreChildren().get(i);
                        getMoreComment(singleThread, commentId, position);
                    }

                }else if(singleThread.getKind().equals("more") && singleThread.getId().equals("_")){
                    //continue thread...
                    String parentId = singleThread.getParentId();
                    parentId = parentId.substring(3);
                    getMoreComment(singleThread, parentId, position);

                }else if(singleThread.getKind().equals("t1")){
                    //click on the comment itself
                }
            }
        });
    }

    //parse comment
    private void commentRepliesParse(JsonElement stringComment) {

        if(stringComment == null || stringComment.isJsonPrimitive()) return;
        Gson gson = new Gson();
        ThreadResponse comment = gson.fromJson(stringComment.toString(), ThreadResponse.class);

        for (int i = 0; i < comment.getData().getChildrenList().size(); i++) {

            String commentAuthor = comment.getData().getChildrenList().get(i).getChildrenData().getAuthor();
            int commentScore = comment.getData().getChildrenList().get(i).getChildrenData().getScore();
            String commentBody = comment.getData().getChildrenList().get(i).getChildrenData().getBody();
            JsonElement commentReplies = comment.getData().getChildrenList().get(i).getChildrenData().getReplies();
            int commentDepth = comment.getData().getChildrenList().get(i).getChildrenData().getDepth();
            String commentId = comment.getData().getChildrenList().get(i).getChildrenData().getId();
            String commentKind = comment.getData().getChildrenList().get(i).getKind();
            int commentTime = comment.getData().getChildrenList().get(i).getChildrenData().getTime();

            if(comment.getData().getChildrenList().get(i).getKind().equals("t1")){
                threads.add(new Threads(commentAuthor, commentScore, commentBody, commentReplies, commentDepth,
                        commentId, commentKind, commentTime));

            }else if(comment.getData().getChildrenList().get(i).getKind().equals("more")){
                List<String> commentMore = comment.getData().getChildrenList().get(i).getChildrenData().getMoreChildren();
                String commentParent = comment.getData().getChildrenList().get(i).getChildrenData().getParentId();
                String commentKindMore = comment.getData().getChildrenList().get(i).getKind();
                int commentMoreDepth = comment.getData().getChildrenList().get(i).getChildrenData().getDepth();
                String commentMoreId = comment.getData().getChildrenList().get(i).getChildrenData().getId();

                threads.add(new Threads(commentMore, commentParent, commentKindMore, commentMoreDepth, commentMoreId));
            }
            commentRepliesParse(commentReplies);
        }
    }

    //parse more comment
    private void commentRepliesParse(JsonElement stringComment, int depth) {

        if(stringComment == null || stringComment.isJsonPrimitive()) return;
        Gson gson = new Gson();
        ThreadResponse comment = gson.fromJson(stringComment.toString(), ThreadResponse.class);

        for (int i = 0; i < comment.getData().getChildrenList().size(); i++) {
            ThreadResponse.Data.Children.ChildrenData childrenData = 
                    comment.getData().getChildrenList().get(i).getChildrenData();
            
            Threads singleThread = new Threads();
            
            /*String commentAuthor = comment.getData().getChildrenList().get(i).getChildrenData().getAuthor();
            int commentScore = comment.getData().getChildrenList().get(i).getChildrenData().getScore();
            String commentBody = comment.getData().getChildrenList().get(i).getChildrenData().getBody();
            JsonElement commentReplies = comment.getData().getChildrenList().get(i).getChildrenData().getReplies();
            int commentDepth = comment.getData().getChildrenList().get(i).getChildrenData().getDepth();
            String commentId = comment.getData().getChildrenList().get(i).getChildrenData().getId();
            String commentKind = comment.getData().getChildrenList().get(i).getKind();
            int commentTime = comment.getData().getChildrenList().get(i).getChildrenData().getTime();

            if(comment.getData().getChildrenList().get(i).getKind().equals("t1")) {
                addThread.add(new Threads(commentAuthor, commentScore, commentBody, commentReplies, depth + commentDepth,
                        commentId, commentKind, commentTime));*/
            if(comment.getData().getChildrenList().get(i).getKind().equals("t1")){
                Log.i("aaa", "inserting t1 stuff");
                singleThread.setAuthor(childrenData.getAuthor());
                singleThread.setScore(childrenData.getScore());
                singleThread.setBody(childrenData.getBody());
                singleThread.setReplies(childrenData.getReplies());
                singleThread.setDepth(childrenData.getDepth() + depth);
                singleThread.setId(childrenData.getId());
                singleThread.setKind(comment.getData().getChildrenList().get(i).getKind());
                singleThread.setTime(childrenData.getTime());
                
                addThread.add(singleThread);
            }else if(comment.getData().getChildrenList().get(i).getKind().equals("more")){
                Log.i("aaa", "inserting more stuff");
                /*List<String> commentMore = comment.getData().getChildrenList().get(i).getChildrenData().getMoreChildren();
                String commentParent = comment.getData().getChildrenList().get(i).getChildrenData().getParentId();
                String commentKindMore = comment.getData().getChildrenList().get(i).getKind();
                int commentMoreDepth = comment.getData().getChildrenList().get(i).getChildrenData().getDepth();
                String commentMoreId = comment.getData().getChildrenList().get(i).getChildrenData().getId();

                threads.add(new Threads(commentMore, commentParent, commentKindMore, depth + commentMoreDepth,
                        commentMoreId));*/

                singleThread.setMoreChildren(childrenData.getMoreChildren());
                singleThread.setParentId(childrenData.getParentId());
                singleThread.setKind(comment.getData().getChildrenList().get(i).getKind());
                singleThread.setDepth(childrenData.getDepth() + depth);
                singleThread.setId(childrenData.getId());

                addThread.add(singleThread);
            }
            commentRepliesParse(singleThread.getReplies(), depth);
        }
    }

    private String makePermalink(String stringPermalink){
        String returnString = "https://www.reddit.com" + stringPermalink + ".json";
        return returnString;
    }

    private String makePermalink(String stringPermalink, String id){
        Log.i("aaa", "https://www.reddit.com" + stringPermalink + id + "/.json");
        return "https://www.reddit.com" + stringPermalink + id + "/.json";
    }

    private void get(String stringPermalink) {
        if(!adapter.isEmpty()) adapter.clear();
        progressBarSub.setVisibility(View.VISIBLE);
        Log.i("aaa", "sending get request subactivity");
        client.get(makePermalink(stringPermalink), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    postObject = response.getJSONObject(0);
                    commentObject = response.getJSONObject(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();

                //mapping the value of postObject to threads dataholder
                ThreadResponse post = gson.fromJson(postObject.toString(), ThreadResponse.class);
                JsonElement comment = gson.fromJson(commentObject.toString(), JsonElement.class);

                //POST
                //i can actually use the PostResponse class instead of this
                String postTitle = post.getData().getChildrenList().get(0).getChildrenData().getTitle();
                String postAuthor = post.getData().getChildrenList().get(0).getChildrenData().getAuthor();
                int postScore = post.getData().getChildrenList().get(0).getChildrenData().getScore();
                String postSelftext = post.getData().getChildrenList().get(0).getChildrenData().getSelftext();
                String postUrl = post.getData().getChildrenList().get(0).getChildrenData().getUrl();
                int postCreated = post.getData().getChildrenList().get(0).getChildrenData().getTime();

                threads.add(new Threads(postAuthor, postTitle, postSelftext, postScore, postUrl, postCreated));

                //comments
                commentRepliesParse(comment);

                progressBarSub.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error getting data from: " + responseString);
                Log.i(TAG, "Status code: " + statusCode);
                Toast.makeText(SubActivity.this, "Error loading post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //the id parameter is the id we want to get
    private void getMoreComment(final Threads singleThread, String id, final int position){
        //minus one because the first child was deleted
        //not subtracting 1 will skip the depth by 1
        final int moreDepth = singleThread.getDepth();
        final int continueThreadDepth = singleThread.getDepth() - 1;

        client.get(makePermalink(permalink, id), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    moreCommentObject = response.getJSONObject(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                JsonElement moreComment = gson.fromJson(moreCommentObject.toString(), JsonElement.class);

                if(singleThread.getId().equals("_")) {
                    commentRepliesParse(moreComment, continueThreadDepth);
                }else {
                    commentRepliesParse(moreComment, moreDepth);
                }
                //removing the first comment so it will not duplicate
                if(singleThread.getId().equals("_")) addThread.remove(0);
                threads.addAll(position, addThread);
                addThread.clear();

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error getting data from: " + responseString);
                Log.i(TAG, "Status code: " + statusCode);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sub_refresh:
                get(permalink);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
