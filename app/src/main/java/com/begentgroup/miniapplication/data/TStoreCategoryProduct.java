package com.begentgroup.miniapplication.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class TStoreCategoryProduct {
    @SerializedName("category")
    public TStoreCategoryInfo info;
    public int totalCount;
    public TStoreProducts products;
}
