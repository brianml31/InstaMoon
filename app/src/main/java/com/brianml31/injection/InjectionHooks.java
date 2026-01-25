package com.brianml31.injection;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.brianml31.instamoon.handlers.ActivityResultHandler;
import com.brianml31.instamoon.handlers.LongClickMenuHandler;
import com.brianml31.instamoon.app.AppContext;
import com.brianml31.instamoon.utils.ExtraOptionsUtils;
import com.brianml31.instamoon.utils.FontUtils;
import com.brianml31.instamoon.utils.GhostModeUtils;
import com.brianml31.instamoon.utils.VersionCheckUtils;
import com.instagram.mainactivity.InstagramMainActivity;

import java.net.URI;

public class InjectionHooks {

    public static void checkVersion(Activity activity){
        VersionCheckUtils.checkVersion(activity);
        Log.i("MainFeedFragment", ";-><init>(Landroid/app/Activity;Landroid/widget/Adapter;");
        Log.i("getRootActivity()Landroid/app/Activity;", "null cannot be cast to non-null type com.instagram.base.activity.tabactivity.TabController");
    }

    private static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data){
        ActivityResultHandler.handleActivityResult(activity, requestCode, resultCode, data);
        Log.i("InstagramMainActivity","onActivityResult(IILandroid/content/Intent;)V");
    }

    private static void setLongClickMenuHandler(InstagramMainActivity instagramMainActivity, View v){
        LongClickMenuHandler.setLongClickMenuHandler(instagramMainActivity, v);
        Log.i("InstagramMainActivity","Landroid/view/ViewGroup;->addView(Landroid/view/View;)V");
    }

    private static void onCreate(Application application){
        AppContext.setContext(application);
        Log.i("InstagramAppShell","Landroid/app/Application;->onCreate()V");
    }

    // Ghost mode
    private static void validateUriHost(URI uri){
        GhostModeUtils.validateUriHost(uri);
        Log.i("TigonServiceLayer","startRequest");
    }

    private static void hideSeenDM(){
        if(GhostModeUtils.hideSeenDM()){
            return;
        }else{
            Log.i("X","mark_thread_seen-");
        }
    }

    private static void hideTypingDM(){
        if(GhostModeUtils.hideTypingDM()){
            return;
        }else{
            Log.i("X","is_typing_indicator_enabled");
        }
    }

    //ExtraOptions
    private static boolean disableAds(){
        if(ExtraOptionsUtils.disableAds()){
            return false;
        }else{
            Log.i("X","SponsoredContentController.insertItem");
            return true;
        }
    }

    private static boolean disableVideoAutoplay(){
        if(ExtraOptionsUtils.disableVideoAutoplay()){
            return true;
        }else{
            Log.i("X","ig_disable_video_autoplay");
            return false;
        }
    }

    private static void disableDoubleTapLike(){
        if(ExtraOptionsUtils.disableDoubleTapLike()){
            return;
        }else{
            Log.i("X","like_media");
        }
    }

    private static boolean hideSuggestedReels(boolean z){
        Log.i("clips_netego", "FeedItem");
        return ExtraOptionsUtils.hideSuggestedReels(z);
    }

    private static void removeEmptySpace() {
        if(!ExtraOptionsUtils.removeEmptySpace()){
            return;
        }
        Log.i("Nab bar height: old=%d new=%d, Activity=%s RetryCount=%d isFullScreenFlagSet=%b windowFlags=0x%X","if-gtz v0, :cond_xxx|2");
    }

    //App font
    private static Typeface getCustomFont(Typeface typeface){
        Log.i(", does not have a backing source. You need to provide either a systemFontName, assetFontName, or a fileDescriptor.","Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        return FontUtils.getCustomFont(typeface);
    }

    //Remove snooze warning
    private static void removeSnoozeWarning(){
        Log.i("Lcom/instagram/release/lockout/DogfoodingEligibilityApi;-><init>", "const/4 vX, 0x0");
    }

}
