package com.akashapplications.shoutube.utilities;

import android.content.Context;

public class Constants {

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String GOOGLE_YOUTUBE_API_KEY = "AIzaSyAdDix7i7a3an-gyXiquTV_14cIsr8-DZg";

    public static final String CHANNEL_ID = "UCETIWfzg3mjhqBf9ImGmK1Q";

    public static final String PASSWORD_PATTER = "^(?=.*?\\p{Lu})(?=.*?[\\p{L}&&[^\\p{Lu}]])(?=.*?\\d)(?=.*?[`~!@#$%^&*()\\-_=+\\\\\\|\\[{\\]};:'\",<.>/?]).*$";

    public static String getChannelURL(Context context)
    {
        return "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + new LocalPreference(context).getChannelID() + "&maxResults=10&key=" + GOOGLE_YOUTUBE_API_KEY + "";
    }
}
