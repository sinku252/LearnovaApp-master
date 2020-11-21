package com.sambhav.tws.base

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rx.Observable
import rx.Subscriber
import rx.Subscription

abstract class BaseViewModel : ViewModel() {
    val mErrorMessage = MutableLiveData<String>()
    val mAuthError = MutableLiveData<Boolean>()
    var message = ObservableField<String>()
    var noIcon = ObservableField<Int>()
    val progress = ObservableBoolean(false)
    val refresh = ObservableBoolean(false)
    private var subscriber: Subscription? = null
    fun <T> requestData(
        api: Observable<T>,
        success: (m: T) -> Unit,
        failed: (message: String?, code: Int) -> Unit = onFailed
    ) {

        progress.set(true)
        subscriber = api.subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
            .unsubscribeOn(rx.schedulers.Schedulers.io())
            .subscribe(object : Subscriber<T>() {
                override fun onError(e: Throwable?) {
                    val message = e?.message?:""
                    progress.set(false)
                    refresh.set(false)
                    failed(message, 0)
                    mAuthError.value = (message.contains("HTTP 401", true))||
                            (message.contains("Unauthorized", true))
                    Log.e("ASDASDASDASDASDASD", "!3" + message + "   " )

                }

                override fun onNext(t: T) {
                    Log.e("ASDASDASDASDASDASD", "!2")

                    progress.set(false)
                    refresh.set(false)
                    success(t)
                    mAuthError.value = false
                }

                override fun onCompleted() {
                }

            })
    }

    val onFailed: (message: String?, code: Int) -> Unit = { message, code ->

    }
}