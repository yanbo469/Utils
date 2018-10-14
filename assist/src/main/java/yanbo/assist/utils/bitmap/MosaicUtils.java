package yanbo.assist.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 马赛克
 *
 * @author Yanbo
 * @date 2018/6/27-下午2:00
 */
public class MosaicUtils {

    /**
     * 普通图像－>像素图
     *
     * @param bitmap
     * @param zoneCount 像素图的大像素方块在短边的个数
     * @return
     */
    public static Bitmap mosaicWithCount(Bitmap bitmap, int zoneCount) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int min = Math.min(bitmapWidth, bitmapHeight);
        if (zoneCount >= min) {
            return bitmap;
        }
        return mosaic(bitmap, min / zoneCount);
    }

    /**
     * 普通图像－>像素图
     *
     * @param bitmap
     * @param zoneWidth 像素图的大像素的宽度
     * @return
     */
    public static Bitmap mosaic(Bitmap bitmap, int zoneWidth) {
        return mosaic(bitmap, zoneWidth, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * 普通图－>像素图
     *
     * @param bitmap
     * @param zoneSize
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     */
    public static Bitmap mosaic(Bitmap bitmap, int zoneSize, int left, int top, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int right = left + width;
        int bottom = top + height;
        right = right > w ? w : right;
        bottom = bottom > h ? h : bottom;
        Bitmap result = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = left; i < right; i += zoneSize) {
            for (int j = top; j < bottom; j += zoneSize) {
                int x = i + zoneSize;
                x = x > w ? w : x;
                int y = j + zoneSize;
                y = y > h ? h : y;
                int color = bitmap.getPixel((i + x) / 2, (j + y) / 2);
                paint.setColor(color);
                int gridRight = Math.min(w, i + zoneSize);
                int gridBottom = Math.min(h, j + zoneSize);
                canvas.drawRect(i, j, gridRight, gridBottom, paint);
            }
        }
        return result;
    }

}
