package yanbo.assist.utils.bitmap.blur;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * @author Yanbo
 * @date 2018/4/27
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class BlurUtils {

    public static Bitmap blurByPercent(Bitmap originalBitmap, float percent, boolean scaleToOriginal) {
        if (percent <= 0) {
            return originalBitmap;
        }
        int w = originalBitmap.getWidth();
        int h = originalBitmap.getHeight();
        int min = Math.min(w, h);
        int radius = (int) (min * percent);
        return blur(originalBitmap, radius, scaleToOriginal);
    }

    public static Bitmap blur(Bitmap originalBitmap, float radius, boolean scaleToOriginal) {
        if (radius <= 0) {
            return originalBitmap;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return GaussianBlur.blur(originalBitmap, radius, scaleToOriginal);
        } else {
            return FastBlur.blur(originalBitmap, (int) radius);
        }
    }

    public static Bitmap blur(Bitmap originalBitmap, float radius, float scaleFactor, boolean scaleToOriginal) {
        if (radius <= 0) {
            return originalBitmap;
        }
        if (scaleFactor <= 1) {
            return blur(originalBitmap, radius, scaleToOriginal);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return GaussianBlur.blur(originalBitmap, radius, scaleFactor, scaleToOriginal);
        } else {
            return FastBlur.blur(originalBitmap, (int) radius, scaleFactor, scaleToOriginal);
        }
    }
}
