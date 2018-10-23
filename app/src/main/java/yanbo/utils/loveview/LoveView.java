package yanbo.utils.loveview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import yanbo.utils.R;

/**
 * Created by mac on 18/6/10.
 */
public class LoveView extends RelativeLayout {

    /**
     * 点赞和取消点赞动画效果的开关
     */
    public static final boolean SHORT_VIDEO_LOVE_ANIM = true;
    /**
     * 随机心形图片角度
     */
    private static final float[] ROTATION_BEGIN = {-15, 0, 15};
    /**
     * 区分点击和连续点击的时长间隔
     */
    private static final long CONTINUITY_DELAY = 500L;
    /**
     * 长按时长间隔
     */
    private static final long LONG_PRESS_DELAY = 600L;
    /**
     * 滑动距离
     */
    private static final float SLIDE_DISTANCE = 10;
    long downTime = 0L;
    private Context mContext;
    private LoveTouchListener listener;
    private long lastTime = 0L;
    private int alphaMax = 255;
    private int size = 300;
    private Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                listener.onSingleTip();
            }
        }
    };
    private int imageResId = R.mipmap.heart_love;
    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            addUnLoveView();
            if (listener != null) {
                listener.onLongPress();
            }
        }
    };
    private float downX = 0;
    private float downY = 0;

    public LoveView(Context context) {
        super(context);
        initView(context);
    }

    public LoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    public void setAlphaMax(int alphaMax) {
        this.alphaMax = alphaMax;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (!SHORT_VIDEO_LOVE_ANIM){
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = System.currentTimeMillis();
                postDelayed(longPressRunnable, LONG_PRESS_DELAY);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                if (getDistance(downX, downY, moveX, moveY) > SLIDE_DISTANCE) {
                    removeCallbacks(longPressRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                long curTime = System.currentTimeMillis();
                if (curTime - downTime < LONG_PRESS_DELAY) {
                    removeCallbacks(longPressRunnable);
                    if (curTime - lastTime < CONTINUITY_DELAY) {
                        removeCallbacks(clickRunnable);
                        if (listener != null) {
                            listener.onMoreTip();
                        }
                        addLoveView(event);
                    } else {
                        if (listener != null) {
                            postDelayed(clickRunnable, CONTINUITY_DELAY);
                        } else {
                            addLoveView(event);
                        }
                    }
                }
                lastTime = curTime;
                break;
            default:
                break;
        }
        return true;
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float dx = Math.abs(x1 - x2);
        float dy = Math.abs(y1 - y2);
        return (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    private ImageView createLoveView(MotionEvent event) {
        int w = getWidth();
        int h = getHeight();
        int x = (int) event.getX();
        int y = (int) event.getY();

        int width = size;
        if (x < size / 2) {
            width = x * 2;
        } else if (x > w - size / 2) {
            width = (w - x) * 2;
        }

        int height = size;
        if (y < size) {
            height = y;
        }

        int minSize = Math.min(width, height);

        final ImageView imageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(minSize, minSize);
        params.leftMargin = x - minSize / 2;
        params.topMargin = y - minSize;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(getBitmapLove());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageAlpha(alphaMax);

        return imageView;
    }

    private void addLoveView(MotionEvent event) {
        final ImageView imageView = createLoveView(event);
        addView(imageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(AnimHelper.rotation(imageView, 0, 0, size / 2, size, ROTATION_BEGIN[new Random().nextInt(ROTATION_BEGIN.length - 1)]))
                .with(AnimHelper.scaleX(imageView, 100, 0, size / 2, size / 2, 2f, 0.9f))
                .with(AnimHelper.scaleY(imageView, 100, 0, size / 2, size / 2, 2f, 0.9f))
                .with(AnimHelper.alpha(imageView, 100, 0, 0, 1))
                .with(AnimHelper.scaleX(imageView, 50, 150, size / 2, size / 2, 0.9f, 1f))
                .with(AnimHelper.scaleY(imageView, 50, 150, size / 2, size / 2, 0.9f, 1f))
                .with(AnimHelper.translationY(imageView, 800, 400, 0, - size * 2))
                .with(AnimHelper.alpha(imageView, 700, 400, 1, 0))
                .with(AnimHelper.scaleX(imageView, 700, 400, size / 2, size / 2, 1, 1.5f))
                .with(AnimHelper.scaleY(imageView, 700, 400, size / 2, size / 2, 1, 1.5f));

        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(imageView);
            }
        });
    }

    private Bitmap bitmapLove = null;
    private Bitmap bitmapUnLove = null;

    public Bitmap getBitmapLove() {
        if (bitmapLove == null){
            bitmapLove = BitmapFactory.decodeResource(getResources(), imageResId);
        }
        return bitmapLove;
    }

    public Bitmap getBitmapUnLove() {
        if (bitmapUnLove != null){
            return bitmapUnLove;
        }
        if (bitmapLove == null){
            bitmapLove = BitmapFactory.decodeResource(getResources(), imageResId);
        }
        int width = bitmapLove.getWidth();
        int height = bitmapLove.getHeight();

        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));

        bitmapUnLove = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapUnLove);
        canvas.drawBitmap(bitmapLove, 0, 0, paint);
        return bitmapUnLove;
    }

    private ImageView createUnLoveView() {
        int w = getWidth();
        int h = getHeight();

        final ImageView imageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(size, size);
        params.leftMargin = (w - size) / 2;
        params.topMargin = (h - size) / 2;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(getBitmapUnLove());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageAlpha(alphaMax);
        return imageView;
    }

    private void addUnLoveView() {
        final ImageView imageView = createUnLoveView();
        addView(imageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(AnimHelper.alpha(imageView, 200, 0, 0, 1))
                .with(AnimHelper.scaleX(imageView, 200, 0, size / 2, size / 2, 2f, 1f))
                .with(AnimHelper.scaleY(imageView, 200, 0, size / 2, size / 2, 2f, 1f));
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(imageView);
                showBrokeLoveAnim();
            }
        });
    }

    public void setImageResId(@DrawableRes int imageResId) {
        this.imageResId = imageResId;
    }

    private void showBrokeLoveAnim() {
        final ImageView left = createBrokeLoveView(getPathBitmap(true));
        addView(left);
        final ImageView right = createBrokeLoveView(getPathBitmap(false));
        addView(right);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(AnimHelper.rotation(left,300, 500, size / 2, size, 0, -8))
                .with(AnimHelper.rotation(right,300, 500, size / 2, size, 0, 8))
                .with(AnimHelper.alpha(left, 400, 700, 1, 0))
                .with(AnimHelper.alpha(right, 400, 700, 1, 0));
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeViewInLayout(left);
                removeViewInLayout(right);
            }
        });
    }

    private ImageView createBrokeLoveView(Bitmap bitmap) {
        final ImageView imageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(size, size);
        params.leftMargin = (getWidth() - size) / 2;
        params.topMargin = (getHeight() - size) / 2;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageAlpha(alphaMax);
        return imageView;
    }

    private Bitmap getPathBitmap(boolean leftOrRight) {
        Bitmap bitmap = getBitmapUnLove();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        Path path = leftOrRight ? PathUtils.getPathLeft(width, height, 0.21f, 0.033f) : PathUtils.getPathRight(width, height, 0.21f, 0.033f);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        return target;
    }

    public void setLoveTouchListener(LoveTouchListener listener) {
        this.listener = listener;
    }
}