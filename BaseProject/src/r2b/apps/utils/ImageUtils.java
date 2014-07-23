/*
 * ImageUtils
 * 
 * 0.1
 * 
 * 2014/07/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

/**
 * Images utility class.
 */
public final class ImageUtils {

	/**
     * Get the image rotation needed to show on screen properly.
     * @param imagePath The absolute path of the image.
     * @return The degrees to rotate.
     */
    public static int getBitmapRotation(final String imagePath) {

        ExifInterface exif;
        int orientation = 0;
        int rotation = 0;

        try {
            exif = new ExifInterface(imagePath);
            orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotation;
    }

    /**
     * Rotate an image.
     * @param degrees The degrees to rotate.
     * @param src The image.
     * @return A new rotated image, null if error.
     */
    public static Bitmap rotateBitmap(final int degrees, final Bitmap src) {
    	Bitmap rotatedBitmap = null;
    	
    	if(src != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);

            rotatedBitmap = Bitmap.createBitmap(src, 0, 0, 
            		src.getWidth(), src.getHeight(), matrix, true);
    	}

        return rotatedBitmap;
    }
    
    /**
     * Convert a drawable into a bitmap.
     * @param drawable The drawable to extract the image.
     * @return The bitmap, null if error.
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {
    	Bitmap bitmap = null;
    	
    	if(drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable)drawable).getBitmap();
            }

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), 
            		drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
    	}

        return bitmap;
    }
    
    /**
     * Decode a bitmap image with the screen size and maintain aspect ratio.
     * @param imagePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmap(String imagePath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }
    
    /**
     * Calculate a sample size value that is a power of two based 
     * on a target width and height.
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, 
    		int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    
}
