package yanbo.utils.widget.dialog.base;

import android.view.View;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ:
 * E-mail:
 * GitHub:
 */
public interface IBackgroundAnim {
    /**
     * 背景进入动画
     *
     * @param background 背景
     * @return 动画时长
     */
    long inAnim(View background);

    /**
     * 背景消失动画
     *
     * @param background 背景
     * @return 动画时长
     */
    long outAnim(View background);
}
