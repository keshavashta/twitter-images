package com.greenapplesolutions.searchmedia;

/**
 * Created with IntelliJ IDEA.
 * User: saxena.arunesh
 * Date: 5/27/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import domain.SearchResult;
import util.DataMessenger;

import java.util.ArrayList;

public class GalleryActivity extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
    private SearchResult searchResult;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.full_image_view);
        // get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        this.position = position;
        this.searchResult = (SearchResult) DataMessenger.Get("result");
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));

        Gallery g = (Gallery) findViewById(R.id.gallery);

        g.setAdapter(new ImageAdapter(this, searchResult.imagedrawables));

        g.setOnItemSelectedListener(this);
        g.setSelection(position, true);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        mSwitcher.setImageDrawable(new BitmapDrawable(getResources(), this.searchResult.imagedrawables.get(position)));
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return i;
    }

    private ImageSwitcher mSwitcher;

    public class ImageAdapter extends BaseAdapter {
        private ArrayList<Bitmap> bitmaps;

        public ImageAdapter(Context c, ArrayList<Bitmap> bitmaps) {
            mContext = c;
            this.bitmaps = bitmaps;
        }


        public int getCount() {
            return this.bitmaps.size();
        }


        public Object getItem(int i) {
            return i;
        }

        public long getItemId(int i) {
            return i;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageBitmap(this.bitmaps.get(position));
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            return i;
        }

        private Context mContext;

    }


}