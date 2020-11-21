package com.sambhav.tws.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;
import com.sambhav.tws.R;


public class CustomEditTextView extends TextInputEditText {

    private TypeFactory mFontFactory;

    public CustomEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    public CustomEditTextView(Context context) {
        super(context);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        int typefaceType;
        try {
            typefaceType = array.getInteger(R.styleable.CustomTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(getTypeFace(typefaceType));
        }

    }

    private Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(getContext());

        switch (type) {
            case Constants.bold:
                return mFontFactory.bold;

            case Constants.medium:
                return mFontFactory.medium;

            case Constants.light:
                return mFontFactory.light;

            default:
                return mFontFactory.regular;
        }
    }

    public interface Constants {
        int bold = 1,
                medium =2,
                light = 3;

    }
}
