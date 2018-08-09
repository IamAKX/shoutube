package com.akashapplications.shoutube;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akashapplications.shoutube.adapters.VideoAdapter;
import com.akashapplications.shoutube.model.VideoModel;
import com.akashapplications.shoutube.utilities.Constants;
import com.akashapplications.shoutube.utilities.LocalPreference;
import com.akashapplications.shoutube.utilities.VideoJsonResponseParser;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Home extends AppCompatActivity {

    ArrayList<VideoModel> videoList = new ArrayList<>();
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    LinearLayoutManager layoutManager;
    String nextPageToken = "";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("ShouTube");
        progressBar = findViewById(R.id.videoProgress);
        progressBar.setVisibility(View.GONE);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
//        imageView.setImageResource(R.drawable.user);
        Glide.with(getBaseContext())
                .load(new LocalPreference(getBaseContext()).getImgPath())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.user).error(R.drawable.user))
                .into(imageView);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getBaseContext(),view);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId())
                        {
                            case R.id.profile:
                                startActivity(new Intent(getBaseContext(),Profile.class));
                                return true;
                            case R.id.logout:
                                new LocalPreference(getBaseContext()).setLoginStatus(false);
                                startActivity(new Intent(getBaseContext(),Authenticate.class));
                                finish();
                                return true;
                        }
                        return false;
                    }
                });// to implement on click event on items of menu
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.home_menu, popup.getMenu());
                popup.show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);

        new FetchVideos().execute();
        fillVideoList();
    }

    private void fillVideoList() {
        videoAdapter = new VideoAdapter(getBaseContext(),videoList);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    new FetchVideos().execute();

                }
            }

        });
        videoAdapter.notifyDataSetChanged();
        recyclerView.invalidate();


    }


    private class FetchVideos extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getChannelURL(getBaseContext())+"&pageToken="+nextPageToken,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray array = object.getJSONArray("items");
                                nextPageToken = object.getString("nextPageToken");
                                for(int i = 0; i< array.length(); i++)
                                {
                                    videoList.add(VideoJsonResponseParser.getParsedDataOf(array.getJSONObject(i)));
                                    videoAdapter.notifyDataSetChanged();
                                    recyclerView.invalidate();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            progressBar.setVisibility(View.GONE);
                            NetworkResponse networkResponse = error.networkResponse;
                            if(networkResponse != null && networkResponse.data != null)
                            {

                            }
                            Toast.makeText(getBaseContext(),"Error in loading video",Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    return params;
                }
            };

            //Adding the string request to the queue
            stringRequest.setShouldCache(false);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            videoAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
//            progressBar.setVisibility(View.GONE);
        }
    }


}
