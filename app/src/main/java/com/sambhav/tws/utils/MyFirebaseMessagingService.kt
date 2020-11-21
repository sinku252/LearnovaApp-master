package com.sambhav.tws.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.sambhav.tws.MyApp
import com.sambhav.tws.ui.home.doubt.model.MessageEvent
import com.sambhav.tws.ui.home.doubt.model.NotificationModel
import org.greenrobot.eventbus.EventBus

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("MyFirebaseMessaging",p0.data.toString())

        p0.data.let {
            val msgData = Gson().toJson(it)
            val model = Gson().fromJson(msgData,NotificationModel::class.java)
            if(model.type == NOTIFICATION_TYPE_KEY_DOUBT) {
                if (MyApp.isAppInForground) {
                    EventBus.getDefault().post(MessageEvent(msgData))
                } else {
                    this.showNotification(model)
                }
            }else{
                this.showNotification(model)
            }
        }
    }

}