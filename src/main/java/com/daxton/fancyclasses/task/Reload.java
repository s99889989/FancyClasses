package com.daxton.fancyclasses.task;


import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.config.MobConfig;
import com.daxton.fancyclasses.config.MythicMobConfig;
import com.daxton.fancyclasses.other.SetDefaultData;

public class Reload {
    //重新讀取的一些任務
    public static void execute(){
        //設定檔
        FileConfig.reload();
        //建立Mob設定檔
        MobConfig.execute();
        //建立MythicMob設定檔
        MythicMobConfig.execute();
        //設置等級基礎值
        SetDefaultData.level();
        //設置技能基礎值
        SetDefaultData.skill();
    }

}
