package com.brianml31.insta_moon

import android.app.Application
import android.content.Context
import android.util.Base64
import com.brianml31.insta_moon.utils.ExtraOptionsUtils
import com.brianml31.insta_moon.utils.GhostModeUtils
import org.acra.ACRA
import java.io.IOException
import java.net.URI


class Brian {
    companion object {
        private var ctx: Context? = null

        fun getCtx(): Context? {
            return ctx
        }

        fun after_onCreate(application: Application) {
            ACRA.init(application)
            ctx = application.applicationContext
        }

        fun decodeBase64(encodedString: String): String {
            return String(Base64.decode(encodedString, Base64.DEFAULT))
        }

        fun validateUriHost(uri: URI) {
            if(uri!=null){
                var uriPath: String = uri.path
                if(uriPath.contains("/v2/media/seen/")){
                    if (GhostModeUtils.hideSeenStories()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.contains("/heartbeat_and_get_viewer_count/")) {
                    if (GhostModeUtils.hideSeenLiveVideos()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/ephemeral_screenshot/") || uriPath.endsWith("/screenshot/")) {
                    if (GhostModeUtils.hideTookScreenshot()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/item_seen/")){
                    if (GhostModeUtils.hideOpenedMedia()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/item_replayed/")){
                    if (GhostModeUtils.hideReplayedMedia()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.contains("graph.instagram.com") || uriPath.contains("graph.facebook.com") || uriPath.contains("/logging_client_events")) {
                    if (ExtraOptionsUtils.disableAnalytics()) {
                        throw IOException("URL has no host")
                    }
                }
            }
        }

    }
}