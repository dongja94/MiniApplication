package com.begentgroup.miniapplication.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class FacebookFeedsResult {
    @SerializedName("data")
    public List<FacebookFeed> feeds;

    public void convertStringToDate() {
        for (FacebookFeed ff : feeds) {
            ff.changeStringToDate();
        }
    }
}
