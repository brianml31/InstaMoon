package com.brianml31.instamoon.handlers

import android.content.Context
import android.view.View
import com.brianml31.instamoon.utils.DialogUtils
import com.instagram.mainactivity.InstagramMainActivity

class LongClickMenuHandler : View.OnLongClickListener {

    private val mainActivity : InstagramMainActivity

    constructor (instagramMainActivity: InstagramMainActivity) {
        this.mainActivity = instagramMainActivity
    }

    companion object {
        fun setLongClickMenuHandler(instagramMainActivity: InstagramMainActivity, view: View) {
            view.setOnLongClickListener(LongClickMenuHandler(instagramMainActivity))
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val context: Context = v!!.context
        val instagramMainActivity: InstagramMainActivity = this.mainActivity
        DialogUtils.showInstaMoonOptionsDialog(context, instagramMainActivity)
        return true
    }


}