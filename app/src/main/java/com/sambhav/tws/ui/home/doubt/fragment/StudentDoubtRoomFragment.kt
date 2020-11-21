package com.sambhav.tws.ui.home.doubt.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseFragment
import com.sambhav.tws.databinding.FragmentDoubtRoomStudentBinding
import com.sambhav.tws.ui.home.MainActivity
import com.sambhav.tws.ui.home.doubt.AllDoubtActivity
import com.sambhav.tws.ui.home.doubt.viewModel.DoubtViewModel
import com.sambhav.tws.ui.subject.adapter.AllSubjectAdapter
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_doubt_room_student.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentDoubtRoomFragment : BaseFragment() {
    private lateinit var mBinding: FragmentDoubtRoomStudentBinding
    private var mParentActivity: MainActivity? = null
    private var mSubList:ArrayList<SubjectData> = ArrayList()
    private val mViewModel: DoubtViewModel by viewModel()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mParentActivity = context
            mSubList = mParentActivity?.mAllSubject ?: ArrayList()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(): StudentDoubtRoomFragment {
            return StudentDoubtRoomFragment().apply {
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
        mBinding = FragmentDoubtRoomStudentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel =mViewModel
        mBinding.executePendingBindings()
        recycler_viewSub.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimension(R.dimen.margin18dp).toInt(), true
            )
        )
        recycler_viewSub.adapter =
            AllSubjectAdapter(requireContext(), mSubList, this)
        if (mSubList.isEmpty()) {
            mViewModel.message.set("No subject found for you")
        }
    }

    override fun onItemClick(position: Int, action: String) {
        super.onItemClick(position, action)
       when(action){
           ACTION_VIEW ->{
               val intent = Intent(requireActivity(), AllDoubtActivity::class.java)
               intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubList[position].subject_id)
               intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubList[position].subject_title)
               intent.putExtra("icon",mSubList[position].subject_image)
               startActivity(intent)
           }
       }
    }
}
