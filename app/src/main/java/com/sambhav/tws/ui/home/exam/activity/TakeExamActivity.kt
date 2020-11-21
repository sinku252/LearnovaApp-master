package com.sambhav.tws.ui.home.exam.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityTakeExamBinding
import com.sambhav.tws.databinding.ItemQuestionBinding
import com.sambhav.tws.ui.home.exam.adapter.QuestionAdapter
import com.sambhav.tws.ui.home.exam.model.AttempQuestions
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.model.QuestionData
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.utils.*
import getAlertDailog
import kotlinx.android.synthetic.main.activity_take_exam.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TakeExamActivity : BaseActivity()
{
    private val mViewModel: ExamViewModel by viewModel()
    private var mList = ArrayList<QuestionData>()
    private var attempQuestionsList = ArrayList<AttempQuestions>()
    private lateinit var mBinding: ActivityTakeExamBinding
    private lateinit var itemQuestionBinding: ItemQuestionBinding
    private var adapter: QuestionAdapter?= null
    /*private var mSubjectId =""
    private var mSubjectName =""*/
    private var examData:ExamData?=null
    private var showPosition:Int=0
    private var page:Int=1

    private var attemptList = ArrayList<AttempQuestions>()
    var checkBoxValueList = ArrayList<String>()
    var checkBoxSelectList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_exam)


        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        /*mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""*/
        val examDataString= intent.getStringExtra(EXTRA_KEY_EXAM_DATA)?:""
        examData = Gson().fromJson(examDataString, ExamData::class.java)
        mBinding.examData=examData;
        mViewModel.getQuestionBanks(examData!!,page.toString())
        init()
        fetchFromViewModel()
        handleAuth(mViewModel)
        takeExamback.setOnClickListener(CustomClickListener {
            this.getAlertDailog("Exit", "Are you sure you want to exit exam?", {
                submitExam();onBackPressed()
            })
        })
    }


    fun init()
    {
        val hours: Int = examData!!.duration / 60 //since both are ints, you get an int
        val minutes: Int = examData!!.duration % 60
        var futureTimestamp: Long
        if(hours>0)
        {
            futureTimestamp =System.currentTimeMillis() + hours * minutes * 60 * 1000
        }
        else{
            futureTimestamp =System.currentTimeMillis() +  minutes * 60 * 1000
        }

        timerText.setEndTime(futureTimestamp)


        ll_clear_response.setOnClickListener(View.OnClickListener {
            var posi:Int=itemQuestionBinding.tvPosition.text.toString().toInt();
            if(mList.get(posi).question_type== QUESTION_TYPE_MCQ)
            {
                for (i in 0 until  itemQuestionBinding.rgExam.childCount)
                {
                    var radioButton: RadioButton=itemQuestionBinding.rgExam.getChildAt(i) as RadioButton
                    radioButton.isChecked=false
                }
            }
            else if(mList.get(posi).question_type== QUESTION_TYPE_MSQ)
            {
                for (i in 0 until  itemQuestionBinding.llExam.childCount)
                {
                    var checkBox: CheckBox=itemQuestionBinding.llExam.getChildAt(i) as CheckBox
                    checkBox.isChecked=false
                }
            }
            else if(mList.get(posi).question_type== QUESTION_TYPE_NT)
            {
                itemQuestionBinding.etOption.setText("")
            }
            mList.get(itemQuestionBinding.tvPosition.text.toString().toInt()).user_input=""
            mList.get(itemQuestionBinding.tvPosition.text.toString().toInt()).checkBox=""
            mList.get(itemQuestionBinding.tvPosition.text.toString().toInt()).radioButton=0

        })

        ll_submit_test.setOnClickListener(View.OnClickListener
        {
            submitExam()
        })

        ll_ans_later.setOnClickListener(View.OnClickListener {
            mList.get(itemQuestionBinding.tvPosition.text.toString().toInt()).answer_later=true
        })

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
                if(examData!!.test_question_count!=mList.size)
                {
                    page=page+1
                    mViewModel.getQuestionBanks(examData!!,page.toString())
                }
                else
                {
                    Toast.makeText(this, "already on Last Question", Toast.LENGTH_SHORT).show()
                }

            }
        })


    }

    override fun onItemClick(position: Int, action: String)
    {
        super.onItemClick(position, action)
        //attempQuestionsList.
        var questionData:QuestionData=mList.get(position)
        questionData.question_attempt=false
        questionData.question_attempt=true

        overridePendingTransition(0,0)
    }

    fun fetchFromViewModel(){
        mViewModel.allQuestionList.observe(this, Observer {
       //     mList.clear()

            if(!it.isEmpty())
            {
                mList.addAll(it)
                adapter?.updateList(mList)
                ShowQuestion(showPosition)
            }
            else
            {
                Toast.makeText(this,"test not started yet",Toast.LENGTH_LONG).show()
                finish()
            }

        })

        mViewModel.submitExam.observe(this, Observer {
            if (it) {
                val examDataString:String=Gson().toJson(examData)
                Toast.makeText(this, "Exam Submited", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ExamResultActivity::class.java)
            /*    intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubjectId)
                intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubjectName)*/
                intent.putExtra(EXTRA_KEY_EXAM_DATA,examDataString);
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to submit Try-Again", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun ShowQuestion(position: Int)
    {


        mBinding.llTakeExam.removeAllViews()
        val viewGroup=mBinding.llTakeExam as ViewGroup
        itemQuestionBinding= ItemQuestionBinding.inflate(layoutInflater, viewGroup, true)
        itemQuestionBinding.event = mList.get(position)
        var qNo:Int
        if(position==0)
        {
            qNo=1
        }
        else
        {
            qNo=position+1;
        }


        if(mList.get(position).question_type== QUESTION_TYPE_MCQ)
        {
            setcheckRadioButton(mList.get(position).radioButton)
        }
        else if(mList.get(position).question_type== QUESTION_TYPE_MSQ)
        {
            //val commaSeperatedString = checkBoxSelectList.joinToString { it -> "\'${it}\'" }
            if(!mList.get(position).checkBox.isEmpty())
            {
                var string:String=mList.get(position).checkBox
                val lstValues: List<String> = string.split(",").map { it -> it.trim() }
                lstValues.forEach { it ->
                    Log.i("Values", "value=$it")
                    if(itemQuestionBinding.cbOption1.id==it.toInt())
                    {
                        itemQuestionBinding.cbOption1.isChecked=true
                    }
                    else if(itemQuestionBinding.cbOption2.id==it.toInt())
                    {
                        itemQuestionBinding.cbOption2.isChecked=true
                    }
                    else if(itemQuestionBinding.cbOption3.id==it.toInt())
                    {
                        itemQuestionBinding.cbOption3.isChecked=true
                    }
                    else if(itemQuestionBinding.cbOption4.id==it.toInt())
                    {
                        itemQuestionBinding.cbOption4.isChecked=true
                    }
                    else if(itemQuestionBinding.cbOption5.id==it.toInt())
                    {
                        itemQuestionBinding.cbOption5.isChecked=true
                    }
                    //Do Something
                }
            }
        }
        else if(mList.get(position).question_type== QUESTION_TYPE_NT)
        {
            itemQuestionBinding.etOption.setText(mList.get(position).user_input.toString())
        }

        itemQuestionBinding.tvPosition.text=position.toString()
        itemQuestionBinding.tvQuestionNo.text ="Q. "+qNo

        if(mList[position].question_type.equals(QUESTION_TYPE_MCQ))
        {
            if(mList[position].option_3_text.length>0)
            {
                itemQuestionBinding.rbOption3.visibility= View.VISIBLE
            }
            else if(mList[position].option_4_text.length>0)
            {
                itemQuestionBinding.rbOption4.visibility= View.VISIBLE
            }
            else if(mList[position].option_5_text.length>0)
            {
                itemQuestionBinding.rbOption5.visibility= View.VISIBLE
            }
        }
        else if(mList[position].question_type.equals(QUESTION_TYPE_MSQ))
        {
            if(mList[position].option_3_text.length>0)
            {
                itemQuestionBinding.cbOption3.visibility= View.VISIBLE
            }
            else if(mList[position].option_4_text.length>0)
            {
                itemQuestionBinding.cbOption4.visibility= View.VISIBLE
            }
            else if(mList[position].option_5_text.length>0)
            {
                itemQuestionBinding.cbOption5.visibility= View.VISIBLE
            }
        }

        itemQuestionBinding.rgExam.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            var radioButton:RadioButton  = findViewById<View>(checkedId) as RadioButton
            Toast.makeText(baseContext, radioButton.getText(), Toast.LENGTH_SHORT).show()
            attempQuestion(radioButton.getText().toString(),radioButton.id,position)
           /* mList[position].question_attempt=true
            mList[position].user_input=radioButton.getText().toString()*/
        }
        )
        itemQuestionBinding.cbOption1.setOnClickListener(CustomClickListener {
            if(itemQuestionBinding.cbOption1.isChecked)
            {
                attempQuestion(itemQuestionBinding.cbOption1.text.toString(),itemQuestionBinding.cbOption1.id,position)
            }
            else
            {
                if(mList.get(position).user_input.contains(getValueFromCheckBox(itemQuestionBinding.cbOption1.id)))
                {
                    mList.get(position).user_input = removeExtraCommas(mList.get(position).user_input.replace(getValueFromCheckBox(itemQuestionBinding.cbOption1.id), ""))
                }

                if(mList.get(position).checkBox.contains(itemQuestionBinding.cbOption1.id.toString()))
                {
                    mList.get(position).checkBox =removeExtraCommas(mList.get(position).checkBox.replace(itemQuestionBinding.cbOption1.id.toString(), ""))
                }
            }
            Log.e("fsa","ccc"+mList.get(position).user_input)

        })
        itemQuestionBinding.cbOption2.setOnClickListener(CustomClickListener {
            if(itemQuestionBinding.cbOption2.isChecked)
            {
                attempQuestion(itemQuestionBinding.cbOption2.text.toString(),itemQuestionBinding.cbOption2.id,position)
            }
            else
            {
                if(mList.get(position).user_input.contains(getValueFromCheckBox(itemQuestionBinding.cbOption2.id)))
                {
                    mList.get(position).user_input =removeExtraCommas(mList.get(position).user_input.replace(getValueFromCheckBox(itemQuestionBinding.cbOption2.id), ""))
                }

                if(mList.get(position).checkBox.contains(itemQuestionBinding.cbOption2.id.toString()))
                {
                    mList.get(position).checkBox =removeExtraCommas(mList.get(position).checkBox.replace(itemQuestionBinding.cbOption2.id.toString(), ""))
                }
            }
            Log.e("fsa","ccc"+mList.get(position).user_input)
        })
        itemQuestionBinding.cbOption3.setOnClickListener(CustomClickListener {
            //mCallback?.onItemClick(position, binding.rbOption3.text.toString())
            if(itemQuestionBinding.cbOption3.isChecked)
            {
                attempQuestion(itemQuestionBinding.cbOption3.text.toString(),itemQuestionBinding.cbOption3.id,position)
            }
            else
            {
                if(mList.get(position).user_input.contains(getValueFromCheckBox(itemQuestionBinding.cbOption3.id)))
                {
                    mList.get(position).user_input =removeExtraCommas(mList.get(position).user_input.replace(getValueFromCheckBox(itemQuestionBinding.cbOption3.id), ""))
                }

                if(mList.get(position).checkBox.contains(itemQuestionBinding.cbOption3.id.toString()))
                {
                    mList.get(position).checkBox =removeExtraCommas(mList.get(position).checkBox.replace(itemQuestionBinding.cbOption3.id.toString(), ""))
                }
            }
        })
        itemQuestionBinding.cbOption4.setOnClickListener(CustomClickListener {
            //mCallback?.onItemClick(position, binding.rbOption4.text.toString())
            if(itemQuestionBinding.cbOption4.isChecked)
            {
                attempQuestion(itemQuestionBinding.cbOption4.text.toString(),itemQuestionBinding.cbOption4.id,position)
            }
            else
            {
                if(mList.get(position).user_input.contains(getValueFromCheckBox(itemQuestionBinding.cbOption4.id)))
                {
                    mList.get(position).user_input =removeExtraCommas(mList.get(position).user_input.replace(getValueFromCheckBox(itemQuestionBinding.cbOption4.id), ""))
                }

                if(mList.get(position).checkBox.contains(itemQuestionBinding.cbOption4.id.toString()))
                {
                    mList.get(position).checkBox =removeExtraCommas(mList.get(position).checkBox.replace(itemQuestionBinding.cbOption4.id.toString(), ""))
                }
            }
        })
        itemQuestionBinding.cbOption5.setOnClickListener(CustomClickListener {
            //mCallback?.onItemClick(position, binding.rbOption5.text.toString())
            if(itemQuestionBinding.cbOption5.isChecked)
            {
                attempQuestion(itemQuestionBinding.cbOption5.text.toString(),itemQuestionBinding.cbOption5.id,position)
            }
            else
            {
                if(mList.get(position).user_input.contains(getValueFromCheckBox(itemQuestionBinding.cbOption5.id)))
                {
                    mList.get(position).user_input =removeExtraCommas(mList.get(position).user_input.replace(getValueFromCheckBox(itemQuestionBinding.cbOption5.id), ""))
                }
                if(mList.get(position).checkBox.contains(itemQuestionBinding.cbOption5.id.toString()))
                {
                    mList.get(position).checkBox =removeExtraCommas(mList.get(position).checkBox.replace(itemQuestionBinding.cbOption5.id.toString(), ""))
                }
            }
        })

        itemQuestionBinding.etOption.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                attempQuestion(s.toString(),itemQuestionBinding.etOption.id,position)
            }
        })
        itemQuestionBinding.executePendingBindings()
    }

    fun attempQuestion(value:String,viewId:Int,position: Int)
    {
        if(mList.get(position).question_type== QUESTION_TYPE_MCQ)
        {
            mList.get(position).radioButton=viewId
            mList.get(position).user_input=getValueFromRaioButton()
        }
        else if(mList.get(position).question_type== QUESTION_TYPE_MSQ)
        {
            if(mList.get(position).user_input.isEmpty())
            {
                mList.get(position).user_input=getValueFromCheckBox(viewId)
                mList.get(position).checkBox=viewId.toString()
            }
            else
            {
                mList.get(position).user_input=mList.get(position).user_input+","+getValueFromCheckBox(viewId)
                mList.get(position).checkBox=mList.get(position).checkBox+","+viewId
            }
        }
        else if(mList.get(position).question_type== QUESTION_TYPE_NT)
        {
            mList.get(position).user_input=value
        }
        mList.get(position).question_attempt=true
        Log.e("fasff",""+mList)
    }


    fun setcheckRadioButton(viewId: Int)
    {
        if(viewId>0)
        itemQuestionBinding.rgExam.check(viewId);
        else
        itemQuestionBinding.rgExam.clearCheck()
        for (i in 0 until itemQuestionBinding.rgExam.childCount)
        {
            if(itemQuestionBinding.rgExam.get(i).id==viewId)
            {

                itemQuestionBinding.rgExam.get(i).setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_blue_back))
            }
            else
            {
                itemQuestionBinding.rgExam.get(i).setBackground(ContextCompat.getDrawable(this,R.drawable.bg_radio_back))
            }
        }
    }

    fun getValueFromCheckBox(viewId: Int):String
    {
        var temp:String="";
        for (i in 0 until itemQuestionBinding.llExam.childCount)
        {
            var checkBox: CheckBox=itemQuestionBinding.llExam.getChildAt(i) as CheckBox
            if(checkBox.id==viewId)
            {
                temp= getOptionFromIndex(i)
            }
        }
        return temp
    }


    fun getValueFromRaioButton(): String {
        if (itemQuestionBinding.rgExam.getCheckedRadioButtonId() !== -1) {
            var id: Int = itemQuestionBinding.rgExam.getCheckedRadioButtonId()
            var radioButton: View = itemQuestionBinding.rgExam.findViewById(id)
            return getOptionFromIndex(itemQuestionBinding.rgExam.indexOfChild(radioButton))
        }
        return ""
    }
    fun getOptionFromIndex(index: Int?):String {
        var temp:String="";
        if(index==0)
            temp="option_1_text"
        else if(index==1)
            temp="option_2_text"
        else if(index==2)
            temp="option_3_text"
        else if(index==3)
            temp="option_4_text"
        else if(index==4)
            temp="option_5_text"
        return temp
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do actions like show message
            submitExam()
            false
        } else super.onKeyDown(keyCode, event)
    }

    fun submitExam()
    {
        this.getAlertDailog("Submit!", "Are you sure you want to Submit exam?", {
            for (i in 0 until mList.size)
            {
                val attempQuestions = AttempQuestions(examData!!.id, mList.get(i).id, mList.get(i).user_input)
                attempQuestionsList.add(attempQuestions)
            }
            mViewModel.submitExam(attempQuestionsList,examData!!.attempt_id)
            //onBackPressed()//    startTest(examDataString)
        })

    }

}