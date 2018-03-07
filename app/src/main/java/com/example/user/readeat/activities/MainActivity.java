package com.example.user.readeat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.readeat.R;
import com.example.user.readeat.dataholder.Subreddits;
import com.example.user.readeat.adapters.ListViewAdapter;
import com.example.user.readeat.responses.PostListResponse;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity{

    private static final String BASE_URL = "https://www.reddit.com";
    private static final String TAG = MainActivity.class.getName();
    private static final int URL_SORT_HOT = 1;
    private static final int URL_SORT_NEW = 2;
    private static final int URL_SORT_RISING = 3;
    private static final int URL_SORT_TOP = 4;
    private static final int URL_SORT_CONTROVERSIAL = 5;
    private static final int URL_SORT_GILDED = 6;
    public static final int THREAD_GET = 1;
    public static final int THREAD_GET_SORT = 2;
    public static final int THREAD_GET_SCROLL = 3;

    private static AsyncHttpClient client;
    private String url = "";
    private List<Subreddits> subreddits;
    private ListView listView;
    private ListViewAdapter adapter;
    private JSONArray jsonArray;
    private TextView noData;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String afterPost, beforePost;
    private boolean isGettingData = false;
    private TextView subredditName, subredditSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subreddits = new ArrayList<Subreddits>();
        client = new AsyncHttpClient();
        noData = (TextView)findViewById(R.id.noDataText);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ListViewAdapter(MainActivity.this, subreddits);
        listView.setAdapter(adapter);
        listView.setEmptyView(noData);
        noData.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBarMain);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        subredditName = (TextView)findViewById(R.id.subredditNameText);
        subredditSort = (TextView)findViewById(R.id.subredditSort);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
        get(url, null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                Subreddits singleThread = subreddits.get(position);
                intent.putExtra("permalink", singleThread.getPermalink());
                noData.setVisibility(View.GONE);
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                this.currentScrollState = scrollState;

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
                /*Log.i(TAG, "first: " + firstVisibleItem + " visible: " + visibleItemCount +
                        " total: " + totalItemCount);*/
                loadMore();
            }

            private void loadMore(){
                if(currentVisibleItemCount + currentFirstVisibleItem >= totalItem - 10 &&
                        !isGettingData && currentFirstVisibleItem != 0){
                    isGettingData = true;
                    Log.i(TAG, "Loading more comment");

                    RequestParams params = new RequestParams();
                    params.put("after", afterPost);
                    Log.i("aaa", "" + params);
                    get(params);
                }
            }
        });
    }

    private String makeUrl(String input){
        if(input.equals("")) return BASE_URL + url + "/.json";
        return BASE_URL + "/r/" + url + "/.json";
    }

    private String makeSortUrl(int sort){
        String urlReturn = "";
        if(url.equals("")){
            switch(sort){
                case URL_SORT_HOT:
                    urlReturn = BASE_URL +"/hot/.json";
                    break;
                case URL_SORT_NEW:
                    urlReturn = BASE_URL + "/new/.json";
                    break;
                case URL_SORT_RISING:
                    urlReturn = BASE_URL + "/rising/.json";
                    break;
                case URL_SORT_TOP:
                    urlReturn = BASE_URL + "/top/.json";
                    break;
                case URL_SORT_CONTROVERSIAL:
                    urlReturn = BASE_URL + "/controversial/.json";
                    break;
                case URL_SORT_GILDED:
                    urlReturn = BASE_URL + "/gilded/.json";
                    break;
            }
        }
        else{
            switch(sort){
                case URL_SORT_HOT:
                    urlReturn = BASE_URL + "/r/" + url + "/hot/.json";
                    break;
                case URL_SORT_NEW:
                    urlReturn = BASE_URL + "/r/" + url + "/new/.json";
                    break;
                case URL_SORT_RISING:
                    urlReturn = BASE_URL + "/r/" + url + "/rising/.json";
                    break;
                case URL_SORT_TOP:
                    urlReturn = BASE_URL + "/r/" + url + "/top/.json";
                    break;
                case URL_SORT_CONTROVERSIAL:
                    urlReturn = BASE_URL + "/r/" + url + "/controversial/.json";
                    break;
                case URL_SORT_GILDED:
                    urlReturn = BASE_URL + "/r/" + url + "/gilded/.json";
                    break;
            }
        }
        return urlReturn;
    }

    //testing shit right here
   /* private void get(int whatToGet, String stringUrl, @Nullable RequestParams params, int sort){
        progressBar.setVisibility(View.VISIBLE);
        if(!adapter.isEmpty()) adapter.clear();
        noData.setVisibility(View.GONE);
        String query = null;

        if(whatToGet == THREAD_GET) query = makeUrl(stringUrl);
        else if(whatToGet == THREAD_GET_SORT) query = makeSortUrl(sort);
        else if(whatToGet == THREAD_GET_SCROLL) query = makeUrl(stringUrl);

        client.get(query, params ,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error retrieving data: " + statusCode);
                Log.i(TAG, "" + responseString);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Gson gson = new Gson();
                PostListResponse postListResponse = gson.fromJson(responseString, PostListResponse.class);

                for(int i = 0; i < postListResponse.getData().getChildrenList().size(); i++) {
                    //make it shorter
                    PostListResponse.Data.Children.ChildrenData childrenData =
                            postListResponse.getData().getChildrenList().get(i).getChildrenData();

                    Subreddits singleThread = new Subreddits();
                    singleThread.setTitle(childrenData.getTitle());
                    singleThread.setAuthor(childrenData.getAuthor());
                    singleThread.setScore(childrenData.getScore());
                    singleThread.setPermalink(childrenData.getPermalink());
                    singleThread.setComment(childrenData.getNum_comments());
                    singleThread.setSubredditName(childrenData.getSubredditName());
                    singleThread.setTime(childrenData.getTime());
                    singleThread.setThumbnail(childrenData.getThumbnail());
                    singleThread.setUrl(childrenData.getUrl());
                    singleThread.setSelf(childrenData.isSelf());
                    singleThread.setPostHint(childrenData.getPostHint());

                    subreddits.add(singleThread);
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }*/



    //get subreddit thread
    private void get(String StringUrl, RequestParams params){
        progressBar.setVisibility(View.VISIBLE);
        if(!adapter.isEmpty()) adapter.clear();
        noData.setVisibility(View.GONE);
        Log.i(TAG, "Sending GET request to: " + makeUrl(StringUrl));

        client.get(makeUrl(StringUrl), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error retrieving data: " + statusCode);
                Log.i(TAG, "" + responseString);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                PostListResponse postListResponse = gson.fromJson(responseString, PostListResponse.class);
                afterPost = postListResponse.getData().getAfter();

                for(int i = 0; i < postListResponse.getData().getChildrenList().size(); i++) {
                    PostListResponse.Data.Children.ChildrenData childrenData =
                            postListResponse.getData().getChildrenList().get(i).getChildrenData();

                    Subreddits singleThread = new Subreddits();
                    singleThread.setTitle(childrenData.getTitle());
                    singleThread.setAuthor(childrenData.getAuthor());
                    singleThread.setScore(childrenData.getScore());
                    singleThread.setPermalink(childrenData.getPermalink());
                    singleThread.setComment(childrenData.getNum_comments());
                    singleThread.setSubredditName(childrenData.getSubredditName());
                    singleThread.setTime(childrenData.getTime());
                    singleThread.setThumbnail(childrenData.getThumbnail());
                    singleThread.setUrl(childrenData.getUrl());
                    singleThread.setSelf(childrenData.isSelf());
                    singleThread.setPostHint(childrenData.getPostHint());

                    subreddits.add(singleThread);
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //sorting subreddit thread
    private void get(int sort){
        progressBar.setVisibility(View.VISIBLE);
        if(!adapter.isEmpty()) adapter.clear();
        noData.setVisibility(View.GONE);
        Log.i(TAG, "Sending GET request to: " + makeSortUrl(sort));

        client.get(makeSortUrl(sort), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error retrieving data: " + statusCode);
                Log.i(TAG, "" + responseString);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Gson gson = new Gson();
                PostListResponse postListResponse = gson.fromJson(responseString, PostListResponse.class);

                for(int i = 0; i < postListResponse.getData().getChildrenList().size(); i++) {
                    //make it shorter
                    PostListResponse.Data.Children.ChildrenData childrenData =
                            postListResponse.getData().getChildrenList().get(i).getChildrenData();

                    Subreddits singleThread = new Subreddits();
                    singleThread.setTitle(childrenData.getTitle());
                    singleThread.setAuthor(childrenData.getAuthor());
                    singleThread.setScore(childrenData.getScore());
                    singleThread.setPermalink(childrenData.getPermalink());
                    singleThread.setComment(childrenData.getNum_comments());
                    singleThread.setSubredditName(childrenData.getSubredditName());
                    singleThread.setTime(childrenData.getTime());
                    singleThread.setThumbnail(childrenData.getThumbnail());
                    singleThread.setUrl(childrenData.getUrl());
                    singleThread.setSelf(childrenData.isSelf());
                    singleThread.setPostHint(childrenData.getPostHint());

                    subreddits.add(singleThread);
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //getting more subreddit post by scrolling listView
    private void get(final RequestParams params){
        client.get(makeUrl(url), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Error retrieving data: " + statusCode);
                Log.i(TAG, "" + responseString);
                Toast.makeText(MainActivity.this, "Error loading data, trying to load again...", Toast.LENGTH_SHORT).show();
                isGettingData = false;

                get(params);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                PostListResponse postListResponse = gson.fromJson(responseString, PostListResponse.class);
                afterPost = postListResponse.getData().getAfter();

                for(int i = 0; i < postListResponse.getData().getChildrenList().size(); i++) {
                    PostListResponse.Data.Children.ChildrenData childrenData =
                            postListResponse.getData().getChildrenList().get(i).getChildrenData();

                    Subreddits singleThread = new Subreddits();
                    singleThread.setTitle(childrenData.getTitle());
                    singleThread.setAuthor(childrenData.getAuthor());
                    singleThread.setScore(childrenData.getScore());
                    singleThread.setPermalink(childrenData.getPermalink());
                    singleThread.setComment(childrenData.getNum_comments());
                    singleThread.setSubredditName(childrenData.getSubredditName());
                    singleThread.setTime(childrenData.getTime());
                    singleThread.setThumbnail(childrenData.getThumbnail());
                    singleThread.setUrl(childrenData.getUrl());
                    singleThread.setSelf(childrenData.isSelf());
                    singleThread.setPostHint(childrenData.getPostHint());

                    subreddits.add(singleThread);
                }
                adapter.notifyDataSetChanged();
                isGettingData = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;

        switch(item.getItemId()){
            case R.id.action_main_refresh:
                //get(url, null);
                get(url, null);
                return true;
            case R.id.action_main_search:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText dialogText = new EditText(this);
                alert.setView(dialogText);
                alert.setTitle("Search subreddit");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        url = dialogText.getText().toString();
                        noData.setVisibility(View.GONE);
                        if(url.equals("") || url == null){
                            Toast.makeText(MainActivity.this, "Please insert valid subreddit", Toast.LENGTH_SHORT).show();
                        }else{
                            subredditName.setText(url);
                            get(url, null);
                        }
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return true;

            case R.id.action_sort_hot:
                get(URL_SORT_HOT);
                subredditSort.setText(R.string.sort_hot);
                return true;
            case R.id.action_sort_new:
                get(URL_SORT_NEW);
                subredditSort.setText(R.string.sort_new);
                return true;
            case R.id.action_sort_rising:
                get(URL_SORT_RISING);
                subredditSort.setText(R.string.sort_rising);
                return true;
            case R.id.action_sort_top:
                get(URL_SORT_TOP);
                subredditSort.setText(R.string.sort_top);
                return true;
            case R.id.action_sort_controversial:
                get(URL_SORT_CONTROVERSIAL);
                subredditSort.setText(R.string.sort_controversial);
                return true;
            /*case R.id.action_sort_gilded:
                get(url, null, URL_SORT_GILDED);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*void setupSpinner(){
        *//*get instance of spinner
          set spinnerArrayAdapter, contains the subreddit name
          store the adapter using sharedpreference, this means everytime the app loads, app must get arrayAdapter from sharedpreference

          public void onItemSelected(...){
          String selection = (String) parent.getItemAtPosition(position);

          url = selection;
          get(url, null);
          yeah man something like that
          }
         *//*
        final ArrayList<String> subredditList = new ArrayList<String>();
        Spinner subredditSpinner = (Spinner)findViewById(R.id.subredditSpinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, subredditList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        HashMap<String, String> keys = new HashMap<String, String>() ;

        subredditSpinner.setAdapter(spinnerAdapter);
        //todo something something sharedpreference
        preferences = getSharedPreferences("subreddit_list", Context.MODE_PRIVATE);
        editor = preferences.edit();
        *//*subredditList.add("askreddit");
        subredditList.add("dota2");

        for (int i = 0; i < subredditList.size(); i++) {
            editor.putString(subredditList.get(i), subredditList.get(i));
        }*//*
        editor.clear();
        editor.putString("list_1", "askreddit");
        editor.putString("list_1", "dota2");
        editor.putString("list_2", "aww");
        editor.putString("list_3", "gifs");
        editor.putString("list_4", "pics");
        *//*for (int i = 0; i < 2; i++) {
            subredditList.add(preferences.getString("list_" + i, ""));
            Log.i(TAG, "subreddit added: " + subredditList.get(i));
        }*//*
        //todo use the bookmarked thingy here. you just need to store eveyrthing
        //todo and then everything will be get and stored to the subredditlist
        //todo oncreate get all value, on destroy put 10 value inside

        *//*for(Map.Entry<String,?> entry : keys.entrySet()){
            if (entry.getValue() instanceof String) {
                subredditList.add((String) entry.getValue());
            }
        }*//*

        Map<String, ?> allEntries = preferences.getAll();
        for(Map.Entry<String, ?> entry : allEntries.entrySet()){
            Log.i("aaa", "yeay key value: " + entry.getValue());
            subredditList.add((String) entry.getValue());
        }

        *//*subredditList.add("askreddit");
        subredditList.add("aww");
        subredditList.add("introvert");
        subredditList.add("socialskills");
        subredditList.add("dota2");
        subredditList.add("pics");
        subredditList.add("gifs");*//*



        //subredditList.addAll((ArrayList<String>) preferences.getAll());
        *//*todo how to use the sharedpreference:
          fill the sharedpreference (7-10 is enough)
          on this method, get the sharedpreference data to the arraylist
          so after finished loading, the spinner will be filled with SHIT

        *//*

        spinnerAdapter.notifyDataSetChanged();
        *//*subredditSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if click, get string, get(url, null);
            }
        });*//*
        subredditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                url = subredditList.get(position);
                get(url, null);
                Log.i("aaa", "going to: " + url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/
}
