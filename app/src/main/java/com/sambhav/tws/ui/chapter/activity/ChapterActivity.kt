package com.sambhav.tws.ui.chapter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.geeksmediapicker.utils.visible
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityChapterBinding
import com.sambhav.tws.ui.chapter.adapter.ChapterAdapter
import com.sambhav.tws.ui.chapter.model.ChapterData
import com.sambhav.tws.ui.home.exam.activity.ExamListActivity
import com.sambhav.tws.ui.home.notes.activities.AddNotesActivity
import com.sambhav.tws.ui.home.notes.activities.TeacherNotesListActivity
import com.sambhav.tws.ui.home.videos.activities.AddVideoActivity
import com.sambhav.tws.ui.home.videos.activities.TeacherVideosListActivity
import com.sambhav.tws.ui.payemnt.BasePaymentActivity
import com.sambhav.tws.ui.schedule.viewModel.ChapterViewModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.CustomItemVerDecoration
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChapterActivity : BaseActivity() {

    private val mViewModel: ChapterViewModel by viewModel()
    private lateinit var mBinding: ActivityChapterBinding
    private var mList = ArrayList<ChapterData>()
    private var adapter:ChapterAdapter?= null
    private var mPageType =""
    private var mSubjectId =""
    private var mSubjectName =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_chapter)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mPageType = intent.getStringExtra(EXTRA_KEY_ACTION)?:""
        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""

        rootLayout.setBackgroundResource(R.drawable.back_class)
        init()
        getDataFromViewModel()
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        if (mPageType == Notes){
            btnAdd.text = getString(R.string.add_notes)
        }
        else if(mPageType== Video)
        {
            btnAdd.text = getString(R.string.add_video)
        }
        else{
            btnAdd.visibility= View.GONE;
        }


        handleAuth(mViewModel)

    }

    override fun onStart() {
        super.onStart()
        mViewModel.getChapter(mSubjectId)
        mViewModel.progress.set(mList.isEmpty())
    }

    private fun init(){

        if (mPageType == Video){
            toolbar.tvTitle.text ="Video"
            toolbar.tvSubTitle.text ="Library"
        }else if(mPageType==Notes){
            toolbar.tvTitle.text ="Notes &"
            toolbar.tvSubTitle.text ="Resources"
        }
        else
        {
            toolbar.tvTitle.text ="Test "
            toolbar.tvSubTitle.text ="Series"
        }

        recycler_view.addItemDecoration(
            CustomItemVerDecoration(
                resources.getDimension(R.dimen.margin18dp).toInt()
            )
        )

        btnAdd.setOnClickListener(CustomClickListener {
            if (btnAdd.text == Notes) {
                val intent = Intent(this, AddNotesActivity::class.java)
                intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
                startActivity(intent)
                overridePendingTransition(0, 0)
            } else {
                val intent = Intent(this, AddVideoActivity::class.java)
                intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        })

        adapter = ChapterAdapter(this, ArrayList(), this)
        recycler_view.adapter = adapter
    }

    private fun getDataFromViewModel(){
        mViewModel.chapterList.observe(this, Observer {
            mList.clear()
            mList.add(ChapterData(chapter_title = "Chapter"))
            mList.addAll(it)
            adapter?.updateList(mList)

            if (mList.size == 1 && !mPreference.isStudent){
                btnAdd.visible()
            }
        })
    }

    override fun onItemClick(position: Int, action: String) {
        super.onItemClick(position, action)
        if (mPreference.isStudent)
        {
            if (getStudentData(mPreference).is_premium == "0")
            {
                if(position==1)
                {
                    openActivity(position,action)
                }
                else
                {
                    openPaymentActivity()
                }
            }
            else
            {
                openActivity(position,action)
            }
        }
        else
        {
            openActivity(position,action)
        }

    }

    fun openPaymentActivity() {
        //startActivityForResult(Intent(this, PaymentActivity::class.java), 1000)
        startActivityForResult(Intent(this, BasePaymentActivity::class.java), 1000)
    }

    fun openActivity(position: Int, action: String)
    {
        val intent = if (mPageType == Video){
            Intent(this, TeacherVideosListActivity::class.java)
        }else if(mPageType==Notes){
            Intent(this, TeacherNotesListActivity::class.java)
        }else{
            Intent(this, ExamListActivity::class.java)
        }
        intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubjectId)
        intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubjectName)
        intent.putExtra(EXTRA_KEY_CHAPTER_ID,mList[position].chapter_id)
        intent.putExtra(EXTRA_KEY_CHAPTER_NAME,mList[position].chapter_title)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

}
