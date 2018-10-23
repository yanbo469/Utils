package yanbo.utils.loveview;

/**
 * @author Cuizhen
 * @date 2018/6/14-上午9:24
 */
public interface LoveTouchListener {
    /**
     * 单次点击
     */
    void onSingleTip();

    /**
     * 连续点击，即执行动画
     */
    void onMoreTip();

    /**
     * 长按
     */
    void onLongPress();
}
