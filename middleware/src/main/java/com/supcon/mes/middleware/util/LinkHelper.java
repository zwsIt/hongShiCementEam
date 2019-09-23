package com.supcon.mes.middleware.util;

import com.supcon.mes.mbap.beans.LinkEntity;
import com.supcon.mes.mbap.beans.Transition;
import com.supcon.mes.middleware.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshizhan on 2018/8/22
 * Email:wangshizhan@supcom.com
 */
public class LinkHelper {

    public static List<Transition> convertToTransition(List<LinkEntity> linkEntities){

        List<Transition> transitions = new ArrayList<>();

        for(LinkEntity linkEntity : linkEntities){
            Transition transition = new Transition();
            transition.transitionDesc = linkEntity.description;
            transition.outCome = linkEntity.name;
            transition.outComeType = Constant.Transition.SUBMIT;
            transitions.add(transition);
        }


        return transitions;

    }

}
