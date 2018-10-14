package yanbo.assist.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * 获取资源文件的工具类
 *
 * @author Yanbo
 * @date 2018/7/9-下午4:56
 */
public class ResourcesUtils {

//    @ColorInt
//    public static int getColor(@ColorRes int colorResId) {
//        return ContextCompat.getColor(getContext(), colorResId);
//    }
//
//    private static Context getContext() {
//        return Utils.getAppContext();
//    }

    /**
     * 获取Resource对象
     */
    public static Resources getResources() {
        return Utils.getAppContext().getResources();
    }

    /**
     * 获取Drawable资源
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取字符串资源
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取color资源
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取dimens资源
     */
    public static float getDimens(int resId) {
        return getResources().getDimension(resId);
    }

    /**
     * 获取字符串数组资源
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }
}
