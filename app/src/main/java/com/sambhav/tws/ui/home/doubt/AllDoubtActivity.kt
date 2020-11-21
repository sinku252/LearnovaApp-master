package com.sambhav.tws.ui.home.doubt

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.geeksmediapicker.GeeksMediaPicker
import com.geeksmediapicker.GeeksMediaType
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.DoubtData
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityAllDoubtBinding
import com.sambhav.tws.ui.home.doubt.adapter.AllDoubtAdapter
import com.sambhav.tws.ui.home.doubt.model.MessageEvent
import com.sambhav.tws.ui.home.doubt.model.NotificationModel
import com.sambhav.tws.ui.home.doubt.viewModel.DoubtViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_all_doubt.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.collections.ArrayList


class AllDoubtActivity : BaseActivity() {
    private lateinit var mBinding: ActivityAllDoubtBinding

    private val mViewModel: DoubtViewModel by viewModel()
    private var mList = ArrayList<DoubtData>()
    private var adapter: AllDoubtAdapter?= null
    private var mLoadMore = false
    private var mSubjectId =""
    private var mSubjectName =""
    private var mRoomId =""
    private var mPage = 1
    private var mDocId = ""
    private val receiverId :String by lazy { intent.getStringExtra("created_by")?:"" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_all_doubt)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""
        mRoomId = intent.getStringExtra("roomId")?:""
        val iconUrl = intent.getStringExtra("icon")?:""

        setUserImageRound(iv_sub,iconUrl)
        tvHeader.text = mSubjectName
        adapter = AllDoubtAdapter(this, ArrayList(),this)
        recycler_view.adapter = adapter

        getDataFromViewModel()
        mDocId = intent.getStringExtra("docId")?:""

        mViewModel.getAllDoubts(mSubjectId,"$mRoomId",mPage.toString(),mDocId)
        initClick()

        handleAuth(mViewModel)

    }

    private fun initClick() {

        back.setOnClickListener {
            onBackPressed()
        }

        iv3.setOnClickListener(CustomClickListener {
            if (!TextUtils.isEmpty(etMsg.text.toString().trim())) {

                createDoubt("", mRoomId)
                etMsg.setText("")
            }
        })

        iv_attach.setOnClickListener(CustomClickListener {
            GeeksMediaPicker.with(this)
                .setMediaType(GeeksMediaType.IMAGE)
                .setIncludesFilePath(true)
                .setEnableCompression(true)
                .startSingle { mediaStoreData ->
                    val fileUri = mediaStoreData.media_path
                    val file = File(fileUri)
                    mViewModel.uploadFile(file)
                }
        })
    }

    private fun createDoubt(url:String="",roomId:String) {
        val map = JsonObject()
        map.addProperty("subject_id",mSubjectId)
        map.addProperty("grade_id",mPreference.gradeId)
        map.addProperty("comments",etMsg.text.toString().trim())
        map.addProperty(API_KEY_LIBRARY_ID,mDocId)
        map.addProperty("app_token",mPreference.firebaseToken)
        map.addProperty("src_url",url)
        map.addProperty("method","")
        if(mPreference.isStudent){
            map.addProperty("school_id", getStudentData(mPreference).school_id)
            map.addProperty("student_id", getStudentData(mPreference).id)
            map.addProperty("commenter_type", TYPE_STUDENT)
            map.addProperty("commenter_id",getStudentData(mPreference).id)
        }else{
            map.addProperty("school_id", getTeacherData(mPreference).school_id)
            map.addProperty("commenter_type", TYPE_TEACHER)
            map.addProperty("student_id", receiverId)
            map.addProperty("commenter_id", getTeacherData(mPreference).id)
        }
        if(url ==""){
            map.addProperty("attachment_type",TYPE_TEXT)
        }else{
            map.addProperty("attachment_type", TYPE_IMAGE)
        }
        mViewModel.createDoubt(map)
    }

    fun getDataFromViewModel(){
        mViewModel.allDoubts.observe(this, Observer {
            Log.d("ItemClick","allDoubts ${it.size} "+mLoadMore)

            if(mList.isNotEmpty()){
                it.reverse()

                if(it.isNotEmpty()){
                    mList.addAll(0,it)
                    adapter?.updateList(mList)
                    recycler_view.scrollToPosition(it.size)

                }
            }else{
                mList = it
                mList.reverse()
                adapter?.updateList(mList)
                recycler_view.scrollToPosition(mList.size-1)
            }

            mLoadMore = it.size >= 20
            Log.d("ItemClick","allDoubts observers $mPage ${mList.size} "+mLoadMore)

        })

        mViewModel.doubt.observe(this, Observer {
            mList.add(it)
            adapter?.updateList(mList)
            recycler_view.scrollToPosition(mList.size-1)
        })

        mViewModel.uploadFileUrl.observe(this, Observer {
            createDoubt(it,mRoomId)
        })
    }

    override fun onItemClick(position: Int, action: String) {
        super.onItemClick(position, action)
        if (position == 0 && action == "LOAD_MORE" && mLoadMore){
            mPage++
            mViewModel.getAllDoubts(mSubjectId,"$mRoomId",mPage.toString(),mDocId)
            mViewModel.progress.set(false)
            mLoadMore=false
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        val mNotificationModel = Gson().fromJson(event.msg,NotificationModel::class.java)
        if (mNotificationModel.subject_id != mSubjectId){
            this.showNotification(mNotificationModel)
           return
        }
        if(!mPreference.isStudent && receiverId != mNotificationModel.sender_id){
            this.showNotification(mNotificationModel)
            return
        }

        mList.clear()
        mViewModel.getAllDoubts(mSubjectId,"$mRoomId",1.toString(),mDocId)
        mViewModel.progress.set(false)

       /* val doubt = DoubtData(
            comments = mNotificationModel.body,
            attachment_type = mNotificationModel.type,
            commenter_type = mNotificationModel.sender_type,
            commenter_id = mNotificationModel.sender_id,
            document_library_id = mNotificationModel.document_library_id,
            grade_id = mNotificationModel.grade_id,
            school_id = mNotificationModel.school_id,
            subject_id = mNotificationModel.subject_id
        )
        mList.add()*/
    }
}
