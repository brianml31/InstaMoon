package com.brianml31.instamoon.utils

class GhostModeUtils {
    companion object {
        fun hideSeenStories(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[0], false)
        }

        fun hideSeenDM(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[1], false)
        }

        fun hideTypingDM(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[2], false)
        }

        fun hideTookScreenshot(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[3], false)
        }

        fun hideOpenedMedia(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[4], false)
        }

        fun hideReplayedMedia(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[5], false)
        }

        fun hideSeenLiveVideos(): Boolean {
            return PrefsUtils.getBoolean(PrefsUtils.arrayGhostModeKeys[6], false)
        }

    }
}