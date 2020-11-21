package com.sambhav.tws.ui.welcome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.sambhav.tws.R
import com.sambhav.tws.utils.setIcon
import kotlinx.android.synthetic.main.item_welcome_lay1.view.*

class ViewPagerAdapter(
    val isForStudent : Boolean,
    var mContext : Context,
    var mLayouts : ArrayList<Int>
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(mLayouts[position], container, false)
        if (isForStudent){
            setIcon(view.imageView,
            R.drawable.welcome_student)
        }else{
            setIcon(view.imageView,
                R.drawable.welcome_teacher)
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return mLayouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val view = `object` as View
        container.removeView(view)
    }
}