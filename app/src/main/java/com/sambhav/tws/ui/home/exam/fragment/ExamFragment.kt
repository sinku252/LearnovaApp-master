package com.sambhav.tws.ui.home.exam.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentExamsBinding
import com.sambhav.tws.databinding.FragmentVideosBinding
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.home.exam.activity.ExamListActivity
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.ui.subject.adapter.AllSubjectAdapter
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ExamFragment : BaseFragment() {
    private lateinit var mBinding:FragmentExamsBinding
    private val mViewModel: ExamViewModel by viewModel()
    private var mSubList:ArrayList<SubjectData> = ArrayList()

    private var mParentActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mParentActivity = context
            mSubList = mParentActivity?.mAllSubject?: ArrayList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentExamsBinding.inflate(inflater,container,false)
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        tvTitle?.text = "Test"
        tvSubTitle?.text = "Series"
        ivIcon?.setImageResource(R.drawable.tool_video_libray)
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
            AllSubjectAdapter(
                requireContext(),
                mSubList, this
            )
        if(mSubList.isEmpty()){
            mViewModel.message.set("No subject found for you")
        }
    }

    override fun onItemClick(position: Int, action: String) {
        //mViewModel.message.set("No subject found for you"+position+action)


        val intent =Intent(requireActivity(), ExamListActivity::class.java)
        intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubList[position].subject_id)
        intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubList[position].subject_title)
        intent.putExtra(EXTRA_KEY_ACTION, Test_Series)
        startActivity(intent)
        requireActivity().overridePendingTransition(0,0)
    }
}
