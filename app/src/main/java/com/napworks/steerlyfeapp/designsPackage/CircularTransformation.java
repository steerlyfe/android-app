package com.napworks.steerlyfeapp.designsPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by admin1 on 25-01-2017.
 */

public class CircularTransformation extends BitmapImageViewTarget {

    private Context context;
    private ImageView imageView;
    private boolean round = false;
    private float radius;

    public CircularTransformation(Context context, ImageView view) {
        super(view);
        this.imageView = view;
        this.context = context;
        round = true;
    }

    public CircularTransformation(Context context, ImageView view, float radius) {
        super(view);
        this.imageView = view;
        this.context = context;
        round = false;
        this.radius = radius;
    }

    @Override
    protected void setResource(Bitmap resource) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
        roundedBitmapDrawable.setCircular(round);
        if (!round) {
            roundedBitmapDrawable.setCornerRadius(radius);
        }
        imageView.setImageDrawable(roundedBitmapDrawable);
    }
}
