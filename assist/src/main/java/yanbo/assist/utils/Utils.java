package yanbo.assist.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author Yanbo
 */
public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context = null;

    public static void init(Context context) {
        Utils.context = context;
        LogUtils.init(context);
        ToastMaker.init(context);
    }

    public static Context getAppContext() {
        if (context == null) {
            throw new InitException("未在Application中初始化");
        }
        return context;
    }

    private static class InitException extends UnsupportedOperationException {
        private InitException(String message) {
            super(message);
        }
    }
}
