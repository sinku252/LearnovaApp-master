package com.sambhav.tws.ui.home.doubt.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentDoubtRoomTeacherBinding
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.home.doubt.AllDoubtActivity
import com.sambhav.tws.ui.home.doubt.adapter.DoubtRoomAdapter
import com.sambhav.tws.ui.home.doubt.model.DoubtRoomModel
import com.sambhav.tws.ui.home.doubt.viewModel.DoubtViewModel
import com.sambhav.tws.ui.subject.adapter.SubListAdapter
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_ID
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_NAME
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_doubt_room_teacher.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class TeacherDoubtRoomFragment : BaseFragment() {
    private lateinit var mBinding: FragmentDoubtRoomTeacherBinding
    private val mViewModel: DoubtViewModel by viewModel()
    private var mList: ArrayList<DoubtRoomModel> = ArrayList()
    var mAdapter: DoubtRoomAdapter? = null
    private var mParentActivity: MainActivity? = null
    private var mSubList: ArrayList<SubjectData> = ArrayList()

    private var mSubjectId = ""
    private var mSubjectName = ""
    private var mSubjectIcon = ""

    companion object {
        @JvmStatic
        fun newInstance(): TeacherDoubtRoomFragment {
            return TeacherDoubtRoomFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

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
        mBinding = FragmentDoubtRoomTeacherBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.executePendingBindings()
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        mAdapter = DoubtRoomAdapter(requireActivity(), mList, this)
        spinner.adapter = SubListAdapter(requireActivity(), mSubList)
        recycler_view.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimension(R.dimen.margin18dp).toInt(), true
            )
        )
        recycler_view.adapter = mAdapter

        initClick()

        if (mSubList.isNotEmpty()) {
            mSubjectId = mSubList[0].subject_id
            mSubjectName = mSubList[0].subject_title
            mSubjectIcon = mSubList[0].subject_image
        }
        fetchDataFromViewModel()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllDoubtRooms(mSubjectId)
        mViewModel.progress.set(mList.isEmpty())
    }

    private fun initClick() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mSubjectId = mSubList[p2].subject_id
                mSubjectName = mSubList[p2].subject_title
                mSubjectIcon = mSubList[p2].subject_image
                mViewModel.getAllDoubtRooms(mSubjectId)
            }
        }
    }

    private fun fetchDataFromViewModel() {

        mViewModel.allDoubtRooms.observe(viewLifecycleOwner, Observer {
            mList = it
            mAdapter?.updateList(mList)
            if (mList.isEmpty()) {
                mViewModel.message.set("No doubt room found!!")
            } else {
                mViewModel.message.set("")
            }
        })

    }

    override fun onItemClick(position: Int, action: String) {

        when (action) {
            ACTION_VIEW -> {
                val intent = Intent(requireActivity(), AllDoubtActivity::class.java)
                intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
                intent.putExtra(EXTRA_KEY_SUBJECT_NAME, mList[position].name)
                intent.putExtra("icon", mList[position].profile_image)
                intent.putExtra("docId", mList[position].document_library_id)
                intent.putExtra("roomId", mList[position].commenter_id)
                intent.putExtra("created_by", mList[position].created_by)
                startActivity(intent)
            }
        }
    }
}
