package com.brianml31.insta_moon.utils

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.widget.EditText
import com.brianml31.insta_moon.Brian
import com.instagram.mainactivity.InstagramMainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class DialogUtils {
    companion object {
        private fun buildAlertDialog(ctx: Context, title: String): AlertDialog.Builder {
            val builder = AlertDialog.Builder(ctx)
            builder.setCancelable(false)
            builder.setIcon(Utils.getAppIcon(ctx))
            builder.setTitle(title)
            return builder
        }

        fun showInstaMoonOptionsDialog(ctx: Context, instagramMainActivity: InstagramMainActivity) {
            val alertDialog = buildAlertDialog(ctx, "INSTA MOON \uD83C\uDF19")
            val options = arrayOf("Ghost Mode", "Extra options", "Open Developer Mode", "Export Developer Mode Settings", "Import Developer Mode Settings", "Clear Developer Mode Settings", "Save File (id_name_mapping.json)", "About the App")
            alertDialog.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> showGhostModeDialog(ctx)
                        1 -> showExtraOptionsDialog(ctx)
                        2 -> DeveloperUtils.openDeveloperMode(ctx, instagramMainActivity)
                        3 -> FileUtils.exportDevSettingsV2(ctx)
                        4 -> com.brianml31.insta_moon.InstagramMainActivity.requestFileToRestore(instagramMainActivity)
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
            val items = arrayOf("Hide Seen in Stories", "Hide Seen in Messages", "Hide Seen in Live Videos")
            val checkedItemsArray = booleanArrayOf(false, false, false)
            val checkedItems = PrefsUtils.loadPreferencesGhostMode(ctx, checkedItemsArray)
            val alertDialog = buildAlertDialog(ctx, "GHOST MODE 👻")

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
            val items = arrayOf("Disable Ads", "Disable analytics")
            val checkedItemsArray = booleanArrayOf(false, false)
            val checkedItems = PrefsUtils.loadPreferencesExtraOptions(ctx, checkedItemsArray)
            val alertDialog = buildAlertDialog(ctx, "EXTRA OPTIONS ⚙\uFE0F")

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
                    PrefsUtils.savePreferencesExtraOptions(ctx, checkedItems)
                    showRestartAppDialog(ctx)
                }
            })

            alertDialog.create()
            alertDialog.show()
        }

        fun showRestartAppDialog(ctx: Context) {
            val alertDialog = buildAlertDialog(ctx, "RESTART APP")
            alertDialog.setMessage("to apply the new changes the app needs to be restarted, press RESTART to restart")
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
            val input = EditText(ctx)
            val outputFileName = "InstaMoon_Backup_" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
            input.setPadding(48, 32, 32, 4)
            input.setTextSize(16f)
            input.setHint("Enter file name")
            input.setText(outputFileName)
            val alertDialog = buildAlertDialog(ctx, "File name:")
            alertDialog.setView(input)
            alertDialog.setNegativeButton("CLOSE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            alertDialog.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    try {
                        var fileNameInput = input.text.toString()
                        if(fileNameInput.isEmpty()){
                            fileNameInput = outputFileName
                        }
                        val directoryOutput = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.BACKUPS_OUTPUT_FOLDER)
                        if (!directoryOutput.exists()) {
                            directoryOutput.mkdirs()
                        }
                        val fileOutput = File(directoryOutput, fileNameInput+".json")
                        if (!fileOutput.exists()) {
                            fileOutput.createNewFile()
                        }
                        FileUtils.copyStream(fileMCOverrides, fileOutput)
                        ToastUtils.showShortToast(ctx, "File exported in " + fileOutput.path)
                    } catch (e: Exception) {
                        ToastUtils.showShortToast(ctx, "Error: Could not export developer mode settings")
                    }
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        private fun showAboutAppDialogDialog(ctx: Context) {
            val alertDialog = buildAlertDialog(ctx, "ABOUT THE APP \uD83D\uDCF1")
            alertDialog.setMessage("InstaMoon \uD83C\uDF19 "+Constants.VERSION+"\n\n⭒Developed by brianml31⭒\n\nBased on version: "+Utils.getVersionName(ctx)+"\n\nThanks to:\n⋆ Monserrat G\n⋆ Revanced")
            alertDialog.setNeutralButton("CHECK UPDATE", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val updateTask = UpdateTask(ctx)
                    updateTask.execute(Brian.decodeBase64(Constants.VERSION_CHECK_URL))
                }
            })
            alertDialog.setNegativeButton("GITHUB", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    Utils.openLink(ctx, Brian.decodeBase64(Constants.GITHUB_URL))
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
            val alertDialog = buildAlertDialog(ctx, title)
            alertDialog.setMessage(message)
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