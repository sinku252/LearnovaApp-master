package com.sambhav.tws.di.module

import android.content.Context
import com.sambhav.tws.utils.SharedPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single {
            val PREF_NAME = "com.dagger"
        val MODE = Context.MODE_PRIVATE
        androidContext().getSharedPreferences(PREF_NAME, MODE)
    }

    single {
        SharedPreferencesHelper(get())
    }

}