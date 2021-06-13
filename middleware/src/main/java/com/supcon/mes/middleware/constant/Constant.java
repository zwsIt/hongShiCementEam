package com.supcon.mes.middleware.constant;

import android.os.Environment;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.middleware.EamApplication;

import java.io.File;

import static com.supcon.common.view.App.getAppContext;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public interface Constant {

    String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "eam" + File.separator;
    String CRASH_PATH = FILE_PATH + "log" + File.separator;
    String XJ_PATH = FILE_PATH + "xj" + File.separator;
    String QX_PATH = FILE_PATH + "quexian" + File.separator;
    String YH_PATH = FILE_PATH + "yh" + File.separator;
    String XJ_BASE_PATH = FILE_PATH + "xj_base" + File.separator;
    String EAM_BASE_PATH = FILE_PATH + "eam_base" + File.separator;
    String IMAGE_SAVE_PATH = FILE_PATH + "pics" + File.separator;   //在线下载的图片存放位置
    String IMAGE_SAVE_YHPATH = YH_PATH + "pics" + File.separator;   //缺陷的图片位置
    String IMAGE_SAVE_XJPATH = XJ_PATH + "pics" + File.separator;   //巡检的图片路径
    String IMAGE_SAVE_GDPATH = FILE_PATH + "gd" + File.separator + "pics" + File.separator;   //工单的图片路径
    String CID = "cid";
    String CNAME = "cname";
    String XJ_GUIDE_IMGPATH = FILE_PATH + "eamInspectionGuideImage" + File.separator; //设备巡检指导图片路径
    String IMAGE_SAVE_WORKTICKETPATH = FILE_PATH + "workTicket" + File.separator + "pics" + File.separator;   //检修工作票的图片路径
    String IMAGE_SAVE_ELE_PATH = FILE_PATH + "electricity" + File.separator + "pics" + File.separator;   //停送电的图片路径
    String IMAGE_SAVE_SCORE_PATH = FILE_PATH + "score" + File.separator + "pics" + File.separator;   //个人评分的图片路径

    /**
     * fir.im相关信息
     */
    interface Fir {
        String API_TOKEN = "84644378657ee08d38ab6ffaf247b539"; // https://fir.im/apps提供，切勿在网站点击重新生成，否则已有app 不能检查更新
        String HONG_SHI_APP_ID = "5ddb877423389f4559ae8303";
    }

    interface CAMERA_ACT {
        int ACT_GALLERY = 0;
        int ACT_CAMERA = 1;
        int ACT_CROP = 2;
        int ACT_PERMISSION = 3;
    }

    interface Controller {
        String ROLE = "ROLE";
        String WORK_FLOW = "WORK_FLOW";
        String LINK = "LINK";

        String REPAIR_STAFF = "REPAIR_STAFF";
        String ACCEPTANCE = "ACCEPTANCE";
        String LUBRICATE_OIL = "LUBRICATE_OIL";
        String SPARE_PART = "SPARE_PART";
        String SUBMIT = "SUBMIT";
    }


    interface Router {

        String TXL_SEARCH_CONTACT_WITH_HEADER = "TXL_SEARCH_CONTACT_WITH_HEADER";
        String MAIN = "main";
        String MAIN_REDLION = "main_redlion";//红狮首页
        String LOGIN = "login";
        String SETTING = "setting";
        String WORK_SETTING = "workSetting";
        String WEB = "web";
        String IMAGE_VIEW = "IMAGE_VIEW";
        String ABOUT = "ABOUT";

        String CONTACT_SELECT = "CONTACT_SELECT";
        String CONTACT_DEPART_TREE_SELECT = "CONTACT_DEPART_TREE_SELECT";
        String CONTACT_POSITION_TREE_SELECT = "CONTACT_POSITION_TREE_SELECT";
        String CONTACT_VIEW = "CONTACT_VIEW";
        String CONTACT_SEARCH_WITH_HEADER = "CONTACT_SEARCH_WITH_HEADER";
        String SEARCH = "SEARCH";

        String PROCESSED = "PROCESSED";//已处理
        String WAIT_DEALT = "WAIT_DEALT";//待办
        String ANOMALY = "ANOMALY";//设备异常
        String RANKING = "RANKING";//人员评分
        String SCORE_RANKING = "SCORE_RANKING";//人员绩效评分排名
        String EAM_DETAIL = "EAM_DETAIL";//设备详情

        String XJGL_LIST = "XJGL_LIST";


        String YXJL_LIST = "YXJLList";
        String YXJL_EDIT = "YXJL_EDIT";

        String ADD_DEVICE = "AddDevice";

        String WXGD_LIST = "WXGDList";  //维修工单列表
        String WXGD_LIST_NEW = "WXGD_LIST_NEW"; // 新版维修工单list
        String WXGD_STATISTICS = "WXGD_STATISTICS";//工单统计
        String WXGD_DISPATCHER = "WXGD_DISPATCHER";//维修工单派单
        String WXGD_RECEIVE = "WXGD_RECEIVE";//维修工单接单
        String WXGD_EXECUTE = "WXGD_EXECUTE";//维修工单执行
        String WXGD_ACCEPTANCE = "WXGD_ACCEPTANCE";//维修工单验收
        String WXGD_COMPLETE = "WXGD_COMPLETE";//维修工单完成
        String WXGD_WARN = "WXGD_WARN";//预警工单pt

        String WXGD_REPAIR_STAFF_LIST = "WXGD_REPAIR_STAFF_LIST";//维修工单维修人员
        String WXGD_ACCEPTANCE_LIST = "WXGD_ACCEPTANCE_LIST";//维修工单验收
        String WXGD_SPARE_PART_LIST = "WXGD_SPARE_PART_LIST";//维修工单备件
        String WXGD_LUBRICATE_OIL_LIST = "WXGD_LUBRICATE_OIL_LIST";//维修工单润滑油
        String WXGD_MAINTENANCE_STAFF_LIST = "WXGD_MAINTENANCE_STAFF_LIST";//维修工单润维保
        String SPARE_PART_RECEIVE = "SPARE_PART_RECEIVE";//备件领用
        String SPARE_PART_CONSUME_LEDGER = "SPARE_PART_CONSUME_LEDGER";     //零部件消耗台账
        String SPARE_PART_RECEIVE_RECORD = "SPARE_PART_RECEIVE_RECORD";     //备件领用记录

        String YHGL_SPARE_PART_LIST = "WXGD_SPARE_PART_LIST";//隐患管理备件
        String YHGL_LUBRICATE_OIL_LIST = "WXGD_LUBRICATE_OIL_LIST";//隐患管理润滑油
        String YHGL_REPAIR_STAFF_LIST = "YHGL_REPAIR_STAFF_LIST";//隐患管理润维修人员
        String YHGL_MAINTENANCE_STAFF_LIST = "YHGL_MAINTENANCE_STAFF_LIST";//隐患管理润维保

        String SJSC = "SJSC";
        String SJXZ = "SJXZ";

        String RH = "RH";
        String BY = "BY";

        String SBDA_ONLINE_LIST = "SBDA_ONLINE_LIST";            //设备档案在线列表页面
        String SBDA_ONLINE_VIEW = "SBDA_ONLINE_VIEW";            //设备档案在线详细信息界面
        String SPARE_PART_LEDGER = "SPARE_PART_LEDGER";            //备件台账

        String SBDA_LIST = "SBDAList";            //设备档案列表页面
        String SBDA_VIEW = "SBDAListView";    //设备档案列表点击进入详细信息界面
        String COMMON_SEARCH = "COMMON_SEARCH";

        //参照
        String SPARE_PART_REF = "SPARE_PART_REF";//备件参照选择列表
        String LUBRICATE_REF = "LUBRICATE_REF";//润滑参照选择列表
        String MAINTAIN_REF = "MAINTAIN_REF";//维保参照选择列表

        String SBDA_SEARCH_DEPARTMENT = "SBDA_SEARCH_DEPARTMENT";
        String COMMON_FILETER_SEARCH = "CommonFilterSearchActivity";

        String XJITEM_LIST = "XJITEM_LIST";
        String XJITEM_LIST_UNHANDLED = "XJITEM_LIST_UNHANDLED";
        String XJITEM_LIST_HANDLED = "XJITEM_LIST_HANDLED";
        String HSCEMENT_XJITEM_LIST = "HSCEMENT_XJITEM_LIST";

        String XJ_QXGL_LIST = "XJ_QXGL_LIST";

        String YH_LIST = "YH_LIST";
        String YH_STATISTICS = "YH_STATISTICS";//隐患统计
        String OFFLINE_YH_LIST = "OFFLINE_YH_LIST";
        String YH_EDIT = "YH_EDIT";
        String OFFLINE_YH_EDIT = "OFFLINE_YH_EDIT";
        String YH_VIEW = "YH_VIEW";
        String YH_LOOK = "YH_LOOK";

        String SEARCH_DEPARTMENT = "SEARCH_DEPARTMENT";

        String TD = "TD";
        String SD = "SD";
        String TSD_RECORD = "TSD_RECORD";

        String BJSQ_LIST = "BJSQ_LIST";
        String XJLX_LIST = "XJLX_LIST";
        String XJQY_LIST = "XJQY_LIST";
        String XJBB = "XJBB";

        String LSXJ_LIST = "LSXJ_LIST";
        String JHXJ_LIST = "JHXJ_LIST";
        String JHXJ_TODAY_RECORDS_LIST = "JHXJ_TODAY_RECORDS_LIST"; // 今日巡检：巡检任务记录
        String JHXJ_TODAY_ITEM_LIST = "JHXJ_TODAY_ITEM_LIST"; // 今日巡检：巡检任务明细
        String XJ_STATISTICS = "XJ_STATISTICS";//巡检统计
        String JHXJ_LX_LIST = "JHXJ_LX_LIST";
        String OLXJ_WORK_LIST_UNHANDLED = "OLXJ_WORK_LIST_UNHANDLED";
        String OLXJ_EAM_UNHANDLED = "OLXJ_EAM_UNHANDLED";//巡检到设备(临时巡检)
        String OLXJ_WORK_LIST_HANDLED = "OLXJ_WORK_LIST_HANDLED";

        String STOP_POLICE = "STOP_POLICE";//停机报警
        String SPARE_EARLY_WARN = "SPARE_EARLY_WARN";//备件更换预警
        String LUBRICATION_EARLY_WARN = "LUBRICATION_EARLY_WARN";//润滑预警
        String DAILY_LUBRICATION_EARLY_WARN = "DAILY_LUBRICATION_EARLY_WARN";//日常润滑
        String PLAN_LUBRICATION_EARLY_WARN = "PLAN_LUBRICATION_EARLY_WARN";//计划润滑
        String TEMPORARY_LUBRICATION_EARLY_WARN = "TEMPORARY_LUBRICATION_EARLY_WARN";//临时润滑
        String DAILY_LUBRICATION_EARLY_PART_WARN = "DAILY_LUBRICATION_EARLY_PART_WARN";//日常润滑部位
        String DAILY_LUBRICATION_EARLY_PART_ENSURE_WARN = "DAILY_LUBRICATION_EARLY_PART_ENSURE_WARN";//日常润滑部位确认
        String MAINTENANCE_EARLY_WARN = "MAINTENANCE_EARLY_WARN";//维保预警
        String DELAYDIALOG = "DELAYDIALOG";//延期弹出框
        String DELAY_RECORD = "DELAY_RECORD";//延期记录
        String GENERATE_WORK_DIALOG = "GENERATE_WORK_DIALOG";//派单弹出框


        String SCORE_EAM_LIST = "SCORE_EAM_LIST";//设备评分绩效列表
        String SCORE_EAM_PERFORMANCE = "SCORE_EAM_PERFORMANCE";//设备评分绩效
        String SCORE_INSPECTOR_STAFF_LIST = "SCORE_INSPECTOR_STAFF_LIST";//巡检人员列表
        String SCORE_MECHANIC_STAFF_LIST = "SCORE_MECHANIC_STAFF_LIST";//机修工列表
        String SCORE_INSPECTOR_STAFF_PERFORMANCE = "SCORE_INSPECTOR_STAFF_PERFORMANCE";//人员评分绩效
        String SCORE_INSPECTOR_STAFF_DAILY_PERFORMANCE = "SCORE_INSPECTOR_STAFF_DAILY_PERFORMANCE";//日常人员绩效
        String SCORE_MECHANIC_STAFF_PERFORMANCE = "SCORE_MECHANIC_STAFF_PERFORMANCE";//机修工评分绩效

        String EAM = "EAM";//设备搜索
        String STAFF = "STAFF";//人员搜索

        String ACCEPTANCE_LIST = "ACCEPTANCE_LIST";//验收列表
        String ACCEPTANCE_EDIT = "ACCEPTANCE_EDIT";//验收编辑
        String ACCEPTANCE_VIEW = "ACCEPTANCE_VIEW";//验收查看


        String TXL_LIST = "TXL_LIST";
        String MULTI_DEPART_SELECT = "MULTI_PART_SELECT";
        String TXL_VIEW = "TXL_VIEW";
        String MINE = "MIME";
        String PROCESSED_FLOW = "PROCESSED_FLOW";//流程图
        String SPARE_PART_APPLY_LIST = "SPARE_PART_APPLY_LIST"; // 备件领用申请list
        String SPARE_PART_APPLY_EDIT = "SPARE_PART_APPLY_EDIT"; // 备件领用申请编辑
        String SPARE_PART_APPLY_SUBMIT_EDIT = "SPARE_PART_APPLY_SUBMIT_EDIT"; // 备件领用申请审批编辑
        String SPARE_PART_APPLY_SEND_EDIT = "SPARE_PART_APPLY_SEND_EDIT"; // 备件领用申请发货
        String SPARE_PART_APPLY_VIEW = "SPARE_PART_APPLY_VIEW"; // 备件领用申请查看
        String SPARE_PART_APPLY_DETAIL_LIST = "SPARE_PART_APPLY_DETAIL_LIST"; // 备件领用申请明细list
        String LUBRICATE_PART_REF = "LUBRICATE_PART_REF"; // 润滑部位参照页面

        String TEXT_SIZE_SETTING = "TEXT_SIZE_SETTING";   //字体大小设置
        String EAM_TYPE_REF = "EAM_TYPE_REF"; // 设备类型参照列表

        String TSD_COMMON = "TSD_COMMON"; // 停送电
        String OVERHAUL_WORKTICKET_LIST = "OVERHAUL_WORKTICKET_LIST"; // 检修工作票list
        String OVERHAUL_WORKTICKET_EDIT = "OVERHAUL_WORKTICKET_EDIT"; // 检修工作票编辑
        String OVERHAUL_WORKTICKET_VIEW = "OVERHAUL_WORKTICKET_VIEW"; // 检修工作票查看

        String FILE_VIEW = "FILE_VIEW"; // 文档预览
        String HS_TD_LIST = "HS_TD_LIST"; // 红狮停电list
        String HS_ELE_OFF_EDIT = "HS_ELE_OFF_EDIT"; // 停电票编辑
        String HS_ELE_OFF_VIEW = "HS_ELE_OFF_VIEW"; // 停电票查看
        String ELE_OFF_TEMPLATE = "ELE_OFF_TEMPLATE"; // 停电模板
        String EAM_FILE_LIST = "EAM_FILE_LIST"; // 设备文档list
        String HS_SD_LIST = "HS_SD_LIST"; // 红狮送电list
        String HS_ELE_ON_EDIT = "HS_ELE_ON_EDIT"; // 送电票编辑
        String HS_ELE_ON_VIEW = "HS_ELE_ON_VIEW"; // 送电票查看
        String WAIT_DEALT_NEW = "WAIT_DEALT_NEW"; // 新版工作提醒
        String LUBRICATION_RECORDS_FINISH_LIST = "LUBRICATION_RECORDS_FINISH_LIST"; // 已完成润滑记录
        String PENDING_LIST = "PENDING_LIST"; // 工作提醒
        String WARN_PLAN_LUBRICATION_NEW = "WARN_PLAN_LUBRICATION_NEW"; // 计划润滑新
        String PLAN_LUBRICATION_WARN_TAB = "PLAN_LUBRICATION_WARN_TAB";//计划润滑（页签：电气、机械）
        String WORK_START_EDIT = "WORK_START_EDIT"; // 工作发起
        String EAM_TREE_SELECT = "EAM_TREE_SELECT"; // 设备层级选择
        String EAM_AREA_TREE_SELECT = "EAM_AREA_TREE_SELECT"; // 设备区域位置层级选择
        String EAM_TYPE_TREE_SELECT = "EAM_TYPE_TREE_SELECT"; // 设备类型层级选择
        String WARN_PENDING_LIST = "WARN_PENDING_LIST"; // 预警工作待办
        String ALL_MENU_LIST = "ALL_MENU_LIST"; // 所有应用
        String SCORE_STAFF_PERFORMANCE = "SCORE_STAFF_PERFORMANCE"; // 绩效评分
        String SCORE_MODIFY_LIST = "SCORE_MODIFY_LIST"; // 评分修改记录列表
    }


    interface IntentKey {
        String LOGIN_INVALID = "loginInvalid";
        String LOGIN_LOGO_ID = "LOGIN_LOGO_ID";
        String LOGIN_BG_ID = "LOGIN_BG_ID";
        String FIRST_LOGIN = "FIRST_LOGIN";
        String MODULE = "MODULE";
        String URL = "URL";
        String DEPLOYMENT_ID = "DEPLOYMENT_ID";
        String CONTACT_ENTITY = "CONTACT_ENTITY";
        String SEARCH_CONTENT = "SEARCH_CONTENT";
        String IS_SELECT_STAFF = "IS_SELECT_STAFF";

        String SBDA_ONLINE_EAMCODE = "SBDA_ONLINE_EAMCODE";
        String SBDA_ONLINE_EAMID = "SBDA_ONLINE_EAMID";
        String SBDA_ENTITY = "sbdaEntitiy";
        String SBDA_ENTITY_ID = "sbdaEntitiyId";
        String YXJL_ENTITY = "yxjlEntity";
        String QXGL_ENTITY = "qxglEntity";
        String YHGL_ENTITY = "yhglEntity";
        String DEVICE_ENTITY = "deviceEntity";

        String XJPATH_ENTITY = "XJPATH_ENTITY";
        String XJAREA_ENTITY = "XJAREA_ENTITY";
        String XJPATH_STATUS = "XJPATH_STATUS";

        String DOWNLOAD_MODULES = "DOWNLOAD_MODULES";
        String DOWNLOAD_VISIBLE = "DOWNLOAD_VISIBLE";

        String IS_MULTI = "IS_MULTI";
        String BASE_SEARCH_LIST = "BASE_SEARCH_LIST";

        String WXGD_ENTITY = "WXGD_ENTITY";
        String COMMON_SAERCH_MODE = "COMMON_SAERCH_MODE";//通用搜索界面搜索模式设置
        String COMMON_SEARCH_TAG = "COMMON_SEARCH_TAG";//通过tag来区别搜索请求从哪个组件发出的

        String REPAIR_STAFF_ENTITIES = "REPAIR_STAFF_ENTITIES";
        String ACCEPTANCE_ENTITIES = "ACCEPTANCE_ENTITIES";
        String LUBRICATE_OIL_ENTITIES = "LUBRICATE_OIL_ENTITIES";
        String MAINTENANCE_ENTITIES = "MAINTENANCE_ENTITIES";
        String SPARE_PART_ENTITIES = "SPARE_PART_ENTITIES";
        String WXGD_WARN_ENTITIES = "WXGD_WARN_ENTITIES";//预警生成工单使用  传预警备件的标体

        String IS_EDITABLE = "IS_EDITABLE";
        String IS_ADD = "IS_ADD";
        String REPAIR_SUM = "REPAIR_SUM";  //工单执行次数

        String IS_ENTITY_CODE = "IS_ENTITY_CODE";//系统编码模式
        String IS_AREA = "IS_AREA";//区域模式
        String IS_STAFF = "IS_STAFF";//人员模式
        String IS_EAM = "IS_EAM"; // 设备模式

        String SEARCH_ENTITY_FLAG = "SEARCH_ENTITY_FLAG";
        String ENTITY_CODE = "ENTITY_CODE";

        String REC_CODE = "REC_CODE";

        String TABLE_STATUS = "TABLE_STATUS";//工单状态
        String TABLE_ACTION = "TABLE_ACTION";//工单视图action

        String PROPERTY = "PROPERTY"; // 数据库字段属性

        String LIST_ID = "LIST_ID"; // 工单ID
        String IS_SPARE_PART_REF = "IS_SPARE_PART_REF"; // 备件参照清单

        String EAM_ID = "EAM_ID"; // 设备ID
        String XJ_YHGL_ENTITY = "XJ_YHGL_ENTITY"; //巡检隐患
        String COMMON_SEARCH_DEFAULT_SEARCH_VALUE = "COMMON_SEARCH_DEFAULT_SEARCH_VALUE";

        String FILTER_SEARCH_TYPE = "FILTER_SEARCH_TYPE";
        String PARAM_MAP = "PARAM_MAP";
        String FILTER_SEARCH_PARAM = "FILTER_SEARCH_PARAM";


        String XJ_TASK_ID = "XJ_TASK_ID";
        String XJ_GROUP_ID = "XJ_GROUP_ID";
        String XJ_WORKLIST_VIEW_TYPE = "XJ_WORKLIST_VIEW_TYPE";

        String XJ_IS_TEMP = "XJ_IS_TEMP";
        String XJ_TASK_ENTITY = "XJ_TASK_ENTITY";
        String XJ_AREA_ENTITY = "XJ_AREA_ENTITY";
        String SPARE_PART_LEDGER_ID = "SPARE_PART_LEDGER_ID";

        String IS_XJ_FINISHED = "IS_XJ_FINISHED";

        String IS_FROM_PENDING = "IS_FROM_PENDING";
        String PENDING_ENTITY = "PENDING_ENTITY";

        String WARN_NEXT_TIME = "WARN_NEXT_TIME";//下次运行时间
        String WARN_SOURCE_TYPE = "WARN_SOURCE_TYPE";//延期来源  润滑BEAM062/01,备件BEAM062/03,维保BEAM062/02
        String WARN_SOURCE_IDS = "WARN_SOURCE_IDS";//延期ids
        String WARN_PEROID_TYPE = "WARN_PEROID_TYPE";//周期类型
        String WARN_SOURCE_URL = "WARN_SOURCE_URL";//URL

        String EAM = "EAM";//EAM设备
        String IS_NFC_SIGN = "IS_NFC_SIGN";//是否是NFC刷卡签到
        String EAM_CODE = "EAM_CODE";//EAM_CODE
        String AREA_NAME = "AREA_NAME";//AREA_NAME
        String isEdit = "isEdit";//是否能编辑
        String UPDATE = "UPDATE"; // 修改
        String IS_MAIN_EAM = "IS_MAIN_EAM";//是否主设备

        String SCORE_ENTITY = "SCORE_ENTITY";//评分记录

        String ACCEPTANCE_ENTITY = "ACCEPTANCE_ENTITY";//验收记录
        String SCORETABLENO = "SCORETABLENO";//评分单据
        String TXL_ENTITY = "TXL_ENTITY";


        String RANKING = "RANKING";//排名
        String TYPE = "TYPE";//人员类型:机修工,巡检工

        String WARN_ID = "WARN_ID";//预警id
        String PERIODTYPE = "PERIODTYPE";//时间类型

        String TABLENO = "TABLENO";//单据编号

        String ISWARN = "ISWARN";//是否来自预警

        String REPAIR_TYPE = "REPAIR_TYPE";//维修类型

        String TASKID = "TASKID";//设备巡检任务id
        String TITLE_CONTENT = "TITLE_CONTENT";
        String TABLE_ID = "TABLE_ID";
        String PENDING_ID = "PENDING_ID";
        String STATISTIC_SORCE = "STATISTIC_SORCE"; // 报表统计跳转
        String ADD_DATA_LIST = "ADD_DATA_LIST"; // 已添加数据：备件、润滑、维保
        String POSITION = "POSITION";
        String HAZARD_CONTRL_POINT = "HAZARD_CONTRL_POINT"; // 检修工作票：危险源控制点
        String ELE_OFF_ON_TEMPLATE = "ELE_OFF_ON_TEMPLATE"; // 停送电模板
        String ElE_OFF_ID = "ElE_OFF_ID"; // 停电票id
        String ElE_OFF_TABLE_INFO_ID = "ElE_OFF_TABLE_INFO_ID"; // 停电票tableInfoId
        String ACTIVITY_NAME = "ACTIVITY_NAME"; // 活动名称
        String IS_SELECT = "IS_SELECT"; // 选择模式
        String SBDA_ONLINE_SUBSIDIARY = "SBDA_ONLINE_SUBSIDIARY"; // 是否附属设备
        String WORK_ID = "WORK_ID"; // 巡检区域ID
    }

    interface FilterSearchParam {
        String AREA_ID = "AREA_ID";
        String DEVICE_BLUR = "DEVICE_BLUR";
    }


    interface WorkType {//设备功能类型

        int GZFQ = 1;  // 工作发起
        int JHXJ = 2;  //计划巡检
        // 设备润滑
        int JHRH = 3;  //计划润滑
        int LSRH = 4;  //临时润滑
        int RHYJ = 5;  //润滑预警
        // 维修执行
        int WXGD = 6;  //维修工单
        int LBJYJ = 7;  //零部件预警
        int YWYJ = 8;  //运维预警
        int TDSQ = 9;//停电申请
        int SDSQ = 10;//送电申请
        int JXZYP = 11;//检修作业票
        // 统计报表
        int XJTJ = 12;  //巡检统计
        int YHTJ = 13;  //隐患统计
        int GDTJ = 14;  //工单统计
        int TJTJ = 15;  //停机统计
        int SPARE_PART_CONSUME_LEDGER = 16;  //零部件消耗台账
        int SPARE_PART_RECEIVE_RECORD = 17;  //备件领用记录
        int TSDTJ = 18;  //停送电统计

        int MORE = 99;  //更多
        int YHGL = 19; // 隐患单

    }

    interface HSWorkType {//设备功能类型
        int JHXJ = 1;  //计划巡检
        int LSXJ = 2;  //临时巡检
        int YHGL = 3;  //隐患单

        int PLAN_LUBRICATION_EARLY_WARN = 5;//计划润滑
        int TEMPORARY_LUBRICATION_EARLY_WARN = 6;//临时润滑
        int LUBRICATION_EARLY_WARN = 7;//润滑预警

        int DAILY_WXGD = 10;  //日常维修工单
        int REPAIR_WXGD = 11;  //周期维修工单
        int OHAUL_WXGD = 12;  //年底大修维修工单
        int SPARE_EARLY_WARN = 13;//备件更换预警
        int MAINTENANCE_EARLY_WARN = 14;//维保预警
        int SPARE_PART_RECEIVE = 15;//备件申领
        int TD = 16;//停电
        int SD = 17;//送电
        int TSD_CANCEL = 18;//停送电作废
        int TSD_APPROVAL = 19;//停送电审批

        int XJ_STATISTICS = 20;  //巡检统计
        int YH_STATISTICS = 21;  //隐患统计
        int WXGD_STATISTICS = 22;  //工单统计
        int STOP_POLICE = 23;  //停机报警
        int SPARE_PART_CONSUME_LEDGER = 24;  //零部件消耗台账
        int SPARE_PART_RECEIVE_RECORD = 25;  //备件领用记录
        int TSD_STATISTICS = 26;//停送电统计
        int HS_JX_TICKETS = 27;// 红狮检修作业票
        int HS_TD = 28;// 红狮停电
        int HS_SD = 29;// 红狮送电

        int ZZ = 99;  //知之应用
    }

    interface HSProcesskey {
        String faultInfoFW = "faultInfoFW";//隐患单
        String work = "work";//工单
    }

    interface SPKey {
        String WORKS = "works";
        String STAFF = "STAFF";
        String HAS_SUPOS = "HAS_SUPOS";
        String DEVICE_TOKEN = "DEVICE_TOKEN";

        String RUN_STATES_ON = "RUN_STATES_ON";
        String RUN_STATES_OFF = "RUN_STATES_OFF";
        String RECENT_DEVICES = "RECENT_DEVICES";
        String RECENT_DEVICES_STR = "RECENT_DEVICES_STR";

        //基本信息参数
        String FAULT_TYPE = "FAULT_TYPE";
        String DEAL_TYPE = "DEAL_TYPE";
        String RUNNING_STATE_PARAM = "RUNNING_STATE_PARAM";
        String STATE_TYPE = "STATE_TYPE";

        String IS_AUTO_REPAIR = "IS_AUTO_REPAIR";

        //下载信息缓存
        String DOWNLOAD_XJ_BASE = "DOWNLOAD_XJ_BASE";
        String DOWNLOAD_XJ = "DOWNLOAD_XJ";
        String DOWNLOAD_EAM_BASE = "DOWNLOAD_EAM_BASE";
        String DOWNLOAD_EAM_DEVICE = "DOWNLOAD_EAM_DEVICE";

        //上传信息缓存
        String UPLOAD_XJ = "UPLOAD_XJ";
        String UPLOAD_QX = "UPLOAD_QX";

        String UPLOAD_YH = "UPLOAD_YH";

        String EAM_DEVICE_NEED_DOWNLOAD = "EAM_DEVICE_NEED_DOWNLOAD";


        String JHXJ_TASK_CONTENT = "JHXJ_TASK_CONTENT";
        String LSXJ_TASK_CONTENT = "LSXJ_TASK_CONTENT";

        String JHXJ_TASK_ENTITY = "JHXJ_TASK_ENTITY";
        String LSXJ_TASK_ENTITY = "LSXJ_TASK_ENTITY";


        String TEXT_SIZE_SETTING = "TEXT_SIZE_SETTING";

        String C_NAME = "C_NAME";
        String C_ID = "C_ID";
        String C_CODE = "C_CODE";
        String COMPANY = "COMPANY";
        String COMPANY_LIST = "COMPANY_LIST";
    }


    interface Date {

        String TODAY = "今天";
        String YESTERDAY = "昨天";
        String TOMORROW = "明天";
        String THREEDAY = "后三天";
        String THREE_DAY = "三天内";
        String THIS_WEEK = "本周";
        String THIS_MONTH = "本月";
        String ALL = "全部";
    }

    interface Transition {    //工作流相关流程
        String SAVE = "save";       //保存\保存按钮
        String CANCEL = "cancel";   //作废
        String REJECT = "reject";   //驳回
        String SUBMIT = "submit";   //提交
        String SAVE_LOCAL = "saveLocal";    //保存到本地
        String SUBMIT_LOCAL = "submitLocal";    //提交到本地， 进行完整性检查
        String TRANSITION_MORE = "transitionMore";  //更多
        String SUBMIT_BTN = "submitBtn"; //提交按钮
        String ROUTER_BTN = "routerBtn"; //操作按钮
        String SAVE_BTN = "saveBtn";
        String NOTIFICATION = "notification";   //通知
        String CANCEL_CN = "作废";   //作废
        String REJECT_CN = "驳回";   //驳回
    }

    interface OperateType { //操作类型， 保存 / 提交
        String SAVE = "save";   //保存
        String SUBMIT = "submit";   //提交

        String EDIT = "edit";       //编辑
        String APPROVE = "approve"; //审批
    }

    interface XJPathStateType {
        String WAIT_CHECK_STATE = "待检";
        String PAST_CHECK_STATE = "已检";
    }

    interface TimeString {
        String START_TIME = " 00:00:00";
        String END_TIME = " 23:59:59";
        String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";
        String YEAR_MONTH_DAY_HOUR_MIN = "yyyy-MM-dd HH:mm";
        String YEAR_MONTH_DAY = "yyyy-MM-dd";
        String MONTH_DAY_HOUR_MIN = "MM-dd HH:mm";
    }

    interface PicType {
        String WORK_TICKET_PIC = EamApplication.getAccountInfo().staffName + "_workTicketRecord";
        String WORK_TICKET_SAFER_PIC = "workTicketRecordSafer";
        String ELE_OFF_PIC = "eleOffRecord";
        String ELE_ON_PIC = "eleOnRecord";
        String XJ_PIC = "xjRecord";
        String YH_PIC = "yhRecord";
        String GD_PIC = "workRecord";
        String SCORE_PIC = "ScoreRecord";
    }

    interface RefreshAction {
        String XJ_WORK_END = "XJ_WORK_END";
        String XJ_WORK_REINPUT = "XJ_WORK_REINPUT";

        String HOME_APP_MENU = "HOME_APP_MENU"; // 主页应用菜单
    }

    interface BAPQuery {
        String VALUE = "VALUE";
        String BILL_STATE = "BILL_STATE";

        String BATCH_TEXT = "BATCH_TEXT";
        String BATCH_NUM = "BATCH_NUM";
        String ID = "ID";
        String NAME = "NAME";
        String SPECIFY = "SPECIFY";
        String PRODUCT_ID = "PRODUCT_ID";
        String PRODUCT_CODE = "PRODUCT_CODE";
        String PRODUCT_NAME = "PRODUCT_NAME";
        String PRODUCT_SPECIF = "PRODUCT_SPECIF";
        String PRODUCT_MODEL = "PRODUCT_MODEL";
        String WARE_CODE = "WARE_CODE";
        String WARE_NAME = "WARE_NAME";
        String PLACE_SET_CODE = "PLACE_SET_CODE";
        String PLACE_SET_NAME = "PLACE_SET_NAME";
        String TABLE_NO = "TABLE_NO";

        String ORDER_DATE = "ORDER_DATE";
        String FIND_TIME = "FIND_TIME";
        String YH_DATE_START = "YH_DATE_START";
        String YH_DATE_END = "YH_DATE_END";
        String CREATE_DATE_START = "CREATE_DATE_START";
        String CREATE_DATE_END = "CREATE_DATE_END";
        String CREATE_TIME = "CREATE_TIME";
        String YH_AREA = "YH_AREA";
        String REPAIR_TYPE = "REPAIR_TYPE";
        String PRIORITY = "PRIORITY";
        String STATUS = "STATUS";
        String FAULT_INFO_TYPE = "FAULT_INFO_TYPE";
        String EAM_NAME = "EAM_NAME";
        String LINK_STATE = "LINK_STATE";

        String XSCK_DATE_START = "XSCK_DATE_START";
        String XSCK_DATE_END = "XSCK_DATE_END";
        String OUT_STORAGE_DATE = "OUT_STORAGE_DATE";

        String TEXT = "TEXT";
        String SYSTEMCODE = "SYSTEMCODE";
        String BAPCODE = "BAPCODE";
        String BOOLEAN = "BOOLEAN";
        String LONG = "LONG";
        String DATETIME = "DATETIME";
        String DATE = "DATE";
        String TYPE_NORMAL = "0";
        String TYPE_JOIN = "2";
        String LIKE = "like";
        String BE = "=";
        String GE = ">=";
        String LE = "<=";
        String LIKE_OPT_BLUR = "%?%";
        String LIKE_OPT_Q = "?";

        String OPT_YH_AREA = "=includeCustSub#BEAM_AREAS";

        /*维修工单快速查询字段条件*/
        String WORK_SOURCE = "WORK_SOURCE";//工单来源
        String WXGD_REPAIR_TYPE = "WXGD_REPAIR_TYPE"; // 维修类型（维修工单列表查询）
        String WXGD_PRIORITY = "WXGD_PRIORITY"; // 优先级（维修工单列表查询）
        String WORK_STATE = "WORK_STATE"; // 工作流状态（维修工单列表查询）

        /*备件参照快速查询*/
        String BEAM_SPARE_PARTS = "BEAM_SPARE_PARTS";

        String EAM_EXACT_CODE = "EAM_EXACT_CODE";//精确设备编码
        String EAM_CODE = "EAM_CODE";//设备编码
        String EAMCODE = "EAMCODE";//设备编码
        //        String EAM_ASSETCODE = "EAM_ASSETCODE"; // 设备编码
        String EAM_STATE = "EAM_STATE";//设备状态
        String EAM_AREA = "EAM_AREA";//区域类型
        String EAM_AREANAME = "EAM_AREANAME";//区域类型主设备
        String IS_MAIN_EQUIP = "IS_MAIN_EQUIP";//是否主设备

        String CONTENT = "CONTENT";//内容

        String OPEN_TIME_START = "OPEN_TIME_START";//时间
        String OPEN_TIME_STOP = "OPEN_TIME_STOP";//时间
        String OPEN_TIME = "OPEN_TIME";//时间
        String ON_OR_OFF = "ON_OR_OFF";//开关状态

        String sourceIds = "sourceIds";
        String sourceType = "sourceType";
        String delayDate = "delayDate";
        String delayReason = "delayReason";
        String peroidType = "peroidType";

        String startDate = "startDate";
        String endDate = "endDate";
        String repairGroupId = "repairGroupId";

        String SCORE_TIME_START = "SCORE_TIME_START";//时间
        String SCORE_TIME_STOP = "SCORE_TIME_STOP";//时间
        String SCORE_TIME = "SCORE_TIME";//时间

        String SCORE_DATA_START = "SCORE_DATA_START";//时间
        String SCORE_DATA_STOP = "SCORE_DATA_STOP";//时间
        String SCORE_DATA = "SCORE_DATA";//时间

        String SCORE_DAILY_START = "SCORE_DAILY_START";//时间
        String SCORE_DAILY_STOP = "SCORE_DAILY_STOP";//时间
        String TIME = "TIME";//时间


        String SCORE_TABLE_NO = "SCORE_TABLE_NO";//设备评分绩效单据编号

        String SOURCE_TYPE = "SOURCE_TYPE";//隐患来源

        String OVERALLSTATE = "OVERALLSTATE";//工作状态
        String OVERDATEFLAG = "OVERDATEFLAG";//是否超期

        String IS_RUN = "IS_RUN";//是否启用
        String IS_EAM_TASK = "IS_EAM_TASK";//是否设备巡检或临时巡检

        String STAR_TIME = "STAR_TIME";//巡检开始时间
        String STAR_TIME1 = "STAR_TIME1";//巡检开始时间起点
        String STAR_TIME2 = "STAR_TIME2";//巡检开始时间终点
        String END_TIME1 = "END_TIME1";//巡检结束时间起点
        String END_TIME2 = "END_TIME2";//巡检结束时间终点
        String END_TIME = "END_TIME";//巡检结束时间

        //停机报警更新单个列表内容时所用的请求参数key值
        String STOP_POLICE_STAFF_ID = "staffId";
        String STOP_POLICE_ID = "id";
        String STOP_POLICE_STOP_TYPE = "stopType";
        String STOP_POLICE_STOP_REASON = "stopReason";
        String STOP_POLICE_STOP_EXPLAIN = "stopExplain";
        String STOP_POLICE_EAM_IDS = "eamIds";

        String EAMTYPE_NAME = "EAMTYPE_NAME"; // 设备类型：名称
        String EAMTYPE_CODE = "EAMTYPE_CODE";//设备类型：编码
        String LUB_PART = "LUB_PART"; // 润滑部位

        String ALL_QUERY = "ALL_QUERY"; // 查询按钮
        String PENDING_QUERY = "PENDING_QUERY"; // 仅查待办

        String DEAL_TIME_S = "DEAL_TIME_S"; // 润滑完成时间
        String DEAL_TIME_E = "DEAL_TIME_E"; // 润滑完成时间
        String DEAL_TIME = "DEAL_TIME"; // 润滑完成时间
        String EAM_ID = "EAM_ID"; // 设备ID

        // 工作提醒
        String STATE = "STATE";  // 状态
        String PROCESSKEY = "PROCESSKEY"; // 工作流程key(单据类型)
        // 我处理的
        String NEWSTATE = "NEWSTATE"; // 单据状态
        String PROCESS_KEY = "PROCESS_KEY"; // 工作流程key(单据类型)
        String SUBORDINATE = "SUBORDINATE"; // 我的下属工作待办
        String SUBORDINATE_DEPARTMENT = "SUBORDINATE_DEPARTMENT"; // 我的下属部门
        String SUBORDINATE_POSITION = "SUBORDINATE_POSITION"; // 我的下属岗位
        String RISK_ASSESSMENT = "RISK_ASSESSMENT"; // 风险评估
        String LUB_TYPE = "LUB_TYPE"; // 润滑类型：电气、机械
        String TARGET_ENTITY_CODE = "TARGET_ENTITY_CODE"; // 实体编码
        String DEPARTMENT_ID = "DEPARTMENT_ID"; // 部门id
    }


    interface SystemCode {
        //巡检系统编码
        String XJ_TYPE = "mobileEAM001";
        String REAL_VALUE = "realValue";
        String WILINK_STATE = "wiLinkState";
        String PASS_REASON = "passReason";
        String CART_REASON = "cartReason";
        String CARD_TYPE = "cardType";
        String MOBILE_EAM001 = "mobileEAM001";
        String MOBILE_EAM055 = "mobileEAM055";
        String MOBILE_EAM054 = "mobileEAM054";
        String LINK_STATE = "LinkState";
        String XJ_END_TASK = "mobileEAM_003"; // 巡检终止原因

        //隐患系统编码
        String QX_TYPE = "BEAM029";
        String YH_STATE = "BEAM2004";
        String YH_WX_TYPE = "BEAM2005"; // 维修类型
        String YH_SOURCE = "BEAM2006";
        String YH_PRIORITY = "BEAM2007"; //优先级
        String YH_DISPOSAL = "BEAM2_2013"; //处理方式

        //维修工单
        String WXGD_SOURCE = "BEAM2003";// 工单来源
        String OIL_TYPE = "BEAM61"; // 加/换油
        String CHECK_RESULT = "BEAM033"; // 验收结论

        //停机报警
        String TJ_TYPE = "BEAM2009";
        String TJ_REASON = "BEAM2_2012";

        // 检修作业票
        String RISK_ASSESSMENT = "WorkTicket_001"; // 风险评估
        String HAZARD_CON_POINT = "WorkTicket_002"; // 危险源控制点

    }

    /**
     * 维修工单单据状态
     */
    interface YHStatus {
        String WAIT = "BEAM2004/01";//待处理
        String IMPLEMENT = "BEAM2004/02";//处理中
        String COMPLETE = "BEAM2004/04";//已处理
    }

    /**
     * 维修工单单据状态
     */
    interface WxgdStatus_CH {
        String DISPATCH = "待派工";
        String CONFIRM = "接收";
        String IMPLEMENT = "待执行";
        String ACCEPTANCE = "待验收";
        String COMPLETE = "已完成";
    }

    interface WxgdWorkSource {
        String patrolcheck = "BEAM2003/01";//巡检
        String lubrication = "BEAM2003/02";//润滑
        String maintenance = "BEAM2003/03";//维保
        String sparepart = "BEAM2003/04";//备件
        String other = "BEAM2003/05";//其它
        String big_repair = "BEAM2003/06";// 大修
        String repair = "BEAM2003/07";// 检修
        String manual_start = "BEAM2003/08";//手工添加
    }

    interface YhglWorkSource {
        String patrolcheck = "BEAM2006/01";//巡检
        String other = "BEAM2006/02";//其它
        String maintenance = "BEAM2006/03";//维保
        String sparepart = "BEAM2006/04";//备件
        String lubrication = "BEAM2006/05";//润滑

    }

    interface WorkSource_CN {
        String patrolcheck = "巡检";//巡检
        String lubrication = "润滑";//润滑
        String maintenance = "维保";//维保
        String sparepart = "备件";//备件
        String other = "其它";//其它

    }

    // 隐患维修类型
    interface YHWXType {

        String DX = "大修";
        String JX = "检修";
        String RC = "日常";

        String DX_SYSCODE = "BEAM2005/03";
        String JX_SYSCODE = "BEAM2005/02";
        String RC_SYSCODE = "BEAM2005/01";

    }

    interface CommonSearchMode {
        String STAFF = "STAFF";
        String AREA = "AREA";
        String DEPARTMENT = "DEPARTMENT";
        String SYSTEM_CODE = "SYSTEM_CODE";
        String INITIAL_VALUE = "INITIAL_VALUE";
        String EAM = "EAM";
    }

    //维修工单执行状态
    interface WxgdAction {

        String STOP = "BEAM2008/01";

        String ACTIVATE = "BEAM2008/02";
    }

    interface WxgdView {
        String DISPATCH_OPEN_URL = "/BEAM2/workList/workRecord/workEdit.action";//派单
        String RECEIVE_OPEN_URL = "/BEAM2/workList/workRecord/workReceiptEdit.action";//接单
        String EXECUTE_OPEN_URL = "/BEAM2/workList/workRecord/workExecuteEdit.action";//执行
        //        String STOP_OPEN_URL = "/BEAM2/workList/workRecord/workExecuteEdit.action";
        String ACCEPTANCE_OPEN_URL = "/BEAM2/workList/workRecord/workCheckEdit.action";//验收
        String VIEW_OPEN_URL = "/BEAM2/workList/workRecord/workView.action";//通知
    }

    /**
     * 周期类型
     */
    interface PeriodType {
        String TIME_FREQUENCY = "BEAM014/01"; // 时间频率
        String RUNTIME_LENGTH = "BEAM014/02"; // 运行时长
    }

    /**
     * 备件领用状态
     */
    interface SparePartUseStatus {
        String NO_USE = "BEAM2011/01"; // 未领用
        String USEING = "BEAM2011/02"; // 领用中
        String UESED = "BEAM2011/03"; // 已领用
        String PRE_USE = "BEAM2011/04"; // 预领用
    }

    /**
     * 模块授权
     */
    interface ModuleAuthorization {
        String BEAM2 = "BEAM2";       // 设备模块
        String mobileEAM = "mobileEAM";  // 移动设备巡检
        String WOM = "";        // 工单
    }

    interface WebUrl {

        //        String TD_LIST = "/BEAMEle/onOrOff/onoroff/eleOffList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9mZkxpc3Rfc2VsZnw_&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOnList&openType=page&clientType=mobile";
        String TD_LIST = "/BEAMEle/onOrOff/onoroff/eleOffList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9mZkxpc3Rfc2VsZnw_&openType=page&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOffList&clientType=mobile";
        String SD_LIST = "/BEAMEle/onOrOff/onoroff/eleOnList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9uTGlzdF9zZWxmfA__&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOnList&openType=page&clientType=mobile";
        String TSD_RECORD = "/BEAMEle/dataStatistic/dataStatistic/statisticList.action?__pc__=QkVBTUVsZV8xLjAuMF9kYXRhU3RhdGlzdGljX3N0YXRpc3RpY0xpc3Rfc2VsZnw_&openType=page&workFlowMenuCode=BEAMEle_1.0.0_dataStatistic_statisticList&clientType=mobile";

        // 新版停送电视图
        String TD_LIST_NEW = "/BEAMEle/onOrOff/onoroff/eleOffList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9mZkxpc3Rfc2VsZnw_&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOffList&openType=page&clientType=mobile";
        String SD_LIST_NEW = "/BEAMEle/onOrOff/onoroff/eleOnList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9uTGlzdF9zZWxmfA__&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOnList&openType=page&clientType=mobile";

        // 停送电作废管理
        String TSD_CANCEL = "/BEAMEle/onOrOff/onoroff/eleReject.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZVJlamVjdF9zZWxmfA__&openType=page&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleReject&clientType=mobile";
        // 停送电审批管理
        String TSD_APPROVAL = "/BEAMEle/approvalManage/approvalStaff/approvalList.action?__pc__=QkVBTUVsZV8xLjAuMF9hcHByb3ZhbE1hbmFnZV9hcHByb3ZhbExpc3Rfc2VsZnw_&openType=page&workFlowMenuCode=BEAMEle_1.0.0_approvalManage_approvalList&clientType=mobile";
        // 停送电情况统计
        String TSD_STATISTICS = "/BEAMEle/dataStatistic/dataStatistic/statisticList.action?__pc__=QkVBTUVsZV8xLjAuMF9kYXRhU3RhdGlzdGljX3N0YXRpc3RpY0xpc3Rfc2VsZnw_&openType=page&workFlowMenuCode=BEAMEle_1.0.0_dataStatistic_statisticList&clientType=mobile";
        // 检修作业票测试
        String JX_TICKETS = "/WorkTicket/workTicket/ohworkticket/workTicketEdit.action?pendingId=1001&__pc__=VGFza0V2ZW50XzBrMzZ1cDh8d29ya1RpY2tldEZX&tableInfoId=1000&entityCode=WorkTicket_8.20.3.03_workTicket&id=1000&clientType=mobile";

        String SCORE = "/BEAM/scoreStandard/soring/eamScoreView.action?clientType=mobile&eamID=";
        String XJ = "/mobileEAM/work/work/pointsList.action?taskId=";
        String FLOWVIEW = "/ec/workflow/flowViewH5.action?env=runtime&__res_html=true";

    }

    interface ZZ {
        String IP = "ZZ_IP";
        String PORT = "ZZ_PORT";
        String URL = "ZZ_URL";
        String ZZ_SUPOS_TOKEN = "ZZ_SUPOS_TOKEN";
    }

    interface AttrMap {
        String SCORE_FRACTION = "BEAM_1_0_0_scoreStandard_itemDetailList_LISTPT_ASSO_8395ad9a_dea0_4fb2_aad1_78cb7ef73cea";
        String SCORE_ITEM = "BEAM_1_0_0_scoreStandard_itemDetailList_LISTPT_ASSO_3e07e500_557f_4b41_9c6b_1396785b8113";
    }

    /**
     * 我的流程(处理过的)单据状态
     */
    interface TableStatus_CH {
        String PRE_DISPATCH = "待派工";
        String PRE_EXECUTE = "待执行";
        String PRE_ACCEPT = "待验收";
        String PRE_NOTIFY = "待通知";
        String END = "已结束";
        String CANCEL = "作废";

        String EDIT = "编辑";
        String DISPATCH = "派工";
        String SPARE_PART_APPLY = "领用人申请";
        String ELE_ON = "送电申请中";
        String ELE_OFF = "停电申请中";
        String EXECUTE = "执行";
        String EXECUTE_NOTIFY = "执行,通知";
        String NOTIFY = "通知";
        String ACCEPT = "验收";
        String RECALL = "撤回";
        String REVIEW = "审核";
        String REVIEW1 = "审批";
        String CONFIRM = "接单(确认)";
        String OVER = "结束";
        String TAKE_EFFECT = "生效";
        String NOTIFY_TAKE_EFFECT = "通知,生效";

    }

    /**
     * 我的流程(处理过的)单据类型
     */
    interface ProcessKey {
//        String WORK = EamApplication.getCid() == 1002 ? "work" : EamApplication.getCompanyCode() + "work"; // 工单
//        String FAULT_INFO = EamApplication.getCid() == 1002 ? "faultInfoFW" : EamApplication.getCompanyCode() + "faultInfoFW"; // 隐患登记
//        String EAM_INFO_EDIT = EamApplication.getCid() == 1002 ? "eaminfoEdit" : EamApplication.getCompanyCode() + "eaminfoEdit"; // 设备档案新增申请
//        String EAM_INFO = EamApplication.getCid() == 1002 ? "eaminfo" : EamApplication.getCompanyCode() + "eaminfo"; // 设备档案申请修改
//        String CHANGE_WF = EamApplication.getCid() == 1002 ? "changeWF" : EamApplication.getCompanyCode() + "changeWF"; // 设备状态变更
//        String CHECK_APPLY_FW = EamApplication.getCid() == 1002 ? "checkApplyFW" : EamApplication.getCompanyCode() + "checkApplyFW"; // 验收申请
//        String ENTRUST_REPAIR = EamApplication.getCid() == 1002 ? "entrustRepair" : EamApplication.getCompanyCode() + "entrustRepair"; // 委外维修单
//        String INSTALL_NEW_WF = EamApplication.getCid() == 1002 ? "installNewWF" : EamApplication.getCompanyCode() + "installNewWF"; // 安装验收移交
//        String RUN_STATE_WF = EamApplication.getCid() == 1002 ? "RunningStateWF" : EamApplication.getCompanyCode() + "RunningStateWF"; // 运行记录处理
//        String SPARE_PART_APPLY = EamApplication.getCid() == 1002 ? "sparePartApply" : EamApplication.getCompanyCode() + "sparePartApply"; // 备件领用申请
//        String WORK_ALLOT_NEW_WF = EamApplication.getCid() == 1002 ? "workAllotNewWF" : EamApplication.getCompanyCode() + "workAllotNewWF"; // 设备调拨
//        String TEMP_WF = EamApplication.getCid() == 1002 ? "tempWF" : EamApplication.getCompanyCode() + "tempWF"; // 临时任务
//        String POTROL_TASK_WF = EamApplication.getCid() == 1002 ? "potrolTaskWF" : EamApplication.getCompanyCode() + "potrolTaskWF"; // 点巡检任务
//        String ELE_OFF = EamApplication.getCid() == 1002 ? "EleOnWorkFlow" : EamApplication.getCompanyCode() + "EleOnWorkFlow"; // 停电
//        String ELE_ON = EamApplication.getCid() == 1002 ? "EleOn" : EamApplication.getCompanyCode() + "EleOn"; // 送电
//        String WORK_TICKET = /*EamApplication.getCid() == 1002 ? "workTicketFW" : EamApplication.getCompanyCode() + */"workTicketFW"; // 检修作业票
    }


    /**
     * 实体编码
     */
    interface EntityCode {
        String WORK = "BEAM2_1.0.0_workList"; // 工单
        String FAULT_INFO = "BEAM2_1.0.0_faultInfo"; // 隐患登记
        //        String EAM_INFO_EDIT = EamApplication.getCid() == 1002 ? "eaminfoEdit" : EamApplication.getCompanyCode() + "eaminfoEdit"; // 设备档案新增申请
//        String EAM_INFO = EamApplication.getCid() == 1002 ? "eaminfo" : EamApplication.getCompanyCode() + "eaminfo"; // 设备档案申请修改
//        String CHANGE_WF = EamApplication.getCid() == 1002 ? "changeWF" : EamApplication.getCompanyCode() + "changeWF"; // 设备状态变更
        String CHECK_APPLY_FW = "BEAM2_1.0.0_checkApply"; // 验收申请
        //        String ENTRUST_REPAIR = EamApplication.getCid() == 1002 ? "entrustRepair" : EamApplication.getCompanyCode() + "entrustRepair"; // 委外维修单
//        String INSTALL_NEW_WF = EamApplication.getCid() == 1002 ? "installNewWF" : EamApplication.getCompanyCode() + "installNewWF"; // 安装验收移交
        String RUN_STATE_WF = "BEAM2_1.0.0_runningState"; // 运行记录处理
        String SPARE_PART_APPLY = "BEAM2_1.0.0_sparePart"; // 备件领用申请
        //        String WORK_ALLOT_NEW_WF = EamApplication.getCid() == 1002 ? "workAllotNewWF" : EamApplication.getCompanyCode() + "workAllotNewWF"; // 设备调拨
//        String TEMP_WF = EamApplication.getCid() == 1002 ? "tempWF" : EamApplication.getCompanyCode() + "tempWF"; // 临时任务
        String POTROL_TASK_WF = "mobileEAM_1.0.0_potrolTaskNew"; // 点巡检任务
        //        String ELE_OFF = EamApplication.getCid() == 1002 ? "EleOnWorkFlow" : EamApplication.getCompanyCode() + "EleOnWorkFlow"; // 停电
//        String ELE_ON = EamApplication.getCid() == 1002 ? "EleOn" : EamApplication.getCompanyCode() + "EleOn"; // 送电
        String ELE_ON_OFF = "BEAMEle_1.0.0_onOrOff"; // 停送电
        String WORK_TICKET = "WorkTicket_8.20.3.03_workTicket"; // 检修作业票
    }

    /**
     * 同实体编码，不同工作流类型
     */
    interface EntityCodeType {
        String ELE_OFF = "BEAMEle001/01"; // （停电）
        String ELE_ON = "BEAMEle001/02"; // （送电）
        String TEMP_XJ = "mobileEAM_1.0.0_potrolTaskNew_tempList"; // （临时巡检）
        String PLAN_XJ = "mobileEAM_1.0.0_potrolTaskNew_potrolTaskList"; // （计划巡检）
    }

    interface WarnType {
        String LUBRICATION_WARN = "润滑提醒";
        String SPARE_PART_WARN = "零部件提醒";
        String MAINTENANCE_WARN = "维保提醒";
    }

    /**
     * 隐患状态
     */
    interface FaultState_ENG {
        String WAIT_DEAL = "BEAM2004/01"; // 待处理
        String DEALING = "BEAM2004/02"; // 处理中
        String DEALED = "BEAM2004/04"; // 已处理
        String CLOED = "BEAM2004/05"; // 已关闭
        String DEAL_FAULT = "BEAM2004/06"; // 直接消缺

    }

    /**
     * 工单工作流状态
     */
    interface WorkState_ENG {
        String DISPATCH = "BEAM049/01"; // 派工
        String CONFIRM = "BEAM049/02"; // 接单(确认)
        String EXECUTE = "BEAM049/03"; // 执行
        String ACCEPT = "BEAM049/04"; // 验收
        String TAKE_EFFECT = "BEAM049/05"; // 生效
        String CANCEL = "BEAM049/06"; // 作废
    }

    /**
     * 备件领用申请单据视图(注：海螺使用)
     */
    interface HLSparePartView {
        String EDIT_URL = "/BEAM2/sparePart/apply/sparePartEdit.action";// 领用人申请
        String SUBMIT_EDIT_URL = "/BEAM2/sparePart/apply/sparePartSubmitEdit.action";
        String VIEW_URL = "/BEAM2/sparePart/apply/sparePartView.action";// 保全处经办人审批、 公司分管领导审批、总经理审批
        String SEND_EDIT_URL = "/BEAM2/sparePart/apply/sparePartSendEdit.action";// 仓库管理员发货
    }

    /**
     * EventBus 发送标识
     */
    interface EventFlag {
        String SPAD = "sparePartApplyDetail"; // 备件领用申请明细PT
        String WORK_TICKET_PT = "workTicketDetail"; // 检修工作票明细PT
    }

    /**
     * 检修作业票单据视图(注：红狮)
     */
    interface HSWorkTicketView {
        String EDIT_URL = "/WorkTicket/workTicket/ohworkticket/workTicketEdit.action";// 编辑
        String VIEW_URL = "/WorkTicket/workTicket/ohworkticket/workTicketView.action";// 查看
        String SAFE_VIEW_URL = "/WorkTicket/workTicket/ohworkticket/workTicketViewsafe.action"; // 安全员视图
    }

    /**
     * 停电票单据视图(注：红狮)
     */
    interface HSEleOffView {
        String EDIT_URL = "/BEAMEle/onOrOff/onoroff/eleOffEdit.action";// 编辑
        String PREVIEW_URL = "";// 审核
        String VIEW_URL = "/BEAMEle/onOrOff/onoroff/eleOffMainView.action";// 查看
    }

    /**
     * 送电票单据视图(注：红狮)
     */
    interface HSEleOnView {
        String EDIT_URL = "/BEAMEle/onOrOff/onoroff/eleOnEdit.action";// 编辑
        String PREVIEW_URL = "";// 审核
        String VIEW_URL = "/BEAMEle/onOrOff/onoroff/eleOnWorkFlow.action"; // 查看
    }

    /**
     * 隐患单据视图
     */
    interface FaultInfoView {
        String EDIT_URL = "/BEAM2/faultInfo/faultInfo/faultInfoEdit.action";// 编辑
        String PREVIEW_URL = "/BEAM2/faultInfo/faultInfo/faultInfoApproval.action";// 审核
        String VIEW_URL = "/BEAM2/faultInfo/faultInfo/faultInfoView.action"; // 查看
    }

    /**
     * 设备验收单
     */
    interface CheckApply {
        String EDIT_URL = "/BEAM2/checkApply/checkApply/checkApplyEdit.action"; // 编辑
        String VIEW_URL = "/BEAM2/checkApply/checkApply/checkApplyView.action"; // 查看
    }


    /**
     * 巡检任务状态
     */
    interface XJTaskLinkStatus {
        String status1 = "LinkState/01"; // 未下发
        String status2 = "LinkState/02"; // 已下发
        String status3 = "LinkState/03"; // 已完成
        String status4 = "LinkState/04"; // 已取消
        String status5 = "LinkState/05"; // 超期未检
        String status7 = "LinkState/07"; // 已终止
    }

    interface CARD_TYPE {
        String cardType1 = "cardType/01"; // 刷卡
        String cardType2 = "cardType/02"; // 手动
    }

    interface EleOffOn{
        String ELE_OFF = "BEAMEle001/01";
        String ELE_ON = "BEAMEle001/02";
    }

}
