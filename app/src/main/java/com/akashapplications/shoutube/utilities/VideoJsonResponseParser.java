package com.akashapplications.shoutube.utilities;

import com.akashapplications.shoutube.model.VideoModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoJsonResponseParser {

    public static VideoModel getParsedDataOf(JSONObject object) throws JSONException {
        VideoModel model = new VideoModel();

        if(object.has("id") && object.getJSONObject("id").has("videoId"))
            model.setVideoId(object.getJSONObject("id").getString("videoId"));
        else
            model.setVideoId("--");

        if(object.has("snippet"))
        {
            JSONObject snippet = object.getJSONObject("snippet");
            if(snippet.has("publishedAt"))
            {
                try {
                    String timeStamp = snippet.getString("publishedAt");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date d = sdf.parse(timeStamp);

                    model.setPublishedAt("Uploaded "+ TimeAgo.using(d.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
                model.setPublishedAt("--");

            if(snippet.has("channelId"))
                model.setChannelId(snippet.getString("channelId"));
            else
                model.setChannelId("--");

            if(snippet.has("title"))
                model.setTitle(snippet.getString("title"));
            else
                model.setTitle("--");

            if(snippet.has("description"))
                model.setDescription(snippet.getString("description"));
            else
                model.setDescription("--");

            if(snippet.has("thumbnails"))
                model.setThumbnail(snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url"));
            else
                model.setThumbnail(" ");

            if(snippet.has("channelTitle"))
                model.setChannelId(snippet.getString("channelTitle"));
            else
                model.setChannelId("--");

        }

        return model;
    }
}
