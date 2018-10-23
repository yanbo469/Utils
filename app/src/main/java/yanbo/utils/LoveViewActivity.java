package yanbo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import butterknife.BindView;
import yanbo.utils.base.mvp.BaseActivity;
import yanbo.utils.base.mvp.BasePresenter;
import yanbo.utils.loveview.LoveView;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 18/10/15
 */
public class LoveViewActivity extends BaseActivity {
    @BindView(R.id.love_view)
    LoveView loveView;

    public static void startSelf(Context context) {
        Intent intent = new Intent(context, LoveViewActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_loveview;
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
