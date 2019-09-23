package com.supcon.mes.middleware.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;

import com.supcon.mes.middleware.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshizhan on 2018/9/7
 * Email:wangshizhan@supcom.com
 */
public class SoundHelper {

    private SparseArray<Integer> mapSRC;
    private SoundPool sp; //声音池
    //初始化声音池
    public void initSoundPool(Context context) {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mapSRC = new SparseArray<>();
        mapSRC.put(1, sp.load(context, R.raw.error, 0));
        mapSRC.put(2, sp.load(context, R.raw.welcome, 0));
        mapSRC.put(3, sp.load(context, R.raw.msg, 0));
        mapSRC.put(4, sp.load("/system/media/audio/ui/VideoRecord.ogg", 0));
    }

    /**
     * 播放声音池的声音
     */
    public void play(int sound, int number) {
        sp.play(mapSRC.get(sound),//播放的声音资源
                1.0f,//左声道，范围为0--1.0
                1.0f,//右声道，范围为0--1.0
                0, //优先级，0为最低优先级
                number,//循环次数,0为不循环
                0);//播放速率，0为正常速率
    }

    public void release(){
        if(sp!=null){
            sp.release();
        }
    }
}
