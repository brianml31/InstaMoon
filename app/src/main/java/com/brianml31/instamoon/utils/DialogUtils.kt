package com.brianml31.instamoon.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.brianml31.instamoon.Brian
import com.instagram.mainactivity.InstagramMainActivity
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class DialogUtils {
    companion object {
        private fun buildAlertDialog(ctx: Context, title: String, message: String?, view: View?): AlertDialog.Builder {
            val builder = AlertDialog.Builder(ctx)
            builder.setCancelable(false)
            builder.setIcon(Utils.getAppIcon(ctx))
            builder.setTitle(title)
            if(message!=null){
                builder.setMessage(message)
            }
            if(view!=null){
                builder.setView(view)
            }
            return builder
        }

        fun showInstaMoonOptionsDialog(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(ctx, "INSTAMOON \uD83C\uDF19", null, null)
            val options = arrayOf("👻 Ghost mode", "⚙️ Extra options", "👨‍💻 Open developer mode", "📤 Export backup", "📥 Import backup", "🧹 Clear developer mode settings", "💾 Save file (id_name_mapping.json)", "ℹ️ About the App")
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> showGhostModeDialog(ctx)
                        1 -> showExtraOptionsDialog(ctx)
                        2 -> DeveloperUtils.openDeveloperMode(ctx, instagramMainActivity)
                        3 -> FileUtils.exportJsonBackup(ctx)
                        4 -> showImportBackupDialog(ctx, instagramMainActivity)
                        5 -> showDeveloperModeResetConfirmation(ctx)
                        6 -> FileUtils.saveFileIdNameMapping(ctx)
                        7 -> showAboutAppDialogDialog(ctx)
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

        fun showGhostModeDialog(ctx: Context) {
            val items = arrayOf("Hide (Seen) in stories", "Hide (Seen) in DM", "Hide (Typing) in DM", "Hide (You took a screenshot) in DM", "Hide (Opened) in media", "Hide (Replayed) in media", "Hide (Seen) in live videos")
            val checkedItems = PrefsUtils.loadPreferencesGhostMode(ctx)
            val alertDialog = buildAlertDialog(ctx, "GHOST MODE 👻", null, null)
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
                    PrefsUtils.savePreferencesGhostMode(ctx, checkedItems)
                    showRestartAppDialog(ctx)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showExtraOptionsDialog(ctx: Context) {
            val items = arrayOf("Disable ads", "Disable analytics", "Disable video autoplay", "Disable 'Like' with double tap", "Hide suggested reels")
            val checkedItems = PrefsUtils.loadPreferencesExtraOptions(ctx)
            val alertDialog = buildAlertDialog(ctx, "EXTRA OPTIONS ⚙\uFE0F", null, null)
            alertDialog.setMultiChoiceItems(items, checkedItems, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
                    checkedItems[which] = isChecked
                    if (which == 0 && isChecked) {
                        showMessageDialog(ctx, "WARNING", "Hides ads in stories, discover, profile, etc. An ad can still appear once when refreshing the home feed")
                    }
                    if (which == 4 && isChecked) {
                        showMessageDialog(ctx, "EXPERIMENTAL FEATURE", "This feature is still experimental and may not work as expected")
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
                    PrefsUtils.savePreferencesExtraOptions(ctx, checkedItems)
                    showRestartAppDialog(ctx)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showImportBackupDialog(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(ctx, "IMPORT BACKUP 📥", null, null)
            val options = arrayOf("Import from .Json", "Import from .igmoon (InstaMoon)")
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> com.brianml31.instamoon.InstagramMainActivity.requestFileJsonToRestore(instagramMainActivity)
                        1 -> com.brianml31.instamoon.InstagramMainActivity.requestFileIgMoonToRestore(instagramMainActivity)
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

        private fun showDeveloperModeResetConfirmation(ctx: Context) {
            val alertDialog = buildAlertDialog(ctx, "CONFIRMATION", "Do you want to proceed to clear the developer mode settings?", null)
            alertDialog.setNegativeButton("YES", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    if (FileUtils.deleteMCOverrides(ctx)) {
                        ToastUtils.showShortToast(ctx, "Developer mode settings successfully cleared")
                    } else {
                        ToastUtils.showShortToast(ctx, "Error clearing commands")
                    }
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("NO", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.show()
        }

        fun showMessageDialog(ctx: Context, title: String, message: String){
            buildAlertDialog(ctx, title, message, null)
                .setPositiveButton("CLOSE", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                })
                .show()
        }

        fun showRestartAppDialog(ctx: Context) {
            val alertDialog = buildAlertDialog(ctx, "RESTART APP", "to apply the new changes the app needs to be restarted, press RESTART to restart", null)
            alertDialog.setPositiveButton("RESTART", object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
                    val pendingIntent = PendingIntent.getActivity(ctx, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.set(AlarmManager.RTC, 100L + System.currentTimeMillis(), pendingIntent)
                    System.exit(0)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showFileNameDialog(ctx: Context, fileMCOverrides: File) {
            val layout = LinearLayout(ctx)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(48, 32, 48, 4)

            val inputFileName = EditText(ctx)
            val defaultFileName = "InstaMoon_Backup_" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
            inputFileName.setText(defaultFileName)
            inputFileName.hint = "Enter file name"
            inputFileName.setTextSize(16f)
            layout.addView(inputFileName)

            val inputPassword = EditText(ctx)
            inputPassword.hint = "Enter password (Optional)"
            inputPassword.setTextSize(16f)
            layout.addView(inputPassword)

            val alertDialog = buildAlertDialog(ctx, "File name and password:", null, layout)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val content = FileUtils.readFile(fileMCOverrides)
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
                        val InstamoonBackupJson = BackupUtils.createInstamoonBackupJson(ctx, hasPassword, content, inputPassword.text.toString())
                        val state = FileUtils.writeContent(fileOutput, InstamoonBackupJson.toString())
                        if(state.equals("SUCCESS")){
                            ToastUtils.showShortToast(ctx, "File exported in " + fileOutput.path)
                        }else{
                            ToastUtils.showShortToast(ctx, "Error: " + state)
                        }
                    } else {
                        ToastUtils.showShortToast(ctx, "Failed to read file")
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
                    val mc_overrides = FileUtils.loadMCOverridesFile(activity)
                    val instamoon_backup_content = JSONObject(contentBackup).getString("instamoon_backup_content")
                    val instamoon_backup_content_decrypted = AESUtils.decrypt(instamoon_backup_content, inputPassword.text.toString())
                    if(instamoon_backup_content_decrypted!=null){
                        val state = FileUtils.writeContent(mc_overrides, instamoon_backup_content_decrypted)
                        if(state.equals("SUCCESS")){
                            ToastUtils.showShortToast(activity, "The backup was imported successfully")
                            showRestartAppDialog(activity)
                        }else{
                            ToastUtils.showShortToast(activity, "Error: " + state)
                        }
                    }else{
                        ToastUtils.showLongToast(activity, "Error: The password is incorrect or the data is corrupted")
                    }

                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showAboutAppDialogDialog(ctx: Context) {
            val alertDialog = buildAlertDialog(ctx, "ABOUT THE APP ℹ️", "InstaMoon \uD83C\uDF19 "+Constants.VERSION+"\n\n⭒Developed by brianml31⭒\n\nBased on version: "+Utils.getVersionName(ctx)+"\n\nThanks to:\n⋆ Monserrat G\n⋆ Revanced\n⋆ Marcos shiinaider\n⋆ Amàzing World", null)
            alertDialog.setNeutralButton("CHECK UPDATE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val updateTask = UpdateTask(ctx)
                    updateTask.execute(Brian.hexToText(Constants.VERSION_CHECK_URL))
                }
            })
            alertDialog.setNegativeButton("GITHUB", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    Utils.openLink(ctx, Brian.hexToText(Constants.GITHUB_URL))
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

        fun showUpdateDialog(ctx: Context, title: String, message: String, isError: Boolean, url: String) {
            val alertDialog = buildAlertDialog(ctx, title, message, null)
            if (!isError) {
                alertDialog.setNegativeButton("UPDATE", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Utils.openLink(ctx, url)
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