package com.example.pixelart;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// it's the same with the GradientDrawable, just make some proper modification to make it compilable
public class ColorGradientDrawable extends Drawable {

    private int mColor; // this is the color which you try to get

    // original setColor function with little modification
    public void setColor(int argb) {
        mColor = argb;
//        mGradientState.setSolidColor(argb);
//        mFillPaint.setColor(argb);
        invalidateSelf();
    }

    // that's how I get the color from this drawable class
    public int getColor() {
        return mColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }


    // it's the same with GradientState, just make some proper modification to make it compilable
    final public static class GradientState extends ConstantState {

        @NonNull
        @Override
        public Drawable newDrawable() {
            return null;
        }

        @Override
        public int getChangingConfigurations() {
            return 0;
        }
    }
}
