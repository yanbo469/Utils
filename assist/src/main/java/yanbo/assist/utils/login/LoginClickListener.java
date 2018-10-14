package yanbo.assist.utils.login;

/**
 * 描述：登录按钮的回调监听
 *
 * @author Yanbo
 * @date 18/9/1
 */
public interface LoginClickListener {
    /**
     * 点击事件回调
     *
     * @param userNumber 账号
     * @param passWord   密码
     */
    void onClick(String userNumber, String passWord);
}