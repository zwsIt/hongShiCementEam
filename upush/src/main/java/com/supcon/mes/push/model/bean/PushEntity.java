package com.supcon.mes.push.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2019/4/29
 * Email:wangshizhan@supcom.com
 */
public class PushEntity extends BaseEntity {

    /**
     * {"display_type":"notification","extra":{"createStaffName":"蒋诗意","createStaffId":1003,"pendingId":"34149","deploymentId":1295,"processKey":"faultInfoFW","deploymentName":"隐患管理",
     * "nowStatus":"编辑","id":"1164","processVersion":3,"tableInfoId":2716,"tableNo":"faultInfo_20190429_005"},"msg_id":"ulcyd8k155654041461601",
     * "body":{"after_open":"go_app","play_lights":"false","play_vibrate":"false","ticker":"BAP待办提醒","text":"蒋诗意提交的隐患管理(faultInfo_20190429_005),当前状态“编辑”","title":"BAP待办提醒","play_sound":"true"},"random_min":0}
     */
    public String display_type;
    public PushBody body;

    public PendingPushEntity extra;
    public String msg_id;
    public int random_min;


    public class PushBody{

        public String after_open;
        public boolean play_lights;
        public boolean play_vibrate;
        public boolean play_sound;
        public String ticker;
        public String text;
        public String title;

    }

}
