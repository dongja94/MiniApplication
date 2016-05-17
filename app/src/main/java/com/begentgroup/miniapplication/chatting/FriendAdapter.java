package com.begentgroup.miniapplication.chatting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.login.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-17.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    List<User> items = new ArrayList<>();
    public void addAll(List<User> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    FriendViewHolder.OnItemClickListener mListener;
    public void setOnItemClickListener(FriendViewHolder.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.setUser(items.get(position));
        holder.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
