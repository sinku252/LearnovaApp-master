package com.sambhav.tws.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.login.LoginActivity
import com.sambhav.tws.ui.welcome.activity.WelcomeActivity
import com.sambhav.tws.utils.CustomClickListener
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (mPreference.isLogin) {
            frame.visibility = View.VISIBLE
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            frame.visibility = View.GONE
        }

        cardApp.setOnClickListener(CustomClickListener {

            if (!mPreference.studentWelcome) {
                mPreference.studentWelcome = true

                startActivity(
                    Intent(this, WelcomeActivity::class.java)
                        .putExtra("FOR_STUDENT", true)
                )
            } else {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .putExtra("FOR_STUDENT", true)
                )
            }
        })

        cardLive.setOnClickListener(CustomClickListener {
            if (!mPreference.teacherWelcome) {
                mPreference.teacherWelcome = true
                startActivity(
                    Intent(this, WelcomeActivity::class.java)
                        .putExtra("FOR_STUDENT", false)
                )
            } else {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .putExtra("FOR_STUDENT", false)
                )
            }

        })
    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }
}
