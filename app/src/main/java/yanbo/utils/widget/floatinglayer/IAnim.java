package yanbo.utils.widget.floatinglayer;

import android.view.View;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ:
 * E-mail:
 * GitHub:
 */
public interface IAnim {
    /**
     * 内容进入动画
     *
     * @param target 内容
     * @return 动画时长
     */
    long inAnim(View target);

    /**
     * 内容消失动画
     *
     * @param target 内容
     * @return 动画时长
     */
    long outAnim(View target);
}
