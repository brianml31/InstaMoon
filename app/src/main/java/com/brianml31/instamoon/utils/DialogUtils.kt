package com.brianml31.instamoon.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

import com.instagram.mainactivity.InstagramMainActivity
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class DialogUtils {
    companion object {
        private fun buildAlertDialog(context: Context, title: String, message: String?, view: View?): AlertDialog.Builder {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setIcon(Utils.getAppIcon(context))
            builder.setTitle(title)
            if(message!=null){
                builder.setMessage(message)
            }
            if(view!=null){
                builder.setView(view)
            }
            return builder
        }

        fun showInstaMoonOptionsDialog(context: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(context, "INSTAMOON \uD83C\uDF19", null, null)
            val options = arrayOf("👻 Ghost mode", "⚙️ Extra options", "🅰️ App font", "👨‍💻 Open developer mode", "📤 Export backup", "📥 Import backup", "🧹 Clear developer mode settings", "💾 Save file (id_name_mapping.json)", "ℹ️ About the App")
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> showGhostModeDialog(context)
                        1 -> showExtraOptionsDialog(context)
                        2 -> showAppFontDialog(context, instagramMainActivity)
                        3 -> DeveloperUtils.openDeveloperMode(context, instagramMainActivity)
                        4 -> BackupUtils.exportBackup(context)
                        5 -> showImportBackupDialog(context, instagramMainActivity)
                        6 -> showClearDeveloperModeSettingsDialog(context)
                        7 -> FileUtils.saveFileIdNameMapping(context)
                        8 -> showAboutAppDialogDialog(context)
                    }
                }
            })
            alertDialog.setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showGhostModeDialog(context: Context) {
            val items = arrayOf("Hide (Seen) in stories", "Hide (Seen) in DM", "Hide (Typing) in DM", "Hide (You took a screenshot) in DM", "Hide (Opened) in media", "Hide (Replayed) in media", "Hide (Seen) in live videos")
            val checkedItems = PrefsUtils.loadPreferencesGhostMode(context)
            val alertDialog = buildAlertDialog(context, "GHOST MODE 👻", null, null)
            alertDialog.setMultiChoiceItems(items, checkedItems, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
                    checkedItems[which] = isChecked
                }
            })
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("SAVE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    PrefsUtils.savePreferencesGhostMode(context, checkedItems)
                    showRestartAppDialog(context)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showExtraOptionsDialog(context: Context) {
            val items = arrayOf("Disable ads", "Disable analytics", "Disable video autoplay", "Disable 'Like' with double tap", "Hide suggested reels")
            val checkedItems = PrefsUtils.loadPreferencesExtraOptions(context)
            val alertDialog = buildAlertDialog(context, "EXTRA OPTIONS ⚙\uFE0F", null, null)
            alertDialog.setMultiChoiceItems(items, checkedItems, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
                    checkedItems[which] = isChecked
                    if (which == 0 && isChecked) {
                        showMessageDialog(context, "WARNING", "Hides ads in stories, discover, profile, etc. An ad can still appear once when refreshing the home feed")
                    }
                }
            })
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("SAVE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    PrefsUtils.savePreferencesExtraOptions(context, checkedItems)
                    showRestartAppDialog(context)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showAppFontDialog(context: Context, instagramMainActivity: InstagramMainActivity) {
            val options = arrayOf("Select custom font", "Clear selected font", "Emoji IOS 18", "Emoji IOS 18.4", "Emoji WhatsApp", "Emoji Facebook")
            val alertDialog = buildAlertDialog(context, "APP FONT 🅰️", null, null)
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> {
                            FontUtils.requestFontFileToApply(instagramMainActivity)
                        }
                        1 -> {
                            FontUtils.clearFont(context)
                        }
                        2 -> {
                            FontUtils.downloadFont(context, "Emoji_iOS_18.ttf", UrlUtils.FONT_IOS_18)
                        }
                        3 -> {
                            FontUtils.downloadFont(context, "Emoji_iOS_18_4.ttf", UrlUtils.FONT_IOS_18_4)
                        }
                        4 -> {
                            FontUtils.downloadFont(context, "Emoji_WhatsApp.ttf", UrlUtils.FONT_WHATSAPP)
                        }
                        5 -> {
                            FontUtils.downloadFont(context, "Emoji_Facebook.ttf", UrlUtils.FONT_FACEBOOK);
                        }
                    }
                }
            })
            alertDialog.setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showImportBackupDialog(context: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(context, "IMPORT BACKUP 📥", null, null)
            val options = arrayOf("Import from .Json", "Import from .igmoon (InstaMoon)")
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> BackupManager.requestFileJsonToRestore(instagramMainActivity)
                        1 -> BackupManager.requestFileIgMoonToRestore(instagramMainActivity)
                    }
                }
            })
            alertDialog.setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showClearDeveloperModeSettingsDialog(context: Context) {
            val alertDialog = buildAlertDialog(context, "CONFIRMATION", "Do you want to proceed to clear the developer mode settings?", null)
            alertDialog.setNegativeButton("YES", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val state = FileUtils.deleteMCOverrides(context)
                    when (state) {
                        "SUCCESS" -> {
                            ToastUtils.showShortToast(context, "Developer mode settings successfully cleared")
                        }
                        "ERROR" -> {
                            ToastUtils.showShortToast(context, "Error clearing commands")
                        }
                        "NO_FILE" -> {
                            ToastUtils.showShortToast(context, "The file \"mc_overrides\" does not exist to delete it")
                        }
                        else -> {
                            ToastUtils.showShortToast(context, "Error: "+state)
                        }
                    }
                }
            })
            alertDialog.setPositiveButton("NO", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.show()
        }

        fun showMessageDialog(context: Context, title: String, message: String){
            buildAlertDialog(context, title, message, null)
                .setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                })
                .show()
        }

        fun showRestartAppDialog(context: Context) {
            val alertDialog = buildAlertDialog(context, "RESTART APP", "to apply the new changes the app needs to be restarted, press RESTART to restart", null)
            alertDialog.setPositiveButton("RESTART", object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    val pendingIntent = PendingIntent.getActivity(context, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.set(AlarmManager.RTC, 100L + System.currentTimeMillis(), pendingIntent)
                    System.exit(0)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showFileNameDialog(context: Context, fileMCOverrides: File) {
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(48, 32, 48, 4)
            //Input file name
            val inputFileName = EditText(context)
            val defaultFileName = "InstaMoon_Backup_" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
            inputFileName.setText(defaultFileName)
            inputFileName.hint = "Enter file name"
            inputFileName.setTextSize(16f)
            layout.addView(inputFileName)
            //Input password
            val inputPassword = EditText(context)
            inputPassword.hint = "Enter password (Optional)"
            inputPassword.setTextSize(16f)
            layout.addView(inputPassword)

            val alertDialog = buildAlertDialog(context, "File name and password:", null, layout)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val content = FileUtils.readTextFromFile(fileMCOverrides)
                    if(content!=null){
                        var customOutputFileName = inputFileName.text.toString()
                        if (customOutputFileName.isEmpty()) {
                            customOutputFileName = defaultFileName
                        }
                        val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.BACKUPS_OUTPUT_FOLDER)
                        if (!directoryOutput.exists()) {
                            directoryOutput.mkdirs()
                        }
                        val fileOutput = File(directoryOutput, customOutputFileName+".igmoon")
                        if (!fileOutput.exists()) {
                            fileOutput.createNewFile()
                        }
                        val hasPassword = if (inputPassword.text.toString().isEmpty()) false else true
                        val InstamoonBackupJson = BackupUtils.createInstamoonBackupJson(context, hasPassword, content, inputPassword.text.toString())
                        val state = FileUtils.writeContent(fileOutput, InstamoonBackupJson.toString())
                        if(state.equals("SUCCESS")){
                            ToastUtils.showShortToast(context, "File exported in " + fileOutput.path)
                        }else{
                            ToastUtils.showShortToast(context, "Error: " + state)
                        }
                    } else {
                        ToastUtils.showShortToast(context, "Failed to read file")
                    }
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showPasswordDialog(activity: Activity, contentBackup: String) {
            val layout = LinearLayout(activity)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(48, 32, 48, 4)

            //Input password
            val inputPassword = EditText(activity)
            inputPassword.hint = "Enter password"
            inputPassword.setTextSize(16f)
            layout.addView(inputPassword)

            val alertDialog = buildAlertDialog(activity, "Password:", null, layout)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val instamoon_backup_content = JSONObject(contentBackup).getString("instamoon_backup_content")
                    val instamoon_backup_content_decrypted = AESUtils.decryptTextWithPassword(instamoon_backup_content, inputPassword.text.toString())
                    if(instamoon_backup_content_decrypted!=null){
                        BackupUtils.applyBackupToOverrides(activity, instamoon_backup_content_decrypted)
                    }else{
                        ToastUtils.showLongToast(activity, "Error: The password is incorrect or the data is corrupted")
                    }
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showAboutAppDialogDialog(context: Context) {
            val alertDialog = buildAlertDialog(context, "ABOUT THE APP ℹ️", "InstaMoon \uD83C\uDF19 "+Constants.VERSION+"\n\n⭒Developed by brianml31⭒\n\nBased on version: "+Utils.getVersionName(context)+"\n\nThanks to:\n⋆ Monserrat G\n⋆ Revanced\n⋆ Marcos shiinaider\n⋆ Amàzing World", null)
            alertDialog.setNeutralButton("CHECK UPDATE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val updateTask = UpdateTask(context)
                    updateTask.execute(UrlUtils.CHECK_VERSION)
                }
            })
            alertDialog.setNegativeButton("GITHUB", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    Utils.openLink(context, AESUtils.decryptTextWithPassword(Constants.GITHUB_URL, "InstaMoon"))
                }
            })
            alertDialog.setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showUpdateDialog(context: Context, title: String, message: String, isError: Boolean, url: String) {
            val alertDialog = buildAlertDialog(context, title, message, null)
            if (!isError) {
                alertDialog.setNegativeButton("UPDATE", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Utils.openLink(context, url)
                    }
                })
            }
            alertDialog.setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

    }
}