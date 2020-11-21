package com.sambhav.tws.ui.home.exam.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivitySolutionExamBinding
import com.sambhav.tws.databinding.ItemQuestionSolutionBinding
import com.sambhav.tws.ui.home.exam.model.AttempQuestionsData
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.model.TestResultModel
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_take_exam.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SolutionActivity : BaseActivity()
{
    private val mViewModel: ExamViewModel by viewModel()
    private var mList = ArrayList<AttempQuestionsData>()
    private lateinit var mBinding: ActivitySolutionExamBinding
    private lateinit var itemQuestionSolutionBinding: ItemQuestionSolutionBinding
    private lateinit var  examData:ExamData
    private lateinit var testResultModel: TestResultModel
    private var showPosition:Int=0
    private var page:Int=1


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_solution_exam)


        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        val resultDataString= intent.getStringExtra(EXTRA_KEY_RESULT_DATA)?:""
        val examDataString= intent.getStringExtra(EXTRA_KEY_EXAM_DATA)?:""
        examData = Gson().fromJson(examDataString, ExamData::class.java)
        testResultModel = Gson().fromJson(resultDataString, TestResultModel::class.java)
        mBinding.examData=examData;
        //mViewModel.getQuestionBanks(examData!!,page.toString())
        init()
        fetchFromViewModel()
        handleAuth(mViewModel)

    }


    fun init()
    {

      //  timerText.setEndTime("00:00:00".toLong())

        tv_clear_response.setTextColor(Color.parseColor("#DADEE0"))
        tv_answer_later.setTextColor(Color.parseColor("#DADEE0"))

        ll_submit_test.visibility=View.INVISIBLE

        ll_previous_qu.setOnClickListener(View.OnClickListener {
            if(showPosition>0)
            {
                showPosition=showPosition-1
                ShowQuestion(showPosition)
            }
        })

        ll_next_qu.setOnClickListener(View.OnClickListener {
            if(showPosition!=mList.size-1)
            {
                showPosition=showPosition+1
                ShowQuestion(showPosition)
            }
            else
            {
                Toast.makeText(this, "already on Last Question", Toast.LENGTH_SHORT).show()
            }
        })


    }



    fun fetchFromViewModel(){

     /*   mList.addAll(testResultData.data?.attempQuestionsList!!)
        Log.e("fafasf",""+mList)*/
       // testResultData.data?.attempQuestionsList?.let { mList.addAll(it) }
        mList.addAll(testResultModel.attempQuestionsList!!)
       ShowQuestion(showPosition)
    }


    private fun ShowQuestion(position: Int)
    {
        mBinding.llTakeExam.removeAllViews()
        val viewGroup=mBinding.llTakeExam as ViewGroup
        itemQuestionSolutionBinding= ItemQuestionSolutionBinding.inflate(layoutInflater, viewGroup, true)
        itemQuestionSolutionBinding.event = mList.get(position)
        var qNo:Int
        if(position==0)
        {
            qNo=1
        }
        else
        {
            qNo=position+1;
        }

        itemQuestionSolutionBinding.tvPosition.text=position.toString()
        itemQuestionSolutionBinding.tvQuestionNo.text ="Q. "+qNo




        if(mList.get(position).question_type== QUESTION_TYPE_MCQ)
        {
            //setcheckRadioButton(mList.get(position).radioButton)
            if(mList.get(position).is_correct=="1")
            {
                setcheckRadioButton(mList.get(position).answer,1)
            }
            else
            {
                setcheckRadioButton(mList.get(position).answer,0)
            }


        }
        else if(mList.get(position).question_type== QUESTION_TYPE_MSQ)
        {
            if(mList.get(position).is_correct=="1")
            {
                setcheckBox(mList.get(position).answer,1)
            }
            else
            {
                setcheckBox(mList.get(position).answer,0)
            }
        }
        else if(mList.get(position).question_type== QUESTION_TYPE_NT)
        {
            itemQuestionSolutionBinding.etOption.setText(mList.get(position).answer)
        }

        itemQuestionSolutionBinding.tvSolution.text= "Solution :- "+fromHtml(mList.get(position).solution)


        itemQuestionSolutionBinding.executePendingBindings()
    }


    fun getOptionFromIndex(string: String?):Int {
        var temp:Int=0;
        if(string.equals("option_1_text",ignoreCase = true))
            temp=0
        if(string.equals("option_2_text",ignoreCase = true))
            temp=1
        if(string.equals("option_3_text",ignoreCase = true))
            temp=2
        if(string.equals("option_4_text",ignoreCase = true))
            temp=3
        if(string.equals("option_5_text",ignoreCase = true))
            temp=4
        return temp
    }

    fun setcheckRadioButton(option: String,isCorrect:Int)
    {
        var radiobtn: RadioButton=itemQuestionSolutionBinding.rgExam.getChildAt(getOptionFromIndex(option)) as RadioButton
        radiobtn.isChecked=true
        if(isCorrect==1)
        {
            radiobtn.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_green_back))
        }
        else
        {
            radiobtn.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_pink_back))
        }

    }

    fun setcheckBox(option: String,isCorrect:Int)
    {
        var result: List<Int> = option.split(",").map {
            Log.e("fasfsaf",it.trim())
        }
        val regex = "\\s*,\\s*"
        val optionList: List<String> = option.split(",").toList()
        for (i in 0 until optionList.size)
        {
            var checkBox: CheckBox=itemQuestionSolutionBinding.llExam.getChildAt(getOptionFromIndex(optionList.get(i))) as CheckBox
            checkBox.isChecked=true
            if(isCorrect==1)
            {
                checkBox.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_green_back))
            }
            else
            {
                checkBox.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_pink_back))
            }
        }

        Log.e("fasfsaf111",""+optionList)



    }





}