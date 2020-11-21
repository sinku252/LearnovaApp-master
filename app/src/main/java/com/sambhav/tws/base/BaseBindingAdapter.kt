package com.sambhav.tws.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sambhav.tws.utils.SharedPreferencesHelper

abstract class BaseBindingAdapter(val context:Context?= null) : RecyclerView.Adapter<BaseViewHolder>() {
    var mPref:SharedPreferencesHelper ?= null
    var mStudent:Boolean= true

    init {
        context?.let {
            val PREF_NAME = "com.dagger"
            val MODE = Context.MODE_PRIVATE
            mPref = SharedPreferencesHelper(it.getSharedPreferences(PREF_NAME,MODE))
        }

        mStudent = mPref?.isStudent?:true
    }
    fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(position)
    }
}