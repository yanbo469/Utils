package yanbo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import yanbo.assist.utils.SPUtils;
import yanbo.assist.utils.Utils;

/**
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/4/11-下午1:03
 */
public class UserUtils {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_INFO = "user_info";

    private static UserUtils mUserUtils = null;

    private String mToken;
    private UserInfoBean mUserInfo;

    public UserUtils() {
        mToken = SPUtils.getInstance(Utils.getAppContext()).get(KEY_TOKEN, "");
        String userInfoJson = SPUtils.getInstance(Utils.getAppContext()).get(KEY_USER_INFO, "");
        try {
            mUserInfo = new Gson().fromJson(userInfoJson, UserInfoBean.class);
        } catch (JsonSyntaxException e) {
            mUserInfo = new UserInfoBean();
        }
    }

    public static UserUtils getInstance() {
        if (mUserUtils == null) {
            mUserUtils = new UserUtils();
        }
        return mUserUtils;
    }

    @NonNull
    public String getToken() {
        if (mToken == null) {
            mToken = "";
        }
        return mToken;
    }

    @NonNull
    public UserInfoBean getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfoBean();
        }
        return mUserInfo;
    }

    public String getUserId() {
        if (getUserInfo().getUser_id() == null) {
            getUserInfo().setUser_id("");
        }
        return getUserInfo().getUser_id();
    }

    public void login(@NonNull String token, @NonNull UserInfoBean userInfoBean) {
        mToken = token;
        mUserInfo = userInfoBean;
        SPUtils.getInstance(Utils.getAppContext()).save(KEY_TOKEN, mToken);
        String userInfoJson = new Gson().toJson(mUserInfo);
        SPUtils.getInstance(Utils.getAppContext()).save(KEY_USER_INFO, userInfoJson);
    }

    public void logout() {
        SPUtils.getInstance(Utils.getAppContext()).clear();
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken()) && !TextUtils.isEmpty(getUserId());
    }

    public boolean doIfLogin(Context context) {
        if (isLogin()) {
            return true;
        }
        EnterActivity.startSelf(context);
        return false;
    }
}
