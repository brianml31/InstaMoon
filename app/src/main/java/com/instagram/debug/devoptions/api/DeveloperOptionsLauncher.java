package com.instagram.debug.devoptions.api;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.brianml31.instamoon.utils.ToastUtils;
import com.instagram.common.session.UserSession;


public class DeveloperOptionsLauncher {
    public static final DeveloperOptionsLauncher INSTANCE = new DeveloperOptionsLauncher();

    public final void loadAndLaunchDeveloperOptions(Context context, FragmentActivity fragmentActivity, UserSession userSession){
        ToastUtils.Companion.showShortToast(context, "Open developer options");
    }

//    public final void loadAndLaunchDeveloperOptions(Context context, _2up _2up, FragmentActivity fragmentActivity, UserSession userSession) {
//
//    }

}
