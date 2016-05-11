package com.begentgroup.miniapplication.facebook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.begentgroup.miniapplication.data.FacebookFeed;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    public FeedViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView;
    }

    public void setFacebookFeed(FacebookFeed feed) {
        textView.setText(feed.story != null ? feed.story : feed.message);
    }
}
