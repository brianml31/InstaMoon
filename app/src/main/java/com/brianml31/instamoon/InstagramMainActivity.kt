package com.brianml31.instamoon

import android.app.Activity
import android.content.Intent
import com.brianml31.instamoon.utils.FileUtils
import com.brianml31.instamoon.utils.PermissionsUtils
import com.instagram.mainactivity.InstagramMainActivity

class InstagramMainActivity {
    companion object {
        private const val REQUEST_CODE_JSON_RESTORE = 74565
        private const val REQUEST_CODE_IGMOON_RESTORE = 74566

        fun after_onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_JSON_RESTORE && data.data != null && resultCode == -1) {
                    FileUtils.importJsonBackup(activity, data.data)
                }
                if (requestCode == REQUEST_CODE_IGMOON_RESTORE && data.data != null && resultCode == -1) {
                    FileUtils.importIgMoonBackup(activity, data.data)
                }
            }
        }

        fun requestFileJsonToRestore(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, REQUEST_CODE_JSON_RESTORE)
            }
        }

        fun requestFileIgMoonToRestore(instagramMainActivity: InstagramMainActivity) {
            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                PermissionsUtils.requestPermission(instagramMainActivity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("*/*")
                instagramMainActivity.startActivityForResult(intent, REQUEST_CODE_IGMOON_RESTORE)
            }
        }

    }

}