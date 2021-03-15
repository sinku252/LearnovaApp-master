package com.sambhav.tws.ui.home.notes.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sambhav.tws.R
import com.sambhav.tws.base.BasePermissionActivity
import com.sambhav.tws.base.PermissionData
import com.sambhav.tws.databinding.ActivityTeacherNotesListBinding
import com.sambhav.tws.ui.home.WebViewActivity
import com.sambhav.tws.ui.home.notes.adapter.NotesListAdapter
import com.sambhav.tws.ui.home.notes.model.NotesSubListModel
import com.sambhav.tws.ui.home.notes.service.NotesDownloadService
import com.sambhav.tws.ui.home.notes.viewModel.NotesViewModel
import com.sambhav.tws.ui.subject.adapter.SubListAdapter
import com.sambhav.tws.utils.*
import getAlertDailog
import kotlinx.android.synthetic.main.activity_teacher_notes_list.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class TeacherNotesListActivity : BasePermissionActivity() {

    private var notes: NotesSubListModel? = null
    private lateinit var mBinding: ActivityTeacherNotesListBinding
    private val mViewModel: NotesViewModel by viewModel()
    private var mList = ArrayList<NotesSubListModel>()
    private var mSubjectId = ""
    private var mSubjectName = ""
    private var mChapterId = ""
    private var mChapterName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_notes_list)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID) ?: ""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME) ?: ""
        mChapterId = intent.getStringExtra(EXTRA_KEY_CHAPTER_ID) ?: ""
        mChapterName = intent.getStringExtra(EXTRA_KEY_CHAPTER_NAME) ?: ""
        observerData()
        initView()
        initClick()
        handleAuth(mViewModel)

    }

    override fun isPermissionAllowed(flag: Boolean) {
        super.isPermissionAllowed(flag)

    }

    /*override fun isPermissionAllowed(flag: Boolean) {
        if (flag) {

        }
    }*/

    private fun observerData() {
        mViewModel.allNotes.observe(this, androidx.lifecycle.Observer {
            setHeader(it)
            val manager = GridLayoutManager(this, 2)
            manager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mList[position].id == "") manager.spanCount else 1
                }
            })
            recycler_view.layoutManager = manager
            val adapter = NotesListAdapter(this, mList, this)
            adapter.isStudent = mPreference.isStudent
            recycler_view.adapter = adapter
        })
    }

    private fun initView() {
        mBinding.toolbar.tvTitle.text = "Notes &"
        mBinding.toolbar.tvSubTitle.text = "Resources"

        rootLayout?.setBackgroundResource(R.drawable.back_notes)
        btnAdd.visibility = if (mPreference.isStudent) View.GONE else View.VISIBLE

        spinner.adapter = SubListAdapter(this, mAllSub)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mSubjectId = mAllSub[p2].subject_id
                mViewModel.getNotes(mSubjectId, mChapterId)
            }
        }
    }

    private fun initClick() {
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        btnAdd.setOnClickListener(CustomClickListener {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
            startActivity(intent)
            overridePendingTransition(0, 0)
        })
    }

    override fun onItemClick(position: Int, action: String) {
        when (action) {
            ACTION_VIEW -> {
                /*val id =  mList[position].id
                if (isDocDownload(mList[position])) {
                    FileOpen.openFile(
                        this,
                        File(BASE_DOC_RECEIVE_PATH + "DOC_" +id)
                    )
                } else {

                }*/

                PermissionData(this).showPermissionNeededDialog(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    val url = mList[position].src_url
                    val browserIntent = if (url.contains(".")) {
                        val extension = mList[position].format_type
                        if (extension.equals(FT_PDF, true) || extension.equals(FT_IMG, true)) {
                            Intent(this, WebViewActivity::class.java)
                                .putExtra(EXTRA_KEY_URL, url)
                                .putExtra(EXTRA_KEY_EXTENSION, extension)
                                .putExtra(EXTRA_KEY_TITLE, mList[position].title)
                                .putExtra(EXTRA_KEY_FILE_PATH, File(BASE_DOC_RECEIVE_PATH + "DOC_" +mList[position].id).absolutePath)
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        } else {
                            Intent(Intent.ACTION_VIEW, Uri.parse(mList[position].src_url))
                        }
                    } else {
                        Intent(Intent.ACTION_VIEW, Uri.parse(mList[position].src_url))
                    }
                    startActivity(browserIntent)
                    overridePendingTransition(0, 0)
                }
                ///FileOpen.openFile(this,File(BASE_DOC_RECEIVE_PATH + "DOC_" +mList[position].id))



            }
            ACTION_DELETE -> {
                this.getAlertDailog("", "Are you sure you want to delete this Notes?", {
                    mViewModel.deleteNotes(mList[position].id)
                    mViewModel.getNotes(mSubjectId, mChapterId)
                })
            }

            ACTION_DOWNLOAD -> {
                val notes = mList[position]
                PermissionData(this).showPermissionNeededDialog(
                    permissions = arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    if (it) {
                        if (!NotesDownloadService.containsTask(notes.id ?: "")) {
                            val intentS =
                                Intent(
                                    this@TeacherNotesListActivity,
                                    NotesDownloadService::class.java
                                )
                            intentS.putExtra(EXTRA_KEY_URL, notes.src_url ?: "")
                            intentS.putExtra("id", notes.id ?: "")
                            startService(intentS)
                        }
                    }
                }

                showPermissionNeededDialog(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    fun setHeader(list: ArrayList<NotesSubListModel>) {
        mList.clear()
        for (i in list.indices) {
            /*val header = NotesSubListModel(title= list[i].created_date.compareDate())
            if(i==0){
                //mList.add(0,header )
                mList.add(NotesSubListModel(title = String.format("%s - %s",mSubjectName,mChapterName)))
            }else{
                val date = list[i].created_date.compareDate()
                val prev = list[i-1].created_date.compareDate()
                Log.d("setHeader","$i date $date prev $prev")
                if(date != prev){
                    mList.add(header)
                }
            }*/

        }
        mList.add(NotesSubListModel(title = String.format("%s - %s", mSubjectName, mChapterName)))
        mList.addAll(list)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getNotes(mSubjectId, mChapterId)
    }
}
