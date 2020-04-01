package com.napworks.steerlyfeapp.fontPackage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditTextRegular extends AppCompatEditText {

    public CustomEditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"fonts/Lato-Regular.ttf");
        setTypeface(typeface);
    }
}
