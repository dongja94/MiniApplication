package com.begentgroup.miniapplication.tstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.TStoreProduct;

/**
 * Created by dongja94 on 2016-05-10.
 */
public class ProductDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_TITLE = 2;
    private static final int VIEW_TYPE_PREVIEW = 3;
    private static final int VIEW_TYPE_MODEL = 4;

    TStoreProduct product;


    public void setProduct(TStoreProduct product) {
        this.product = product;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_HEADER;
        position--;
        if (product.getPreviewUrlList().size() > 0) {
            if (position == 0) return VIEW_TYPE_TITLE;
            position--;
            if (position < product.getPreviewUrlList().size()) {
                return VIEW_TYPE_PREVIEW;
            }
            position-=product.getPreviewUrlList().size();
        }
        if (product.getModels().modelList.size() > 0) {
            if (position == 0) return VIEW_TYPE_TITLE;
            position--;
            if (position < product.getModels().modelList.size()) {
                return VIEW_TYPE_MODEL;
            }
            position-=product.getModels().modelList.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER : {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tstore_product, null);
                return new ProductViewHolder(view);
            }
            case VIEW_TYPE_TITLE : {
                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
                return new TitleViewHolder(view);
            }
            case VIEW_TYPE_PREVIEW : {
                View view = new ImageView(parent.getContext());
                return new ImageViewHolder(view);
            }
            case VIEW_TYPE_MODEL : {
                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
                return new ModelViewHolder(view);
            }
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ProductViewHolder h = (ProductViewHolder)holder;
            h.setProduct(product);
            return;
        }
        position--;
        if (product.getPreviewUrlList().size() > 0) {
            if (position == 0) {
                TitleViewHolder h = (TitleViewHolder)holder;
                h.setTitle("Preview");
                return;
            }
            position--;
            if (position < product.getPreviewUrlList().size()) {
                ImageViewHolder h = (ImageViewHolder)holder;
                h.setPreviewImage(product.getPreviewUrlList().get(position));
                return;
            }
            position-=product.getPreviewUrlList().size();
        }
        if (product.getModels().modelList.size() > 0) {
            if (position == 0) {
                TitleViewHolder h = (TitleViewHolder)holder;
                h.setTitle("Model");
                return ;
            }
            position--;
            if (position < product.getModels().modelList.size()) {
                ModelViewHolder h = (ModelViewHolder)holder;
                h.setModel(product.getModels().modelList.get(position));
                return ;
            }
            position-=product.getModels().modelList.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (product == null) return 0;
        int size = 1;
        if (product.getPreviewUrlList().size() > 0) {
            size += 1;
            size += product.getPreviewUrlList().size();
        }
        if (product.getModels().modelList.size() > 0) {
            size += 1;
            size += product.getModels().modelList.size();
        }
        return size;
    }
}
