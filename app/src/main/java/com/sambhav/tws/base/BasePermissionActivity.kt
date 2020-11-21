package com.sambhav.tws.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions

abstract class BasePermissionActivity : BaseActivity(){

    val mTAG = "BasePermissionActivity"
    var fromSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun isPermissionAllowed(flag: Boolean) {}

    private val permissionHandler = object : PermissionHandler() {
        override fun onGranted() {
            Log.d(mTAG, "permissionHandler onGranted")
            isPermissionAllowed(true)
        }

        override fun onDenied(context: Context?, deniedPermissions: java.util.ArrayList<String>?) {
            isPermissionAllowed(false)
        }

        override fun onJustBlocked(
            context: Context?,
            justBlockedList: java.util.ArrayList<String>?,
            deniedPermissions: java.util.ArrayList<String>?
        ) {
            isPermissionAllowed(false)
        }
    }

    fun showPermissionNeededDialog( permissions:Array<String>) {
    Permissions.check(
            this,
            permissions,
            null,
            null,
            permissionHandler
        )

    }
}