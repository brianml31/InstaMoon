package com.brianml31.instamoon

import android.app.Application
import android.content.Context
import com.brianml31.instamoon.utils.ExtraOptionsUtils
import com.brianml31.instamoon.utils.GhostModeUtils
import org.acra.ACRA
import org.json.JSONObject
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

        /*fun textToHex(text: String): String {
            val hex = StringBuilder()
            val chars = text.toCharArray()
            for (i in chars.indices) {
                val c = chars[i]
                hex.append(String.format("%02x", c.code))
            }
            return hex.toString()
        }*/

        fun hexToText(hex: String): String {
            val text = StringBuilder()
            var i = 0
            while (i < hex.length) {
                val part = hex.substring(i, i + 2)
                val charCode = part.toInt(16)
                text.append(charCode.toChar())
                i += 2
            }
            return text.toString()
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