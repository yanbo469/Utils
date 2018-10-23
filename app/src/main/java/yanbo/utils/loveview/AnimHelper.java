package yanbo.utils.loveview;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author Cuizhen
 * @date 2018/6/14-上午9:28
 */
class AnimHelper {

    static ObjectAnimator scaleX(View view, long time, long delayTime, float pivotX, float pivotY, float... values) {
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "scaleX", values);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    static ObjectAnimator scaleY(View view, long time, long delayTime, float pivotX, float pivotY, float... values) {
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "scaleY", values);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    static ObjectAnimator translationX(View view, long time, long delayTime, float... values) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationX", values);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    static ObjectAnimator translationY(View view, long time, long delayTime, float... values) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", values);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    static ObjectAnimator alpha(View view, long time, long delayTime, float... values) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "alpha", values);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    static ObjectAnimator rotation(View view, long time, long delayTime, float pivotX, float pivotY, float... values) {
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setStartDelay(delayTime);
        rotation.setDuration(time);
        return rotation;
    }
}
