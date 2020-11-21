package com.sambhav.tws.ui.home.classes.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.base.BaseFragment

import com.sambhav.tws.R
import com.sambhav.tws.databinding.FragmentClassBinding

/**
 * A simple [Fragment] subclass.
 */
class ClassFragment : BaseFragment() {
    private lateinit var mBinding:FragmentClassBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.executePendingBindings()
        mBinding.toolbar.tvTitle.text = "Live"
        mBinding.toolbar.tvSubTitle.text = "Classes"
        mBinding.toolbar.ivIcon.setImageResource(R.drawable.tool_live_class)

        Log.d("mBinding","toolbar "+mBinding.toolbar?.tvTitle?.text)
        if(mPreference.isStudent){
            replaceFragment(ClassesListFragment.newInstance())
        }else{
            replaceFragment(TeacherLiveMenuFragment.newInstance())
        }
    }


    fun replaceFragment(frag:Fragment){
        childFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer,frag)
            .commit()
    }
}
