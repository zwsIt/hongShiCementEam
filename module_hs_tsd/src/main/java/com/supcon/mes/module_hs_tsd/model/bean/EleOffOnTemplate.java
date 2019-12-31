package com.supcon.mes.module_hs_tsd.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * 停送电模板
 */
public class EleOffOnTemplate extends BaseEntity {
    /**
     * attrMap : {"BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166":"停电操作","BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9":"核对工作票内容，检查现场控制按钮钥匙在中控操作位置,现场确认中控操作员已将设备在停止状态,将控制按钮钥匙开关打到检修位置，取下钥匙,拉开现场检修开关挂上低压停电工作票,去电力室校对低压柜双重命名,检查低压柜确在热备用状态（主电源已断开）,低压柜由热备用改为冷备用状态（拉开总开关）,车间发令人、操作人、检修负责人、检修人员各自挂上低压停电工作票及警示牌"}
     * cid : 1000
     * createStaff : null
     * createTime : null
     * eamId : {"code":"31017M13","eamAssetcode":"7M13","id":1075,"name":"7#辊压机"}
     * id : 1062
     * remark : null
     * status : null
     * tableInfoId : null
     * tableNo : null
     * templateType : {"id":"BEAMEle001/01","value":"停电"}
     * valid : true
     * version : 2
     */

    private AttrMapBean attrMap;
    public String remark;
    public boolean valid;
    public int version;
    public Long id;
    public String code; // 编码
    public EamEntity eamId; // 设备
    public SystemCodeEntity templateType; // 停送电类型 BEAMEle001/01:停电,BEAMEle001/02:送电

    public AttrMapBean getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(AttrMapBean attrMap) {
        this.attrMap = attrMap;
    }

    public static class AttrMapBean {
        /**
         * BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166 : 停电操作
         * BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9 : 核对工作票内容，检查现场控制按钮钥匙在中控操作位置,现场确认中控操作员已将设备在停止状态,将控制按钮钥匙开关打到检修位置，取下钥匙,拉开现场检修开关挂上低压停电工作票,去电力室校对低压柜双重命名,检查低压柜确在热备用状态（主电源已断开）,低压柜由热备用改为冷备用状态（拉开总开关）,车间发令人、操作人、检修负责人、检修人员各自挂上低压停电工作票及警示牌
         */

        private String BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166;
        private String BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9;

        public String getBEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166() {
            return BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166;
        }

        public void setBEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166(String BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166) {
            this.BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166 = BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_83af2416_2ede_4d75_93d2_f45c623ea166;
        }

        public String getBEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9() {
            return BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9;
        }

        public void setBEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9(String BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9) {
            this.BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9 = BEAMEle_1_0_0_template_eleTemplateRef_LISTPT_ASSO_18eb9c3a_3b57_45f4_82c2_a171deabfaf9;
        }
    }

}
