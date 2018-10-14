package yanbo.assist.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import yanbo.assist.utils.listener.SimpleTextWatcher;


/**
 * 描述：输入金额的文本框
 *
 * @author Yanbo
 * @date 2018/9/21
 */
public class EditTextUtils {

    @SuppressLint("SetTextI18n")
    public static void makeEditTextWithShowInputWordsNumber(EditText inputEditText, final TextView numTextView, final int maxNum) {
        numTextView.setText(inputEditText.getText().toString().length() + "/" + maxNum);
        inputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        inputEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                numTextView.setText(editable.toString().length() + "/" + maxNum);
            }
        });
    }


    public static void makeEditTextWithAmountInput(final EditText editText, final float minMoney, final float allMoney) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                    if (".".equals(s.toString().trim())) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }
                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!".".equals(s.toString().substring(1, 2))) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString()) && !".".equals(s.toString())) {
                    if (!"".equals(s.toString()) && !".".equals(s.toString())) {
                        if (Float.valueOf(s.toString()) > allMoney) {
                            if (allMoney < minMoney) {
                                ToastMaker.showShort("账户余额不足");
                            }
                            setTextAndSelection(editText, String.valueOf(allMoney));
                            return;
                        }
                        if (minMoney < Float.valueOf(s.toString())) {
                            ToastMaker.showShort("可提现");
                        }

                    }
                }
            }
        });
    }

    public static void setTextAndSelection(EditText editText, CharSequence text) {
        editText.setText(text);
        editText.setSelection(editText.getText().toString().length());
    }

}
