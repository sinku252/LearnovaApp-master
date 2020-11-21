package com.sambhav.tws.utils

import android.util.Log
import com.sambhav.tws.BuildConfig

class LogUtils {
    companion object{
        @JvmStatic
        fun e(key:String,value:String){
            if(BuildConfig.LOG){
                Log.e(key,value)
            }
        }
        @JvmStatic
        fun d(key:String,value:String){
            if(BuildConfig.LOG){
                Log.d(key,value)
            }
        }
        @JvmStatic
        fun i(key:String,value:String){
            if(BuildConfig.LOG){
                Log.i(key,value)
            }
        }
    }
}