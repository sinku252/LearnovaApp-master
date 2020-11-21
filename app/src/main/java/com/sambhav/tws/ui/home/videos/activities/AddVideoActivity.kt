package com.sambhav.tws.ui.home.videos.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.geeksmediapicker.GeeksMediaPicker
import com.geeksmediapicker.GeeksMediaType
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityAddVideoBinding
import com.sambhav.tws.ui.grade.adapter.ChapterListAdapter
import com.sambhav.tws.ui.home.videos.viewModel.VideoViewModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_add_video.*
import kotlinx.android.synthetic.main.activity_add_video.back
import kotlinx.android.synthetic.main.activity_add_video.btn_cancel
import kotlinx.android.synthetic.main.activity_add_video.btn_publish
import kotlinx.android.synthetic.main.activity_add_video.chapter_spinner
import kotlinx.android.synthetic.main.activity_add_video.checkboxSelectChapter
import kotlinx.android.synthetic.main.activity_add_video.checkboxUrl
import kotlinx.android.synthetic.main.activity_add_video.et_chaptername
import kotlinx.android.synthetic.main.activity_add_video.et_name
import kotlinx.android.synthetic.main.activity_add_video.et_url
import kotlinx.android.synthetic.main.activity_add_video.lay_upload
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.regex.Pattern


class AddVideoActivity : BaseActivity(){
    private val mViewModel: VideoViewModel by viewModel()

