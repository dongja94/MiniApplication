package com.begentgroup.miniapplication.facebook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.data.FacebookFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-11.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    List<FacebookFeed> items = new ArrayList<>();
    public void addAll(List<FacebookFeed> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.setFacebookFeed(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
