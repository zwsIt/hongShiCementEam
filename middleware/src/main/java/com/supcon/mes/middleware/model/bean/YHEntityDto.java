package com.supcon.mes.middleware.model.bean;

import android.annotation.SuppressLint;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by xushiyun on 2018/8/14
 * Email:ciruy.victory@gmail.com
 *
 * @author xushiyun
 */
public class YHEntityDto extends BaseEntity {

    /**
     * 单据ID,按照当前的系统时间对其进行赋值
     */
    private Long tableId = System.currentTimeMillis();
    /**
     * 隐患编号
     */
    private String faultInfoNo;
    /**
     * 区域位置ID
     */
    private Long areaInstallId;
    /**
     * 设备ID
     */
    private Long eamId;
    /**
     * 发现人
     */
    private Long findStaffId;
    /**
     * 隐患类型
     */
    private String faultType;
    /**
     * 优先级
     */
    private String priority;
    /**
     * 发现时间
     */
    private String findDate;
    /**
     * 要求完成时间
     */
    private String endTime;
    /**
     * 维修组ID
     */
    private Long repiarGroupId;
    /**
     * 维修类型
     */
    private String repairType;
    /**
     * 隐患现象
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 隐患来源
     */
    private String sourceType;
    /**
     * 上游单据Id
     */
    private Long srcID;
    /**
     * 上游单据类型
     */
    private String srcType;
    /**
     * 任务明细ID
     */
    private Long taskId;
    /**
     * 需要上传的文件的路径
     */
    private String faultPicPaths;
    /**
     * 需要删除文件的路径
     */
    private String deletePicPaths;
    /**
     * 临时图片文件
     */
    private String tempFaultPicPaths;
    /**
     * 本地图片路径
     */
//    private String localPicPaths;
//    private String localImgNames;
    /**
     * 以下为工作流相关
     */
    private String outCome;
    private String outComeType;
    private String pendingId;
    private String transitionDesc;

    @SuppressLint("CheckResult")
    public static List<YHEntityDto> genUploadEntities() {
        final List<YHEntityDto> result = new ArrayList<>();
        Flowable.fromIterable(EamApplication.dao().getYHEntityVoDao().queryBuilder().
                where(YHEntityVoDao.Properties.Ip.eq(EamApplication.getIp()))
                .where(YHEntityVoDao.Properties.Status.eq(true)).list())
                .subscribe(new Consumer<YHEntityVo>() {
                    @Override
                    public void accept(YHEntityVo vo) throws Exception {
                        result.add(GsonUtil.gsonToBean(vo.toString(), YHEntityDto.class));
                    }
                });
        return result;
    }

    public String getFaultPicPaths() {
        return faultPicPaths;
    }

    public void setFaultPicPaths(String faultPicPaths) {
        this.faultPicPaths = faultPicPaths;
    }

    public Long getTableId() {
        return tableId;
    }
}