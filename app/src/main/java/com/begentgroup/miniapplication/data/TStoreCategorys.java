package com.begentgroup.miniapplication.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class TStoreCategorys {
    @SerializedName("category")
    public List<TStoreCategory> categoryList;
}
