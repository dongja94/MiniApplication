package com.begentgroup.miniapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
