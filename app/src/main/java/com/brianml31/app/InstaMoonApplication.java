package com.brianml31.app;

import android.app.Application;

import com.brianml31.instamoon.Brian;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", mailTo = "null@null.com", mode = ReportingInteractionMode.TOAST, toastText = "Oops! The app has encountered an error and will now close. We're working to fix this issue.")
public class InstaMoonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Brian.Companion.after_onCreate(InstaMoonApplication.this);
    }
}
