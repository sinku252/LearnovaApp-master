package com.sambhav.tws.ui.home.classes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentTeacherLiveMenuBinding
import com.sambhav.tws.ui.schedule.activities.ScheduleActivity
import com.sambhav.tws.ui.home.classes.adapter.TeacherSubAdapter
import com.sambhav.tws.ui.schedule.activities.ViewScheduleActivity
import com.sambhav.tws.ui.subject.adapter.SubListAdapter
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.CustomItemVerDecoration
import kotlinx.android.synthetic.main.fragment_teacher_live_menu.*
import kotlinx.android.synthetic.main.layout_spinner.*

class TeacherLiveMenuFragment : BaseFragment() {
   private var mParent: ClassFragment? = null
    var mSubjectId =""
    private lateinit var mBinding:FragmentTeacherLiveMenuBinding
    companion object {
        @JvmStatic
        fun newInstance(): TeacherLiveMenuFragment {
            return TeacherLiveMenuFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeacherLiveMenuBinding.inflate(inflater, container, false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.executePendingBindings()
        mParent = parentFragment as ClassFragment
        init()
    }

    private fun init() {

        frameSpinner?.visibility = if(mPreference.isStudent)View.GONE else View.VISIBLE
        spinner?.adapter = SubListAdapter(requireActivity(), mAllSub)
        if(mAllSub.isNotEmpty()){
            mSubjectId = mAllSub[0].subject_id
        }
        recycler_view.addItemDecoration(
            CustomItemVerDecoration(
                resources.getDimension(R.dimen.margin18dp).toInt()
            )
        )
        recycler_view.adapter = TeacherSubAdapter(
            requireContext(),
            getTeacherMenuList(), this
        )

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mSubjectId = mAllSub[p2].subject_id
            }
        }
    }

    override fun onItemClick(position: Int, action: String) {
        when (action) {
            ACTION_VIEW -> {
                val intent = Intent(activity, ViewScheduleActivity::class.java)
                intent.putExtra(EXTRA_KEY_IS_START, false)
                startActivity(intent)
            }

            ACTION_START -> {
                val intent = Intent(activity, ViewScheduleActivity::class.java)
                intent.putExtra(EXTRA_KEY_IS_START, true)
                startActivity(intent)
            }

            ACTION_ADD -> {
                spinner?.selectedItemPosition?.let {
                    val intent = Intent(activity, ScheduleActivity::class.java)
                    intent.putExtra(EXTRA_KEY_ACTION, ACTION_ADD)
                    intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
                    startActivity(intent)
                }
            }
        }
    }
}
