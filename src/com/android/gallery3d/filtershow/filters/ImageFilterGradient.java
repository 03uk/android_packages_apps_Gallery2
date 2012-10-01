
package com.android.gallery3d.filtershow.filters;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

import com.android.gallery3d.filtershow.ui.Spline;

public class ImageFilterGradient extends ImageFilter {

    private Bitmap mGradientBitmap = null;
    private int[] mColors = null;
    private float[] mPositions = null;

    public ImageFilterGradient() {
        mName = "Gradient";
    }

    @Override
    public ImageFilter clone() throws CloneNotSupportedException {
        ImageFilterGradient filter = (ImageFilterGradient) super.clone();
        for (int i = 0; i < mColors.length; i++) {
            filter.addColor(mColors[i], mPositions[i]);
        }
        return filter;
    }

    public void addColor(int color, float position) {
        int length = 0;
        if (mColors != null) {
            length = mColors.length;
        }
        int[] colors = new int[length + 1];
        float[] positions = new float[length + 1];

        for (int i = 0; i < length; i++) {
            colors[i] = mColors[i];
            positions[i] = mPositions[i];
        }

        colors[length] = color;
        positions[length] = position;

        mColors = colors;
        mPositions = positions;
    }

    public void apply(Bitmap bitmap) {

        createGradient();
        int[] gradient = new int[256];
        int[] redGradient = new int[256];
        int[] greenGradient = new int[256];
        int[] blueGradient = new int[256];
        mGradientBitmap.getPixels(gradient, 0, 256, 0, 0, 256, 1);

        for (int i = 0; i < 256; i++) {
            redGradient[i] = Color.red(gradient[i]);
            greenGradient[i] = Color.green(gradient[i]);
            blueGradient[i] = Color.blue(gradient[i]);
        }
        nativeApplyGradientFilter(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                redGradient, greenGradient, blueGradient);
    }

    public void createGradient() {
        if (mGradientBitmap != null) {
            return;
        }

        /* Create a 200 x 200 bitmap and fill it with black. */
        Bitmap b = Bitmap.createBitmap(256, 1, Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.BLACK);

        /* Create your gradient. */

        /*
         * int[] colors = new int[2]; colors[0] = Color.argb(255, 20, 20, 10);
         * colors[0] = Color.BLACK; colors[1] = Color.argb(255, 228, 231, 193);
         * float[] positions = new float[2]; positions[0] = 0; positions[1] = 1;
         */

        LinearGradient grad = new LinearGradient(0, 0, 255, 1, mColors,
                mPositions, TileMode.CLAMP);

        /* Draw your gradient to the top of your bitmap. */
        Paint p = new Paint();
        p.setStyle(Style.FILL);
        p.setShader(grad);
        c.drawRect(0, 0, 256, 1, p);
        mGradientBitmap = b;
    }

}
