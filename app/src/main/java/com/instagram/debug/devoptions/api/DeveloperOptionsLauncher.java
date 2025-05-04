package com.instagram.debug.devoptions.api;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.brianml31.insta_moon.utils.ToastUtils;
import com.instagram.common.session.UserSession;

import X._2up;

public class DeveloperOptionsLauncher {
    public static final DeveloperOptionsLauncher INSTANCE = new DeveloperOptionsLauncher();

    public final void loadAndLaunchDeveloperOptions(Context context, FragmentActivity fragmentActivity, UserSession userSession){
        ToastUtils.Companion.showShortToast(context, "Open developer options");
    }

//    public final void loadAndLaunchDeveloperOptions(Context context, _2up _2up, FragmentActivity fragmentActivity, UserSession userSession) {
//
//    }

}
