package yanbo.assist.utils.bitmap.blur;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;


/**
 * 高斯模糊图片工具类
 *
 * @author Yanbo
 * @date 2018/4/26-上午10:30
 */
public class BlurHelper {

    private final Bitmap inputBitmap;
    private float percent = 0;
    private int radius = 0;
    private float scale = 1;
    private boolean scaleToOriginal = false;
    private BlurCallback blurCallback = null;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap blur = (Bitmap) msg.obj;
            if (blurCallback != null) {
                blurCallback.blurDown(blur);
            }
        }
    };

    private BlurHelper(Bitmap input) {
        inputBitmap = input;
    }

    public static BlurHelper newInstance(Bitmap input) {
        return new BlurHelper(input);
    }

    private void sendBlur(Bitmap blur) {
        Message msg = handler.obtainMessage();
        msg.obj = blur;
        handler.sendMessage(msg);
    }

    public void blur() {
        if (inputBitmap == null) {
            if (blurCallback != null) {
                blurCallback.blurDown(null);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (inputBitmap == null) {
                    sendBlur(null);
                } else {
                    if (percent != 0) {
                        sendBlur(BlurUtils.blurByPercent(inputBitmap, percent, scaleToOriginal));
                    } else {
                        sendBlur(BlurUtils.blur(inputBitmap, radius, scale, scaleToOriginal));
                    }
                }
            }
        }).start();
    }

    public BlurHelper setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public BlurHelper setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public BlurHelper setPercent(float percent) {
        this.percent = percent;
        return this;
    }

    public BlurHelper setScaleToOriginal(boolean scaleToOriginal) {
        this.scaleToOriginal = scaleToOriginal;
        return this;
    }

    public BlurHelper setBlurCallback(BlurCallback blurCallback) {
        this.blurCallback = blurCallback;
        return this;
    }

    public interface BlurCallback {
        void blurDown(Bitmap bitmap);
    }
}
