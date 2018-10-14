package yanbo.assist.utils.timer;

/**
 * 秒倒计时
 *
 * @author Yanbo
 * @date 2018/9/20
 */
public class SecondTimer extends BaseTimer {

    private OnTimerStartListener mOnTimerStartListener = null;
    private OnTimerTickListener mOnTimerTickListener = null;
    private OnTimerFinishListener mOnTimerFinishListener = null;

    public SecondTimer(long duration, long delay) {
        super(duration * 1000, delay * 1000);
    }

    @Override
    protected void onTimerStart(long millisUntilFinished) {
        if (mOnTimerStartListener != null) {
            mOnTimerStartListener.onStart(millisUntilFinished / 1000);
        }
    }

    @Override
    protected void onTimerTick(long millisUntilFinished) {
        if (mOnTimerTickListener != null) {
            mOnTimerTickListener.onTick(millisUntilFinished / 1000);
        }
    }

    @Override
    public void onTimerFinish() {
        if (mOnTimerFinishListener != null) {
            mOnTimerFinishListener.onFinish();
        }
    }

    public void setOnTimerStartListener(OnTimerStartListener onTimerStartListener) {
        mOnTimerStartListener = onTimerStartListener;
    }

    public void setOnTimerTickListener(OnTimerTickListener onTimerTickListener) {
        mOnTimerTickListener = onTimerTickListener;
    }

    public void setOnTimerFinishListener(OnTimerFinishListener onTimerFinishListener) {
        mOnTimerFinishListener = onTimerFinishListener;
    }

    public interface OnTimerStartListener {
        void onStart(long secondUntilFinished);
    }

    public interface OnTimerTickListener {
        void onTick(long secondUntilFinished);
    }

    public interface OnTimerFinishListener {
        void onFinish();
    }
}

