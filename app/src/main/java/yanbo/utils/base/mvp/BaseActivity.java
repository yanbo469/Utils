package yanbo.utils.base.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import yanbo.assist.utils.ClickHelper;
import yanbo.assist.utils.DisplayInfoUtils;
import yanbo.assist.utils.ResourcesUtils;
import yanbo.assist.utils.StatusBarUtils;
import yanbo.assist.utils.listener.SimpleCallback;
import yanbo.utils.BaseApp;
import yanbo.utils.MainActivity;
import yanbo.utils.R;
import yanbo.utils.UserUtils;
import yanbo.utils.common.Config;
import yanbo.utils.dialong.LoadingDialog;
import yanbo.utils.dialong.TipDialog;
import yanbo.utils.receiver.ForceOfflineReceiver;

/**
 * Activity间传值，强制使用Bundle的形式传递，禁止直接添加Extras
 *
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/4/4-下午1:23
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, View.OnClickListener {
    private static final long LOADING_MIN_SHOW_TIME = 500L;
    public P presenter;
    private Unbinder unbinder;
    private long loadingStartTime = 0L;
    private Runnable loadingRunnable = null;
    private ForceOfflineReceiver forceOfflineReceiver = null;
    private LocalBroadcastManager localBroadcastManager = null;
    private LoadingDialog loadingDialog = null;
    private TipDialog forceOfflineDialog = null;
    @SuppressLint("HandlerLeak")
    private Handler forceOfflineHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            synchronized (getActivity()) {
                if (forceOfflineDialog == null) {
                    Bundle bundle = message.getData();
                    int code = bundle.getInt(ForceOfflineReceiver.KEY_CODE);
                    String msg = bundle.getString(ForceOfflineReceiver.KEY_MSG);
                    forceOfflineDialog = TipDialog.with(getActivity())
                            .cancelable(false)
                            .title(null)
                            .message(msg)
                            .onYes(new SimpleCallback<Void>() {
                                @Override
                                public void onResult(Void data) {
//                                    EnterActivity.startSelf(getContext(), EnterActivity.TYPE_LOGIN);
                                    BaseApp.finishActivityWithout(MainActivity.class);
                                }
                            })
                            .onNo(new SimpleCallback<Void>() {
                                @Override
                                public void onResult(Void data) {
                                    BaseApp.finishActivityWithout(MainActivity.class);
                                }
                            });
                    forceOfflineDialog.show();
                }
            }
        }
    };

    /**
     * 获取布局资源文件
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化presenter
     */
    protected abstract P initPresenter();

    /**
     * 获取数据
     */
    protected abstract void initData(@NonNull Bundle bundle);

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 绑定数据
     */
    protected abstract void loadData();

    /**
     * 再次进入当前界面时的新数据，即onRestart中获取的 Bundle
     */
    protected void regainBundle(@NonNull Bundle bundle) {
    }

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @ColorInt
    protected int getStatusViewColor() {
        return ResourcesUtils.getColor(R.color.view_status_bar);
    }

    /**
     * 状态栏View的id
     */
    @IdRes
    protected int getStatusBarId() {
        return 0;
    }

    /**
     * 加载中view的id
     */
    @IdRes
    protected int getLoadingBarId() {
        return 0;
    }

    /**
     * 必须登录的点击事件
     * 如果已经登录直接执行，没有登录时跳转登录界面
     */
    public void onClickCheckLogin(View v) {
    }

    /**
     * 不需要登录的点击事件
     */
    public boolean onClickWithoutLogin(View v) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getLayoutId());
        initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            regainBundle(bundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getActivity().getClass().getName());
//        MobclickAgent.onResume(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.ACTION_FORCE_OFFLINE);
        forceOfflineReceiver = new ForceOfflineReceiver(forceOfflineHandler);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(forceOfflineReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getActivity().getClass().getName());
//        MobclickAgent.onPause(this);

        if (forceOfflineReceiver != null && localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(forceOfflineReceiver);
            forceOfflineReceiver = null;
            localBroadcastManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (!isFinishing() && null != unbinder) {
            unbinder.unbind();
        }
        if (presenter != null) {
            presenter.onDestroy();
        }
        clearLoading();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    protected void initWindow() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        StatusBarUtils.translucentStatusBar(this);
    }

    private void initStatusBar() {
        int id = getStatusBarId();
        if (id == -1) {
            return;
        }
        if (0 == id) {
            id = R.id.view_status_bar;
        }
        View view = findViewById(id);
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = DisplayInfoUtils.getInstance().getStatusBarHeight();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
        view.setBackgroundColor(getStatusViewColor());
        view.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        initStatusBar();
        unbinder = ButterKnife.bind(this);
        presenter = initPresenter();
        if (presenter != null) {
            presenter.onCreate(this);
        }
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            initData(bundle);
        }
        initView();
        loadData();
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    private View getLoadingBar() {
        int id = getLoadingBarId();
        if (id == -1) {
            return null;
        }
        if (0 == id) {
            id = R.id.loading_bar;
        }
        return findViewById(id);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    protected Activity getActivity() {
        return this;
    }

    /**
     * 用注解绑定点击事件时，在该方法绑定
     *
     * @param v
     */
    @Override
    public void onClick(final View v) {
        ClickHelper.onlyFirstSameView(v, new ClickHelper.Callback() {
            @Override
            public void onClick(View view) {
                if (!onClickWithoutLogin(view)) {
                    if (UserUtils.getInstance().doIfLogin(getContext())) {
                        onClickCheckLogin(view);
                    }
                }
            }
        });
    }

    public void showLoadingBar() {
        final View view = getLoadingBar();
        if (view != null) {
            if (loadingRunnable != null) {
                view.removeCallbacks(loadingRunnable);
            }
            view.setVisibility(View.VISIBLE);
            loadingStartTime = System.currentTimeMillis();
        }
    }

    public void dismissLoadingBar() {
        final View view = getLoadingBar();
        if (view != null) {
            long loadingEndTime = System.currentTimeMillis();
            long loadingTime = loadingEndTime - loadingStartTime;
            long delay = LOADING_MIN_SHOW_TIME - loadingTime;
            delay = delay < 0 ? 0 : delay;
            if (loadingRunnable == null) {
                loadingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                };
            }
            view.postDelayed(loadingRunnable, delay);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.with(this);
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void clearLoading() {
        final View view = getLoadingBar();
        if (view != null) {
            view.setVisibility(View.GONE);
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
