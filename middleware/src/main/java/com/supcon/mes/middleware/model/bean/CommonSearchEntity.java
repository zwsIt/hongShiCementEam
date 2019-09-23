package com.supcon.mes.middleware.model.bean;

import com.supcon.mes.middleware.util.PinYinUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Xushiyun
 * @date 2018/8/26
 * Email:ciruy_victory@gmail.com
 */
public interface CommonSearchEntity {
    /**
     * 获取搜索列表中所需要的id值,必须不为null
     *
     * @return id值
     */
    String getSearchId();

    /**
     * 获取搜索列表中所需要的name值,必须不为null
     *
     * @return name值
     */
    String getSearchName();

    /**
     * 获取搜索列表中所需要的某个字段值(自定义),必须不为null
     *
     * @return property值
     */
    String getSearchProperty();

    /**
     * 获取搜索列表中所需要的pinyin值,设置默认值为搜索使用PinyinUtil解析name的结果
     * 一般来说，如果需要使用组件，都会在entity中定义pinyin这个字段，但是我还是希望这个组件能够更加聪明一点
     *
     * @return pinyin值
     */
    default String getSearchPinyin() {
        return PinYinUtils.getPinyin(Objects.requireNonNull(getSearchName()));
    }

    /**
     * 其实搜索列表中字母索引并不是直接需要Pinyin值，相对的，更加需要知道首字母信息
     * 搜索的首字母只有字母和#
     * @return 返回搜索用首字母
     */
    default String getHeaderLetter() {
        return PinYinUtils.getHeaderLetter(getSearchName()).toString();
    }

    /**
     * 在搜索列表时会有按照首字母获取item位置的需要，所以需要进行逐个比较字符串大小的操作
     * 虽然当时的思路是比较首字母，但是感觉直接比较字符串效果一致
     * 现在，如果直接知道了首字母信息，直接进行比较就行了
     * @param word 比较的字母，这里一定为大写字母或者‘#’号
     * @return 返回比较结果
     */
    default int compareWithLetter(String word) {
        return getHeaderLetter().compareTo(word);
    }
}
