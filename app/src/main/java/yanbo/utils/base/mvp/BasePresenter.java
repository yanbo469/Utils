package yanbo.utils.base.mvp;

import android.content.Context;

/**
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/4/4-下午1:23
 */
public abstract class BasePresenter<V extends BaseView> {
    protected Context context;
    private V baseView;

    void onCreate(V baseView) {
        this.baseView = baseView;
        context = baseView.getContext();
    }

    void onStop() {
    }

    void onDestroy() {
        baseView = null;
    }

    public V getBaseView() {
        return baseView;
    }

    public boolean isAttachView() {
        return baseView != null;
    }

    public Context getContext() {
        return context;
    }

    public void showLoading() {
        if (baseView != null) {
            baseView.showLoadingDialog();
        }
    }

    public void dismissLoading() {
        if (baseView != null) {
            baseView.dismissLoadingDialog();
        }
    }
}
