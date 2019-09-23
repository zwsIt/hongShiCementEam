package com.supcon.mes.module_tsd.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.supcon.common.view.util.ToastUtils;

/**
 * Created by wangshizhan on 2019/7/29
 * Email:wangshizhan@supcom.com
 */
public class TextUtil {

    public static void copyText(Context context, String text){
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText("手机唯一码",text));
        ToastUtils.show(context, "复制成功!", Toast.LENGTH_LONG);

    }


}
