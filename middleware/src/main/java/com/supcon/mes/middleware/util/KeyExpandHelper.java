package com.supcon.mes.middleware.util;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.supcon.mes.mbap.utils.KeyHelper;
import com.supcon.mes.mbap.utils.KeyboardUtil;

/**
 * @author yangfei.cao
 * @ClassName gxdepot
 * @date 2018/11/21
 * ------------- Description -------------
 */
public class KeyExpandHelper extends KeyHelper {

    //键盘enter变搜索
    public static void doActionSearch(EditText editText, boolean hideKeyboard, final KeyHelper.OnActionListener listener) {
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        editText.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (hideKeyboard)
                    KeyboardUtil.hideKeyboard(view.getContext(), view);
                if (listener != null)
                    listener.onActionCompleted();
                return true;
            }
            return false;
        });

    }
}
