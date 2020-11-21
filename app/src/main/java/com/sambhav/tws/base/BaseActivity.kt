package com.sambhav.tws.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sambhav.tws.ui.SplashActivity
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.SharedPreferencesHelper
import getSessionDialog
import org.koin.android.ext.android.get

abstract class BaseActivity : AppCompatActivity(),BaseCallback {
    val mPreference:SharedPreferencesHelper = get()
    var mAllSub = getAllSub()
    fun getAllSub(): ArrayList<SubjectData> {
        val myType = object : TypeToken<List<SubjectData>>() {}.type
        val list = Gson().fromJson<List<SubjectData>>(mPreference.allSubject, myType)?:ArrayList()
        return list as ArrayList
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onItemClick(position: Int, action: String) {

    }

    fun toast(value: String){
        Toast.makeText(this,value,Toast.LENGTH_SHORT).show()
    }

    fun handleAuth(viewModel:BaseViewModel){
        viewModel.mAuthError.observe(this, Observer {
            if(it){
                this.getSessionDialog {
                    mPreference.clear()
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                    finishAffinity()
                }
            }
        })
    }
}
