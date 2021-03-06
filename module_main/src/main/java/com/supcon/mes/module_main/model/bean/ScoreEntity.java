package com.supcon.mes.module_main.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/7/29
 * ------------- Description -------------
 */
public class ScoreEntity extends BaseEntity {

    public Integer ranking;
    public Float score;
    public String type; // BEAM_065/01 : 设备评分；BEAM_065/02 : 巡检个人评分；BEAM_065/03 : 机修个人评分；BEAM_065/04 : 验收单；BEAM_065/05 : 巡检工每日评分
    public int staffID;

}
