package com.supcon.mes.middleware.util;

import com.supcon.common.view.App;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/7/11
 * Email:wangshizhan@supcom.com
 */
public class AccountInfoHelper {

    public static List<AccountInfo> convertStaffInfoList(List<UserInfo> userInfos){

        List<AccountInfo> accountInfos = new ArrayList<>();
        long cid = SharedPreferencesUtils.getParam(App.getAppContext(), Constant.CID, 0L);

        for(UserInfo userInfo : userInfos){
            if(userInfo.id == 1000){
                continue;
            }

            AccountInfo accountInfo = new AccountInfo();
            accountInfo.id = userInfo.id;
            accountInfo.cid = cid;
            accountInfo.userId      = userInfo.id;
            accountInfo.userName    = userInfo.name;
            accountInfo.staffName   = userInfo.staff.name;
            accountInfo.staffCode   = userInfo.staff.code;
            accountInfo.staffId     = userInfo.staff.id;

//            accountInfo.companyName =  userInfo.staff.mainPosition.company.name;
//            accountInfo.departmentId = userInfo.staff.mainPosition.department.id;
//            accountInfo.departmentName = userInfo.staff.mainPosition.department.name;
//            accountInfo.positionName = userInfo.staff.mainPosition.name;

            accountInfos.add(accountInfo);
        }

        return accountInfos;

    }

    public static AccountInfo convertStaffInfo(UserInfo userInfo){

        long cid = SharedPreferencesUtils.getParam(App.getAppContext(), Constant.CID, 0L);
        EamApplication.setStaff(userInfo.staff);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.id = userInfo.id;
        accountInfo.cid = cid;
        accountInfo.userId      = userInfo.id;
        accountInfo.userName    = userInfo.name;
        accountInfo.staffName   = userInfo.staff.name;
        accountInfo.staffCode   = userInfo.staff.code;
        accountInfo.staffId     = userInfo.staff.id;

//        accountInfo.companyName =  staffInfo.staff.mainPosition.company.name;
//        accountInfo.departmentId = staffInfo.staff.mainPosition.department.id;
//        accountInfo.departmentName = staffInfo.staff.mainPosition.department.name;
//        accountInfo.positionName = staffInfo.staff.mainPosition.name;

        return accountInfo;

    }

}
