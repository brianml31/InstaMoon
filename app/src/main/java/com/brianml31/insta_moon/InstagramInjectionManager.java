package com.brianml31.insta_moon;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.brianml31.instamoon.handlers.LongClickMenuHandler;
import com.brianml31.instamoon.utils.AppContext;
import com.brianml31.instamoon.utils.BackupManager;
import com.brianml31.instamoon.utils.ExtraOptionsUtils;
import com.brianml31.instamoon.utils.FontUtils;
import com.brianml31.instamoon.utils.GhostModeUtils;
import com.instagram.mainactivity.InstagramMainActivity;

import java.net.URI;

public class InstagramInjectionManager {
    //Invocations

    static void after_onActivityResultKotlin(Activity activity, int requestCode, int resultCode, Intent data){
        BackupManager.Companion.after_onActivityResult(activity, requestCode, resultCode, data);
        Log.i("InstagramMainActivity","onActivityResult(IILandroid/content/Intent;)V");
    }

    static void setLongClickMenuHandler(InstagramMainActivity instagramMainActivity, View v){
        LongClickMenuHandler.Companion.setLongClickMenuHandler(instagramMainActivity, v);
        Log.i("InstagramMainActivity","Landroid/view/ViewGroup;->addView(Landroid/view/View;)V");
    }

    static void onCreate(Application application){
        AppContext.Companion.setContext(application);
        Log.i("InstagramAppShell","Landroid/app/Application;->onCreate()V");
    }

    // Ghost mode
    static void validateUriHostKotlin(URI uri){
        GhostModeUtils.Companion.validateUriHost(uri);
        Log.i("TigonServiceLayer","startRequest");
    }

    static void hideSeenDM(){
        if(GhostModeUtils.Companion.hideSeenDM()){
            return;
        }else{
            Log.i("X","mark_thread_seen-");
        }
    }

    static void hideTyping(){
        if(GhostModeUtils.Companion.hideTypingDM()){
            return;
        }else{
            Log.i("X","is_typing_indicator_enabled");
        }
    }

    //ExtraOptions
    static boolean disableAds(){
        if(ExtraOptionsUtils.Companion.disableAds()){
            return false;
        }else{
            Log.i("X","SponsoredContentController.insertItem");
            return true;
        }
    }

    static boolean disableVideoAutoplay(){
        if(ExtraOptionsUtils.Companion.disableVideoAutoplay()){
            return true;
        }else{
            Log.i("X","ig_disable_video_autoplay");
            return false;
        }
    }

    static void disableDoubleTapLike(){
        if(ExtraOptionsUtils.Companion.disableDoubleTapLike()){
            return;
        }else{
            Log.i("X","like_media");
        }
    }

    static boolean hideSuggestedReels(boolean z){
        Log.i("clips_netego", "FeedItem");
        return ExtraOptionsUtils.Companion.hideSuggestedReels(z);
    }

    //App font
    public static Typeface getCustomFont(Typeface typeface){
        Log.i(", does not have a backing source. You need to provide either a systemFontName, assetFontName, or a fileDescriptor.","Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        return FontUtils.Companion.getCustomFont(typeface);
    }

    //Remove snooze warning
    public static void removeSnoozeWarning(){
        Log.i("invoke-direct\\s*\\{\\s*v\\d+\\s*,\\s*v\\d+\\s*\\},\\s*Lcom/instagram/release/lockout/DogfoodingEligibilityApi;-><init>", "const/4 vX, 0x0");
    }

}
