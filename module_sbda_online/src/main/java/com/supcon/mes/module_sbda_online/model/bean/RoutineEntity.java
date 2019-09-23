package com.supcon.mes.module_sbda_online.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/2
 * ------------- Description -------------
 */
public class RoutineEntity extends ResultEntity {

    public TimeEntity pointInfos;
    public RunInfo runInfos;

    public RunInfo getRunInfos() {
        if (runInfos==null) {
            runInfos = new RunInfo();
        }
        return runInfos;
    }

    public class TimeEntity extends BaseEntity {
        public String nextTime;
        public String lastTime;
    }

    public class RunInfo extends BaseEntity {
        public String id;

        public String lastValue;

        //停机
        public String stopDay;
        public String stopHH;
        public String stopMM;
        public String stopSS;
        public String stopTotal;
        //停机次数
        public String stopNum;
        //运行
        public String runDay;
        public String runHH;
        public String runMM;
        public String runSS;
        public String runTotal;
    }

}
