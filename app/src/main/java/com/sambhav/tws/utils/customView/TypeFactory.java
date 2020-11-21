package com.sambhav.tws.utils.customView;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.sambhav.tws.R;


class TypeFactory {

    final Typeface bold;
    final Typeface medium;
    final Typeface regular;
    final Typeface light;

    public TypeFactory(Context context) {
        bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
        medium = ResourcesCompat.getFont(context, R.font.poppins_medium);
        regular = ResourcesCompat.getFont(context, R.font.poppins_regular);
        light = ResourcesCompat.getFont(context, R.font.poppins_light);
    }
}
