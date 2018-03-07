package com.example.user.readeat.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.user.readeat.R;
import com.example.user.readeat.Utils;
import com.example.user.readeat.activities.ViewImageActivity;
import com.example.user.readeat.dataholder.Threads;
//import com.github.chrisbanes.photoview.PhotoView;

import org.w3c.dom.Text;

import java.util.List;

public class CommentListViewAdapter extends ArrayAdapter<Threads>{
    private View customViewComment;
    Bitmap imageBitmap;
    BitmapDrawable drawable;
    ImageView postImage;
    String imageLink;

    public static final String TAG = CommentListViewAdapter.class.getName();

    public CommentListViewAdapter(Context context, List<Threads> objects) {
        super(context, R.layout.thread_post, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Threads threads = (Threads) getItem(position);

        if(position == 0){
            LayoutInflater myInflater = LayoutInflater.from(getContext());
            View customViewPost = myInflater.inflate(R.layout.thread_post, parent, false);

            TextView postTitle = (TextView) customViewPost.findViewById(R.id.postTitle);
            TextView postAuthor = (TextView) customViewPost.findViewById(R.id.postAuthor);
            TextView postScore = (TextView) customViewPost.findViewById(R.id.postScore);
            TextView postSelfText = (TextView) customViewPost.findViewById(R.id.postSelfText);
            postImage = (ImageView) customViewPost.findViewById(R.id.postImage);
            TextView postTime = (TextView) customViewPost.findViewById(R.id.threadPostTime);

            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ViewImageActivity.class);
                    intent.putExtra("imageLink", imageLink);
                    Log.i("aaa", "image link is: " + imageLink);
                    getContext().startActivity(intent);
                }
            });

            postTitle.setText(threads.getTitle());
            postAuthor.setText(threads.getAuthor());
            String stringTime = Utils.utcToReadableTime(threads.getTime());
            postTime.setText(stringTime);
            postScore.setText(String.valueOf(threads.getScore()) + " points");
            postSelfText.setVisibility(View.GONE);
            postImage.setVisibility(View.GONE);

            String spanString = threads.getSelftext();
            if(spanString == null) {
                //IMAGE LINK


                if(threads.getUrl().contains(".jpg") ||
                        threads.getUrl().contains(".png") ||
                        threads.getUrl().contains(".gif") ||
                        threads.getUrl().contains(".gifv") ||
                        threads.getUrl().contains(".mp4") ||
                        threads.getUrl().contains("gfycat")){
                    imageLink = "";
                    //postImage.setVisibility(View.VISIBLE);
                    try {
                        imageLink = threads.getUrl();
                    } catch (NullPointerException e) {
                        Log.e(TAG, "NO IMAGE LINK" + e);
                    }



                    Log.i(TAG, "Image link is: " + imageLink);
                    if (imageLink.contains(".jpg") || imageLink.contains(".png")) {
                        //image
                        Glide.with(getContext())
                                .load(imageLink)
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .placeholder(android.R.color.transparent)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(postImage);
                        postImage.setVisibility(View.VISIBLE);
                    } else {
                        //GIF
                        if (imageLink.contains(".gifv")) {
                            //imgur or some shit
                            int vIndex = imageLink.lastIndexOf('v');
                            imageLink = imageLink.substring(0, vIndex);
                            //imageLink = "https://media.giphy.com/media/8EzqYFdwMSzPa/giphy.gif";
                        }else if(imageLink.contains("gfycat")){
                            //gyfcat
                            //+1 because I dont wanna incldue the / char
                            String parameter = imageLink.substring(imageLink.lastIndexOf("/") + 1);
                            imageLink = "https://giant.gfycat.com/" + parameter + ".gif";
                            Log.i(TAG, "gyfcat gif link: " + imageLink);
                        }

                        Log.i(TAG, "Loading gif... " + imageLink);
                        postImage.setVisibility(View.VISIBLE);
                        Glide.with(getContext())
                                .load(imageLink)
                                .asGif()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                //.crossFade()
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.refresh)
                                .into(postImage);

                    }
                }else{
                    //LINK BUT NOT IMAGE
                    //for example twitter
                }
            }
            else {
                //if text post

                postSelfText.setVisibility(View.VISIBLE);
                spanString = spanString.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

                //fromhtml appends \n\n after </p>
                //so after get spanned using fromhtml, we trim it
                CharSequence trimmed = trim(Html.fromHtml(spanString));
                postSelfText.setText(trimmed);
                //postSelfText.setMovementMethod(LinkMovementMethod.getInstance());
                //postSelfText.setMovementMethod(LinkMovementMethod.getInstance().onTouchEvent(postSelfText, spanString, null));
                //get selftext spannable, start activity
                URLSpan[] urlSpan = postSelfText.getUrls();
                for(URLSpan span : urlSpan) {
                    Log.i(TAG, span.getURL());
                }
            }

            customViewPost.setEnabled(false);
            customViewPost.setOnClickListener(null);

            return customViewPost;
        }else{
            LayoutInflater myInflater = LayoutInflater.from(getContext());

            if(threads.getKind().equals("t1")) {
                //comment
                customViewComment = myInflater.inflate(R.layout.thread_comment, parent, false);
                TextView commentAuthor = (TextView) customViewComment.findViewById(R.id.commentAuthor);
                TextView commentScore = (TextView) customViewComment.findViewById(R.id.commentScore);
                TextView commentBody = (TextView) customViewComment.findViewById(R.id.commentBody);
                TextView commentBackground = (TextView) customViewComment.findViewById(R.id.colorBackgroundText);
                TextView commentTime = (TextView) customViewComment.findViewById(R.id.commentTime);

                //todo, find op manually, by comparing it to post authhor
                commentAuthor.setText(threads.getAuthor());

                /*int createdSeconds = setTimeView(threads, commentTime);
                if(createdSeconds > 3600) {
                    commentScore.setText(threads.getScore() + " points");
                }else{
                    commentScore.setText(R.string.score_hidden);
                }*/

                String stringTime = Utils.utcToReadableTime(threads.getTime());
                commentTime.setText(stringTime);
                if(stringTime.contains("minutes ago")){
                    commentScore.setText(R.string.score_hidden);
                }else{
                    commentScore.setText(threads.getScore() + " points");
                }

                String spanString = threads.getBody();
                if (spanString == null) spanString = "";
                spanString = spanString.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
                        .replace("/u/", "readeat://www.reddit.com/user/")
                        .replace("&#39;", "'");
                CharSequence trimmed = trim(Html.fromHtml(spanString));
                commentBody.setText(trimmed);
                commentBody.setMovementMethod(LinkMovementMethod.getInstance());
                /*URLSpan[] urlSpan = commentBody.getUrls();
                //commentBody.getMovementMethod();
                for(URLSpan span : urlSpan) {
                    Log.i(TAG, span.getURL());
                }*/
                //commentBody.getMovementMethod();

                int depth = threads.getDepth();
                Resources r = getContext().getResources();
                float density = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
                float depthDip = (depth - 1) * density;

                //set color
                if(depth%4 == 0)commentBackground.setBackgroundColor(Color.parseColor("#ff9694"));
                else if(depth%4 == 1)commentBackground.setBackgroundColor(Color.parseColor("#94fdff"));
                else if(depth%4 == 2)commentBackground.setBackgroundColor(Color.parseColor("#9694ff"));
                else if(depth%4 == 3)commentBackground.setBackgroundColor(Color.parseColor("#fdff94"));

                if (depth == 0) commentBackground.setVisibility(View.GONE);
                else {
                    commentBackground.setVisibility(View.VISIBLE);
                    customViewComment.setPadding((int) depthDip, 0, 0, 0);
                }

            }else if(threads.getKind().equals("more") && !threads.getId().equals("_")){
                //more comment
                customViewComment = myInflater.inflate(R.layout.more_comment, parent, false);
                int depth = threads.getDepth();
                Resources r = getContext().getResources();
                float density = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
                float depthDip = (depth - 1) * density;
                TextView moreCommentBackground = (TextView)customViewComment.findViewById(R.id.moreCommentBackground);

                //set color
                if(depth%4 == 0)moreCommentBackground.setBackgroundColor(Color.parseColor("#ff9694"));
                else if(depth%4 == 1)moreCommentBackground.setBackgroundColor(Color.parseColor("#94fdff"));
                else if(depth%4 == 2)moreCommentBackground.setBackgroundColor(Color.parseColor("#9694ff"));
                else if(depth%4 == 3)moreCommentBackground.setBackgroundColor(Color.parseColor("#fdff94"));

                customViewComment.setPadding((int) depthDip, 0, 0, 0);

            }else if(threads.getKind().equals("more") && threads.getId().equals("_")){
                //click to continue comment
                customViewComment = myInflater.inflate(R.layout.more_comment, parent, false);
                int depth = threads.getDepth();
                Resources r = getContext().getResources();
                float density = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
                float depthDip = (depth - 1) * density;

                TextView moreCommentBackground = (TextView)customViewComment.findViewById(R.id.moreCommentBackground);
                TextView moreComment = (TextView)customViewComment.findViewById(R.id.moreComment);

                //set color
                if(depth%4 == 0)moreCommentBackground.setBackgroundColor(Color.parseColor("#ff9694"));
                else if(depth%4 == 1)moreCommentBackground.setBackgroundColor(Color.parseColor("#94fdff"));
                else if(depth%4 == 2)moreCommentBackground.setBackgroundColor(Color.parseColor("#9694ff"));
                else if(depth%4 == 3)moreCommentBackground.setBackgroundColor(Color.parseColor("#fdff94"));

                moreComment.setText(R.string.continue_thread);
                Log.i("aaa", "continue thread item");
                customViewComment.setPadding((int) depthDip, 0, 0, 0);
            }
        }
        return customViewComment;
    }

    private CharSequence trim(CharSequence s) {
        int start = 0;
        int end = s.length();
        //it goes from s[0] until it finds a whitespace
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        //it goes from s[end-1] until it finds a whitespace
        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }

    private int setTimeView(Threads threads, TextView timeView){
        long unixTime = System.currentTimeMillis() / 1000L;
        long created = threads.getTime();

        int createdSeconds = (int)(unixTime - created);
        if(createdSeconds > 86400){
            //days ago
            timeView.setText(String.valueOf(((unixTime - created) / 86400) + " days ago"));
        }
        else if(createdSeconds > 3600) {
            //hours ago
            timeView.setText(String.valueOf(((unixTime - created) / 3600) + " hours ago"));
        }else{
            //minutes ago
            timeView.setText(String.valueOf(((unixTime - created) / 60) + " minutes ago"));
        }
        return createdSeconds;
    }
}
