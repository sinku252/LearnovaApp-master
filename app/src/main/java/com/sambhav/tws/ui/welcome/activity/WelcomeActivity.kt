package com.sambhav.tws.ui.welcome.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : BaseActivity() {

    private var isForStudent = true
    private var dots= ArrayList<TextView>()
    private var layouts= ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        isForStudent = intent.getBooleanExtra("FOR_STUDENT",true)
        init()
    }


    private fun init(){

        layouts = arrayListOf(
            R.layout.item_welcome_lay1,
            R.layout.item_welcome_lay1,
            R.layout.item_welcome_lay1
        )

        layouts.forEach { _ ->
            dots.add(TextView(this))
        }

        val viewPagerAdapter = com.sambhav.tws.ui.welcome.adapter.ViewPagerAdapter(isForStudent,this,layouts)
        view_pager.adapter = viewPagerAdapter

        addBottomDots(0)


        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                addBottomDots(position);
            }
        })

        btn_previous.setOnClickListener {
            if (view_pager.currentItem > 0) {
                // move to next screen
                view_pager.currentItem = view_pager.currentItem-1
            }
         }

        btn_next.setOnClickListener {
            if (view_pager.currentItem < layouts.size-1) {
                // move to next screen
                view_pager.currentItem = view_pager.currentItem+1
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("FOR_STUDENT",isForStudent)
                startActivity(intent)
                finish()
                finishAffinity()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        layoutDots.removeAllViews()
        for (i in 0 until dots.size) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35F
            dots[i].setTextColor(ContextCompat.getColor(this,R.color.colorTextNormal))
            layoutDots.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage].setTextColor(ContextCompat.getColor(this,R.color.colorLightGray))
    }
}
