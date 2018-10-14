package yanbo.assist.utils.timer;

import android.os.CountDownTimer;

/**
 * 描述：毫秒倒计时
 *
 * @author Yanbo
 * @date 2018/9/20
 */
public abstract class BaseTimer extends CountDownTimer {

    private boolean mStart = false;

    public BaseTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture + 499, countDownInterval);
    }

    @Override
    public final void onTick(long millisUntilFinished) {
        if (!mStart) {
            mStart = true;
            onTimerStart(millisUntilFinished);
        }
        onTimerTick(millisUntilFinished);
    }

    public boolean isStart() {
        return mStart;
    }

    public void finish(){
        onFinish();
        cancel();
    }

    @Override
    public final void onFinish() {
        onTimerFinish();
    }

    protected abstract void onTimerStart(long millisUntilFinished);

    protected abstract void onTimerTick(long millisUntilFinished);

    protected abstract void onTimerFinish();
}

