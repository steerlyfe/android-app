package com.napworks.steerlyfeapp.fontPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewRegular extends AppCompatTextView {

    public CustomTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"fonts/Lato-Regular.ttf");
        setTypeface(typeface);
    }
}
