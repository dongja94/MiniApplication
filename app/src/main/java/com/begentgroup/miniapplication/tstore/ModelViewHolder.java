package com.begentgroup.miniapplication.tstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.begentgroup.miniapplication.data.TStorePhoneModel;

/**
 * Created by dongja94 on 2016-05-10.
 */
public class ModelViewHolder extends RecyclerView.ViewHolder {
    TextView nameView;
    public ModelViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView;
    }
    TStorePhoneModel model;
    public void setModel(TStorePhoneModel model) {
        this.model = model;
        nameView.setText(model.modelName + "(" + model.modelCode + ")");
    }
}
