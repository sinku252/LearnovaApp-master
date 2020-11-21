package com.sambhav.tws.ui.home.notes.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentNotesBinding
import com.sambhav.tws.ui.chapter.activity.ChapterActivity
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.home.notes.viewModel.NotesViewModel
import com.sambhav.tws.ui.subject.adapter.AllSubjectAdapter
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.EXTRA_KEY_ACTION
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_ID
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_NAME
import com.sambhav.tws.utils.Notes
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesFragment : BaseFragment() {
    private lateinit var mBinding: FragmentNotesBinding
    private val mViewModel: NotesViewModel by viewModel()
    private var mParentActivity: MainActivity? = null

    private var mSubList:ArrayList<SubjectData> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mParentActivity = context
            mSubList = mParentActivity?.mAllSubject ?: ArrayList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNotesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        tvTitle?.text = "Notes"
        tvSubTitle?.text = "Resources"
        ivIcon?.setImageResource(R.drawable.tool_notes)
        init()
    }


    fun init() {
        recycler_view.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimension(R.dimen.margin18dp).toInt(), true
            )
        )
        recycler_view.adapter =
            AllSubjectAdapter(requireContext(), mSubList, this)

        if (mSubList.isEmpty()) {
            mViewModel.message.set("No subject found for you")
        }
    }

    override fun onItemClick(position: Int, action: String) {

        val intent =Intent(requireActivity(), ChapterActivity::class.java)
        intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubList[position].subject_id)
        intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubList[position].subject_title)
        intent.putExtra(EXTRA_KEY_ACTION, Notes)
        startActivity(intent)
        requireActivity().overridePendingTransition(0,0)

    }
}
