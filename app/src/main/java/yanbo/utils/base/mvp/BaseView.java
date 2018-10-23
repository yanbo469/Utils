package yanbo.utils.base.mvp;

import android.content.Context;

/**
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/4/4-下午1:23
 */
public interface BaseView {
    Context getContext();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void clearLoading();
}
