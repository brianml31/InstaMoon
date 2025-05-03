package com.brianml31.mainactivity;

import android.app.Application;

import com.brianml31.insta_moon.Brian;

public class InstaMoonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Brian.Companion.setCtx(InstaMoonApplication.this);
    }
}
