package yanbo.assist.utils.bitmap.blur;

import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.RequiresApi;

import yanbo.assist.utils.LogUtils;
import yanbo.assist.utils.Utils;


/**
 * @author Yanbo
 * @date 2018/4/4
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class GaussianBlur {

    private static final String TAG = GaussianBlur.class.getSimpleName();

    private static RenderScript renderScript = null;
    private static ScriptIntrinsicBlur gaussianBlur = null;

    /**
     * 模糊
     * 采用系统自带的RenderScript
     * 输出图与原图参数相同
     * 模糊半径小于0则返回原图，大于25则进行缩放再模糊
     *
     * @param originalBitmap 原图
     * @param radius         模糊半径（小于0则返回原图，大于25则进行缩放再模糊）
     * @return 模糊Bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blur(Bitmap originalBitmap, float radius, boolean scaleToOriginal) {
        if (radius <= 0) {
            return originalBitmap;
        }
        if (radius <= 25) {
            return blurIn25(originalBitmap, radius);
        }
        return blur(originalBitmap, 25, (radius / 25), scaleToOriginal);
    }

    /**
     * 模糊
     * 采用系统自带的RenderScript
     * 输出图与原图参数相同
     *
     * @param originalBitmap 原图
     * @param scaleFactor    缩放因子（>=1）
     * @param radius         模糊半径
     * @return 模糊Bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blur(Bitmap originalBitmap,
                       float radius,
                       float scaleFactor,
                       boolean scaleToOriginal) {
        if (radius <= 0) {
            return originalBitmap;
        }
        if (radius > 25) {
            radius = 25;
            scaleFactor = scaleFactor * (radius / 25);
        }
        if (scaleFactor == 1) {
            return blurIn25(originalBitmap, radius);
        }
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        long start = System.currentTimeMillis();
        Bitmap input = Bitmap.createScaledBitmap(originalBitmap, (int) (width / scaleFactor), (int) (height / scaleFactor), true);
        Bitmap output = blurIn25(input, radius);
        input.recycle();
        input = null;
        if (scaleToOriginal) {
            Bitmap outputScaled = Bitmap.createScaledBitmap(output, width, height, true);
            output.recycle();
            output = outputScaled;
        }
        long end = System.currentTimeMillis();
        LogUtils.i(TAG, "blur : " + "start = " + start + " , end = " + end + " , cost = " + (end - start));
        return output;
    }

    /**
     * 高斯模糊
     * 采用系统自带的RenderScript
     * 图像越大耗时越长，测试时1280*680的图片耗时在30~60毫秒
     * 建议在子线程模糊通过Handler回调获取
     *
     * @param originalBitmap 原图
     * @param radius         模糊半径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blurIn25(Bitmap originalBitmap,
                           @FloatRange(fromInclusive = false, from = 0.0, to = 25.0) float radius) {
        long start = System.currentTimeMillis();
        // 创建输出图片
        Bitmap blurBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
        init();
        // 开辟输入内存
        Allocation allIn = Allocation.createFromBitmap(renderScript, originalBitmap);
        // 开辟输出内存
        Allocation allOut = Allocation.createFromBitmap(renderScript, blurBitmap);
        // 设置模糊半径，范围0f<radius<=25f
        gaussianBlur.setRadius(radius);
        // 设置输入内存
        gaussianBlur.setInput(allIn);
        // 模糊编码，并将内存填入输出内存
        gaussianBlur.forEach(allOut);
        // 将输出内存编码为Bitmap，图片大小必须注意
        allOut.copyTo(blurBitmap);
        // 关闭RenderScript对象
        // renderScript.destroy();
        long end = System.currentTimeMillis();
        LogUtils.i(TAG, "blurIn25 : " + "start = " + start + " , end = " + end + " , cost = " + (end - start));
        return blurBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void init() {
        // 构建一个RenderScript对象
        if (renderScript == null) {
            renderScript = RenderScript.create(Utils.getAppContext());
        }
        // 创建高斯模糊脚本
        if (gaussianBlur == null) {
            gaussianBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        }
    }
}
