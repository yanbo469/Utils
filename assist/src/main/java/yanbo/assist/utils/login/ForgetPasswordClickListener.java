package yanbo.assist.utils.login;

/**
 * 描述：忘记密码完成按钮的回调监听
 *
 * @author Yanbo
 * @date 18/9/1
 */
public interface ForgetPasswordClickListener {
    /**
     * 点击事件回调
     *
     * @param userNumber 账号
     * @param passWord   密码
     * @param code       验证码
     */
    void onClick(String userNumber, String passWord, String code);
}
