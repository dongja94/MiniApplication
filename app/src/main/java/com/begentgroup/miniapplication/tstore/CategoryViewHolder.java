package com.begentgroup.miniapplication.tstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.begentgroup.miniapplication.data.TStoreCategory;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView nameView;
    TStoreCategory category;
    public interface OnItemClickListener {
        public void onItemClick(View view, TStoreCategory category);
    }

    OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, category);
                }
            }
        });
    }

    public void setCategory(TStoreCategory category) {
        this.category = category;
        nameView.setText(category.getCategoryName());
    }
}
