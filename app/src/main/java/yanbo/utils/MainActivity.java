package yanbo.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import per.goweii.actionbarex.SimpleActionBar;
import yanbo.utils.base.mvp.BaseActivity;
import yanbo.utils.base.mvp.BasePresenter;

public class MainActivity extends BaseActivity {


    @BindView(R.id.action_bar_ex)
    SimpleActionBar actionBarEx;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

    @OnClick({R.id.tv_login})
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public boolean onClickWithoutLogin(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                LoveViewActivity.startSelf(this);
                return true;

            default:
                return false;
        }
    }
}