    private lateinit var mBinding: ActivityAddVideoBinding
    private var mSubjectId = ""
    private var file: File? = null
    private var mUploadFileUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_video)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID) ?: ""

        initView()
        initClick()

        mViewModel.getTeacherChapters(mSubjectId)
        fetchFromViewModel()
        handleAuth(mViewModel)

    }

    override fun onBackPressed() {
        if(mViewModel.progress.get()){

        }else{
            super.onBackPressed()
        }
    }
    private fun fetchFromViewModel() {
        mViewModel.uploadData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Video uploaded", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to uploaded Try-Again", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.allChapter.observe(this, Observer {
            spinner.adapter = ChapterListAdapter(this, it)
        })

        mViewModel.uploadFileUrl.observe(this, Observer {
            tv_file_name.text = "Uploading 100%"
            mUploadFileUrl = it
            //setFileType(it)
        })
    }

    private fun initView() {
        mBinding.toolbar.tvTitle.text = "Video &"
        mBinding.toolbar.tvSubTitle.text = "Library"
        rootLayout?.setBackgroundResource(R.drawable.back_video)
    }

    private fun initClick() {
        checkboxPdf.text = getString(R.string.upload_video_from_library_less_than)
            .replace("MAX_SIZE", getTeacherData(mPreference).max_upload_size)

        checkboxPdf.setOnCheckedChangeListener { compoundButton, b ->
            lay_upload.visibility = if (b) View.VISIBLE else View.GONE
        }

        checkboxUrl.setOnCheckedChangeListener { compoundButton, b ->
            et_url.visibility = if (b) View.VISIBLE else View.GONE
            lay_upload.visibility = if (b) View.GONE else View.VISIBLE
        }
        checkboxSelectChapter.setOnCheckedChangeListener { _, b ->
            chapter_spinner.visibility = if (b) View.VISIBLE else View.GONE
            et_chaptername.visibility = if (b) View.GONE else View.VISIBLE
        }
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        btn_cancel.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        iv_picker.setOnClickListener(CustomClickListener {
            if (TextUtils.isEmpty(et_name.text.toString())) {
                toast("Fill video name")
                return@CustomClickListener
            }
            GeeksMediaPicker.with(this)
                .setMediaType(GeeksMediaType.VIDEO)
                .setIncludesFilePath(true)
                .startSingle { mediaStoreData ->
                    val videoUri = mediaStoreData.media_path
                    file = File(videoUri)
                    try
                    {
                        mViewModel.uploadFile(file!!);

                    }
                    catch (e:Exception)
                    {
                        e.printStackTrace()
                    }




                    tv_file_name.text = "Uploading"
                    Log.d("GeeksMediaPicker", " " + file?.path)
                }
        })
        btn_publish.setOnClickListener(CustomClickListener {
            if (checkValidation()) {
                if (this@AddVideoActivity.checkboxUrl?.isChecked == true) {
                    addVideo(this@AddVideoActivity.et_url.text.toString())
                } else {
                    addVideo()
                }
            }

        })
    }

    private fun uploadFile(link: String? = "") {
      /*  VimeoService.uploadFile(
            get(),
            link ?: "",
            file!!
        ) {
            runOnUiThread {
                tv_file_name.text = "Uploading 100%"
                mViewModel.progress.set(false)
                addVideo(mUploadFileUrl)
            }
        }*/
    }

    private fun checkValidation(): Boolean {
        if (checkboxUrl.isChecked) {
            if (et_url.text.isNullOrEmpty()) {
                toast("Enter source url")
                return false
            }
            if (!Patterns.WEB_URL.matcher(et_url.text.toString()).matches()) {
                toast("Enter valid url")
                return false
            }
        } else {
            if (checkboxPdf.isChecked) {
                if (file == null || file?.exists() != true) {
                    toast("Upload a document")
                    return false
                }
            }
        }
        if (!checkboxSelectChapter.isChecked) {
            if (et_chaptername.text.isNullOrEmpty()) {
                toast("Enter chapter name")
                return false
            }
        }
        if (et_name.text.isNullOrEmpty()) {
            toast("Enter resource name")
            return false
        }
        return true
    }

    private fun addVideo(url:String = "") {
        var url1="";
        if (this@AddVideoActivity.checkboxUrl?.isChecked == true)
            url1=et_url.text.toString()
        else
            url1= mUploadFileUrl

        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(mPreference).school_id)
        map.addProperty(API_KE_TEACHER_ID, getTeacherData(mPreference).id)
        map.addProperty(API_KEY_SUBJECT_ID, mSubjectId)
        map.addProperty(API_KEY_GRADE_ID,mPreference.gradeId)
        map.addProperty(
            API_KEY_CHAPTER_ID, if (checkboxSelectChapter.isChecked)
                (spinner.adapter as ChapterListAdapter).mList[spinner.selectedItemPosition].chapter_id
            else ""
        )
        map.addProperty(
            API_KEY_CHAPTER_TITLE, if (checkboxSelectChapter.isChecked) ""
            else et_chaptername.text.toString()
        )
        map.addProperty(API_KEY_METHOD, UPLOAD)
        map.addProperty(API_KEY_TITLE, et_name.text.toString())
        //map.addProperty(API_KEY_FORMATE_TYPE, getVideoType(url1))
        map.addProperty(
            API_KEY_FORMATE_TYPE, if (this@AddVideoActivity.checkboxUrl?.isChecked == true)
                "video"
            else getVideoType(url1)
        )
       // map.addProperty(API_KEY_SRC_URL, url)

        map.addProperty(API_KEY_SRC_URL, url1)

        map.addProperty(API_KEY_LENGTH, "")
        map.addProperty(API_KEY_SIZE, "")

        val json = JSONObject()
        val jsonUp = JSONObject()
        val jsonPrivacy = JSONObject()
        jsonPrivacy.put("embed","whitelist")
        jsonPrivacy.put("view","anybody")
        jsonPrivacy.put("add",true)

        jsonUp.put("approach", "tus")
        jsonUp.put("size", file?.length()?.toString() ?: "0")

        json.put("upload", jsonUp)
        json.put("name", ""+et_name?.text?.toString())
        json.put("privacy", jsonPrivacy)
    //    Log.e("fsafsafas",map.toString());

        mViewModel.uploadVideo(map)
        /*if(url ==""){
            mViewModel.progress.set(true)
          //  mUploadFileUrl= mViewModel.uploadFileUrl.
            VimeoService.createVideo(get(), json.toString()) { res ->
                runOnUiThread {
                    val data = Gson().fromJson(res, CreateVideoResponse::class.java)
                    mUploadFileUrl = data?.link?:""
                    Log.d("VimeoService", "Video Created " + data?.link)
                    uploadFile(data?.upload?.upload_link)
                }
            }

        }else{
            mViewModel.uploadVideo(map)
        }*/
    }

    private fun getVideoType(youTubeUrl: String): String {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            VIDEO_TYPE_YT_VDO
        } else {
            VIDEO_TYPE_VM_VDO
        }
    }


}
