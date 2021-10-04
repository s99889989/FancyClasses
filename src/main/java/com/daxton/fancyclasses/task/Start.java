package com.daxton.fancyclasses.task;


import com.daxton.fancyclasses.config.MobConfig;
import com.daxton.fancyclasses.config.MythicMobConfig;

import com.daxton.fancyclasses.other.CreateFile;
import com.daxton.fancyclasses.other.ManaReg;
import com.daxton.fancyclasses.other.SetDefaultData;

public class Start {
    //只在開服時執行的任務
    public static void execute(){
        //建立Mob設定檔
        MobConfig.execute();
        //建立MythicMob設定檔
        MythicMobConfig.execute();
        //設置等級基礎值
        SetDefaultData.level();
        //設置技能基礎值
        SetDefaultData.skill();
        //魔力回復
        ManaReg.execute();

        //CreateFile.execute();


    }

}
