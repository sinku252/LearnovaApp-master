package com.sambhav.tws.base

import android.app.Activity
import android.content.Context
import android.util.Log
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
 class PermissionData (private val activity: Activity){

    val mTAG = "BasePermissionActivity"
    var fromSplash = true




    fun showPermissionNeededDialog( permissions:Array<String>,success:(flag:Boolean)->Unit) {
        val permissionHandler = object : PermissionHandler() {
            override fun onGranted() {
                Log.d(mTAG, "permissionHandler onGranted")
                success(true)
            }

            override fun onDenied(context: Context?, deniedPermissions: java.util.ArrayList<String>?) {
                success(false)
            }

            override fun onJustBlocked(
                context: Context?,
                justBlockedList: java.util.ArrayList<String>?,
                deniedPermissions: java.util.ArrayList<String>?
            ) {
                success(false)
            }
        }
    Permissions.check(
        activity,
            permissions,
            null,
            null,
            permissionHandler
        )

    }
}