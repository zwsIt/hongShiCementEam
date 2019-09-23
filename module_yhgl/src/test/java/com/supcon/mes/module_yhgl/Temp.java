package com.supcon.mes.module_yhgl;

import com.google.common.net.InternetDomainName;

/**
 * @author 徐时运
 * @E-mail ciruy.victory@gmail.com
 * @date 2018/11/289:50
 */
public class Temp implements Cloneable {
    public Inner a = new Inner();
    public Inner b = new Inner();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
