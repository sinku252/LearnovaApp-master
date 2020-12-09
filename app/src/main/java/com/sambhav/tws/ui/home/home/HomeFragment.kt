package com.sambhav.tws.ui.home.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentHomeBinding
import com.sambhav.tws.ui.grade.adapter.GradeListAdapter
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.home.classes.fragment.ClassFragment
import com.sambhav.tws.ui.home.doubt.fragment.DoubtFragment
import com.sambhav.tws.ui.home.exam.activity.ExamListActivity
import com.sambhav.tws.ui.home.home.adapter.HomeListAdapter
import com.sambhav.tws.ui.home.home.adapter.NotificationListAdapter
import com.sambhav.tws.ui.home.home.viewModel.HomeViewModel
import com.sambhav.tws.ui.home.notes.fragments.NotesFragment
import com.sambhav.tws.ui.home.videos.fragment.VideosFragment
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_spinner.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.android.ext.android.get

//import us.zoom.sdk.*


class HomeFragment : BaseFragment(),HomeListAdapter.Callback {
    private lateinit var mBinding: FragmentHomeBinding
    private var mFragmentView: View? = null
    private var parentActivity: MainActivity? = null
    private val mViewModel : HomeViewModel = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.executePendingBindings()
        mFragmentView = view

        if(activity is MainActivity){
            parentActivity = activity as MainActivity
        }
        init()
        observerData()
    }

    private fun init() {

        toolbar.tvTitle.text = "Welcome"
        toolbar?.tvSubTitle?.text =
            if (mPreference.isStudent) "${getStudentData(mPreference).name} !" else
                "${mTeacherData.first_name} !"
        toolbar?.ivIcon?.setImageResource(R.drawable.tool_home)

        frameSpinner.visibility = if (mPreference.isStudent) View.GONE else View.VISIBLE

        recycler_view.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimension(R.dimen.margin8dp).toInt(), true
            )
        )
        val list = getMenuList(mPreference.isStudent)
        recycler_view.adapter = HomeListAdapter(requireContext(), list,this)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mPreference.gradeId = getAllGrade()[p2].grade_id
                (activity as MainActivity).viewModel.getTeacherSubject()
            }
        }
    }


    override fun onItemClick(position: Int, action: String) {

    }
    override fun onItemClick(position: Int) {
        val list =getMenuList(mPreference.isStudent)
        Log.d("onItemClick"," "+position+" "+list[position].name)
        when (list[position].name) {
            Live -> {
                //parentActivity?.replaceFragment(ClassFragment(), R.id.menu_live)
                if (mPreference.isStudent)
                {
                    if (getStudentData(mPreference).is_premium == "0")
                    {
                        parentActivity?.openPaymentActivity()
                        //openPaymentActivity()
                    }
                    else
                    {
                        parentActivity?.replaceFragment(ClassFragment(), R.id.menu_live)
                    }
                }
                else
                {
                    parentActivity?.replaceFragment(ClassFragment(), R.id.menu_live)
                }
            }
            Video -> {
                parentActivity?.replaceFragment(VideosFragment(), R.id.menu_video)
            }
            Notes -> {
                parentActivity?.replaceFragment(NotesFragment(), R.id.menu_notes)
            }
            Doubt_Room -> {
               // parentActivity?.replaceFragment(DoubtFragment(), R.id.menu_doubt)
                if (mPreference.isStudent)
                {
                    if (getStudentData(mPreference).is_premium == "0")
                    {
                        parentActivity?.openPaymentActivity()
                        //openPaymentActivity()
                    }
                    else
                    {
                        parentActivity?.replaceFragment(DoubtFragment(), R.id.menu_doubt)
                    }
                }
                else
                {
                    parentActivity?.replaceFragment(DoubtFragment(), R.id.menu_doubt)
                }
            }
            Test_Series -> {
                val intent = Intent(requireActivity(), ExamListActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(0,0)
               // parentActivity?.replaceFragment(ExamFragment(), -1)
                //login("sinkupaliwal4@gmail.com","Sinkusinku1@")
                //joinMeeting(requireActivity().applicationContext, "72832670353", "Mkht6t");
            }


        }
    }
    private fun observerData() {
        parentActivity?.mAllGrade?.observe(requireActivity(), Observer {
            spinner.adapter = GradeListAdapter(requireActivity(), getAllGrade())
        })
        mViewModel.allNotification.observe(requireActivity(), Observer {
            Log.e("ASDASDASDASDAD",it.size.toString())
            recycler_view_notification.adapter =
                NotificationListAdapter(requireContext(), it,this)
            /*(recycler_view_notification.adapter as NotificationListAdapter)
                .mList = it*/
        })
    }

    override fun onResume() {
        super.onResume()
    }
/*zoom
    fun startMeeting(context: Context?) {
        val sdk = ZoomSDK.getInstance()
        if (sdk.isLoggedIn) {
            val meetingService = sdk.meetingService
            val options = StartMeetingOptions()
            meetingService.startInstantMeeting(context, options)
        }
    }
//77725762764  7MCA57 zoom
    fun login(username: String?, password: String?) {
        val result = ZoomSDK.getInstance().loginWithZoom(username, password)
        if (result == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
            // Request executed, listen for result to start meeting
            ZoomSDK.getInstance().addAuthenticationListener(authListener)
        }
    }


    private val authListener: ZoomSDKAuthenticationListener =
        object : ZoomSDKAuthenticationListener {
            *//**
             * This callback is invoked when a result from the SDK's request to the auth server is
             * received.
             *//*
            override fun onZoomSDKLoginResult(result: Long) {
                if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong()) {
                    // Once we verify that the request was successful, we may start the meeting
                    //startMeeting(activity)
                }
            }

            override fun onZoomSDKLogoutResult(l: Long) {}
            override fun onZoomIdentityExpired() {}
            override fun onZoomAuthIdentityExpired() {}
        }


    private fun joinMeeting(context: Context,meetingNumber: String,password: String
    ) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams()
        params.displayName = "maddy" // TODO: Enter your name
        params.meetingNo = meetingNumber
        params.password = password
        meetingService.joinMeetingWithParams(context, params, options)
    }*/
}
