package com.begentgroup.miniapplication.tstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.data.TStoreCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    List<TStoreCategory> items = new ArrayList<TStoreCategory>();

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(TStoreCategory category) {
        items.add(category);
        notifyDataSetChanged();
    }

    public void addAll(List<TStoreCategory> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    CategoryViewHolder.OnItemClickListener mListener;
    public void setOnItemClickListener(CategoryViewHolder.OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.setCategory(items.get(position));
        holder.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
