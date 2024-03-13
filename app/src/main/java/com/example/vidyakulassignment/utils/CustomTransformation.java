package com.example.vidyakulassignment.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

class CustomTransformation extends BitmapTransformation {

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        // Calculate the target width based on the 1:2 aspect ratio
        int targetWidth = toTransform.getHeight() / 2;

        // Calculate the scale factor to fit the target width
        float scaleFactor = (float) targetWidth / toTransform.getWidth();

        // Create a matrix for the scaling transformation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);

        // Create a new bitmap with the target width and height
        Bitmap scaledBitmap = Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);

        // Create a new bitmap with the 1:2 aspect ratio
        Bitmap finalBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), targetWidth);

        // Recycle the intermediate bitmaps
        scaledBitmap.recycle();
        toTransform.recycle();

        return finalBitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        // No-op
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof CustomTransformation;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
