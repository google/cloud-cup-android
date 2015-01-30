/*
Copyright 2014 Google Inc. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.google.cloudcup;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Some utility functions that can be called from AsyncTask.doInBackground() to fetch and decode
 * images into Bitmaps.
 */
public final class BitmapUtils {
    private static final int STREAM_BUFFER_SIZE = 64 * 1024;

    // This class provides only static methods, so constructor should be private.
    private BitmapUtils() {
    }

    /**
     * Calculates the ratio of linear dimension of source / required so that a bitmap
     * can be decoded to the smallest size big enough to completely fill the destination.
     *
     * For pedagogical clarity, the most straightforward implementation is presented. The
     * documentation notes that inSampleSize is rounded down to the nearest power of 2.
     * Algorithms that exploit this seem to run about 30% faster.
     *
     * @param srcWidth width of the original image.
     * @param srcHeight height of the original image.
     * @param reqWidth width of the destination Bitmap.
     * @param reqHeight height of the destination Bitmap.
     * @return ratio of src/rec (>= 1)
     */
    public static int calculateInSampleSize(int srcWidth, int srcHeight,
                                            int reqWidth, int reqHeight) {
        if ((reqHeight > 0) && (reqWidth > 0) && (srcHeight > reqHeight) && (srcWidth > reqWidth)) {
            return Math.min(srcWidth / reqWidth, srcHeight / reqHeight);
        } else {
            return 1;
        }
    }

    /**
     * Acquires input stream for the image resource identified by uri.
     *
     * This is a long-running I/O operation that must run in a background thread.
     *
     * @param context
     * @param uri
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(Context context, Uri uri) throws IOException {
        if (uri.getScheme().contentEquals(ContentResolver.SCHEME_CONTENT)) {
            return context.getContentResolver().openInputStream(uri);
        } else {
            return (InputStream) new URL(uri.toString()).getContent();
        }
    }

    /**
     * Decodes image from inputstream into a new Bitmap of specified dimensions.
     *
     * This is a long-running operation that must run in a background thread.
     *
     * @param is InputStream containing the image.
     * @param maxWidth target width of the output Bitmap.
     * @param maxHeight target height of the output Bitmap.
     * @return new Bitmap containing the image.
     * @throws IOException
     */
    public static Bitmap decodeBitmapBounded(InputStream is, int maxWidth, int maxHeight)
            throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is, STREAM_BUFFER_SIZE);
        try {
            bufferedInputStream.mark(STREAM_BUFFER_SIZE);  // should be enough to read image dimensions.

            // TODO(mattfrazier): fail more gracefully if mark isn't supported, but it should always be
            // by bufferedinputstream.

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedInputStream, null, bmOptions);
            bufferedInputStream.reset();

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = calculateInSampleSize(bmOptions.outWidth, bmOptions.outHeight,
                    maxWidth, maxHeight);

            // TODO(mattfrazier): Samsung devices yield a rotated bitmap no matter what orientation is
            // captured. Read Exif data and rotate in place or communicate Exif data and rotate display
            // with matrix.
            return BitmapFactory.decodeStream(bufferedInputStream, null, bmOptions);
        } finally {
            bufferedInputStream.close();
        }
    }
}