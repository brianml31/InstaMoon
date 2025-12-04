package com.brianml31.instamoon.utils

import android.content.Context
import X.SgW
import com.instagram.mainactivity.InstagramMainActivity


class DeveloperUtils {
    companion object {
        fun openDeveloperMode(context: Context, instagramMainActivity: InstagramMainActivity) {
            //"settings_devoptions"
            SgW.A00.A02(context, instagramMainActivity, instagramMainActivity.A05)
        }
    }
}