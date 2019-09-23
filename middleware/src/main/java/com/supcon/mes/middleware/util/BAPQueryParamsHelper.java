package com.supcon.mes.middleware.util;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.Area;
import com.supcon.mes.middleware.model.bean.BaseSubcondEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.model.bean.SubcondEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import static com.supcon.mes.middleware.constant.Constant.BAPQuery.BE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.GE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_BLUR;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.LIKE_OPT_Q;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.SYSTEMCODE;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TEXT;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_JOIN;
import static com.supcon.mes.middleware.constant.Constant.BAPQuery.TYPE_NORMAL;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
public class BAPQueryParamsHelper {


    public static FastQueryCondEntity createSingleFastQueryCond(Map<String, Object> queryMap) {

        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();

        List<BaseSubcondEntity> subcondEntities = crateSubcondEntity(queryMap);
        fastQueryCondEntity.subconds = subcondEntities;

        return fastQueryCondEntity;
    }

    public static FastQueryCondEntity createJoinFastQueryCond(Map<String, Object> queryMap) {
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        List<BaseSubcondEntity> subcondEntities = new ArrayList<>();
        for (String key : queryMap.keySet()) {

            if (queryMap.get(key) == null) {
                continue;
            }
            BaseSubcondEntity baseSubcondEntity = parseKey(key, queryMap.get(key));

            if (baseSubcondEntity != null) {
                subcondEntities.add(baseSubcondEntity);
            }

            JoinSubcondEntity joinSubcondEntity = parseKeyList(key, queryMap.get(key));

            if (joinSubcondEntity != null) {
                subcondEntities.add(joinSubcondEntity);
            }

        }
        fastQueryCondEntity.subconds = subcondEntities;

        return fastQueryCondEntity;
    }

    public static List<BaseSubcondEntity> createJoinSubcondEntity(Map<String, Object> queryMap) {
        List<BaseSubcondEntity> subcondEntities = new ArrayList<>();
        for (String key : queryMap.keySet()) {
            if (queryMap.get(key) == null) {
                continue;
            }
            JoinSubcondEntity joinSubcondEntity = parseKeyList(key, queryMap.get(key));
            if (joinSubcondEntity != null) {
                subcondEntities.add(joinSubcondEntity);
            }
        }
        return subcondEntities;
    }

    //获取SubcondEntity对象
    public static List<BaseSubcondEntity> crateSubcondEntity(Map<String, Object> queryMap) {
        List<BaseSubcondEntity> subcondEntitys = new LinkedList<>();
        for (String key : queryMap.keySet()) {
            if (queryMap.get(key) == null) {
                continue;
            }
            SubcondEntity subcondEntity = parseKey(key, queryMap.get(key));
            if (subcondEntity != null) {
                subcondEntitys.add(subcondEntity);
            }
        }
        return subcondEntitys;
    }

    //获取JoinSubcondEntity对象
    public static JoinSubcondEntity crateJoinSubcondEntity(Map<String, Object> queryMap, String joinInfo) {
        JoinSubcondEntity joinSubcondEntity = new JoinSubcondEntity();
        joinSubcondEntity.type = TYPE_JOIN;
        joinSubcondEntity.joinInfo = joinInfo;
        joinSubcondEntity.subconds = new LinkedList<>();
        for (String key : queryMap.keySet()) {
            if (queryMap.get(key) == null) {
                continue;
            }
            SubcondEntity baseSubcondEntity = parseKey(key, queryMap.get(key));

            if (baseSubcondEntity != null) {
                joinSubcondEntity.subconds.add(baseSubcondEntity);
            }
        }
        return joinSubcondEntity;
    }


    private static SubcondEntity parseKey(String key, Object value) {
        SubcondEntity subcondEntity = null;
        switch (key) {
            case Constant.BAPQuery.NAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.CONTENT:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SPECIFY:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.PRODUCT_NAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.PRODUCT_SPECIF:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = key;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.PLACE_SET_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.PLACE_SET_CODE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);

                break;

            case Constant.BAPQuery.PLACE_SET_NAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.PLACE_SET_NAME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);

                break;

            case Constant.BAPQuery.BATCH_TEXT:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.BATCH_TEXT;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);

                break;
            case Constant.BAPQuery.BATCH_NUM:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.BATCH_NUM;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);

                break;
            case Constant.BAPQuery.TABLE_NO:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.TABLE_NO;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.YH_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.FIND_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);

                break;
            case Constant.BAPQuery.YH_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.FIND_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.CREATE_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.CREATE_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);

                break;
            case Constant.BAPQuery.CREATE_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.CREATE_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.XSCK_DATE_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OUT_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.XSCK_DATE_END:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OUT_STORAGE_DATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATE;
                subcondEntity.operator = LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

