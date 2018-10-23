package yanbo.utils.widget.floatinglayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import yanbo.assist.utils.listener.OnClickListenerWithoutLogin;
import yanbo.utils.R;

/**
 * @author Cuizhen
 */
public class FloatingLayer implements View.OnKeyListener {

    private static final String TAG = FloatingLayer.class.getSimpleName();
    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private final View mDecorView;
    private final ViewGroup mRootView;
    private final FrameLayout mContainer;
    private final ImageView mBackground;
    private final BlurView mBackgroundBlur;

    private final ViewHolder mViewHolder;

    private View mContent;

    private OnVisibleChangeListener mOnVisibleChangeListener = null;

    private boolean mShow = false;
    private boolean mDismissing = false;
    private boolean mShowing = false;

    private int mGravity = Gravity.CENTER;
    private float mBackgroundBlurRadius = 0;
    private Bitmap mBackgroundBitmap = null;
    private int mBackgroundResource = -1;
    private Drawable mBackgroundDrawable = null;
    private int mBackgroundColor = Color.TRANSPARENT;

    private boolean mCancelableOnTouchOutside = true;
    private boolean mCancelableOnClickKeyBack = true;

    private IAnim mBackgroundAnim = null;
    private Animation mBackgroundInAnim = null;
    private Animation mBackgroundOutAnim = null;
    private IAnim mContentAnim = null;
    private Animation mContentInAnim = null;
    private Animation mContentOutAnim = null;
    private long mBackgroundAnimDuration = 200;
    private long mContentAnimDuration = 250;

    private IDataBinder mDataBinder = null;

    private FloatingLayer(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity);
        mDecorView = mActivity.getWindow().getDecorView();
        mRootView = mDecorView.findViewById(android.R.id.content);

        mContainer = (FrameLayout) mInflater.inflate(R.layout.floating_layer_layout, mRootView, false);
        mBackground = mContainer.findViewById(R.id.iv_background);
        mBackgroundBlur = mContainer.findViewById(R.id.bv_background);

