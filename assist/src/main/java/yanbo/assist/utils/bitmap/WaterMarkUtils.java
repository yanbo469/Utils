package yanbo.assist.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;

import yanbo.assist.utils.Utils;


/**
 * 水印
 *
 * @author Yanbo
 * @date 2018/6/27-下午2:33
 */
public class WaterMarkUtils {

    private static final float MAX_X_PERCENT = 0.3F;
    private static final float MAX_Y_PERCENT = 0.1F;

    private static int MARK_RES_ID = 0;

    private static void init(@DrawableRes int markResId) {
        MARK_RES_ID = markResId;
    }

    /**
     * 图片添加水印
     *
     * @param original 原图
     * @param mark     水印
     * @return Bitmap
     */
    private static Bitmap mark(Bitmap original, Bitmap mark, int left, int top) {
        if (original == null) {
            return null;
        }
        Bitmap result = original.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        canvas.drawBitmap(original, 0, 0, paint);
        canvas.drawBitmap(mark, left, top, paint);
        return result;
    }

    public static Bitmap mark(Bitmap original) {
        if (original == null) {
            return null;
        }

        Bitmap mark = getMarkBitmap();
        int originalW = original.getWidth();
        int originalH = original.getHeight();
        int markW = mark.getWidth();
        int markH = mark.getHeight();

        float maxW = originalW * MAX_X_PERCENT;
        float maxH = originalH * MAX_Y_PERCENT;

        float scale = Math.max(markW / maxW, markH / maxH);
        markW = (int) (markW / scale);
        markH = (int) (markH / scale);

        mark = Bitmap.createScaledBitmap(mark, markW, markH, false);

        return mark(original, mark, originalW / 2 - markW / 2, originalH - markH);
    }

    private static Bitmap getMarkBitmap() {
        return BitmapFactory.decodeResource(Utils.getAppContext().getResources(), MARK_RES_ID);
    }

    /**
     * 图片添加水印文字
     *
     * @param original      原图
     * @param textStr       文字
     * @param textSize      字体大小
     * @param textColor     字体颜色
     * @param paddingBottom 距离底部的偏移
     * @return Bitmap
     */
    private static Bitmap mark(Bitmap original,
                               String textStr, int textSize, int textColor, int alpha,
                               int paddingBottom) {
        Bitmap result = original.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(original, 0, 0, paint);

        paint.setColor(textColor);
        paint.setAlpha(alpha);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        float bottom = paint.getFontMetrics().bottom;
        canvas.drawText(textStr, original.getWidth() / 2, original.getHeight() - bottom - paddingBottom, paint);
        return result;
    }
}