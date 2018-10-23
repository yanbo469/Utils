package yanbo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import yanbo.utils.base.mvp.BaseActivity;
import yanbo.utils.base.mvp.BasePresenter;

/**
 * 描述：登录的主Activity，用于管理不同的登录注册页面显示
 *
 * @author Cuizhen
 * @date 2018/9/9
 */
public class EnterActivity extends BaseActivity  {



    public static void startSelf(Context context) {
        Intent intent = new Intent(context, EnterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initWindow() {
        super.initWindow();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData(@NonNull Bundle bundle) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {
    }

}
