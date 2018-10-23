package yanbo.assist.utils.login;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import yanbo.assist.R;
import yanbo.assist.utils.RegexUtils;
import yanbo.assist.utils.ResourcesUtils;
import yanbo.assist.utils.ToastMaker;
import yanbo.assist.utils.listener.OnClickListenerWithoutLogin;


/**
 * 描述：登录辅助类
 * new 一个对象传入对应的控件以及设置监听 new LoginClickListener
 * new LoginUtils(getContext(), et_usernumber, et_password, btn_login, new LoginClickListener() {
 * public void onClick(String userNumber, String passWord) {//去登陆ToastMaker.showShort("登陆成功");}});
 *
 * @author Yanbo
 * @date 18/9/1
 */
public class LoginUtils {


    private String userNumber;
    private String passWord;

    /**
     * 登录辅助类
     *
     * @param context
     * @param et_usernumber      登录账号输入框
     * @param et_password        登录密码输入框
     * @param btn_login          登录按钮
     * @param loginClickListener 回调监听
     */
    public LoginUtils(Context context, final EditText et_usernumber, final EditText et_password, final TextView btn_login, final LoginClickListener loginClickListener) {

        userNumber = et_usernumber.getText().toString().trim();
        passWord = et_password.getText().toString().trim();

        btn_login.setOnClickListener(new OnClickListenerWithoutLogin() {
            @Override
            public void onClickWithoutLogin(View v) {
                //校验账号是否合法
                if (!RegexUtils.matchPhone(userNumber)) {
                    //提示
                    ToastMaker.showShort(ResourcesUtils.getString(R.string.login_set_phone));
                    return;
                }
                //校验密码是否合法
                if (!RegexUtils.matchPassword(passWord)) {
                    //提示
                    ToastMaker.showShort(ResourcesUtils.getString(R.string.login_set_password));
                    return;
                }
                //回调
                if (loginClickListener != null) {
                    loginClickListener.onClick(userNumber, passWord);
                }
            }
        });
        //设置按钮不可点击
        btn_login.setClickable(false);
        //设置按钮半透明
        btn_login.setAlpha(0.6f);
        et_usernumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userNumber = et_usernumber.getText().toString().trim();
                //校验和判断按钮是否可以点击
                if (!TextUtils.isEmpty(userNumber) && !TextUtils.isEmpty(passWord)) {
                    //校验账号和密码是否合法
                    if (editable.length() > 5 && passWord.length() > 5) {
                        //设置按钮可点击
                        btn_login.setClickable(true);
                        //设置按钮全部显示
                        btn_login.setAlpha(1f);
                    } else {
                        //设置按钮不可点击
                        btn_login.setClickable(false);
                        //设置按钮半透明
                        btn_login.setAlpha(0.6f);
                        return;
                    }
                } else {
                    //设置按钮不可点击
                    btn_login.setClickable(false);
                    //设置按钮半透明
                    btn_login.setAlpha(0.6f);
                    return;
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passWord = et_password.getText().toString().trim();
                //校验和判断按钮是否可以点击
                if (!TextUtils.isEmpty(userNumber) && !TextUtils.isEmpty(passWord)) {
                    //校验账号和密码是否合法
                    if (userNumber.length() > 5 && editable.length() > 5) {
                        //设置按钮可点击
                        btn_login.setClickable(true);
                        //设置按钮全部显示
                        btn_login.setAlpha(1f);
                    } else {
                        //设置按钮不可点击
                        btn_login.setClickable(false);
                        //设置按钮半透明
                        btn_login.setAlpha(0.6f);
                        return;
                    }
                } else {
                    //设置按钮不可点击
                    btn_login.setClickable(false);
                    //设置按钮半透明
                    btn_login.setAlpha(0.6f);
                    return;
                }
            }
        });


    }

}
