package com.sambhav.tws.ui.home.notes.activities


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityStudentNotesListBinding
import com.sambhav.tws.ui.home.notes.adapter.NotesDataAdapter
import com.sambhav.tws.ui.home.notes.viewModel.NotesViewModel
import com.sambhav.tws.ui.home.videos.model.CommonDataModel
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.getNotesList
import kotlinx.android.synthetic.main.activity_student_notes_list.*
import kotlinx.android.synthetic.main.layout_back.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class StudentNotesListActivity : BaseActivity() {
    private lateinit var mBinding: ActivityStudentNotesListBinding
    private val mViewModel: NotesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_notes_list)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mBinding.toolbar.tvTitle.text = "Notes &"
        mBinding.toolbar.tvSubTitle.text = "Resources"
        rootLayout?.setBackgroundResource(R.drawable.back_notes)

        initView()
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        handleAuth(mViewModel)

    }

    private fun initView() {
        val list = ArrayList<CommonDataModel>()
        list.add(CommonDataModel("Today", getNotesList()))
        list.add(CommonDataModel("Previous", getNotesList()))

        NotesDataAdapter(this, list, this).apply {
            isStudent = mPreference.isStudent
            recycler_view.adapter =this
        }
    }

    override fun onItemClick(position: Int, action: String) {

    }
}
