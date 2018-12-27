package com.sagar.memoir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

class ImageUtils {
    public static byte[] drawableToByteArray(Drawable d) {

        if (d != null) {
            Bitmap imageBitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream bAos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bAos);
            byte[] byteData = bAos.toByteArray();

            return byteData;
        } else
            return null;

    }


    public static Drawable byteToDrawable(byte[] data) {

        if (data == null)
            return null;
        else
            return new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
    }
}
