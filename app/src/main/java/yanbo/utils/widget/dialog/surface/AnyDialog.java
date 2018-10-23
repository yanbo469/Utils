package yanbo.utils.widget.dialog.surface;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import yanbo.assist.utils.DisplayInfoUtils;
import yanbo.assist.utils.SoftInputUtils;
import yanbo.assist.utils.listener.OnClickListenerWithoutLogin;
import yanbo.utils.BaseApp;
import yanbo.utils.R;
import yanbo.utils.widget.dialog.anim.AnimHelper;
import yanbo.utils.widget.dialog.base.IBackgroundAnim;
import yanbo.utils.widget.dialog.base.IContentAnim;
import yanbo.utils.widget.dialog.base.IDataBinder;
import yanbo.utils.widget.dialog.holder.ViewHolder;
import yanbo.utils.widget.dialog.listener.OnDialogClickListener;


/**
 * @author Cuizhen
 * @date 2018/4/30
 * QQ:
 * E-mail:
 * GitHub:
 */
public class AnyDialog extends Dialog {

    private final Context context;
    private final ViewHolder viewHolder;

    private BlurView backgroundBlurView;
    private ImageView backgroundImageView;
    private RelativeLayout contentWrapper;

    @LayoutRes
    private int contentId = -1;
    private View contentView = null;

    private float backgroundBlurRadius = 0;
    @ColorInt
    private int backgroundColor = Color.TRANSPARENT;
    private Drawable backgroundDrawable = null;
    private Bitmap backgroundBitmap = null;
    @DrawableRes
    private int backgroundResource = -1;

    private long backgroundAnimDuration = 100;
    private long contentAnimDuration = 150;

    private IBackgroundAnim backgroundAnim = null;
    private Animation backgroundInAnim = null;
    private Animation backgroundOutAnim = null;
    private IContentAnim contentAnim = null;
    private Animation contentInAnim = null;
    private Animation contentOutAnim = null;

    private IDataBinder dataBinder = null;

    private boolean fitStatusBar = false;
    private int gravity = -1;
    private boolean touchOutsideCancelable = true;

    private AnyDialog(@NonNull Context context) {
        super(context, R.style.DialogFullscreen);
        this.context = context;
        this.viewHolder = new ViewHolder(this);
    }

