package com.codepath.photoviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class PhotoActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "dd0d80021aeb46638e8e8ea6fffe90f4";
    private ArrayList<InstaPhoto>photos = null;
    InstaPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photos= new ArrayList<>();
        aPhotos = new InstaPhotosAdapter(this,photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        //SEND out network request to get Photos


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularPhotos();

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        getPopularPhotos();
    }


    public void getPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               JSONArray photosJSON = null;
                aPhotos.clear();
                try{
                    photosJSON = response.getJSONArray("data");
                    for(int i =0; i<photosJSON.length();i++){
                       JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstaPhoto photo = new InstaPhoto();

                        if(!photoJSON.isNull("user") && photoJSON.getJSONObject("images") !=null && photoJSON.getJSONObject("images").getJSONObject("standard_resolution")!=null) {
                            photo.imgUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        }
                        if(!photoJSON.isNull("caption") && photoJSON.getJSONObject("caption")!=null){
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        if(!photoJSON.isNull("user") && photoJSON.getJSONObject("user")!=null){
                            photo.profilePic = photoJSON.getJSONObject("user").getString("profile_picture");
                            photo.username= photoJSON.getJSONObject("user").getString("username");
                        }
                        if(!photoJSON.isNull("images")&&photoJSON.getJSONObject("images")!=null &&photoJSON.getJSONObject("images").getJSONObject("standard_resolution")!=null) {
                            photo.imgHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        }
                        if(!photoJSON.isNull("likes") && photoJSON.getJSONObject("likes")!=null ) {
                            photo.likeCounts = photoJSON.getJSONObject("likes").getInt("count");
                        }
                        if(!photoJSON.isNull("created_time")) {
                            long time = photoJSON.getLong("created_time");
                            photo.timeAgo = DateUtils.getRelativeTimeSpanString(time * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
                        }
                        JSONArray comments = null;
                        if(!photoJSON.isNull("comments") && photoJSON.getJSONObject("comments")!=null) {
                            comments = photoJSON.getJSONObject("comments").getJSONArray("data");
                        }
                        if(comments.length() > 0) {
                            photo.comment1 = comments.getJSONObject(comments.length()-1).getString("text");
                            photo.uname_comment1 = comments.getJSONObject(comments.length()-1).getJSONObject("from").getString("username");
                            if(comments.length() >1) {
                                photo.comment2 = comments.getJSONObject(comments.length() - 2).getString("text");
                                photo.uname_comment2 = comments.getJSONObject(comments.length() - 2).getJSONObject("from").getString("username");
                            }
                        }
                        photos.add(photo);

                    }
                    aPhotos.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);


                }catch (JSONException e){
                 e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //do something
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
