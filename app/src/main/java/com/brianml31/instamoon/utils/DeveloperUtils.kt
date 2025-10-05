package com.brianml31.instamoon.utils

import android.content.Context
import X.Ygy
import com.instagram.mainactivity.InstagramMainActivity


class DeveloperUtils {
    companion object {
        fun openDeveloperMode(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            Ygy.A00.A02(ctx, instagramMainActivity, instagramMainActivity.A06)
        }
    }
}