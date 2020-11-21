package com.sambhav.tws.ui.home.doubt.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.R
import com.sambhav.tws.databinding.FragmentDoubtBinding

class DoubtFragment : BaseFragment() {

    private lateinit var mBinding: FragmentDoubtBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDoubtBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(mPreference.isStudent){
            loadFragment(StudentDoubtRoomFragment.newInstance())
        }else{
            loadFragment(TeacherDoubtRoomFragment.newInstance())
        }
    }

    fun loadFragment(frag: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, frag)
            .commit()
    }
}
