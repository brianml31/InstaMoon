package com.brianml31.insta_moon.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.brianml31.insta_moon.Brian
import com.brianml31.insta_moon.R
import com.brianml31.insta_moon.ui.InstaMoonMenuBottomSheet
import com.brianml31.insta_moon.ui.InstaMoonSettingsBottomSheet
import com.brianml31.insta_moon.ui.MenuOption
import com.brianml31.insta_moon.ui.SettingOption
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess


class DialogUtils {
    companion object {

        private fun getActivityContext(context: Context): FragmentActivity? {
            var currentContext = context
            while (currentContext is android.content.ContextWrapper) {
                if (currentContext is FragmentActivity) {
                    return currentContext
                }
                currentContext = currentContext.baseContext
            }
            if (context is Activity && context is FragmentActivity) {
                 return context
            }
            return null
        }


        fun showInstaMoonOptionsDialog(ctx: Context, instagramActivity: InstagramMainActivity) {
            val activity = getActivityContext(ctx) ?: return

            val options = listOf(
                MenuOption(0, "👻", "Ghost mode"),
                MenuOption(1, "⚙️", "Extra options"),
                MenuOption(2, "👨‍💻", "Open developer mode"),
                MenuOption(3, "📤", "Export backup"),
                MenuOption(4, "📥", "Import backup"),
                MenuOption(5, "🧹", "Clear developer mode settings"),
                MenuOption(6, "💾", "Save file (id_name_mapping.json)"),
                MenuOption(7, "ℹ️", "About the App")
            )

            val bottomSheet = InstaMoonMenuBottomSheet.newInstance(options)

            bottomSheet.setMenuOptionClickListener(object : InstaMoonMenuBottomSheet.MenuOptionClickListener {
                override fun onMenuOptionClicked(optionId: Int) {
                    handleMenuOptionClick(ctx, instagramActivity, optionId)
                }
            })

            bottomSheet.show(activity.supportFragmentManager, "InstaMoonMenu")
        }

        private fun handleMenuOptionClick(ctx: Context, instagramActivity: InstagramMainActivity, which: Int) {
            when (which) {
                0 -> showGhostModeDialog(ctx)
                1 -> showExtraOptionsDialog(ctx)
                2 -> DeveloperUtils.openDeveloperMode(ctx, instagramActivity)
                3 -> FileUtils.exportJsonBackup(ctx)
                4 -> showImportBackupDialog(ctx, instagramActivity)
                5 -> {
                    if (FileUtils.deleteMCOverrides(ctx)) {
                        ToastUtils.showShortToast(ctx, "Developer mode settings successfully cleared")
                    } else {
                        ToastUtils.showShortToast(ctx, "Error clearing commands")
                    }
                }
                6 -> FileUtils.saveFileIdNameMapping(ctx)
                7 -> showAboutAppDialogDialog(ctx)
            }
        }


        fun showGhostModeDialog(ctx: Context) {
            val activity = getActivityContext(ctx) ?: return

            val items = arrayOf("Hide (Seen) in stories", "Hide (Seen) in DM", "Hide (Typing) in DM", "Hide (You took a screenshot) in DM", "Hide (Opened) in media", "Hide (Replayed) in media", "Hide (Seen) in live videos")
            val checkedItems = PrefsUtils.loadPreferencesGhostMode(ctx)

            val options = items.mapIndexed { index, text ->
                SettingOption(index, text, checkedItems[index])
            }.toList()

            val bottomSheet = InstaMoonSettingsBottomSheet.newInstance("GHOST MODE 👻", options)

            bottomSheet.setSettingsSaveListener(object : InstaMoonSettingsBottomSheet.SettingsSaveListener {
                override fun onSettingsSaved(savedOptions: List<SettingOption>) {
                    val newCheckedItems = BooleanArray(savedOptions.size) { i -> savedOptions[i].isChecked }
                    PrefsUtils.savePreferencesGhostMode(ctx, newCheckedItems)
                    showRestartAppDialog(ctx)
                }
            })

            bottomSheet.show(activity.supportFragmentManager, "GhostModeSettings")
        }

        fun showExtraOptionsDialog(ctx: Context) {
            val activity = getActivityContext(ctx) ?: return

            val items = arrayOf("Disable ads", "Disable analytics", "Disable video autoplay")
            val checkedItems = PrefsUtils.loadPreferencesExtraOptions(ctx)

            val options = items.mapIndexed { index, text ->
                SettingOption(index, text, checkedItems[index])
            }.toList()

            val bottomSheet = InstaMoonSettingsBottomSheet.newInstance("EXTRA OPTIONS ⚙️", options)

            bottomSheet.setSettingsSaveListener(object : InstaMoonSettingsBottomSheet.SettingsSaveListener {
                override fun onSettingsSaved(savedOptions: List<SettingOption>) {
                    val newCheckedItems = BooleanArray(savedOptions.size) { i -> savedOptions[i].isChecked }

                    val oldAdsDisabled = checkedItems.getOrElse(0) { false }
                    val newAdsDisabled = newCheckedItems.getOrElse(0) { false }
                    if (!oldAdsDisabled && newAdsDisabled) {
                        showMessageDialog(ctx, "WARNING || Disable ads", "Hides ads in stories, discover, profile, etc. An ad can still appear once when refreshing the home feed")
                    }

                    PrefsUtils.savePreferencesExtraOptions(ctx, newCheckedItems)
                    showRestartAppDialog(ctx)
                }
            })

            bottomSheet.show(activity.supportFragmentManager, "ExtraOptionsSettings")
        }

        private fun buildAlertDialog(ctx: Context, title: String): AlertDialog.Builder {
            val builder = AlertDialog.Builder(ctx)
            builder.setCancelable(true)
            try {
                builder.setIcon(Utils.getAppIcon(ctx))
            } catch (e: Exception) {

            }
            builder.setTitle(title)
            return builder
        }


        fun showImportBackupDialog(ctx: Context, instagramActivity: InstagramMainActivity) {
             val activity = getActivityContext(ctx) ?: return

            val alertDialog = buildAlertDialog(ctx, "IMPORT BACKUP")
            val options = arrayOf("Import from .JSON", "Import from .ibackup (instafel)")
            alertDialog.setItems(options) { dialog, which ->
                when (which) {

                    0 -> com.brianml31.insta_moon.InstagramMainActivity.requestFileJsonToRestore(activity)
                    1 -> com.brianml31.insta_moon.InstagramMainActivity.requestFileIbackupToRestore(activity)
                }
            }
            alertDialog.setPositiveButton("CLOSE") { dialog, _ -> dialog.dismiss() }
            alertDialog.create().show()
        }


        fun showMessageDialog(ctx: Context, title: String, message: String){
            buildAlertDialog(ctx, title)
                .setMessage(message)
                .setPositiveButton("CLOSE", null)
                .show()
        }

        fun showRestartAppDialog(ctx: Context) {
            val activity = getActivityContext(ctx) ?: return

            val alertDialog = buildAlertDialog(ctx, "RESTART APP")
            alertDialog.setMessage("To apply the new changes the app needs to be restarted. Press RESTART to restart.")
            alertDialog.setPositiveButton("RESTART") { _, _ ->
                val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)?.apply {
                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                val pendingIntent = PendingIntent.getActivity(activity, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
                exitProcess(0)
            }
            alertDialog.setNegativeButton("LATER", null)
            alertDialog.create().show()
        }

        fun showFileNameDialog(ctx: Context, fileMCOverrides: File) {
            val input = EditText(ctx)
            val outputFileName = "InstaMoon_Backup_" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", java.util.Locale.getDefault()).format(Date())
            input.setPadding(48, 32, 32, 4)
            input.setTextSize(16f)
            input.hint = "Enter file name"
            input.setText(outputFileName)

            val alertDialog = buildAlertDialog(ctx, "File name:")
            alertDialog.setView(input)
            alertDialog.setNegativeButton("CLOSE", null)
            alertDialog.setPositiveButton("OK") { _, _ ->
                val content = FileUtils.readFile(fileMCOverrides)
                if (content != null) {
                    var customOutputFileName = input.text.toString().trim()
                    if (customOutputFileName.isEmpty()) {
                        customOutputFileName = outputFileName
                    }
                    if (!customOutputFileName.endsWith(".json", ignoreCase = true)) {
                        customOutputFileName += ".json"
                    }

                    val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.BACKUPS_OUTPUT_FOLDER)
                    if (!directoryOutput.exists()) {
                        directoryOutput.mkdirs()
                    }
                    val fileOutput = File(directoryOutput, customOutputFileName)

                    if (fileOutput.exists()) {

                    } else {
                        try { fileOutput.createNewFile() } catch (e: Exception) { }
                    }

                    val state = FileUtils.writeContent(content, fileOutput)
                    if (state == "SUCCESS") {
                        ToastUtils.showShortToast(ctx, "File exported to Downloads/${Constants.BACKUPS_OUTPUT_FOLDER}")
                    } else {
                        ToastUtils.showShortToast(ctx, "Error: $state")
                    }
                } else {
                    ToastUtils.showShortToast(ctx, "Failed to read original settings file")
                }
            }
            alertDialog.create().show()
        }

        private fun showAboutAppDialogDialog(ctx: Context) {
             val activity = getActivityContext(ctx) ?: return
             val versionName = try { Utils.getVersionName(activity) } catch (e: Exception) { "N/A" }

            val message = """
            InstaMoon 🌙 ${Constants.VERSION}

            ⭒Developed by brianml31⭒

            Based on version: $versionName

            Thanks to:
            ⋆ Monserrat G
            ⋆ Revanced
            ⋆ mamiiblt
            ⋆ Marcos shiinaider
            """.trimIndent()

            val alertDialog = buildAlertDialog(ctx, "ABOUT THE APP 📱")
            alertDialog.setMessage(message)
            alertDialog.setNeutralButton("CHECK UPDATE") { _, _ ->
                val updateTask = UpdateTask(ctx)
                updateTask.execute(Brian.decodeBase64(Constants.VERSION_CHECK_URL))
            }
            alertDialog.setNegativeButton("GITHUB") { _, _ ->
                Utils.openLink(ctx, Brian.decodeBase64(Constants.GITHUB_URL))
            }
            alertDialog.setPositiveButton("CLOSE", null)
            alertDialog.create().show()
        }

        fun showUpdateDialog(ctx: Context, title: String, message: String, isError: Boolean, url: String) {
            val alertDialog = buildAlertDialog(ctx, title)
            alertDialog.setMessage(message)
            if (!isError && url.isNotEmpty()) {
                alertDialog.setNegativeButton("UPDATE") { _, _ ->
                    Utils.openLink(ctx, url)
                }
            }
            alertDialog.setPositiveButton("CLOSE", null)
            alertDialog.create().show()
        }
    }
}
