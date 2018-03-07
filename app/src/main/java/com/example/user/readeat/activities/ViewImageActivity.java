package com.example.user.readeat.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.user.readeat.R;
import com.github.chrisbanes.photoview.PhotoView;
//import com.github.chrisbanes.photoview.PhotoView;

public class ViewImageActivity extends AppCompatActivity {
    public static final String TAG = ViewImageActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        String imageLink = getIntent().getStringExtra("imageLink");
        if (imageLink.contains(".gifv")) {
            int vIndex = imageLink.lastIndexOf('v');
            imageLink = imageLink.substring(0, vIndex);
        }else if(imageLink.contains("gfycat")){
            //gyfcat
            //+1 because I dont wanna incldue the / char
            String parameter = imageLink.substring(imageLink.lastIndexOf("/") + 1);
            imageLink = "https://giant.gfycat.com/" + parameter + ".gif";
        }

        Log.i(TAG, "imagelink in imageViewactivity: " + imageLink);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(ViewImageActivity.this)
                .load(imageLink)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(android.R.color.transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);
    }
}
