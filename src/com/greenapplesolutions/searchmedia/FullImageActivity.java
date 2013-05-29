package com.greenapplesolutions.searchmedia;

import adapter.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import domain.SearchResult;
import util.DataMessenger;

import java.util.ArrayList;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");

        SearchResult result = (SearchResult) DataMessenger.Get("result");
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        imageView.setImageBitmap(result.imagedrawables.get(position));
    }

}

