package com.supcon.mes.module_main.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.List;
import java.util.Map;

 /**
  * @description
  * @author: zhangwenshuai
  * @date: 2020/6/7 17:14
  */
@ContractFactory(entites = {List.class, List.class})
public interface MainMenuAPI {
     void listMyMenu( boolean isHome);
    void listAllMenu();

}