        mViewHolder = new ViewHolder(this, mContainer);
    }

    public static FloatingLayer with(Activity activity) {
        return new FloatingLayer(activity);
    }

    public FloatingLayer setOnVisibleChangeListener(OnVisibleChangeListener mOnVisibleChangeListener) {
        this.mOnVisibleChangeListener = mOnVisibleChangeListener;
        return this;
    }

    public FloatingLayer gravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public FloatingLayer contentAnim(IAnim contentAnim) {
        this.mContentAnim = contentAnim;
        return this;
    }

    public FloatingLayer contentInAnim(@AnimRes int anim) {
        contentInAnim(AnimationUtils.loadAnimation(mActivity, anim));
        return this;
    }

    public FloatingLayer contentInAnim(@NonNull Animation anim) {
        mContentInAnim = anim;
        mContentAnimDuration = Math.max(mContentAnimDuration, mContentInAnim.getDuration());
        return this;
    }

    public FloatingLayer contentOutAnim(@AnimRes int anim) {
        contentOutAnim(AnimationUtils.loadAnimation(mActivity, anim));
        return this;
    }

    public FloatingLayer contentOutAnim(@NonNull Animation anim) {
        mContentOutAnim = anim;
        mContentAnimDuration = Math.max(mContentAnimDuration, mContentOutAnim.getDuration());
        return this;
    }

    public FloatingLayer backgroundAnim(IAnim backgroundAnim) {
        this.mBackgroundAnim = backgroundAnim;
        return this;
    }

    public FloatingLayer backgroundInAnim(@AnimRes int anim) {
        backgroundInAnim(AnimationUtils.loadAnimation(mActivity, anim));
        return this;
    }

    public FloatingLayer backgroundInAnim(@NonNull Animation anim) {
        mBackgroundInAnim = anim;
        mBackgroundAnimDuration = Math.max(mBackgroundAnimDuration, mBackgroundInAnim.getDuration());
        return this;
    }

    public FloatingLayer backgroundOutAnim(@AnimRes int anim) {
        backgroundOutAnim(AnimationUtils.loadAnimation(mActivity, anim));
        return this;
    }

    public FloatingLayer backgroundOutAnim(@NonNull Animation anim) {
        mBackgroundOutAnim = anim;
        mBackgroundAnimDuration = Math.max(mBackgroundAnimDuration, mBackgroundOutAnim.getDuration());
        return this;
    }

    public FloatingLayer defaultContentAnimDuration(long defaultAnimDuration) {
        this.mContentAnimDuration = defaultAnimDuration;
        return this;
    }

    public FloatingLayer defaultBackgroundAnimDuration(long defaultAnimDuration) {
        this.mBackgroundAnimDuration = defaultAnimDuration;
        return this;
    }

    public FloatingLayer contentView(@NonNull View contentView) {
        mContent = contentView;
        return this;
    }

    public FloatingLayer contentView(@LayoutRes int contentViewId) {
        mContent = mInflater.inflate(contentViewId, mContainer, false);
        return this;
    }

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #backgroundColorInt(int)}为半透明颜色
     *
     * @param radius 模糊半径
     * @return PopupView
     */
    public FloatingLayer backgroundBlur(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius) {
        mBackgroundBlurRadius = radius;
        return this;
    }

    public FloatingLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        mBackgroundBitmap = bitmap;
        return this;
    }

    public FloatingLayer backgroundResource(@DrawableRes int resource) {
        mBackgroundResource = resource;
        return this;
    }

    public FloatingLayer backgroundDrawable(@NonNull Drawable drawable) {
        mBackgroundDrawable = drawable;
        return this;
    }

    /**
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlur(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorInt ColorInt
     * @return PopupView
     */
    public FloatingLayer backgroundColorInt(@ColorInt int colorInt) {
        mBackgroundColor = colorInt;
        return this;
    }

    public FloatingLayer backgroundColorRes(@ColorRes int colorRes) {
        mBackgroundColor = ContextCompat.getColor(mActivity, colorRes);
        return this;
    }

    public FloatingLayer cancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        return this;
    }

    public FloatingLayer cancelableOnClickKeyBack(boolean cancelable) {
        mCancelableOnClickKeyBack = cancelable;
        return this;
    }

    public FloatingLayer onClick(OnFloatingLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        mViewHolder.addOnClickListener(listener, viewId, viewIds);
        return this;
    }

    public <V extends View> V getView(@IdRes int viewId) {
        return mViewHolder.getView(viewId);
    }

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public FloatingLayer bindData(IDataBinder dataBinder) {
        mDataBinder = dataBinder;
        return this;
    }

    private void onAttached() {
        initBackground();

        initContent();

        mViewHolder.bindListener();

        if (mDataBinder != null) {
            mDataBinder.bind(this);
        }

        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onShow(FloatingLayer.this);
        }
    }

    public View getContentView() {
        return mContent;
    }

    public ImageView getBackground() {
        return mBackground;
    }

    public BlurView getBackgroundBlur() {
        return mBackgroundBlur;
    }

    private void initBackground() {
        if (mBackgroundBlurRadius > 0) {
            mBackground.setVisibility(View.GONE);
            mBackgroundBlur.setupWith(mRootView)
                    .windowBackground(mDecorView.getBackground())
                    .setOverlayColor(mBackgroundColor)
                    .blurAlgorithm(new RenderScriptBlur(mActivity))
                    .blurRadius(mBackgroundBlurRadius)
                    .setHasFixedTransformationMatrix(true);
        } else {
            mBackgroundBlur.setVisibility(View.GONE);
            if (mBackgroundBitmap != null) {
                mBackground.setImageBitmap(mBackgroundBitmap);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundResource != -1) {
                mBackground.setImageResource(mBackgroundResource);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundDrawable != null) {
                mBackground.setImageDrawable(mBackgroundDrawable);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundColor != Color.TRANSPARENT) {
                mBackground.setImageDrawable(new ColorDrawable(mBackgroundColor));
            }
        }
    }

    private View currentKeyView = null;

    private void initContent() {
        if (mContent != null) {
            if (mContent.getParent() == null) {
                mContent.setClickable(true);
                if (mGravity != -1) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mContent.getLayoutParams();
                    params.gravity = mGravity;
                    mContent.setLayoutParams(params);
                }
                mContainer.addView(mContent);
            }
            mContent.setFocusable(true);
            mContent.setFocusableInTouchMode(true);
            mContent.requestFocus();
            currentKeyView = mContent;
            currentKeyView.setOnKeyListener(this);
            ViewTreeObserver observer = mContent.getViewTreeObserver();
            observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                @Override
                public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                    if (currentKeyView != null) {
                        currentKeyView.setOnKeyListener(null);
                    }
                    if (oldFocus != null) {
                        oldFocus.setOnKeyListener(null);
                    }
                    if (newFocus != null) {
                        currentKeyView = newFocus;
                        currentKeyView.setOnKeyListener(FloatingLayer.this);
                    }
                }
            });
        }
        if (mCancelableOnTouchOutside) {
            mContainer.setOnClickListener(new OnClickListenerWithoutLogin() {
                @Override
                public void onClickWithoutLogin(View v) {
                    dismiss();
                }
            });
        }
        mRootView.addView(mContainer);
        final ViewTreeObserver observer = mContainer.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShowing = false;
                    }
                }, getDuration());
                doInAnim();
                return true;
            }
        });
    }

    private void onDetached() {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        mRootView.removeView(mContainer);
        mShow = false;
        mDismissing = false;
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onDismiss(FloatingLayer.this);
        }
    }

    private long getDuration() {
        return Math.max(mBackgroundAnimDuration, mContentAnimDuration);
    }

    public boolean isShow() {
        return mContainer.getParent() != null || mShow;
    }

    public void show() {
        if (isShow()) {
            return;
        }
        mShow = true;
        if (mShowing) {
            return;
        }
        onAttached();
    }

    private void doInAnim() {
        startContentInAnim();
        startBackgroundInAnim();
    }

    public void dismiss() {
        if (mContainer.getParent() == null || !mShow) {
            return;
        }
        if (mDismissing) {
            return;
        }
        mDismissing = true;
        doOutAnim();
        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                onDetached();
            }
        }, getDuration());
    }

    private void doOutAnim() {
        startContentOutAnim();
        startBackgroundOutAnim();
    }

    private void startContentInAnim() {
        if (mContentAnim != null) {
            mContentAnimDuration = mContentAnim.inAnim(mContent);
        } else {
            if (mContentInAnim != null) {
                mContent.startAnimation(mContentInAnim);
            } else {
                AnimHelper.startZoomInAnim(mContent, mContentAnimDuration);
            }
        }
    }

    private void startContentOutAnim() {
        if (mContentAnim != null) {
            mContentAnimDuration = mContentAnim.outAnim(mContent);
        } else {
            if (mContentOutAnim != null) {
                mContent.startAnimation(mContentOutAnim);
            } else {
                AnimHelper.startZoomOutAnim(mContent, mContentAnimDuration);
            }
        }
    }

    private void startBackgroundInAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundAnimDuration = mBackgroundAnim.inAnim(mBackground);
        } else {
            if (mBackgroundInAnim != null) {
                mBackground.startAnimation(mBackgroundInAnim);
            } else {
                AnimHelper.startAlphaInAnim(mBackground, mBackgroundAnimDuration);
            }
        }
    }

    private void startBackgroundOutAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundAnimDuration = mBackgroundAnim.outAnim(mBackground);
        } else {
            if (mBackgroundOutAnim != null) {
                mBackground.startAnimation(mBackgroundOutAnim);
            } else {
                AnimHelper.startAlphaOutAnim(mBackground, mBackgroundAnimDuration);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mContainer.getParent() == null || !mShow) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mCancelableOnClickKeyBack) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }
}
