package yanbo.utils.widget.floatinglayer;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import yanbo.assist.utils.listener.OnClickListenerWithoutLogin;


/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ:
 * E-mail:
 * GitHub:
 */
public class ViewHolder {

    private final FloatingLayer floatingLayer;
    private FrameLayout mContainer;
    private SparseArray<View> views = null;
    private SparseArray<OnFloatingLayerClickListener> onClickListeners = null;

    public ViewHolder(FloatingLayer floatingLayer, FrameLayout container) {
        this.floatingLayer = floatingLayer;
        this.mContainer = container;
    }

    public void bindListener() {
        if (onClickListeners == null) {
            return;
        }
        for (int i = 0; i < onClickListeners.size(); i++) {
            int viewId = onClickListeners.keyAt(i);
            final OnFloatingLayerClickListener listener = onClickListeners.valueAt(i);
            getView(viewId).setOnClickListener(new OnClickListenerWithoutLogin() {
                @Override
                public void onClickWithoutLogin(View v) {
                    listener.onClick(floatingLayer, v);
                }
            });
        }
    }

    public <V extends View> V getView(@IdRes int viewId) {
        if (views == null) {
            views = new SparseArray<>();
        }
        if (views.indexOfKey(viewId) < 0) {
            V view = mContainer.findViewById(viewId);
            views.put(viewId, view);
            return view;
        }
        return (V) views.get(viewId);
    }

    public void addOnClickListener(OnFloatingLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        if (onClickListeners == null) {
            onClickListeners = new SparseArray<>();
        }
        if (onClickListeners.indexOfKey(viewId) < 0) {
            onClickListeners.put(viewId, listener);
        }
        if (viewIds != null && viewIds.length > 0) {
            for (int id : viewIds) {
                if (onClickListeners.indexOfKey(id) < 0) {
                    onClickListeners.put(id, listener);
                }
            }
        }
    }
}