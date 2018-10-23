package yanbo.utils.dialong;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import yanbo.assist.utils.listener.SimpleCallback;
import yanbo.utils.R;
import yanbo.utils.widget.dialog.anim.AnimHelper;
import yanbo.utils.widget.floatinglayer.FloatingLayer;
import yanbo.utils.widget.floatinglayer.IAnim;
import yanbo.utils.widget.floatinglayer.IDataBinder;
import yanbo.utils.widget.floatinglayer.OnFloatingLayerClickListener;


/**
 * @author Cuizhen
 * @date 2018/6/21-上午10:00
 */
public class TipDialog {

    private static final long ANIM_DURATION = 200;
    private final Activity activity;
    private CharSequence title;
    private CharSequence msg;
    private CharSequence yesText;
    private CharSequence noText;
    private boolean singleBtnYes = false;
    private boolean cancelable = true;
    private SimpleCallback<Void> callbackYes = null;
    private SimpleCallback<Void> callbackNo = null;

    private TipDialog(Activity activity) {
        this.activity = activity;
    }

    public static TipDialog with(Activity activity) {
        return new TipDialog(activity);
    }

    public TipDialog yesText(CharSequence yesText) {
        this.yesText = yesText;
        return this;
    }

    public TipDialog yesText(@StringRes int yesText) {
        this.yesText = activity.getString(yesText);
        return this;
    }

    public TipDialog noText(CharSequence noText) {
        this.noText = noText;
        return this;
    }

    public TipDialog noText(@StringRes int noText) {
        this.noText = activity.getString(noText);
        return this;
    }

    public TipDialog title(CharSequence title) {
        this.title = title;
        return this;
    }

    public TipDialog title(@StringRes int title) {
        this.title = activity.getString(title);
        return this;
    }

    public TipDialog message(CharSequence msg) {
        this.msg = msg;
        return this;
    }

    public TipDialog message(@StringRes int msg) {
        this.msg = activity.getString(msg);
        return this;
    }

    public TipDialog singleYesBtn() {
        singleBtnYes = true;
        return this;
    }

    public TipDialog cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public TipDialog onYes(SimpleCallback<Void> callback) {
        callbackYes = callback;
        return this;
    }

    public TipDialog onNo(SimpleCallback<Void> callback) {
        callbackNo = callback;
        return this;
    }

    public void show() {
        FloatingLayer.with(activity)
                .contentView(R.layout.dialog_tip)
                .cancelableOnTouchOutside(cancelable)
                .bindData(new IDataBinder() {
                    @Override
                    public void bind(FloatingLayer anyDialog) {
                        FrameLayout fl_dialog_yes = anyDialog.getView(R.id.fl_dialog_yes);
                        ImageView iv_dialog_yes = anyDialog.getView(R.id.iv_dialog_yes);
                        TextView tv_dialog_yes = anyDialog.getView(R.id.tv_dialog_yes);
                        FrameLayout fl_dialog_no = anyDialog.getView(R.id.fl_dialog_no);
                        ImageView iv_dialog_no = anyDialog.getView(R.id.iv_dialog_no);
                        TextView tv_dialog_no = anyDialog.getView(R.id.tv_dialog_no);
                        View view_line = anyDialog.getView(R.id.view_line);

                        if (singleBtnYes) {
                            fl_dialog_no.setVisibility(View.GONE);
                            view_line.setVisibility(View.GONE);
                        } else {
                            if (noText == null) {
                                iv_dialog_no.setVisibility(View.VISIBLE);
                                tv_dialog_no.setVisibility(View.GONE);
                            } else {
                                iv_dialog_no.setVisibility(View.GONE);
                                tv_dialog_no.setVisibility(View.VISIBLE);
                                tv_dialog_no.setText(noText);
                            }
                        }

                        if (yesText == null) {
                            iv_dialog_yes.setVisibility(View.VISIBLE);
                            tv_dialog_yes.setVisibility(View.GONE);
                        } else {
                            iv_dialog_yes.setVisibility(View.GONE);
                            tv_dialog_yes.setVisibility(View.VISIBLE);
                            tv_dialog_yes.setText(yesText);
                        }

                        TextView tv_dialog_title = anyDialog.getView(R.id.tv_dialog_title);
                        if (title == null) {
                            tv_dialog_title.setVisibility(View.GONE);
                        } else {
                            tv_dialog_title.setText(title);
                        }

                        TextView tv_dialog_content = anyDialog.getView(R.id.tv_dialog_content);
                        tv_dialog_content.setText(msg);
                    }
                })
                .backgroundColorRes(R.color.dialog_bg)
                .gravity(Gravity.CENTER)
                .contentAnim(new IAnim() {
                    @Override
                    public long inAnim(View content) {
                        AnimHelper.startBottomAlphaInAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }

                    @Override
                    public long outAnim(View content) {
                        AnimHelper.startBottomAlphaOutAnim(content, ANIM_DURATION);
                        return ANIM_DURATION;
                    }
                })
                .onClick(new OnFloatingLayerClickListener() {
                    @Override
                    public void onClick(FloatingLayer anyDialog, View v) {
                        anyDialog.dismiss();
                        if (callbackYes != null) {
                            callbackYes.onResult(null);
                        }
                    }
                }, R.id.fl_dialog_yes)
                .onClick(new OnFloatingLayerClickListener() {
                    @Override
                    public void onClick(FloatingLayer anyDialog, View v) {
                        anyDialog.dismiss();
                        if (callbackNo != null) {
                            callbackNo.onResult(null);
                        }
                    }
                }, R.id.fl_dialog_no)
                .show();
    }
}
