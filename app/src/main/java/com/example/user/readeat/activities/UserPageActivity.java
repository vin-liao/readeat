package com.example.user.readeat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.readeat.R;
import com.example.user.readeat.Utils;
import com.example.user.readeat.dataholder.Users;
import com.example.user.readeat.responses.UserDataResponse;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class UserPageActivity extends AppCompatActivity {
    public static final String TAG = UserPageActivity.class.getName();
    private TextView createdText;
    private TextView linkKarmaText;
    private TextView commentKarmaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        final String username = getIntent().getStringExtra("username");
        AsyncHttpClient client = new AsyncHttpClient();

        createdText = (TextView)findViewById(R.id.createdText);
        linkKarmaText = (TextView)findViewById(R.id.linkKarmaText);
        commentKarmaText = (TextView)findViewById(R.id.commentKarmaText);

        Toolbar toolbar = (Toolbar)findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView usernameText = (TextView)findViewById(R.id.usernameText);
        usernameText.setText(username);

        client.get(makeUrl(username), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(UserPageActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error loading user data");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                UserDataResponse userResponse =  gson.fromJson(responseString, UserDataResponse.class);

                /*users.setUsername(userResponse.getData().getUsername());
                users.setCommentKarma(userResponse.getData().getCommentKarma());
                users.setLinkKarma(userResponse.getData().getLinkKarma());
                users.setCreatedUtc(userResponse.getData().getCreatedUtc());*/

                linkKarmaText.setText("User link karma: " + userResponse.getData().getLinkKarma());
                commentKarmaText.setText("User comment karma: " + userResponse.getData().getCommentKarma());

                //created utc
                String stringTime = Utils.utcToReadableTime(userResponse.getData().getCreatedUtc());
                createdText.setText(stringTime);
            }
        });

        //get extra intent
        //asynchttpclient get user data
        //parse it



    }
    private String makeUrl(String username){
        return "https://www.reddit.com/user/" + username + "/about.json";
    }
}
