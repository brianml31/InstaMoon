package com.brianml31.instamoon.utils

import android.content.Context
import X.Yka
import com.instagram.mainactivity.InstagramMainActivity


class DeveloperUtils {
    companion object {
        fun openDeveloperMode(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            Yka.A00.A02(ctx, instagramMainActivity, instagramMainActivity.A05)
        }
    }
}