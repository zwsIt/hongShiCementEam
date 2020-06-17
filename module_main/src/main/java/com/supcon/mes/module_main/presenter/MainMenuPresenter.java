package com.supcon.mes.module_main.presenter;

import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WorkInfo;
import com.supcon.mes.middleware.model.bean.WorkInfoDao;
import com.supcon.mes.middleware.util.WorkHelper;
import com.supcon.mes.module_main.R;
import com.supcon.mes.module_main.model.contract.MainMenuContract;

import java.util.List;

public class MainMenuPresenter extends MainMenuContract.Presenter {

    @Override
    public void listMyMenu(boolean isHome) {
        WorkInfoDao workInfoDao = EamApplication.dao().getWorkInfoDao();
        List<WorkInfo> workInfoList = workInfoDao.queryBuilder()
                .where(WorkInfoDao.Properties.Ip.eq(EamApplication.getIpPort()),WorkInfoDao.Properties.UserId.eq(EamApplication.getAccountInfo().userId),WorkInfoDao.Properties.MySort.isNotNull())
                .orderAsc(WorkInfoDao.Properties.MySort)
                .limit(11)
                .list();
        if (isHome){ // 不存在则取默认前4
            if (workInfoList.size() == 0) {
                workInfoList = getDefaultWorkInfos(workInfoDao);
            }
            addMore(workInfoList);
        }
        getView().listMyMenuSuccess(workInfoList);
        workInfoDao.detachAll(); // 清理缓存
    }

    private List<WorkInfo> getDefaultWorkInfos(WorkInfoDao workInfoDao) {
        List<WorkInfo> workInfoList;
        workInfoList = workInfoDao.queryBuilder()
                .where(WorkInfoDao.Properties.Ip.eq(EamApplication.getIpPort()),WorkInfoDao.Properties.UserId.eq(EamApplication.getAccountInfo().userId),
                        WorkInfoDao.Properties.DefaultMenu.eq(true),WorkInfoDao.Properties.ViewType.eq(WorkInfo.VIEW_TYPE_CONTENT))
                .orderAsc(WorkInfoDao.Properties.Sort)
                .list();
        for (WorkInfo workInfo : workInfoList){
            workInfo.mySort = workInfoList.indexOf(workInfo);
            workInfo.isAdd = true;
        }
        workInfoDao.saveInTx(workInfoList); // 保存默认
        return workInfoList;
    }

    @Override
    public void listAllMenu() {
        WorkInfoDao workInfoDao = EamApplication.dao().getWorkInfoDao();
        List<WorkInfo> myWorkInfoList = workInfoDao.queryBuilder()
                .where(WorkInfoDao.Properties.Ip.eq(EamApplication.getIpPort()),WorkInfoDao.Properties.UserId.eq(EamApplication.getAccountInfo().userId),
                        WorkInfoDao.Properties.MySort.isNotNull()).orderAsc(WorkInfoDao.Properties.MySort)
                .limit(11)
                .list(); // 我的菜单
//        if (myWorkInfoList.size() == 0){
//            myWorkInfoList = getDefaultWorkInfos(workInfoDao);
//        }

        // 增加全部应用header显示
        WorkInfo headerWorkInfo = new WorkInfo();
        headerWorkInfo.viewType = WorkInfo.VIEW_TYPE_HEADER;
        headerWorkInfo.name = EamApplication.getAppContext().getString(R.string.main_other_menu);

        List<WorkInfo> workInfoList = workInfoDao.queryBuilder()
                .where(WorkInfoDao.Properties.Ip.eq(EamApplication.getIpPort()),WorkInfoDao.Properties.UserId.eq(EamApplication.getAccountInfo().userId),WorkInfoDao.Properties.MySort.isNull())
                .orderAsc(WorkInfoDao.Properties.Sort)
                .list(); // 其他菜单
        myWorkInfoList.add(headerWorkInfo);
        myWorkInfoList.addAll(workInfoList);
        getView().listAllMenuSuccess(myWorkInfoList);
    }


    private void addMore(List<WorkInfo> list){
        WorkInfo workInfo = new WorkInfo();
//        workInfo.setAppType(appType);
        workInfo.iconResId = R.drawable.ic_work_more_xxh;
        workInfo.router = Constant.Router.ALL_MENU_LIST;
        workInfo.name = "更多应用";
        list.add(workInfo);
    }


}
