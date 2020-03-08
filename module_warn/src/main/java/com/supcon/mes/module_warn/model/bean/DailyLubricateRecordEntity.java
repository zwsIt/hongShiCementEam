package com.supcon.mes.module_warn.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.JWXItem;
import com.supcon.mes.middleware.model.bean.Staff;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/8
 * Email zhangwenshuai1@supcon.com
 * Desc 日常润滑记录实体
 */
public class DailyLubricateRecordEntity extends BaseEntity {

    /**
     * attrMap : null
     * beamId : {"eamAssetcode":"7M14","id":1074}
     * cid : 1000
     * createStaff : null
     * createTime : null
     * dealStaff : {"id":1206,"name":"蒋孝洪"}
     * dealTime : 1583473092614
     * id : 1326
     * jwxItemId : {"claim":"点数：1×2，油温（℃）<60","content":"轴承腔2/3，油枪","eamID":{"code":"7M14","id":1074,"name":"7#料饼提升机"},"id":1318,"lubricateOil":{"id":1015,"name":"二硫化钼锂基脂"},"lubricatePart":"尾部轴承","oilType":{"id":"BEAM61/01","value":"加油"},"period":3,"periodType":{"id":"BEAM014/01","value":"时间频率"},"periodUnit":{"id":"BEAM016/02","value":"月"},"sum":1}
     * tableInfoId : null
     * taskType : {"id":"BEAM_067/01","value":"日常润滑"}
     * valid : true
     * version : 0
     */

    private EamEntity beamId; // 设备档案
    private Long cid;
    private Staff createStaff;
    private Long createTime;
    private Staff dealStaff; // 完成人
    private Long dealTime;
    private Long id;
    private JWXItem jwxItemId;
    private Long tableInfoId;
    private SystemCodeEntity taskType;// 任务类型：日常润滑( BEAM_067/01)、临时润滑（ BEAM_067/02）、工单润滑（ BEAM_067/03）
    private boolean valid;
    private int version;

    public EamEntity getBeamId() {
        return beamId;
    }

    public void setBeamId(EamEntity beamId) {
        this.beamId = beamId;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Staff getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Staff createStaff) {
        this.createStaff = createStaff;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Staff getDealStaff() {
        return dealStaff;
    }

    public void setDealStaff(Staff dealStaff) {
        this.dealStaff = dealStaff;
    }

    public Long getDealTime() {
        return dealTime;
    }

    public void setDealTime(Long dealTime) {
        this.dealTime = dealTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JWXItem getJwxItemId() {
        return jwxItemId;
    }

    public void setJwxItemId(JWXItem jwxItemId) {
        this.jwxItemId = jwxItemId;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public SystemCodeEntity getTaskType() {
        return taskType;
    }

    public void setTaskType(SystemCodeEntity taskType) {
        this.taskType = taskType;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class JwxItemIdBean {
        /**
         * claim : 点数：1×2，油温（℃）<60
         * content : 轴承腔2/3，油枪
         * eamID : {"code":"7M14","id":1074,"name":"7#料饼提升机"}
         * id : 1318
         * lubricateOil : {"id":1015,"name":"二硫化钼锂基脂"}
         * lubricatePart : 尾部轴承
         * oilType : {"id":"BEAM61/01","value":"加油"}
         * period : 3
         * periodType : {"id":"BEAM014/01","value":"时间频率"}
         * periodUnit : {"id":"BEAM016/02","value":"月"}
         * sum : 1
         */

        private String claim;
        private String content;
        private EamIDBean eamID;
        private int id;
        private LubricateOilBean lubricateOil;
        private String lubricatePart;
        private OilTypeBean oilType;
        private int period;
        private PeriodTypeBean periodType;
        private PeriodUnitBean periodUnit;
        private int sum;

        public String getClaim() {
            return claim;
        }

        public void setClaim(String claim) {
            this.claim = claim;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public EamIDBean getEamID() {
            return eamID;
        }

        public void setEamID(EamIDBean eamID) {
            this.eamID = eamID;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LubricateOilBean getLubricateOil() {
            return lubricateOil;
        }

        public void setLubricateOil(LubricateOilBean lubricateOil) {
            this.lubricateOil = lubricateOil;
        }

        public String getLubricatePart() {
            return lubricatePart;
        }

        public void setLubricatePart(String lubricatePart) {
            this.lubricatePart = lubricatePart;
        }

        public OilTypeBean getOilType() {
            return oilType;
        }

        public void setOilType(OilTypeBean oilType) {
            this.oilType = oilType;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public PeriodTypeBean getPeriodType() {
            return periodType;
        }

        public void setPeriodType(PeriodTypeBean periodType) {
            this.periodType = periodType;
        }

        public PeriodUnitBean getPeriodUnit() {
            return periodUnit;
        }

        public void setPeriodUnit(PeriodUnitBean periodUnit) {
            this.periodUnit = periodUnit;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public static class EamIDBean {
            /**
             * code : 7M14
             * id : 1074
             * name : 7#料饼提升机
             */

            private String code;
            private int id;
            private String name;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class LubricateOilBean {
            /**
             * id : 1015
             * name : 二硫化钼锂基脂
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class OilTypeBean {
            /**
             * id : BEAM61/01
             * value : 加油
             */

            private String id;
            private String value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class PeriodTypeBean {
            /**
             * id : BEAM014/01
             * value : 时间频率
             */

            private String id;
            private String value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class PeriodUnitBean {
            /**
             * id : BEAM016/02
             * value : 月
             */

            private String id;
            private String value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

}
