package com.begentgroup.miniapplication.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class FacebookFeed {
    public String story;
    public String message;
    public String created_time;
    public Date createdDate;
    public String id;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public void changeStringToDate() {
        try {
            createdDate = simpleDateFormat.parse(created_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
