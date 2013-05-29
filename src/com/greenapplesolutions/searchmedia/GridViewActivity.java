package com.greenapplesolutions.searchmedia;

/**
 * Created with IntelliJ IDEA.
 * User: saxena.arunesh
 * Date: 5/28/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class GridViewActivity extends BaseActivity {


    ArrayList<String> imagesList;

    DisplayImageOptions options;
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);

        imagesList = new ArrayList<String>();
        imagesList.add("http://media2.intoday.in/wonderwoman/images/Photo_gallery/sunny3_050713115449.jpg");
        imagesList.add("http://media.apunkachoice.com/image/nw/qz/main/main_image-153138.jpg");
        imagesList.add("http://media.apunkachoice.com/image/nw/cb/main/main_image-153137.jpg");
        imagesList.add("http://media.apunkachoice.com/image/nw/tj/main/main_image-153136.jpg");
        imagesList.add("http://media1.santabanta.com/full5/Global%20Celebrities(F)/Sunny%20Leone/sunny-leone-49a.jpg");
        imagesList.add("http://media.santabanta.com/medium1/global%20celebrities(f)/sunny%20leone/sunny-leone-18a.jpg");

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, ImagePagerActivity.class);

//                    // passing array index
//
                    intent.putExtra("id", position);
        startActivity(intent);
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }
            File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                    .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75)
                    .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                    .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 1) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // default
                    .memoryCacheSize(2 * 1024 * 1024)
                    .discCache(new UnlimitedDiscCache(cacheDir)) // default
                    .discCacheSize(50 * 1024 * 1024)
                    .discCacheFileCount(100)
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .enableLogging()
                    .build();
            imageLoader.init(config);
            imageLoader.displayImage(imagesList.get(position), imageView, options);

            return imageView;
        }
    }
}