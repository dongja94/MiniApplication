package com.begentgroup.testappengine;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class FacebookLoginModule {

    private static final String ME_FAICEBOOK_URL = "https://graph.facebook.com/me?fields=id,name,email&access_token=%s";

    static Gson gson = new Gson();
    public static FacebookInfo getFacebookInfo(String token) throws InvalidUserInfoException, IOException {
        String urlText = String.format(ME_FAICEBOOK_URL, token);
        URL url = new URL(urlText);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        int code = conn.getResponseCode();
        if (code >= 200 && code < 300) {
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            FacebookInfo info = gson.fromJson(isr, FacebookInfo.class);
            if (info.error == null) {
                return info;
            }
        }

        throw new InvalidUserInfoException("invalid token");
    }

}
