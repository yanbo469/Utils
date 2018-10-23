package yanbo.utils.dialong;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import yanbo.utils.R;
import yanbo.utils.widget.floatinglayer.AnimHelper;
import yanbo.utils.widget.floatinglayer.FloatingLayer;
import yanbo.utils.widget.floatinglayer.IAnim;


/**
 * loading弹窗
 *
 * @author Cuizhen
 * @date 2018/6/21-上午10:00
 */
public class LoadingDialog {

    private static final long ANIM_DURATION = 200;
    private final Activity activity;
    private FloatingLayer floatingLayer;

    private LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public static LoadingDialog with(Activity activity) {
        return new LoadingDialog(activity);
    }

    public void show() {
        floatingLayer = FloatingLayer.with(activity)
                .contentView(R.layout.dialog_loading)
                .cancelableOnTouchOutside(false)
                .cancelableOnClickKeyBack(false)
                .gravity(Gravity.CENTER)
                .contentAnim(new IAnim() {
                    @Override
                    public long inAnim(View content) {
                        AnimHelper.startZoomInAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }

                    @Override
                    public long outAnim(View content) {
                        AnimHelper.startZoomOutAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }
                });
        floatingLayer.show();
    }

    public void dismiss() {
        if (floatingLayer != null) {
            floatingLayer.dismiss();
            floatingLayer = null;
        }
    }
}
