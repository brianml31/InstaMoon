package com.brianml31.instamoon.utils

import X.VYl
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.brianml31.instamoon.handlers.ActivityResultHandler
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class DialogUtils {
    companion object {
        private fun builderAlertDialog(context: Context, title: String, message: String?, view: View?): AlertDialog.Builder {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setIcon(Utils.getAppIcon(context))
            builder.setTitle(title)
            if(message != null){
                builder.setMessage(message)
            }
            if(view != null){
                builder.setView(view)
            }
            return builder
        }

        fun showInstamoonOptionsDialog(context: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "InstaMoon \uD83C\uDF19", null, null)
            val isDarkMode = ThemeManager.isDarkModeEnabled(context)
            val options: Array<String> = arrayOf(
                "👻 Ghost mode",
                "⚙️ Other options",
                "🅰️ App font",
                "👨‍💻 Developer options",
                "🌙 Dark mode (" + (if (isDarkMode) "Enabled" else "Disabled") + ")",
                "ℹ️ About InstaMoon"
            )
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> showGhostModeDialog(context)
                        1 -> showOtherOptionsDialog(context)
                        2 -> showAppFontDialog(context, instagramMainActivity)
                        3 -> showDeveloperOptionsDialog(context, instagramMainActivity)
                        4 -> ThemeManager.changeTheme(instagramMainActivity)
                        5 -> showAboutAppDialog(context)
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

        private fun showGhostModeDialog(context: Context) {
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "Ghost mode 👻", null, null)
            val options: Array<String> = arrayOf(
                "Hide (Seen) in stories",
                "Hide (Seen) in DM",
                "Hide (Typing) in DM",
                "Hide (You took a screenshot) in DM",
                "Hide (Opened) in media",
                "Hide (Replayed) in media",
                "Hide (Seen) in live videos"
            )
            val checkedItems: BooleanArray = PrefsUtils.loadPreferences(context, options, PrefsUtils.arrayGhostModeKeys)
            alertDialog.setMultiChoiceItems(options, checkedItems, object : DialogInterface.OnMultiChoiceClickListener {
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
                    PrefsUtils.savePreferences(context, checkedItems, PrefsUtils.arrayGhostModeKeys)
                    showRestartAppDialog(context)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showOtherOptionsDialog(context: Context) {
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "Other options ⚙️", null, null)
            val options: Array<String> = arrayOf(
                "Disable ads",
                "Disable analytics",
                "Disable video autoplay",
                "Disable 'Like' with double tap",
                "Hide suggested reels",
                "Remove empty bottom space"
            )
            val checkedItems: BooleanArray = PrefsUtils.loadPreferences(context, options, PrefsUtils.arrayExtraOptionsKeys)
            alertDialog.setMultiChoiceItems(options, checkedItems, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
                    checkedItems[which] = isChecked
//                    if (which == 0 && isChecked) {
//                        showMessageDialog(context, "WARNING", "Hides ads in stories, discover, profile, etc. An ad can still appear once when refreshing the home feed")
//                    }
                }
            })
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("SAVE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    PrefsUtils.savePreferences(context, checkedItems, PrefsUtils.arrayExtraOptionsKeys)
                    showRestartAppDialog(context)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showAppFontDialog(context: Context, instagramMainActivity: InstagramMainActivity){
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "App font 🅰️", null, null)
            val options: Array<String> = arrayOf(
                "Default",
                "Custom (from storage)",
                "Facebook Emoji",
                "Ios Emoji",
                "Joy Pixel Emoji",
                "WhatsApp Emoji"
            )
            var selectedItem: Int = PrefsUtils.getInt("appFontSelectedItem",0)
            alertDialog.setSingleChoiceItems(options, selectedItem, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    selectedItem = which
                    when (which) {
                        1 -> {
                            FontUtils.requestFontFileToApply(instagramMainActivity)
                        }
                        2 -> {
                            FontUtils.downloadFont(context, "FacebookEmoji.ttf", UrlUtils.FACEBOOK_EMOJI)
                        }
                        3 -> {
                            FontUtils.downloadFont(context, "IosEmoji.ttf", UrlUtils.IOS_EMOJI)
                        }
                        4 -> {
                            FontUtils.downloadFont(context, "JoyPixelEmoji.ttf", UrlUtils.JOY_PIXEL_EMOJI)
                        }
                        5 -> {
                            FontUtils.downloadFont(context, "WhatsAppEmoji.ttf", UrlUtils.WHATSAPP_EMOJI);
                        }
                    }
                }
            })
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("APPLY", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (selectedItem) {
                        0 -> {
                            PrefsUtils.removeString(context, "fontPath")
                            PrefsUtils.removeString(context, "customFontPath")
                            PrefsUtils.saveInt(context, "appFontSelectedItem", selectedItem)
                            ToastUtils.showShortToast(context, "Default font selected")
                            showRestartAppDialog(context)
                        }
                        1 -> {
                            if(PrefsUtils.getString("customFontPath", null) != null){
                                PrefsUtils.saveInt(context, "appFontSelectedItem", selectedItem)
                                ToastUtils.showShortToast(context, "Custom font selected")
                                showRestartAppDialog(context)
                            }else{
                                ToastUtils.showShortToast(context, "Please select a font")
                            }
                        }
                        2 -> {
                            FontUtils.applyFont(context, selectedItem, "FacebookEmoji.ttf")
                        }
                        3 -> {
                            FontUtils.applyFont(context, selectedItem, "IosEmoji.ttf")
                        }
                        4 -> {
                            FontUtils.applyFont(context, selectedItem, "JoyPixelEmoji.ttf")
                        }
                        5 -> {
                            FontUtils.applyFont(context, selectedItem, "WhatsAppEmoji.ttf")
                        }
                    }

                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showDeveloperOptionsDialog(context: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "Dev. options 👨‍💻", null, null)
            val options: Array<String> = arrayOf(
                "👨‍💻 Open developer mode",
                "📤 Export dev settings",
                "📥 Import dev settings (.json)",
                "📥 Import dev settings (.igmoon)",
                "💾 Save mapping file",
                "📂 Import mapping file",
                "🗑️ Reset dev settings"
            )
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> {
                            //"settings_devoptions"
                            VYl.A00.A02(context, instagramMainActivity, instagramMainActivity.A05)
                        }
                        1 -> {
                            BackupUtils.exportDevSettings(context)
                        }
                        2 -> {
                            BackupManager.requestFileJsonToRestore(instagramMainActivity)
                        }
                        3 -> {
                            BackupManager.requestFileIgMoonToRestore(instagramMainActivity)
                        }
                        4 -> {
                            FileUtils.saveMappingFile(context)
                        }
                        5 -> {
                            if (!PermissionsUtils.checkPermission(instagramMainActivity)) {
                                PermissionsUtils.requestPermission(instagramMainActivity)
                            } else {
                                val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                                intent.addCategory(Intent.CATEGORY_OPENABLE)
                                intent.setType("*/*")
                                instagramMainActivity.startActivityForResult(intent, ActivityResultHandler.REQUEST_CODE_MAPPING_FILE_IMPORT)
                            }
                        }
                        6 -> {
                            showResetDevSettingsDialog(context)
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

        private fun showAboutAppDialog(context: Context) {
            showDialog(
                context,
                "About InstaMoon ℹ️",
                "InstaMoon 🌙 " + Constants.VERSION + "\n\n" +
                        "⭒Developed by brianml31⭒\n\n" +
                        "Based on version: " + Utils.getVersionName(context) + "\n\n" +
                        "Thanks to:\n" +
                        "⋆ Monserrat G\n" +
                        "⋆ Revanced\n" +
                        "⋆ Marcos shiinaider\n" +
                        "⋆ Amàzing World\n" +
                        "⋆ seven332 (library - UniFile)\n" +
                        "⋆ ACRA",
                false,
                null,
                null,
                true,
                "GITHUB",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        val github: String? = AESUtils.decryptTextWithPassword(UrlUtils.GITHUB, "InstaMoon")
                        Utils.openLink(context, github)
                    }
                },
                true,
                "CLOSE",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                }
            )
        }

        fun showDialog(
            context: Context,
            title: String,
            message: String,
            hasNeutralButton: Boolean,
            neutralButtonText: String?,
            neutralAction: DialogInterface.OnClickListener?,
            hasNegativeButton: Boolean,
            negativeButtonText: String?,
            negativeAction: DialogInterface.OnClickListener?,
            hasPositiveButton: Boolean,
            positiveButtonText: String?,
            positiveAction: DialogInterface.OnClickListener?,
        ){
            val alertDialog: AlertDialog.Builder = builderAlertDialog(
                context,
                title,
                message,
                null
            )
            if(hasNeutralButton){
                alertDialog.setNeutralButton(neutralButtonText, neutralAction)
            }
            if (hasNegativeButton) {
                alertDialog.setNegativeButton(negativeButtonText, negativeAction);
            }
            if (hasPositiveButton) {
                alertDialog.setPositiveButton(positiveButtonText, positiveAction);
            }
            alertDialog.create()
            alertDialog.show()
        }

        fun showRestartAppDialog(context: Context) {
            val alertDialog: AlertDialog.Builder = builderAlertDialog(
                context,
                "Restart application",
                "To apply the new changes, the app needs to be restarted. Press RESTART to continue.",
                null
            )
            alertDialog.setPositiveButton("RESTART", object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent: Intent? = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 123456, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.set(AlarmManager.RTC, 100L + System.currentTimeMillis(), pendingIntent)
                    System.exit(0)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showBackupExportDialog(context: Context, fileMCOverrides: File) {
            val layout: LinearLayout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(48, 32, 48, 4)
            val inputFileName: EditText = EditText(context)
            val date: String = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
            val defaultFileName: String = "InstaMoon_Backup_" + date
            inputFileName.setText(defaultFileName)
            inputFileName.hint = "Enter file name"
            inputFileName.setTextSize(16f)
            layout.addView(inputFileName)

            val inputPassword: EditText = EditText(context)
            inputPassword.hint = "Enter password (Optional)"
            inputPassword.setTextSize(16f)
            layout.addView(inputPassword)

            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "Export dev settings 📤", null, layout)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val contentFile = FileUtils.readTextFromFile(fileMCOverrides)
                    if(contentFile!=null){
                        var customOutputFileName: String = inputFileName.text.toString()
                        if (customOutputFileName.isEmpty()) {
                            customOutputFileName = defaultFileName
                        }
                        BackupUtils.exportInstaMoonBackup(customOutputFileName, inputPassword, context, contentFile)
                    } else {
                        ToastUtils.showShortToast(context, "Failed to read file")
                    }
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        fun showPasswordDialog(context: Context, contentFile: String) {
            val layout: LinearLayout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(48, 32, 48, 4)
            val inputPassword: EditText = EditText(context)
            inputPassword.hint = "Enter password"
            inputPassword.setTextSize(16f)
            layout.addView(inputPassword)
            val alertDialog: AlertDialog.Builder = builderAlertDialog(context, "Password 🔒", null, layout)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    BackupUtils.processBackupContent(context, contentFile, inputPassword.text.toString())
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showResetDevSettingsDialog(context: Context) {
            showDialog(
                context,
                "Confirm action",
                "Are you sure you want to reset the developer mode settings?",
                false,
                null,
                null,
                true,
                "Cancel",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                },
                true,
                "Confirm",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        val state: String? = FileUtils.deleteMCOverrides(context)
                        when (state) {
                            "SUCCESS" -> {
                                ToastUtils.showShortToast(context, "Developer mode settings have been reset successfully")
                            }
                            "ERROR" -> {
                                ToastUtils.showShortToast(context, "An error occurred while resetting the settings")
                            }
                            "NO_FILE" -> {
                                ToastUtils.showShortToast(context, "No developer settings file was found to reset")
                            }
                            else -> {
                                ToastUtils.showShortToast(context, "Unexpected error: " + state)
                            }
                        }
                    }
                }
            )
        }


    }
}