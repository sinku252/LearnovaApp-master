package com.sambhav.tws.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.LiveClassData
import getBackColorForClass
import getTextColorForClass
import getTextColorForClass2

@BindingAdapter("setProgressTouch")
fun setProgressTouch(view:View?,onTouch:Boolean){
    view?.setOnTouchListener { view, motionEvent ->
        true
    }
}
@BindingAdapter("setIcon")
fun setIcon(imageView:ImageView?,icon:Int){
    imageView?.setImageResource(icon)
}

@BindingAdapter("setImage")
fun setImage(imageView:ImageView?,url:String?){
    url?.let {
        imageView?.let { iv->
            if (!TextUtils.isEmpty(it))
                Glide.with(iv.context)
                    .load(url)
                    .placeholder(R.drawable.ic_dummy_subject)
                    .into(iv)
        }
    }
}

@BindingAdapter("setImageTeacher")
fun setImageTeacher(imageView:ImageView?,url:String?){
    imageView?.let { iv->
        Glide.with(iv.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_teacher)
            .into(iv)
    }
}

@BindingAdapter("setRefresh")
fun setRefresh(swipeRefreshLayout:SwipeRefreshLayout?,refresh:ObservableBoolean){
    if(refresh.get()){
        swipeRefreshLayout?.isRefreshing = false
    }
}


@BindingAdapter("setUserImageRound")
fun setDoubtImage(view:ImageView?, url:String?){
    Log.d("setDoubtImage","url $url ")
    view?.let { iv->
        Glide.with(iv.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30))
                .error(R.drawable.chat_placeholder)
                .placeholder(R.drawable.chat_placeholder)
            )
            .into(iv)
    }
}

@BindingAdapter("setUserImage")
fun setUserImage(view:ImageView?, url:String){
    view?.let { iv->
        Glide.with(iv.context)
            .load(Uri.parse(url))
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(iv)
    }

}

@BindingAdapter("setUserImageRound")
fun setUserImageRound(view:ImageView?, url:String?){
    view?.let { iv->
        Glide.with(iv.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop())
                .error(R.drawable.ic_teacher)
                .placeholder(R.drawable.ic_teacher)
            )
            .into(iv)
    }
}

@BindingAdapter("setUserImageRound")
fun setUserImageRound(view:ImageView?, url:Int){
    view?.let { iv->
        Glide.with(iv.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(iv)
    }
}

@BindingAdapter("setPrimaryColor")
fun setPrimaryColor(view:TextView?, color:String){
    view?.setTextColor(Color.parseColor(color))
}


@BindingAdapter("setEventBackColor")
fun setEventBackColor(view: View?, classData:LiveClassData){
    view?.let {layout->
        val color = getBackColorForClass(classData.schedule_date,classData.schedule_time)
        layout.background?.let {back->
            back.colorFilter = PorterDuffColorFilter(
                Color.parseColor(color),
                PorterDuff.Mode.SRC_IN
            )

        }
    }
}


@BindingAdapter("setEventPrimaryColor")
fun setEventPrimaryColor(view: TextView?, classData:LiveClassData){
    view?.let {layout->
        val color = getTextColorForClass(classData.schedule_date,classData.schedule_time)
        layout.setTextColor(Color.parseColor(color))
    }
}


@BindingAdapter("setEventPrimarySecondaryColor")
fun setEventPrimarySecondaryColor(view: TextView?, classData:LiveClassData){
    view?.let {layout->
        val color = getTextColorForClass2(classData.schedule_date,classData.schedule_time)
        layout.setTextColor(Color.parseColor(color))
    }
}

@BindingAdapter("setBackColor")
fun setBackColor(view: View?, color:String){
    view?.let {layout->
        layout.background?.let {back->
            back.colorFilter = PorterDuffColorFilter(
                Color.parseColor(color),
                PorterDuff.Mode.SRC_IN
            )

        }
    }
}

@BindingAdapter("setBackColor")
fun setBackColor(view: View?, color:Int){
    view?.let {layout->
        layout.background = ContextCompat.getDrawable(view.context,color)
    }
}

@BindingAdapter("setSecondaryColor")
fun setSecondaryColor(view:TextView?, color:String){
    view?.setTextColor(Color.parseColor(color))
}
