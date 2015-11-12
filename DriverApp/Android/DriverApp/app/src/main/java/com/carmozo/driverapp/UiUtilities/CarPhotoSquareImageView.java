package com.carmozo.driverapp.UiUtilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by shreyasgs on 01-10-2015.
 */

public class CarPhotoSquareImageView extends ImageView {

    public CarPhotoSquareImageView(Context context) {
        super(context);
    }

    public CarPhotoSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarPhotoSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //snap to width
    }
}
