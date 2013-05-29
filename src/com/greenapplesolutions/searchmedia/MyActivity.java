package com.greenapplesolutions.searchmedia;

import adapter.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import domain.SearchParam;
import domain.SearchResult;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DataMessenger;
import util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    GridView gridView;
    ArrayList<Drawable> list;
    private SearchResult searchResult;
    private CustomProgressDialog progressDialog;
    private TextView queryTextView;
    private Button searchButton;
    private Button nextButton;
    private Button previousButton;
    private SearchParam searchParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        queryTextView = (TextView) findViewById(R.id.queryText);

        searchButton = (Button) findViewById(R.id.searcButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryText = queryTextView.getText().toString();
                if (!Util.isStringNullOrEmpty(queryText)) {
                    TweetSearchTask task = new TweetSearchTask();
                    SearchParam searchParam = MyActivity.this.searchResult.searchParam;
                    searchParam.setPageNumber(searchParam.getPageNumber() + 1);
                    task.execute(searchParam);

                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryText = queryTextView.getText().toString();
                if (!Util.isStringNullOrEmpty(queryText)) {
                    TweetSearchTask task = new TweetSearchTask();
                    SearchParam searchParam = MyActivity.this.searchResult.searchParam;
                    searchParam.setPageNumber(searchParam.getPageNumber() + 1);
                    task.execute(searchParam);

                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryText = queryTextView.getText().toString();
                if (!Util.isStringNullOrEmpty(queryText)) {
                    TweetSearchTask task = new TweetSearchTask();

                    MyActivity.this.searchParam = new SearchParam(queryText.trim(), 1, 100);
                    task.execute(MyActivity.this.searchParam);

                }
            }
        });

        gridView = (GridView) findViewById(R.id.gridview);


        /**
         * On Click event for Single Gridview Item
         * */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                try {
                    DataMessenger.Set("result", searchResult);
                    // Sending image id to FullScreenActivity
                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                    // passing array index

                    i.putExtra("id", position);
                    startActivity(i);
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });
        previousButton.setClickable(false);
        nextButton.setClickable(false);
    }


//        SearchParam param = new SearchParam();
//        param.queryParam = "q=testing&from=keshavashta&replies=false&limit=100&include_entities=true&rpp=100";
//        TweetSearchTask task = new TweetSearchTask();
//        task.execute(param);

    protected void showProgressDialog(String title, String message) {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.hide();
    }

    public Bitmap getDrawableFromImageUrl(String urlString) {


//        try {
//            URL url = new URL(urlString);
//            InputStream inputStream = url.openConnection().getInputStream();
//            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//            inputStream.close();
//
//            return bmp;
//        } catch (Exception e) {
//            Log.e("Drawable Exception", e.getMessage());
//            return null;
//        }
//        try {
//            URL url = new URL(urlString);
//            //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
//            HttpGet httpRequest = null;
//
//            httpRequest = new HttpGet(url.toURI());
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = (HttpResponse) httpclient
//                    .execute(httpRequest);
//
//            HttpEntity entity = response.getEntity();
//            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
//            InputStream input = b_entity.getContent();
//
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//
//            return bitmap;
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            return null;
//        }
        try {

            InputStream is = fetch(urlString);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            return bmp;

        } catch (Exception e) {

            Log.e("Converting Bitmap", e.getMessage());
            return null;

        }
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlString);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }

    private class TweetSearchTask extends AsyncTask<SearchParam, Void, SearchResult> {
        @Override
        protected void onPostExecute(SearchResult result) {

            MyActivity.this.searchResult = result;


            try {
                hideProgressDialog();
                gridView.setAdapter(new ImageAdapter(MyActivity.this, result.imagedrawables));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        @Override
        protected void onPreExecute() {

            showProgressDialog("Loading", "Loading Images, Please wait.");
        }

        @Override
        protected SearchResult doInBackground(SearchParam... params) {
            SearchParam searchParam = params[0];


            ArrayList<Bitmap> drawableList = new ArrayList<Bitmap>();
            ArrayList<String> imagePathList = new ArrayList<String>();


            try {

                String url = Util.getUrlStringFromQueryParams("http://search.twitter.com/search.json", searchParam.getParams());

                String response = getHttpResponse(url);
                if (!Util.isStringNullOrEmpty(response)) {
                    JSONObject jsonReceived = new JSONObject(response);

                    try {
                        JSONArray results = jsonReceived.getJSONArray("results");
                        if (results != null && results.length() >= 0) {

                            for (int i = 0; i < results.length(); ++i) {
                                JSONObject resultsJSONObject = results.getJSONObject(i).getJSONObject("entities");
                                if (resultsJSONObject.has("media")) {
                                    JSONArray mediaArray = resultsJSONObject.getJSONArray("media");
                                    for (int j = 0; j < mediaArray.length(); ++j) {
                                        String imageUrl = null;
                                        if (mediaArray.getJSONObject(j).getString("type").equals("photo") && !imagePathList.contains(imageUrl = mediaArray.getJSONObject(j).getString("media_url"))) {
                                            imagePathList.add(imageUrl);
                                        }
                                    }
                                }
                            }
                            boolean isNextPageExist = jsonReceived.has("next_page");
                            if (!isNextPageExist) {
                                MyActivity.this.nextButton.setClickable(false);

                            } else {
                                MyActivity.this.nextButton.setClickable(true);
                            }
                            if (searchParam.getPageNumber() == 1) {
                                MyActivity.this.previousButton.setClickable(false);

                            } else {
                                MyActivity.this.previousButton.setClickable(true);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("HTTP GET:", e.toString());
                    }
                }

            } catch (Exception e) {
                Log.e("HTTP GET:", e.toString());
            }

            for (String s : imagePathList) {
                Bitmap drawable = getDrawableFromImageUrl(s);
                if (drawable != null)
                    drawableList.add(drawable);
            }
            SearchResult searchResult = new SearchResult();
            searchResult.imagedrawables = drawableList;
            searchResult.imagePathList = imagePathList;
            searchResult.searchParam = searchParam;
            return searchResult;
        }
    }

    private String getHttpResponse(String urlString) {
        try {
            URL url = null;
            String response = null;

            url = new URL(urlString);
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //set the request method to GET
            connection.setRequestMethod("GET");
            //get the output stream from the connection you created
            OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
            //write your data to the ouputstream

            request.flush();
            request.close();
            String line = "";
            //create your inputsream
            InputStreamReader isr = new InputStreamReader(
                    connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //get the string version of the response data
            response = sb.toString();
            //do what you want with the data now

            //always remember to close your input and output streams
            isr.close();
            reader.close();
            return response;
        } catch (Exception e) {
            Log.e("HTTP GET:", e.toString());
            return null;
        }

    }
}