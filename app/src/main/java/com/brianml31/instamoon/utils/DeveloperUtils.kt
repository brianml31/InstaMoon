package com.brianml31.instamoon.utils

import android.content.Context
import X.TXA
import com.instagram.mainactivity.InstagramMainActivity


class DeveloperUtils {
    companion object {
        fun openDeveloperMode(context: Context, instagramMainActivity: InstagramMainActivity) {
            //"settings_devoptions"
            TXA.A00.A02(context, instagramMainActivity, instagramMainActivity.A05)
        }
    }
}