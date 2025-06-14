package com.brianml31.insta_moon;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.brianml31.instamoon.Brian;
import com.brianml31.instamoon.LongClickMenuHandler;
import com.brianml31.instamoon.utils.Constants;
import com.brianml31.instamoon.utils.ExtraOptionsUtils;
import com.brianml31.instamoon.utils.GhostModeUtils;
import com.instagram.mainactivity.InstagramMainActivity;

import java.net.URI;

public class InstagramInjectionManager {
    //In this class are the invocation lines for InstaMoon functions.

    public static void after_onActivityResultKotlin(Activity activity, int requestCode, int resultCode, Intent data){
        com.brianml31.instamoon.InstagramMainActivity.Companion.after_onActivityResult(activity, requestCode, resultCode, data);
    }

    public static void setLongClickMenuHandlerKotlin(InstagramMainActivity instagramMainActivity, View v){
        LongClickMenuHandler.Companion.setLongClickMenuHandler(instagramMainActivity, v);
    }

    public static void setonCreate(Application application){
        Brian.Companion.after_onCreate(application);
    }

    public static int ExtendSnoozeWarningDuration(){
        Log.i("","snooze_expiration_lockout_manager");
        return Constants.EXTEND_SNOOZE_WARNING_DURATION;
    }


    // Ghost mode
    public static void validateUriHostKotlin(URI uri){
        Brian.Companion.validateUriHost(uri);
    }

    public static void hideSeenDM(){
        if(GhostModeUtils.Companion.hideSeenDM()){
            return;
        }else{
            Log.i("","mark_thread_seen-");
        }
    }

    public static void hideTyping(){
        if(GhostModeUtils.Companion.hideTypingDM()){
            return;
        }else{
            Log.i("","is_typing_indicator_enabled");
        }
    }

    //ExtraOptions
    public static boolean disableAds(){
        if(ExtraOptionsUtils.Companion.disableAds()){
            return false;
        }else{
            Log.i("","SponsoredContentController.insertItem");
            return true;
        }
    }

    public static boolean disableVideoAutoplay(){
        if(ExtraOptionsUtils.Companion.disableVideoAutoplay()){
            return true;
        }else{
            Log.i("","ig_disable_video_autoplay");
            return false;
        }
    }

    public static void disableDoubleTapLike(){
        if(ExtraOptionsUtils.Companion.disableDoubleTapLike()){
            return;
        }else{
            Log.i("","like_media");
        }
    }

    public static boolean hideSuggestedReels(boolean z){
        Log.i("clips_netego", "FeedItem");
        return ExtraOptionsUtils.Companion.hideSuggestedReels(z);
    }

}
