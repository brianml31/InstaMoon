package com.brianml31.insta_moon;

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
import com.instagram.mainactivity.InstagramMainActivity;

import java.net.URI;

public class Injection {

    private static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data){
        ActivityResultHandler.Companion.handleActivityResult(activity, requestCode, resultCode, data);
        Log.i("InstagramMainActivity","onActivityResult(IILandroid/content/Intent;)V");
    }

    private static void setLongClickMenuHandler(InstagramMainActivity instagramMainActivity, View v){
        LongClickMenuHandler.Companion.setLongClickMenuHandler(instagramMainActivity, v);
        Log.i("InstagramMainActivity","Landroid/view/ViewGroup;->addView(Landroid/view/View;)V");
    }

    private static void onCreate(Application application){
        AppContext.Companion.setContext(application);
        Log.i("InstagramAppShell","Landroid/app/Application;->onCreate()V");
    }

    // Ghost mode
    private static void validateUriHost(URI uri){
        GhostModeUtils.Companion.validateUriHost(uri);
        Log.i("TigonServiceLayer","startRequest");
    }

    private static void hideSeenDM(){
        if(GhostModeUtils.Companion.hideSeenDM()){
            return;
        }else{
            Log.i("X","mark_thread_seen-");
        }
    }

    private static void hideTypingDM(){
        if(GhostModeUtils.Companion.hideTypingDM()){
            return;
        }else{
            Log.i("X","is_typing_indicator_enabled");
        }
    }

    //ExtraOptions
    private static boolean disableAds(){
        if(ExtraOptionsUtils.Companion.disableAds()){
            return false;
        }else{
            Log.i("X","SponsoredContentController.insertItem");
            return true;
        }
    }

    private static boolean disableVideoAutoplay(){
        if(ExtraOptionsUtils.Companion.disableVideoAutoplay()){
            return true;
        }else{
            Log.i("X","ig_disable_video_autoplay");
            return false;
        }
    }

    private static void disableDoubleTapLike(){
        if(ExtraOptionsUtils.Companion.disableDoubleTapLike()){
            return;
        }else{
            Log.i("X","like_media");
        }
    }

    private static boolean hideSuggestedReels(boolean z){
        Log.i("clips_netego", "FeedItem");
        return ExtraOptionsUtils.Companion.hideSuggestedReels(z);
    }

    //App font
    private static Typeface getCustomFont(Typeface typeface){
        Log.i(", does not have a backing source. You need to provide either a systemFontName, assetFontName, or a fileDescriptor.","Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        return FontUtils.Companion.getCustomFont(typeface);
    }

    //Remove snooze warning
    private static void removeSnoozeWarning(){
        Log.i("Lcom/instagram/release/lockout/DogfoodingEligibilityApi;-><init>", "const/4 vX, 0x0");
    }

}
