package com.sambhav.tws.ui.home.classes.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.LiveClassData
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentLiveClassListBinding
import com.sambhav.tws.ui.home.classes.activities.StudentLiveClassActivity
import com.sambhav.tws.ui.home.classes.adapter.LiveClassAdapter
import com.sambhav.tws.ui.home.classes.viewModel.LiveViewModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.CustomItemVerDecoration
import getDate
import getEvent
import getScheduleTime
import kotlinx.android.synthetic.main.fragment_live_class_list.*
import org.koin.android.ext.android.get

/**
 * A simple [Fragment] subclass.
 */
class ClassesListFragment : BaseFragment() {
    private lateinit var mBinding: FragmentLiveClassListBinding
    private var mViewModel : LiveViewModel = get()
    private var adapter:LiveClassAdapter?= null
    private var mList = ArrayList<LiveClassData>()

    companion object {
        @JvmStatic
        fun newInstance(isStart: Boolean = false): ClassesListFragment {
            return ClassesListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_KEY_IS_START, isStart)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLiveClassListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        recycler_view.addItemDecoration(
            CustomItemVerDecoration(
                resources.getDimension(R.dimen.margin18dp).toInt()
            )
        )
        adapter = LiveClassAdapter(requireActivity(), mList, this)
        adapter?.isStudent = mPreference.isStudent
        recycler_view.adapter =adapter

        getDataFromViewModel()
    }

    fun getDataFromViewModel(){
        mViewModel.liveClasses.observe(requireActivity(), Observer {
            mList.clear()
            setHeader(it)
            adapter?.updateList(mList)
        })

        mViewModel.getStudentLiveClasses("2")
    }

    fun setHeader(list: ArrayList<LiveClassData>){
        mList.clear()
        for (i in list.indices){
            val classData = list[i]
            classData.schedule_date = classData.schedule_date.getDate()
            classData.schedule_time = classData.schedule_time.getScheduleTime()
            classData.date_header =
                (classData.schedule_date + " " + classData.schedule_time).getEvent()
        }

        val map: LinkedHashMap<String, ArrayList<LiveClassData>> =
            list.groupBy { it.date_header } as LinkedHashMap<String, ArrayList<LiveClassData>>

        val todayList  = map.get(TODAY_EVENT)?:ArrayList()
        val UpList  = map.get(UPCOMIG_EVENT)?:ArrayList()
        val pastList  = map.get(PAST_EVENT)?:ArrayList()

        if(todayList.isNotEmpty()){
            mList.add(LiveClassData(schedule_date = TODAY_EVENT))
            mList.addAll(todayList)
        }
        if(UpList.isNotEmpty()){
            mList.add(LiveClassData(schedule_date = UPCOMIG_EVENT))
            mList.addAll(UpList)
        }
        if(pastList.isNotEmpty()){
            mList.add(LiveClassData(schedule_date = PAST_EVENT))
            mList.addAll(pastList)
        }

        Log.d("forEach"," "+mList.size)

    }
    override fun onItemClick(position: Int, action: String) {
        when (action) {
            ACTION_VIEW -> {
                if (mPreference.isStudent) {
                    val intent = Intent(requireActivity(), StudentLiveClassActivity::class.java)
                    intent.putExtra(EXTRA_KEY_ACTION, ACTION_START)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0,0)
                }
            }
        }

    }
}
