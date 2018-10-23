package yanbo.utils.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import yanbo.assist.utils.ClickHelper;
import yanbo.assist.utils.DisplayInfoUtils;
import yanbo.assist.utils.ResourcesUtils;
import yanbo.utils.R;
import yanbo.utils.UserUtils;
import yanbo.utils.dialong.LoadingDialog;

/**
 * 懒加载
 *
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/3/10-下午12:38
 */
public abstract class BaseLazyFragment<T extends BasePresenter> extends LazyFragment implements BaseView, View.OnClickListener {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    private static final long LOADING_MIN_SHOW_TIME = 500L;
    protected T presenter;
    boolean showLoading = true;
    private Unbinder unbinder;
    private long loadingStartTime = 0L;
    private Runnable loadingRunnable = null;
    private LoadingDialog loadingDialog = null;

    /**
     * 获取布局资源文件
     *
     * @return int
     */
    @Override
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化presenter
     *
     * @return P
     */
    protected abstract T initPresenter();

    /**
     * 绑定事件
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    protected int getStatusViewColor() {
        return ResourcesUtils.getColor(R.color.view_status_bar);
    }

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStatusBar(view);
        initialize();
    }

    @Override
    public void onDestroyView() {
        if (null != getRootView() && null != unbinder) {
            unbinder.unbind();
        }
        if (presenter != null) {
            presenter.onDestroy();
        }
        clearLoading();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initStatusBar(View view) {
        int id = getStatusBarId();
        if (id == -1) {
            return;
        }
        if (0 == id) {
            id = R.id.view_status_bar;
        }
        View statusBar = view.findViewById(id);
        if (statusBar == null) {
            return;
        }
        ViewGroup.LayoutParams params = statusBar.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DisplayInfoUtils.getInstance().getStatusBarHeight();
        statusBar.setBackgroundColor(getStatusViewColor());
        statusBar.setLayoutParams(params);
        statusBar.setVisibility(View.VISIBLE);
    }

    protected void initialize() {
        if (null != getRootView()) {
            unbinder = ButterKnife.bind(this, getRootView());
        }
        presenter = initPresenter();
        if (presenter != null) {
            presenter.onCreate(this);
        }
        initView();
        loadData();
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    private View getLoadingBar() {
        if (null == getRootView()) {
            return null;
        }
        int id = getLoadingBarId();
        if (id == -1) {
            return null;
        }
        if (0 == id) {
            id = R.id.loading_bar;
        }
        return getRootView().findViewById(id);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onClick(final View v) {
        ClickHelper.onlyFirstSameView(v, new ClickHelper.Callback() {
            @Override
            public void onClick(View view) {
                if (!onClickWithoutLogin(view)) {
                    if (UserUtils.getInstance().doIfLogin(getContext())) {
                        onClickIfLogin(view);
                    }
                }
            }
        });
    }

    public boolean onClickWithoutLogin(View v) {
        return false;
    }

    public void onClickIfLogin(View v) {

    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
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
            loadingDialog = LoadingDialog.with(getActivity());
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
