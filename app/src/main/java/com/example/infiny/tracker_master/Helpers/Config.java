package com.example.infiny.tracker_master.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by infiny on 8/3/17.
 */

public class Config {

    public static String BASE_URL = "http://dev2.infiny.in:3030/";

    public static String convertToBase64(Uri imagePath, Context context) {

        Bitmap bm = getBitmapFromUri(imagePath,context);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        byte[] byteArrayImage = baos.toByteArray();

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;

    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            Bitmap bitmap = ExifUtils.rotateBitmap(ExifUtils.getPath(context, uri), image);
            parcelFileDescriptor.close();
            return bitmap;
        } catch (Exception e) {
            Log.e("Ex", "Failed to load image.", e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOEx", "Error closing ParcelFile Descriptor");
            }
        }
    }

}
