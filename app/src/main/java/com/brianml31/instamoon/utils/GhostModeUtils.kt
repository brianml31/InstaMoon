package com.brianml31.instamoon.utils

import java.io.IOException
import java.net.URI

class GhostModeUtils {
    companion object {

        @JvmStatic
        fun validateUriHost(uri: URI) {
            if(uri!=null){
                var uriPath: String = uri.path
                //Log.i("DEBUG | Uri host",uriPath)
                if(uriPath.contains("/v2/media/seen/")){
                    if (hideSeenStories()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.contains("/heartbeat_and_get_viewer_count/")) {
                    if (hideSeenLiveVideos()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/ephemeral_screenshot/") || uriPath.endsWith("/screenshot/")) {
                    if (hideTookScreenshot()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/item_seen/")){
                    if (hideOpenedMedia()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.endsWith("/item_replayed/")){
                    if (hideReplayedMedia()) {
                        throw IOException("URL has no host")
                    }
                }
                if (uriPath.contains("graph.instagram.com") || uriPath.contains("graph.facebook.com") || uriPath.contains("/logging_client_events") || uriPath.contains("/pigeon_nest")) {
                    if (ExtraOptionsUtils.disableAnalytics()) {
                        throw IOException("URL has no host")
                    }
                }
            }
        }

        private fun hideSeenStories(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[0], false)
        }

        @JvmStatic
        fun hideSeenDM(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[1], false)
        }

        @JvmStatic
        fun hideTypingDM(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[2], false)
        }

        private fun hideTookScreenshot(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[3], false)
        }

        private fun hideOpenedMedia(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[4], false)
        }

        private fun hideReplayedMedia(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[5], false)
        }

        private fun hideSeenLiveVideos(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[6], false)
        }

    }
}