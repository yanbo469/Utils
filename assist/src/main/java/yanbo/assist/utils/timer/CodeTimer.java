package yanbo.assist.utils.timer;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * 描述：对一个View进行倒计时
 *
 * @author Yanbo
 * @date 2018/9/20
 */
public class CodeTimer<V extends View> implements SecondTimer.OnTimerTickListener, SecondTimer.OnTimerStartListener, SecondTimer.OnTimerFinishListener {

    private final SecondTimer mSecondTimer;
    private final V mTimerView;
    private OnTimerListener<V> mOnTimerListener = null;

    public CodeTimer(int second, @NonNull V timerView) {
        this.mTimerView = timerView;
        mSecondTimer = new SecondTimer(second, 1);
        mSecondTimer.setOnTimerStartListener(this);
        mSecondTimer.setOnTimerTickListener(this);
        mSecondTimer.setOnTimerFinishListener(this);
    }

    public CodeTimer<V> setOnTimerListener(OnTimerListener<V> onTimerListener) {
        this.mOnTimerListener = onTimerListener;
        return this;
    }

    public void start(){
        if (!mSecondTimer.isStart()) {
            mSecondTimer.start();
        }
    }

    public void cancel(){
        mSecondTimer.cancel();
    }

    @Override
    public void onTick(long secondUntilFinished) {
        if (mOnTimerListener != null) {
            mOnTimerListener.onTick(mTimerView, secondUntilFinished);
        }
    }

    @Override
    public void onStart(long secondUntilFinished) {
        if (mOnTimerListener != null) {
            mOnTimerListener.onStart(mTimerView, secondUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (mOnTimerListener != null) {
            mOnTimerListener.onFinish(mTimerView);
        }
    }

    public interface OnTimerListener<V> {
        void onStart(V timerView, long secondUntilFinished);
        void onTick(V timerView, long secondUntilFinished);
        void onFinish(V timerView);
    }
}
