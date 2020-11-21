package com.sambhav.tws.utils.networkRequest;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;

/**
 * Will handel all api response and error
 */
abstract public class CallbackWrapper<T> extends Subscriber<T> {
    private static final String TAG = CallbackWrapper.class.getSimpleName();

    public abstract void onSuccess(T t);

    public abstract void onError(String message,Integer statusCode);

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Log.e("error",e.getMessage()+"     LOL");
        onError(e.getMessage(),0);
    }

    @Override
    public void onNext(T t) {
        Log.e("callbackresponse",new Gson().toJson(t));
        ErrorCheckModel errorCheckModel = checkError(t);
        if (!errorCheckModel.error) {
            onSuccess(t);
        } else {
            onError(errorCheckModel.message,errorCheckModel.statusCode);
        }
    }

    private ErrorCheckModel checkError(Object data) {
        ErrorCheckModel errorCheckModel = new ErrorCheckModel();
        try {
            JSONObject object = new JSONObject(new Gson().toJson(data));
            errorCheckModel.error = object.getBoolean("error");
            errorCheckModel.message = object.getString("message");
            errorCheckModel.statusCode = 0;
        } catch (JSONException e) {
        }
        return errorCheckModel;
    }

    private class ErrorCheckModel {
        private boolean error=false;
        private Integer statusCode=0;
        private String message="";
    }


}
