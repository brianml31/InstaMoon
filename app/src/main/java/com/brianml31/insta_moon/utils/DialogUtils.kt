package com.brianml31.insta_moon.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.brianml31.insta_moon.Brian
import com.brianml31.insta_moon.InstagramMainActivity // Bu import doğru olmayabilir, kontrol et
import com.brianml31.insta_moon.R
import com.brianml31.insta_moon.ui.InstaMoonMenuBottomSheet
import com.brianml31.insta_moon.ui.InstaMoonSettingsBottomSheet
import com.brianml31.insta_moon.ui.MenuOption
import com.brianml31.insta_moon.ui.SettingOption
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

class DialogUtils {
    companion object {

        private fun getActivityContext(context: Context): FragmentActivity? {
            return context as? FragmentActivity
        }

        fun showInstaMoonOptionsDialog(ctx: Context, instagramMainActivity: InstagramMainActivity) {
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

            val bottomSheet = InstaMoonMenuBottomSheet() // Use newInstance if you make MenuOption Parcelable

             // Set listener (assuming InstagramMainActivity implements it or can delegate)
             if (instagramMainActivity is InstaMoonMenuBottomSheet.MenuOptionClickListener) {
                 bottomSheet.setMenuOptionClickListener(instagramMainActivity)
             } else {
                 // Fallback or alternative listener setup needed
                 // For now, handle clicks directly here based on ID
                 bottomSheet.setMenuOptionClickListener(object : InstaMoonMenuBottomSheet.MenuOptionClickListener {
                     override fun onMenuOptionClicked(optionId: Int) {
                         handleMenuOptionClick(ctx, instagramMainActivity, optionId)
                     }
                 })
             }

            // Pass options via arguments if Parcelable, otherwise use a workaround
            // val args = Bundle()
            // args.putParcelableArrayList("options", ArrayList(options))
            // bottomSheet.arguments = args


            bottomSheet.show(activity.supportFragmentManager, "InstaMoonMenu")
        }

         // Helper function to handle clicks if listener isn't implemented by activity
        private fun handleMenuOptionClick(ctx: Context, instagramMainActivity: InstagramMainActivity, which: Int) {
             when (which) {
                 0 -> showGhostModeDialog(ctx)
                 1 -> showExtraOptionsDialog(ctx)
                 2 -> DeveloperUtils.openDeveloperMode(ctx, instagramMainActivity)
                 3 -> FileUtils.exportJsonBackup(ctx)
                 4 -> showImportBackupDialog(ctx, instagramMainActivity) // Keep as AlertDialog for now
                 5 -> {
                     if (FileUtils.deleteMCOverrides(ctx)) {
                         ToastUtils.showShortToast(ctx, "Developer mode settings successfully cleared")
                     } else {
                         ToastUtils.showShortToast(ctx, "Error clearing commands")
                     }
                 }
                 6 -> FileUtils.saveFileIdNameMapping(ctx)
                 7 -> showAboutAppDialogDialog(ctx) // Keep as AlertDialog for now
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
                    showRestartAppDialog(ctx) // Keep as AlertDialog for now
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

                    // Show warning only if ads were just enabled
                    val oldAdsDisabled = checkedItems.getOrElse(0) { false }
                    val newAdsDisabled = newCheckedItems.getOrElse(0) { false }
                    if (!oldAdsDisabled && newAdsDisabled) {
                         showMessageDialog(ctx, "WARNING || Disable ads", "Hides ads in stories, discover, profile, etc. An ad can still appear once when refreshing the home feed")
                    }

                    PrefsUtils.savePreferencesExtraOptions(ctx, newCheckedItems)
                    showRestartAppDialog(ctx) // Keep as AlertDialog for now
                }
            })

            bottomSheet.show(activity.supportFragmentManager, "ExtraOptionsSettings")
        }

        private fun buildAlertDialog(ctx: Context, title: String): AlertDialog.Builder {
            val builder = AlertDialog.Builder(ctx) // Keep using AlertDialog for simpler dialogs
            builder.setCancelable(false)
            try {
                 builder.setIcon(Utils.getAppIcon(ctx))
            } catch (e: Exception) {
                 // Handle exception if icon retrieval fails
            }
            builder.setTitle(title)
            return builder
        }


        fun showImportBackupDialog(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(ctx, "IMPORT BACKUP")
            val options = arrayOf("Import from .JSON", "Import from .ibackup (instafel)")
            alertDialog.setItems(options) { dialog, which ->
                when (which) {
                     0 -> com.brianml31.insta_moon.InstagramMainActivity.requestFileJsonToRestore(instagramMainActivity) // Check class path
                     1 -> com.brianml31.insta_moon.InstagramMainActivity.requestFileIbackupToRestore(instagramMainActivity) // Check class path
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
            val alertDialog = buildAlertDialog(ctx, "RESTART APP")
            alertDialog.setMessage("To apply the new changes the app needs to be restarted. Press RESTART to restart.")
            alertDialog.setPositiveButton("RESTART") { _, _ ->
                val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
                val pendingIntent = PendingIntent.getActivity(ctx, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
                    // Ensure filename ends with .json
                    if (!customOutputFileName.endsWith(".json", ignoreCase = true)) {
                        customOutputFileName += ".json"
                    }

                    val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.BACKUPS_OUTPUT_FOLDER)
                    if (!directoryOutput.exists()) {
                        directoryOutput.mkdirs()
                    }
                    val fileOutput = File(directoryOutput, customOutputFileName)

                    // Check if file already exists (optional: ask user to overwrite)
                    if (fileOutput.exists()) {
                         // For now, overwrite silently or show another dialog
                    } else {
                         fileOutput.createNewFile()
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
            val message = """
            InstaMoon 🌙 ${Constants.VERSION}

            ⭒Developed by brianml31⭒

            Based on version: ${Utils.getVersionName(ctx)}

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
