package com.sambhav.tws.ui.profile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sambhav.tws.R
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.setUserImageRound
import kotlinx.android.synthetic.main.activity_complete_profile.*
import kotlinx.android.synthetic.main.layout_login_header.*

class CompleteProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        mSubmit.setOnClickListener(CustomClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        })

        setUserImageRound(mProfileImage,R.drawable.ic_student)
        tvTitle.text = getString(R.string.complete_title_text)
        tvTitleDes.visibility = View.GONE
    }
}
