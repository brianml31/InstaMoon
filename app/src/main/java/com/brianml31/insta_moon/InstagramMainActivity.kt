package com.brianml31.insta_moon

import android.app.Activity
import android.content.Intent
import com.brianml31.insta_moon.utils.FileUtils
import com.brianml31.insta_moon.utils.PermissionsUtils
import com.instagram.mainactivity.InstagramMainActivity

class InstagramMainActivity {
    companion object {
        private const val REQUEST_CODE_JSON_RESTORE = 74565
        private const val REQUEST_CODE_IBACKUP_RESTORE = 74566

        fun after_onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            if (data?.data != null && resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    REQUEST_CODE_JSON_RESTORE -> FileUtils.importJsonBackup(activity, data.data!!)
                    REQUEST_CODE_IBACKUP_RESTORE -> FileUtils.importIbackupBackup(activity, data.data!!)
                }
            }
        }

        fun requestFileJsonToRestore(activity: Activity) {
            if (!PermissionsUtils.checkPermission(activity)) {
                PermissionsUtils.requestPermission(activity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                }
                activity.startActivityForResult(intent, REQUEST_CODE_JSON_RESTORE)
            }
        }

        fun requestFileIbackupToRestore(activity: Activity) {
            if (!PermissionsUtils.checkPermission(activity)) {
                PermissionsUtils.requestPermission(activity)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                activity.startActivityForResult(intent, REQUEST_CODE_IBACKUP_RESTORE)
            }
        }
    }
}
