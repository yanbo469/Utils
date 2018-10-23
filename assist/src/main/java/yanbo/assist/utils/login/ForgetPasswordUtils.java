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
 * 描述：忘记密码的辅助类
 * new 一个对象传入对应的控件以及设置监听 new ForgetPasswordClickListener
 *
 * @author Yanbo
 * @date 18/9/1
 */
public class ForgetPasswordUtils implements TextWatcher {

    private EditText et_usernumber, et_code, et_password1, et_password2;
    private TextView btn_confirm;
    private String userNumber, code, passWord1, passWord2;

    /**
     * @param context
     * @param et_usernumber               账号
     * @param et_code                     验证码
     * @param et_password1                密码
     * @param et_password2                确认密码
     * @param btn_confirm                 注册按钮
     * @param forgetPasswordClickListener 回调监听
     */
    public ForgetPasswordUtils(Context context, EditText et_usernumber, EditText et_code,
                               EditText et_password1, EditText et_password2, TextView btn_confirm,
                               ForgetPasswordClickListener forgetPasswordClickListener) {
        this.et_usernumber = et_usernumber;
        this.et_code = et_code;
        this.et_password1 = et_password1;
        this.et_password2 = et_password2;
        this.btn_confirm = btn_confirm;
        init(forgetPasswordClickListener);
    }

    private void init(final ForgetPasswordClickListener forgetPasswordClickListener) {

        et_usernumber.addTextChangedListener(this);
        et_code.addTextChangedListener(this);
        et_password1.addTextChangedListener(this);
        et_password2.addTextChangedListener(this);
        btn_confirm.setOnClickListener(new OnClickListenerWithoutLogin() {
            @Override
            public void onClickWithoutLogin(View v) {
                //校验账号是否合法
                if (!RegexUtils.matchPhone(userNumber)) {
                    //提示
                    ToastMaker.showShort(ResourcesUtils.getString(R.string.login_set_phone));
                    return;
                }
                //校验密码是否合法
                if (!RegexUtils.matchPassword(passWord1)) {
                    //提示
                    ToastMaker.showShort(ResourcesUtils.getString(R.string.login_set_password));
                    return;
                }
                if (!passWord1.equals(passWord2)) {
                    //提示
                    ToastMaker.showShort(ResourcesUtils.getString(R.string.login_is_password));
                    return;
                }
                if (forgetPasswordClickListener != null) {
                    forgetPasswordClickListener.onClick(userNumber, code, passWord1);
                }
            }
        });
        //设置按钮不可点击
        btn_confirm.setClickable(false);
        //设置按钮半透明
        btn_confirm.setAlpha(0.6f);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        userNumber = et_usernumber.getText().toString().trim();
        code = et_code.getText().toString().trim();
        passWord1 = et_password1.getText().toString().trim();
        passWord2 = et_password2.getText().toString().trim();
        if (!TextUtils.isEmpty(userNumber) && !TextUtils.isEmpty(code) &&
                !TextUtils.isEmpty(passWord1) && !TextUtils.isEmpty(passWord2)) {
            //校验账号和密码是否合法
            if (userNumber.length() > 5 && code.length() > 5 &&
                    passWord1.length() > 5 && passWord2.length() > 5) {
                //设置按钮可点击
                btn_confirm.setClickable(true);
                //设置按钮全部显示
                btn_confirm.setAlpha(1f);
            } else {
                //设置按钮不可点击
                btn_confirm.setClickable(false);
                //设置按钮半透明
                btn_confirm.setAlpha(0.6f);
                return;
            }
        } else {
            //设置按钮不可点击
            btn_confirm.setClickable(false);
            //设置按钮半透明
            btn_confirm.setAlpha(0.6f);
            return;
        }

    }
}
