package com.begentgroup.miniapplication.tstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by dongja94 on 2016-05-10.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView)itemView;
    }

    public void setPreviewImage(String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
