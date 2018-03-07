package com.example.user.readeat.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.readeat.R;
import com.example.user.readeat.Utils;
import com.example.user.readeat.activities.MainActivity;
import com.example.user.readeat.activities.ViewImageActivity;
import com.example.user.readeat.dataholder.Subreddits;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Subreddits>{
    Subreddits subreddits;

    public ListViewAdapter(Context context, List<Subreddits> objects) {
        super(context, R.layout.subreddit_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //position is the position of the item within the adapter's data set of the item whose view we want, something like index

        //convertView is a view that gets called / created when there is still space left on the screen. If you have
        //no space left in the screen to create a view, then it will be null

        //is the view which contained the item's view which getView() generates. Normally it is ListView or Gallery


        //inflate means getting the view object from xml file, in this case, the subreddit_item
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        //get the xml file from subreddit_item and turn it into a view
        View customView = myInflater.inflate(R.layout.subreddit_item, parent, false);

        //get the index of the from the list to get the data
        subreddits = (Subreddits) getItem(position);

        TextView postTitle = (TextView)customView.findViewById(R.id.subredditPostTitle);
        TextView postAuthor = (TextView)customView.findViewById(R.id.subredditPostAuthor);
        TextView postScore = (TextView)customView.findViewById(R.id.subredditPostScore);
        TextView postComments = (TextView)customView.findViewById(R.id.subredditPostComments);
        final TextView subredditName = (TextView)customView.findViewById(R.id.subredditPostName);
        TextView postTime = (TextView)customView.findViewById(R.id.subredditPostTime);
        final ImageView postThumbnail = (ImageView)customView.findViewById(R.id.threadThumbnail);
        postThumbnail.setTag(R.string.tagUrl, subreddits.getUrl());
        postThumbnail.setTag(R.string.tagPostHint, subreddits.getPostHint());

        //fixme thumbnail also can be null, doens't show anything
        if(subreddits.isSelf() || subreddits.getThumbnail() == null){
            //selftext
            postThumbnail.setImageResource(R.drawable.ic_text_format);
            postThumbnail.setClickable(false);
        }else if(subreddits.getPostHint() == null){
            //if there is no post hint, could be image, video, selftext, anything.
            postThumbnail.setImageResource(R.drawable.ic_link_black);
            postThumbnail.setClickable(false);
        }else if(subreddits.getPostHint().equals("link") && subreddits.getThumbnail().equals("default")){
            //links
            postThumbnail.setImageResource(R.drawable.ic_link_black);
        }else if(subreddits.getPostHint().equals("link") && !subreddits.getThumbnail().equals("default")){
            //links with image attached
            Picasso.with(getContext())
                    .load(subreddits.getThumbnail())
                    .into(postThumbnail);
        }else if(subreddits.getThumbnail().equals("nsfw")){
            //nsfw
            Picasso.with(getContext())
                    .load(subreddits.getThumbnail())
                    .into(postThumbnail);
        }else {
            //image or gif
            Picasso.with(getContext())
                    .load(subreddits.getThumbnail())
                    .into(postThumbnail);
        }


        /*if(subreddits.isSelf()){
            postThumbnail.setImageResource(R.drawable.ic_text_format);
        }else if(!subreddits.isSelf() && subreddits.getThumbnail().equals("default")){
            postThumbnail.setImageResource(R.drawable.ic_link_black);
        }else{
            Picasso.with(getContext())
                    .load(subreddits.getThumbnail())
                    .into(postThumbnail);
        }*/

        postThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagUrl = (String)postThumbnail.getTag(R.string.tagUrl);
                String postHint = (String)postThumbnail.getTag(R.string.tagPostHint);

                if(postHint == null){
                    //do nothing
                }else if(postHint.equals("link") && !tagUrl.contains(".gif")){
                    //opens the link
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tagUrl));
                    getContext().startActivity(browserIntent);
                }else{
                    //get url, start image activity
                    Intent imageIntent = new Intent(getContext(), ViewImageActivity.class);
                    imageIntent.putExtra("imageLink", tagUrl);
                    Log.i("aaa", "sending image: " + tagUrl);
                    getContext().startActivity(imageIntent);
                }
            }
        });

        postTitle.setText(subreddits.getTitle());
        postAuthor.setText(subreddits.getAuthor());
        postScore.setText(String.valueOf(subreddits.getScore() + " points"));
        postComments.setText(String.valueOf(subreddits.getComment() + " comments"));
        subredditName.setText(subreddits.getSubredditName());
        //counting the time
        /*long unixTime = System.currentTimeMillis() / 1000L;
        long created = subreddits.getTime();

        int createdSeconds = (int)(unixTime - created);
        if(createdSeconds > 86400){
            //days ago
            postTime.setText(String.valueOf(((unixTime - created) / 86400) + " days ago"));
        }
        else if(createdSeconds > 3600) {
            //hours ago
            postTime.setText(String.valueOf(((unixTime - created) / 3600) + " hours ago"));
        }else{
            //minutes ago
            postTime.setText(String.valueOf(((unixTime - created) / 60) + " minutes ago"));
        }*/

        String stringTime = Utils.utcToReadableTime(subreddits.getTime());
        postTime.setText(stringTime);
        if(stringTime.contains("minutes ago")){
            postScore.setText(R.string.score_hidden);
        }else{
            postScore.setText(subreddits.getScore() + " points");
        }

        //interpunct ・
        //the single row, with the information included, ready to populate the listView
        return customView;
    }
}
