package com.supcon.mes.middleware.util;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NFC读取卡片信息处理类
 */
public class NFCHelper {

    private static class NFCHelperHolder {
        private final static NFCHelper INSTANCE = new NFCHelper();
    }

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    private OnNFCListener onNFCListener;

    private NFCHelper() {
    }


    public static NFCHelper getInstance() {
        return new NFCHelper();
    }

    /**
     * @param
     * @description 获取NFC适配器
     * @author zhangwenshuai1
     * @date 2018/6/27
     */
    public void setup(Context context) {

        if (!checkNFC(context)) {
            return;
        }

        //创建PendingIntent对象,当检测到一个Tag标签就会执行此Intent; new Intent(context,context.getClass())自己的activity内接收intent
//        if (mPendingIntent == null)
        mPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, context.getClass()), 0);
    }

    /**
     * 释放 mPendingIntent
     */
    public void release() {
        mPendingIntent = null;
    }


    /**
     * @param
     * @description 检测NFC功能, 创建PendingIntent对象
     * @author zhangwenshuai1
     * @date 2018/6/27
     */
    public boolean checkNFC(Context context) {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        //判断设备是否支持NFC功能
        if (mNfcAdapter == null) {
            ToastUtils.show(context, "设备不支持NFC功能!", 3000);
            return false;
        }

        //判断设备NFC功能是否打开
        if (!mNfcAdapter.isEnabled()) {
            ToastUtils.show(context, "请到系统设置中打开NFC功能!", 3000);
            return false;
        }

        return true;
    }

    /**
     * @param
     * @description 拦截NFC, 设置处理优于所有其他NFC的处理, 呈现在用户界面的最前面
     * @author zhangwenshuai1
     * @date 2018/6/27
     */
    public void onResumeNFC(Activity activity) {
        if (mNfcAdapter != null && mPendingIntent != null)
            mNfcAdapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
    }

    /**
     * @param
     * @description 恢复默认状态
     * @author zhangwenshuai1
     * @date 2018/6/27
     */
    public void onPauseNFC(Activity activity) {
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(activity);
    }

    /**
     * @param
     * @description 获取到Tag对象
     * @author zhangwenshuai1
     * @date 2018/6/27
     */
    public String dealNFCTag(Intent intent) {
        String action = intent.getAction();
//        Log.d(TAG, "action " + action);
        if (TextUtils.isEmpty(action)) {
            return "获取标签信息失败!";
        }

        //1.获取Tag对象
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
            return "获取标签对象为空!";

//        Parcelable[] parcelables = intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));  //附加信息
//        for (Parcelable parcelable : parcelables){
//            NdefMessage ndefMessage = (NdefMessage)parcelable;
//            Log.d(TAG, ndefMessage.toString());
//
//            NdefRecord[] ndefRecords =ndefMessage.getRecords();
//        }

//        MifareClassic mifareClassic = MifareClassic.get(tag);
//        mifareClassic.getTag().getTechList();
//        NfcV nfcV = NfcV.get(tag);
//        nfcV.getTag().getTechList();
//        NfcA nfcA = NfcA.get(tag);
//        nfcA.getTag().getTechList();


        //2.获取Ndef的实例
        String nfcMsg = "";
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Ndef ndef = Ndef.get(tag);
            String textRecord = readNfcTag(intent);
            nfcMsg = fireNdefEvent(ndef, textRecord);
            try {
                ndef.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            for (String tagTech : tag.getTechList()) {
                LogUtil.d(tagTech);
                if (tagTech.equals(NdefFormatable.class.getName())) {
                    nfcMsg = fireTagEvent(tag);
                } else if (tagTech.equals(Ndef.class.getName())) {
                    Ndef ndef = Ndef.get(tag);
                    String textRecord = readNfcTag(ndef);
                    nfcMsg = fireNdefEvent(ndef, textRecord);
                }
            }
        }
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            nfcMsg = fireTagEvent(tag);
        }
        if (onNFCListener != null) {
            onNFCListener.onNFCReceived(nfcMsg);
        }
        return nfcMsg;

    }


    /**
     * @param
     * @description 解析Ndef信息
     * @author zhangwenshuai1
     * @date 2018/6/19
     */
    private String fireNdefEvent(Ndef ndef, String textRecord) {
        if (ndef != null) {
            byte[] bytes = ndef.getTag().getId();
            String id = bytesToHexString(bytes);

            Map<String, String> jsonObject = new HashMap<>();
            jsonObject.put("id", id);
            jsonObject.put("type", ndef.getType());
            jsonObject.put("maxsize", ndef.getMaxSize() + " bytes");
            jsonObject.put("textRecord", textRecord);

            Gson gson = new Gson();
            String nfc = gson.toJson(jsonObject);
            return nfc;
        }
        return "";
    }

    /**
     * @param
     * @description 解析tag信息
     * @author zhangwenshuai1
     * @date 2018/6/19
     */
    public String fireTagEvent(Tag tag) {
        if (tag != null) {
            byte[] bytes = tag.getId();
            String id = bytesToHexString(bytes);
            LogUtil.d(id);

            /*JSONObject jsonObject = Util.tagToJSON(tag);
            Log.d(TAG, jsonObject.toString());
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("id");
                byte[] bytes1 = Util.jsonToByteArray(jsonArray);
                String message1 = bytesToHexString(bytes1);
                Log.d(TAG, message1);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            String[] techList = tag.getTechList();
            List<String> techTypes = new ArrayList<>();
            for (String tech : techList) {
                techTypes.add(tech);
            }

            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("id", id);
            jsonObject.put("techTypes", techTypes);

            Gson gson = new Gson();
            String nfc = gson.toJson(jsonObject);

            return nfc;

        }
        return "";
    }


    /**
     * 读取NFC标签文本数据
     */
    private static String readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    return textRecord;
                }
            } catch (Exception e) {
            }
        }
        return "";
    }

    private static String readNfcTag(Ndef ndef) {
        if (ndef != null) {
            try {
                ndef.connect();
                byte[] bytes = ndef.getTag().getId();
                String id = bytesToHexString(bytes);
                LogUtil.d(id);

                NdefMessage ndefMessage = ndef.getNdefMessage();   //附加信息
                if (ndefMessage != null) {
                    NdefRecord[] ndefRecords = ndefMessage.getRecords();
                    String textRecord = "";
                    for (NdefRecord ndefRecord : ndefRecords) {
                        textRecord = parseTextRecord(ndefRecord);//读取写入的文本信息
                    }
                    return textRecord;
                }

            } catch (IOException | FormatException e) {
                e.printStackTrace();
            } finally {
                try {
                    ndef.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型 注意：写入的是Text文本此处代码Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT) = true，否则会return null
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }


    /**
     * @param
     * @description 字节数组转为字符串信息
     * @author zhangwenshuai1
     * @date 2018/6/19
     */
    public static String bytesToHexString(byte[] bytes) {
        final String HEX = "0123456789ABCDEF";
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
//        for (int i = bytes.length - 1; i >= 0; i--) {
        for (int i = 0; i < bytes.length; i++) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt((bytes[i] >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt(bytes[i] & 0x0f));
        }

        return sb.toString();
    }

    public static String byteArrayToString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src != null && src.length > 0) {
            for (int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString().toUpperCase();
        } else {
            return null;
        }
    }


    public void setOnNFCListener(OnNFCListener onNFCListener) {
        this.onNFCListener = onNFCListener;
    }

    public interface OnNFCListener {
        void onNFCReceived(String nfc);
    }


}