//            case Constant.BAPQuery.BATCH_CODE:
//                subcondEntity = new SubcondEntity();
//                subcondEntity.columnName = Constant.BAPQuery.BATCH_CODE;
//                subcondEntity.type = TYPE_NORMAL;
//                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
//                subcondEntity.operator = LIKE;
//                subcondEntity.paramStr = LIKE_OPT_BLUR;
//                subcondEntity.value = String.valueOf(value);
//                break;
//            case Constant.BAPQuery.DIRECTION:
//                subcondEntity = new SubcondEntity();
//                subcondEntity.columnName = Constant.BAPQuery.DIRECTION;
//                subcondEntity.type = TYPE_NORMAL;
//                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
//                subcondEntity.operator = BE;
//                subcondEntity.paramStr = LIKE_OPT_Q;
//                subcondEntity.value = String.valueOf(value);
//                break;
            case Constant.BAPQuery.SOURCE_TYPE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = Constant.BAPQuery.SOURCE_TYPE;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.WORK_SOURCE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = Constant.BAPQuery.WORK_SOURCE;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.WORK_STATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.columnName = Constant.BAPQuery.WORK_STATE;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;

            case Constant.BAPQuery.REPAIR_TYPE:
            case Constant.BAPQuery.PRIORITY:
            case Constant.BAPQuery.FAULT_INFO_TYPE:
            case Constant.BAPQuery.LINK_STATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);

                break;
            case Constant.BAPQuery.EAM_EXACT_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.EAM_CODE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAMCODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.IS_MAIN_EQUIP:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BOOLEAN;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.PRODUCT_CODE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_NAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_STATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_AREA:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "ID";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.LONG;
                subcondEntity.operator = "=includeCustSub#BEAM_AREAS";
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_TYPE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = "EAMTYPE_CODE";
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.EAM_AREANAME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.NAME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = "=includeCustSub#BEAM_AREAS";
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.ON_OR_OFF:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = key;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.OPEN_TIME_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OPEN_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.OPEN_TIME_STOP:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OPEN_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SCORE_TIME_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.SCORE_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SCORE_TIME_STOP:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.SCORE_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SCORE_DATA_START:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.SCORE_DATA;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SCORE_DATA_STOP:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.SCORE_DATA;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.STAR_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.STAR_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.LE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.END_TIME:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.END_TIME;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.DATETIME;
                subcondEntity.operator = Constant.BAPQuery.GE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.SCORE_TABLE_NO:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.SCORE_TABLE_NO;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BAPCODE;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.OVERALLSTATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OVERALLSTATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.OVERDATEFLAG:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.OVERDATEFLAG;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.IS_RUN:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.IS_RUN;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BOOLEAN;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.IS_EAM_TASK:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.IS_EAM_TASK;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = Constant.BAPQuery.BOOLEAN;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                break;
            case Constant.BAPQuery.NEWSTATE:
                subcondEntity = new SubcondEntity();
                subcondEntity.columnName = Constant.BAPQuery.NEWSTATE;
                subcondEntity.type = TYPE_NORMAL;
                subcondEntity.dbColumnType = TEXT;
                subcondEntity.operator = Constant.BAPQuery.LIKE;
                subcondEntity.paramStr = LIKE_OPT_BLUR;
                subcondEntity.value = String.valueOf(value);
                break;
        }

        return subcondEntity;
    }

    public static JoinSubcondEntity parseKeyList(String key, Object value) {
        SubcondEntity subcondEntity;
        JoinSubcondEntity joinSubcondEntity = new JoinSubcondEntity();
        joinSubcondEntity.type = TYPE_JOIN;
        joinSubcondEntity.subconds = new LinkedList<>();
        switch (key) {
            case "TASK_DESCRIPTION":
                subcondEntity = generateSubcond(key, value);
                joinSubcondEntity.subconds.add(subcondEntity);
                return joinSubcondEntity;
            case Constant.BAPQuery.YH_AREA:
                joinSubcondEntity.joinInfo = "BEAM_AREAS,ID,BEAM2_FAULT_INFOS,AREA_INSTALL";
                Area area = (Area) value;
                subcondEntity = generateSubcond(Constant.BAPQuery.ID, area.id);
                subcondEntity.operator = Constant.BAPQuery.OPT_YH_AREA;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;

                joinSubcondEntity.subconds.add(subcondEntity);
                subcondEntity = generateSubcond(Constant.BAPQuery.NAME, area.name);
                subcondEntity.operator = Constant.BAPQuery.OPT_YH_AREA;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;
                joinSubcondEntity.subconds.add(subcondEntity);
                return joinSubcondEntity;

            case Constant.BAPQuery.EAM_NAME:
                joinSubcondEntity.joinInfo = "EAM_BaseInfo,EAM_ID,BEAM2_FAULT_INFOS,EAMID";
                subcondEntity = generateSubcond(Constant.BAPQuery.EAM_NAME, value);
                joinSubcondEntity.subconds.add(subcondEntity);
                return joinSubcondEntity;

            case Constant.BAPQuery.WXGD_REPAIR_TYPE:

            case Constant.BAPQuery.WXGD_PRIORITY:
                joinSubcondEntity.joinInfo = "BEAM2_FAULT_INFOS,ID,BEAM2_WORK_RECORDS,FAULT_INFO";
                subcondEntity = new SubcondEntity();
                subcondEntity.type = TYPE_NORMAL;
                if (key.contains("PRIORITY")) {
                    subcondEntity.columnName = Constant.BAPQuery.PRIORITY;
                }
                if (key.contains("REPAIR_TYPE")) {
                    subcondEntity.columnName = Constant.BAPQuery.REPAIR_TYPE;
                }
                subcondEntity.dbColumnType = SYSTEMCODE;
                subcondEntity.operator = Constant.BAPQuery.BE;
                subcondEntity.paramStr = Constant.BAPQuery.LIKE_OPT_Q;
                subcondEntity.value = String.valueOf(value);
                joinSubcondEntity.subconds.add(subcondEntity);
                return joinSubcondEntity;
        }
        return null;
    }

    private static SubcondEntity generateSubcond(String key, Object value) {

        SubcondEntity subcondEntity = new SubcondEntity();
        subcondEntity.columnName = key;
        subcondEntity.type = TYPE_NORMAL;
        if (key.contains("ID")) {
            subcondEntity.dbColumnType = Constant.BAPQuery.LONG;
        } else {
            subcondEntity.dbColumnType = Constant.BAPQuery.TEXT;
        }
        subcondEntity.operator = LIKE;
        subcondEntity.paramStr = LIKE_OPT_BLUR;
        subcondEntity.value = String.valueOf(value);


        return subcondEntity;
    }
}
