package com.sambhav.tws.ui.home.notes.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BasePermissionActivity
import com.sambhav.tws.base.PermissionData
import com.sambhav.tws.databinding.ActivityAddNotesBinding
import com.sambhav.tws.ui.grade.adapter.ChapterListAdapter
import com.sambhav.tws.ui.home.notes.viewModel.NotesViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.koin.android.ext.android.get
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class AddNotesActivity : BasePermissionActivity() {
    private lateinit var mBinding: ActivityAddNotesBinding
    private var mViewModel : NotesViewModel = get()
    private var file: File?= null

    private var mSubjectId =""
    private var mUploadFileUrl =""
    private var mUploadFileType =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_notes)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        mBinding.toolbar.tvTitle.text = "Notes &"
        mBinding.toolbar.tvSubTitle.text = "Resources"
        rootLayout?.setBackgroundResource(R.drawable.back_notes)

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mViewModel.getTeacherChapters(mSubjectId)


        initClick()
        fetchFromViewModel()
        handleAuth(mViewModel)

    }

    private fun fetchFromViewModel(){
        mViewModel.uploadData.observe(this, Observer {
            if(it){
                Toast.makeText(this,"Document uploaded",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"Failed to uploaded Try-Again",Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.uploadFileUrl.observe(this, Observer {
            tv_upload_progress.text = "Uploading 100%"
            mUploadFileUrl = it
            setFileType(it)
        })
        mViewModel.mErrorMessage.observe(this, Observer {
            toast(it)
        })
        mViewModel.allChapter.observe(this, Observer {
            spinner.adapter = ChapterListAdapter(this, it)
        })
    }

    private fun initClick() {

        checkboxUpload.text = getString(R.string.upload_notes_from_library_less_than)
            .replace("MAX_SIZE", getTeacherData(mPreference).max_upload_size)
        checkboxUrl.setOnCheckedChangeListener { compoundButton, b ->
            et_url.visibility = if(b) View.VISIBLE else View.GONE
            lay_upload.visibility = if(b) View.GONE else View.VISIBLE
        }
        checkboxSelectChapter.setOnCheckedChangeListener { _, b ->
            chapter_spinner.visibility = if(b) View.VISIBLE else View.GONE
            et_chaptername.visibility = if(b) View.GONE else View.VISIBLE
        }
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        btn_cancel.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        btn_publish.setOnClickListener(CustomClickListener {
            if (checkValidation()) {
                addDocument()
                /*this.getFullScreenDialog(R.layout.dialog_class).apply {
                    show()
                    btn_submit.setOnClickListener(DebounceClickListener {
                        dismiss()
                    })
                    btn_notSubmit.setOnClickListener(DebounceClickListener {
                        dismiss()
                    })
                }*/
            }
        })
        picker_document.setOnClickListener(CustomClickListener {
            if (TextUtils.isEmpty(et_name.text.toString())) {
                toast("Fill resource name")
                return@CustomClickListener
            }
            PermissionData(this).showPermissionNeededDialog(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                if (it) {
                    val intent = getFileChooserIntent()
                    try {
                        startActivityForResult(
                            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), DocumnetRequest
                        )
                    } catch (ex: Exception) {
                        // Potentially direct the user to the Market with a Dialog
                        toast("Please install a File Manager.")
                    }
                    Log.e("IS_PERMISSION_ALLOWED", it.toString())
                }
            }
        })
    }

    private fun checkValidation():Boolean{
        if(checkboxUrl.isChecked){
            if(et_url.text.isNullOrEmpty()){
                toast("Enter source url")
                return false
            }
            if(!Patterns.WEB_URL.matcher(et_url.text.toString()).matches()){
                toast("Enter valid url")
                return false
            }
        }else{
            if(checkboxUpload.isChecked){
                if(TextUtils.isEmpty(mUploadFileUrl)){
                    toast("Upload a document")
                    return false
                }
            }
        }
        if (!checkboxSelectChapter.isChecked){
            if(et_chaptername.text.isNullOrEmpty()){
                toast("Enter chapter name")
                return false
            }
        }
        if(et_name.text.isNullOrEmpty()){
            toast("Enter resource name")
            return false
        }

        return true
    }

    private fun addDocument(){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(mPreference).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(mPreference).id)
        map.addProperty(API_KEY_SUBJECT_ID,mSubjectId)
        map.addProperty(API_KEY_GRADE_ID,mPreference.gradeId)
        map.addProperty(API_KEY_METHOD, UPLOAD)
        map.addProperty(API_KEY_TITLE, et_name.text.toString())
        map.addProperty(API_KEY_CHAPTER_ID,if (checkboxSelectChapter.isChecked)
            (spinner.adapter as ChapterListAdapter).mList[spinner.selectedItemPosition].chapter_id
        else "")
        map.addProperty(API_KEY_CHAPTER_TITLE,if (checkboxSelectChapter.isChecked) ""
        else et_chaptername.text.toString())
        map.addProperty(API_KEY_FORMATE_TYPE, mUploadFileType)
        map.addProperty(API_KEY_SRC_URL, if (checkboxUpload.isChecked)
            mUploadFileUrl
        else
            et_url.text.toString())
        map.addProperty(API_KEY_LENGTH, "")
        map.addProperty(API_KEY_SIZE, "")
        mViewModel.uploadNotes(map)
    }

    override fun isPermissionAllowed(flag: Boolean) {
        super.isPermissionAllowed(flag)

    }

    private fun getFileChooserIntent(): Intent? {
        val mimeTypes =
            arrayOf("image/*", "application/pdf","application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
        if (mimeTypes.isNotEmpty()) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        return intent
    }

    private fun setFileType(url:String){
        val extension: String = url.substring(url.lastIndexOf(".")+1)
        mUploadFileType = if (extension == "jpg"
            || extension == "jpeg"
            || extension == "tif"
            || extension == "png"){
            FT_IMG
        }else if(extension == "pdf"){
            FT_PDF
        }else if(extension == "docx"){
            FT_DOC
        }else{
            ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when (requestCode) {
                DocumnetRequest -> {
                    if (data != null) {
                        tv_upload_progress.text = "Uploading"
                        val file = File(FileUtils.getPath(this, data.data))
                        mViewModel.uploadFile(file)
                    }
                }
            }
        }
    }
}
