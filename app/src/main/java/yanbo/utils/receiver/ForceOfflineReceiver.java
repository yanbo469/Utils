package yanbo.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import yanbo.utils.BaseApp;
import yanbo.utils.common.Config;


/**
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/4/4-上午9:11
 */
public class ForceOfflineReceiver extends BroadcastReceiver {
    public static final String KEY_CODE = "code";
    public static final String KEY_MSG = "msg";

    private final Handler forceOfflineHandler;

    public ForceOfflineReceiver(Handler forceOfflineHandler) {
        this.forceOfflineHandler = forceOfflineHandler;
    }


    public static void send(int code, String msg) {
        Intent intent = new Intent(Config.ACTION_FORCE_OFFLINE);
        Bundle bundle = new Bundle(2);
        bundle.putInt(KEY_CODE, code);
        bundle.putString(KEY_MSG, msg);
        intent.putExtra("bundle", bundle);
        LocalBroadcastManager.getInstance(BaseApp.getAppContext()).sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Message message = forceOfflineHandler.obtainMessage();
        message.setData(intent.getBundleExtra("bundle"));
        forceOfflineHandler.sendMessage(message);
    }
}
