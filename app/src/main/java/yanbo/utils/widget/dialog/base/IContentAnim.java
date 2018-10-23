package yanbo.utils.widget.dialog.base;

import android.view.View;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ:
 * E-mail:
 * GitHub:
 */
public interface IContentAnim {
    /**
     * 内容进入动画
     *
     * @param content 内容
     * @return 动画时长
     */
    long inAnim(View content);

    /**
     * 内容消失动画
     *
     * @param content 内容
     * @return 动画时长
     */
    long outAnim(View content);
}