    public static AnyDialog with(@NonNull Context context) {
        return new AnyDialog(context);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        startBackgroundOutAnim();
        startContentOutAnim();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (contentView != null) {
                    contentView.setAlpha(1);
                    contentView.setScaleX(1);
                    contentView.setScaleY(1);
                    contentView.setTranslationX(0);
                    contentView.setTranslationY(0);
                }
                AnyDialog.super.dismiss();
            }
        }, getDuration());
    }

    private long getDuration() {
        return Math.max(backgroundAnimDuration, contentAnimDuration);
    }

    private void initWindow() {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        window.setWindowAnimations(0);
        window.clearFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 1f;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayInfoUtils.getInstance().getAppUsableScreenSize().y;

        layoutParams.dimAmount = 0;
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        window.setAttributes(layoutParams);

        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        int uiOptions = decorView.getSystemUiVisibility();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            int activityFlags = activity.getWindow().getAttributes().flags;
            window.addFlags(activityFlags);
            if (((activityFlags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    || ((activityFlags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN)) {
                fitStatusBar = true;
            }
            int activityUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
            uiOptions = uiOptions | activityUiOptions;
            if (((activityUiOptions & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                    || ((activityUiOptions & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_FULLSCREEN)) {
                fitStatusBar = true;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                fitStatusBar = true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                uiOptions = uiOptions | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                fitStatusBar = true;
            }
        }
        window.setUiOptions(uiOptions);
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_full_screen);

        initWindow();

        backgroundImageView = findViewById(R.id.iv_background);
        backgroundBlurView = findViewById(R.id.bv_background);
        if (backgroundImageView != null && backgroundBlurView != null) {
            initBackground();
        }

        contentWrapper = findViewById(R.id.rl_content);
        if (contentWrapper != null) {
            initContentWrapper();
        }

        viewHolder.bindListener(contentWrapper);

        if (dataBinder != null) {
            dataBinder.bind(this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            startContentInAnim();
            startBackgroundInAnim();
        }
    }

    public View getContentView() {
        return contentView;
    }

    private void initContentWrapper() {
        if (contentId != -1) {
            contentView = LayoutInflater.from(context).inflate(contentId, contentWrapper, false);
        }
        if (contentView != null) {
            contentView.setFocusable(true);
            contentView.setClickable(true);
            contentWrapper.addView(contentView);
        }
        if (gravity != -1) {
            contentWrapper.setGravity(gravity);
        }
        if (getWindow() != null) {
            SoftInputUtils.init(getWindow()).duration(200).raise(contentView);
        }
    }

    private void initBackground() {
        if (touchOutsideCancelable) {
            backgroundBlurView.setOnClickListener(new OnClickListenerWithoutLogin() {
                @Override
                public void onClickWithoutLogin(View v) {
                    dismiss();
                }
            });
        }
        if (backgroundBlurRadius > 0) {
            backgroundImageView.setVisibility(View.GONE);
            Activity activity = BaseApp.currentActivity();
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                ViewGroup rootView = decorView.findViewById(android.R.id.content);
                Drawable windowBackground = decorView.getBackground();
                backgroundBlurView.setupWith(rootView)
                        .windowBackground(windowBackground)
                        .setOverlayColor(backgroundColor)
                        .blurAlgorithm(new RenderScriptBlur(context))
                        .blurRadius(backgroundBlurRadius)
                        .setHasFixedTransformationMatrix(true);
            } else {
                backgroundImageView.setImageDrawable(new ColorDrawable(backgroundColor));
            }
        } else {
            backgroundImageView.setVisibility(View.VISIBLE);
            if (backgroundBitmap != null) {
                backgroundImageView.setImageBitmap(backgroundBitmap);
                backgroundImageView.setColorFilter(backgroundColor);
            } else if (backgroundResource != -1) {
                backgroundImageView.setImageResource(backgroundResource);
                backgroundImageView.setColorFilter(backgroundColor);
            } else if (backgroundDrawable != null) {
                backgroundImageView.setImageDrawable(backgroundDrawable);
                backgroundImageView.setColorFilter(backgroundColor);
            } else if (backgroundColor != Color.TRANSPARENT) {
                backgroundImageView.setImageDrawable(new ColorDrawable(backgroundColor));
            }
        }
    }

    private void startContentInAnim() {
        if (contentAnim != null) {
            contentAnimDuration = contentAnim.inAnim(contentView);
        } else {
            if (contentInAnim != null) {
                contentView.startAnimation(contentInAnim);
            } else {
                AnimHelper.startZoomInAnim(contentView, contentAnimDuration);
            }
        }
    }

    private void startContentOutAnim() {
        if (contentAnim != null) {
            contentAnimDuration = contentAnim.outAnim(contentView);
        } else {
            if (contentOutAnim != null) {
                contentView.startAnimation(contentOutAnim);
            } else {
                AnimHelper.startZoomOutAnim(contentView, contentAnimDuration);
            }
        }
    }

    private void startBackgroundInAnim() {
        if (backgroundAnim != null) {
            backgroundAnimDuration = backgroundAnim.inAnim(backgroundBlurView);
        } else {
            if (backgroundInAnim != null) {
                backgroundBlurView.startAnimation(backgroundInAnim);
            } else {
                AnimHelper.startAlphaInAnim(backgroundBlurView, backgroundAnimDuration);
            }
        }
    }

    private void startBackgroundOutAnim() {
        if (backgroundAnim != null) {
            backgroundAnimDuration = backgroundAnim.outAnim(backgroundBlurView);
        } else {
            if (backgroundOutAnim != null) {
                backgroundBlurView.startAnimation(backgroundOutAnim);
            } else {
                AnimHelper.startAlphaOutAnim(backgroundBlurView, backgroundAnimDuration);
            }
        }
    }

    public AnyDialog contentAnim(IContentAnim contentAnim) {
        this.contentAnim = contentAnim;
        return this;
    }

    public AnyDialog contentInAnim(@AnimRes int anim) {
        contentInAnim(AnimationUtils.loadAnimation(context, anim));
        return this;
    }

    public AnyDialog contentInAnim(@NonNull Animation anim) {
        contentInAnim = anim;
        contentAnimDuration = Math.max(contentAnimDuration, contentInAnim.getDuration());
        return this;
    }

    public AnyDialog contentOutAnim(@AnimRes int anim) {
        contentOutAnim(AnimationUtils.loadAnimation(context, anim));
        return this;
    }

    public AnyDialog contentOutAnim(@NonNull Animation anim) {
        contentOutAnim = anim;
        contentAnimDuration = Math.max(contentAnimDuration, contentOutAnim.getDuration());
        return this;
    }

    public AnyDialog backgroundAnim(IBackgroundAnim backgroundAnim) {
        this.backgroundAnim = backgroundAnim;
        return this;
    }

    public AnyDialog backgroundInAnim(@AnimRes int anim) {
        backgroundInAnim(AnimationUtils.loadAnimation(context, anim));
        return this;
    }

    public AnyDialog backgroundInAnim(@NonNull Animation anim) {
        backgroundInAnim = anim;
        backgroundAnimDuration = Math.max(backgroundAnimDuration, backgroundInAnim.getDuration());
        return this;
    }

    public AnyDialog backgroundOutAnim(@AnimRes int anim) {
        backgroundOutAnim(AnimationUtils.loadAnimation(context, anim));
        return this;
    }

    public AnyDialog backgroundOutAnim(@NonNull Animation anim) {
        backgroundOutAnim = anim;
        backgroundAnimDuration = Math.max(backgroundAnimDuration, backgroundOutAnim.getDuration());
        return this;
    }

    public AnyDialog defaultContentAnimDuration(long defaultAnimDuration) {
        this.contentAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyDialog defaultBackgroundAnimDuration(long defaultAnimDuration) {
        this.backgroundAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyDialog contentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public AnyDialog contentView(@LayoutRes int contentViewId) {
        contentId = contentViewId;
        return this;
    }

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #backgroundColorInt(int)}为半透明颜色
     *
     * @param radius 模糊半径
     * @return AnyDialog
     */
    public AnyDialog backgroundBlur(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius) {
        backgroundBlurRadius = radius;
        return this;
    }

    public AnyDialog backgroundBitmap(Bitmap bitmap) {
        backgroundBitmap = bitmap;
        return this;
    }

    public AnyDialog backgroundResource(@DrawableRes int resource) {
        backgroundResource = resource;
        return this;
    }

    public AnyDialog backgroundDrawable(Drawable drawable) {
        backgroundDrawable = drawable;
        return this;
    }

    /**
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlur(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param color ColorInt
     * @return AnyDialog
     */
    public AnyDialog backgroundColorInt(@ColorInt int color) {
        backgroundColor = color;
        return this;
    }

    public AnyDialog backgroundColorRes(@ColorRes int color) {
        backgroundColor = context.getResources().getColor(color);
        return this;
    }

    public AnyDialog clickBackCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public AnyDialog touchOutsideCancelable(boolean cancelable) {
        setCanceledOnTouchOutside(cancelable);
        this.touchOutsideCancelable = cancelable;
        return this;
    }

    /**
     * {@link Gravity}
     *
     * @param gravity Gravity
     * @return AnyDialog
     */
    public AnyDialog gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public AnyDialog bindData(IDataBinder dataBinder) {
        this.dataBinder = dataBinder;
        return this;
    }

    public AnyDialog onClick(@IdRes int viewId, OnDialogClickListener listener) {
        viewHolder.addOnClickListener(viewId, listener);
        return this;
    }

    public <V extends View> V getView(@IdRes int viewId) {
        return viewHolder.getView(viewId);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }
}
